package tablab.PartitionPlayer;

import tablab.Fraction;
import tablab.MusicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PartitionPlayer {

    private MusicPartition partition;

    // The thread running the player
    private Thread thread = new Thread(new PlayerRunnable());

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
        this.partition = partition;
        stopPlaying();
        isStarted = true;
        isPaused = false;
        // start playing
        thread.start();
    }

    /** Stop to play the partition.
     */
    public void stopPlaying() {
        isStarted = false;
        // unlock the thread if the player is paused
        if (isPaused) {
            synchronized (pauseLock) {
                pauseLock.notify();
            }
        }
        // interrupt the thread if not paused
        else if (thread != null) {
            thread.interrupt();
        }

        // Call the callbacks
        for (PlayerCallback callback : callbacks) {
            callback.onStop();
        }
    }

    /** Pause the partition, with saving the current position in the partition.
     */
    public void pausePlaying() {
        if (!isPaused && thread != null) {
            isPaused = true;
            thread.interrupt();
        }

        // Call the callbacks
        for (PlayerCallback callback : callbacks) {
            callback.onPause();
        }
    }

    /** Resume the partition, at the current position.
     */
    public void resumePlaying() {
        isPaused = false;
        // notify the lock
        if (thread != null) {
            synchronized (pauseLock) {
                pauseLock.notify();
            }
            // Call the callbacks
            for (PlayerCallback callback : callbacks) {
                callback.onResume();
            }
        }
    }

    /**
     * Return true if the playing is playing.
     * "Playing" means the player was started and was not stopped or finished to play, a player can be playing and paused.
     * @return if the playing is playing
     */
    public boolean isPlaying() {
        return isStarted;
    }


    /**
     * Return true if the playing is paused.
     * A player can be paused and still playing.
     * @return if the playing is paused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Add a PlayerCallback to the player.
     * @param callback the callback to add
     */
    public void addPlayerCallback(PlayerCallback callback) {
        callbacks.add(callback);
    }

    /**
     * Remove a PlayerCallback to the player.
     * @param callback the callback to remove.
     */
    public void removePlayerCallback(PlayerCallback callback) {
        callbacks.remove(callback);
    }


    private class PlayerRunnable implements Runnable {

        @Override
        public void run() {

            int nbBars = partition.getMusicBars().size();
            int nbBeats = partition.getSettings().notesNumber;
            float timeBetweenNoteValue = 60000 / partition.getSettings().tempo; // The time (in ms) between 2 notes
            List<NoteDelay> noteDelays = new ArrayList<>(); // A list with the time of all the notes

            // Compute the time (time value) of all the notes in the partition
            for (int bar = 1; bar <= nbBars; bar++) {
                for (int beat = 1; beat <= nbBeats; beat++) {
                    int nbNotes = partition.getMusicBar(bar).getBeatStructure(beat).size();
                    // Get the time evolution of the beat :
                    List<Fraction> fractions = partition.getMusicBar(bar).getBeatStructure(beat).getFractionEvolution(partition.getSettings());
                    for (int note = 1; note <= nbNotes; note++) {
                        float time;
                        // Compute the real time of the note :
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

            // Call the callbacks
            for (PlayerCallback callback : callbacks) {
                callback.onStart();
            }

            // Start playing the partition
            int i = 0;
            while (isStarted && i != noteDelays.size()) {

                try {

                    for (; i < noteDelays.size(); i++) {
                        NoteDelay noteDelay = noteDelays.get(i);

                        long currentTime = System.currentTimeMillis();
                        // Call the callback wiht the current position of the partition
                        for (PlayerCallback callback : callbacks) {
                            callback.onNextNote(noteDelay.barNumber, noteDelay.beatNumber, noteDelay.noteNumber);
                        }

                        // Sleep with the given time :
                        float sleepTime = noteDelay.delay - (System.currentTimeMillis() - currentTime);
                        Thread.sleep((long) sleepTime, (int) (1000 * (sleepTime - (long) sleepTime)));
                    }
                }
                // Handle interruption (pause and stop)
                catch (InterruptedException e) {
                    // pause the partition (lock)
                    if (isPaused) {
                        synchronized (pauseLock) {
                            try {
                                pauseLock.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    // Stop, or error : stop playing
                    else {
                        isStarted = false;
                    }
                }
            }

            isStarted = false;
            // Call the callbacks
            for (PlayerCallback callback : callbacks) {
                callback.onFinish();
            }
        }
    }

    /**
     * Data structure which contains the current position in the partition (bar, beat and note), the time of this 'position' (i.e. a note (pitch value))
     */
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
