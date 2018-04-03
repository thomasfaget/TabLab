package tablab.PartitionPlayer;

public interface PlayerCallback {

    /**
     * Method called when the player starts to play the partition.
     */
    void onStart();

    /**
     * Method called when the player finishes to play the partition.
     * This method is called even if the player restarts the partition (call play if the player is already playing) or if stop is called.
     */
    void onFinish();

    /**
     * Method called when the player pauses the partition.
     * If the partition is already paused or not playing, this method is still called.
     */
    void onPause();
    /**
     * Method called when the player resumes the partition.
     * If the partition is not paused or not playing, this method is still called
     */
    void onResume();

    /**
     * Method called when the player stops the partition.
     * This method is called even if the player restarts the partition (call play if the player is already playing) or if the player is not playing.
     */
    void onStop();

    /**
     * The player, playing the partition in real time, call this method every time a note (pitch value) is triggered.
     * The speed of the playing is determine by the tempo, the time signature and the beat structure of the partition.
     * This method is called with the current bar, beat and note in the partition.
     *
     * @param barNumber the current bar
     * @param beatNumber the current beat
     * @param noteNumber the current note
     */
    void onNextNote(int barNumber, int beatNumber, int noteNumber);
}
