package tablab.PartitionPlayer;

import tablab.Fraction;
import tablab.MusicPartition;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractPartitionPlayer is an implementation of PartitionPlayer, with a abstraction for the multithreading aspects.
 * These aspects are sleeping on a thread or locking and unlocking of a mutex.
 */
public abstract class AbstractPartitionPlayer implements PartitionPlayer {

    // The thread running the player
    private Thread thread;

    // State of the player
    private boolean isStarted;
    private boolean isPaused;

    private List<PlayerCallback> callbacks = new ArrayList<>();
    private MusicPartition partition;

    /**
     * A method to make wait the current with the given time
     *
     * @param millis millisecond to wait
     * @param nano nanosecond to wait
     * @throws InterruptedException if exception with the method
     */
    abstract void sleep(long millis, int nano) throws InterruptedException;

    /**
     * A method to lock the current thread, such as a mutual exclusion lock.
     * If lockPause is called, the current thread have to wait until the method notifyPause is call by a another thread.
     *
     * @throws InterruptedException id exception with the method
     */
    abstract void lockPause() throws InterruptedException;

    /**
     * A method to notify the current thread if lock, such as a mutual exclusion lock.
     * This method have to unlock the thread lock at 'lockPause".
     */
    abstract void notifyPause();

    public void playPartition(MusicPartition partition) {
        this.partition = partition;
        if (isStarted) {
            stopPlaying();
        }
        isStarted = true;
        isPaused = false;
        // start playing
        thread = new Thread(new PlayerRunnable());
        thread.start();
    }

    public void stopPlaying() {
        isStarted = false;
        // unlock the thread if the player is paused
        if (isPaused) {
            notifyPause();
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

    public void resumePlaying() {
        isPaused = false;
        // notify the lock
        if (thread != null) {
            notifyPause();
            // Call the callbacks
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
                        if (sleepTime > 0) {
                            sleep((long) sleepTime, (int) (1000 * (sleepTime - (long) sleepTime)));
                        }
                    }
                }
                // Handle interruption (pause and stop)
                catch (InterruptedException e) {
                    // pause the partition (lock)
                    if (isPaused) {
                        try {
                            lockPause();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
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
