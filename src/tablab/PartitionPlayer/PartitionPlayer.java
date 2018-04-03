package tablab.PartitionPlayer;

import tablab.Fraction;
import tablab.MusicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PartitionPlayer {

    // The thread running the player
    private Thread thread;

    // State of the player
    private boolean isStarted;
    private boolean isPaused;

    // A pauseLock to pause the player
    private final Lock pauseLock = new ReentrantLock(true);

    private List<PlayerCallback> callbacks = new ArrayList<>();

    /** Play the given partition.
     * If the partition is already playing, the player restart the partition from the beginning.
     *
     * @param partition the partition to play
     */
    public void playPartition(MusicPartition partition) {
        stopPlaying();

        isStarted = true;
        isPaused = false;
        PlayerRunnable playerRunnable = new PlayerRunnable(partition);
        thread = new Thread(playerRunnable);
        thread.start();
    }

    public void stopPlaying() {
        isStarted = false;
        if (isPaused) {
            synchronized (pauseLock) {
                pauseLock.notify();
            }
        }
        else if (thread != null) {
            thread.interrupt();
        }

        for (PlayerCallback callback : callbacks) {
            callback.onStop();
        }
    }

    public void pausePlaying() {
        if (!isPaused) {
            isPaused = true;
            thread.interrupt();
        }

        for (PlayerCallback callback : callbacks) {
            callback.onPause();
        }
    }

    public void resumePlaying() {
        isPaused = false;
        if (thread != null) {
            synchronized (pauseLock) {
                pauseLock.notify();
            }
            for (PlayerCallback callback : callbacks) {
                callback.onResume();
            }
        }
    }

    public boolean isPlaying() {
        return isStarted;
    }

    public boolean isPaused() {
        return isPaused;
    }


    public void addPlayerCallback(PlayerCallback callback) {
        callbacks.add(callback);
    }

    public void removePlayerCallback(PlayerCallback callback) {
        callbacks.remove(callback);
    }


    private class PlayerRunnable implements Runnable {

        private MusicPartition partition;

        PlayerRunnable(MusicPartition partition) {
            this.partition = partition;
        }

        @Override
        public void run() {

            int nbBars = partition.getMusicBars().size();
            int nbBeats = partition.getSettings().notesNumber;
            float timeBetweenNoteValue = 60000 / partition.getSettings().tempo; // The time (in ms) between 2 notes

            List<NoteDelay> noteDelays = new ArrayList<>();

            for (int bar = 1; bar <= nbBars; bar++) {
                for (int beat = 1; beat <= nbBeats; beat++) {
                    int nbNotes = partition.getMusicBar(bar).getBeatStructure(beat).size();
                    List<Fraction> fractions = partition.getMusicBar(bar).getBeatStructure(beat).getFractionEvolution(partition.getSettings());
                    for (int note = 1; note <= nbNotes; note++) {
                        float time;
                        if (note < nbNotes) {
                            time = (float) (fractions.get(note).doubleValue() - fractions.get(note - 1).doubleValue());
                        } else {
                            time = (float) (1 - fractions.get(note - 1).doubleValue());
                        }
                        time *= timeBetweenNoteValue;
                        noteDelays.add(new NoteDelay(time, bar, beat, note));
                    }
                }
            }

            for (PlayerCallback callback : callbacks) {
                callback.onStart();
            }

            int i = 0;

            while (isStarted && i != noteDelays.size()) {

                try {

                    for (; i < noteDelays.size(); i++) {
                        NoteDelay noteDelay = noteDelays.get(i);

                        long currentTime = System.currentTimeMillis();
                        for (PlayerCallback callback : callbacks) {
                            callback.onNextNote(noteDelay.barNumber, noteDelay.beatNumber, noteDelay.noteNumber);
                        }

                        float sleepTime = noteDelay.delay - (System.currentTimeMillis() - currentTime);
                        Thread.sleep((long) sleepTime, (int) (1000 * (sleepTime - (long) sleepTime)));
                    }
                }
                catch (InterruptedException e) {
                    if (isPaused) {
                        synchronized (pauseLock) {
                            try {
                                pauseLock.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    else {
                        isStarted = false;
                    }
                }
            }

            isStarted = false;
            for (PlayerCallback callback : callbacks) {
                callback.onFinish();
            }
        }
    }

    private class NoteDelay {

        float delay;
        int barNumber;
        int beatNumber;
        int noteNumber;

        NoteDelay(float delay, int barNumber, int beatNumber, int noteNumber) {
            this.delay = delay;
            this.barNumber = barNumber;
            this.beatNumber = beatNumber;
            this.noteNumber = noteNumber;
        }
    }
}
