package tablab.PartitionPlayer;

import tablab.MusicPartition;

/** PartitionPlayer is an class that can play in real time a partition, with realizing an event each time a note is played.
 *
 * The speed of the player is given by the tempo, the time structure of the partition and the structure of each beat.
 * The events (each time a note is played and others) are done throw callbacks : the PlayerCallback.
 * PlayerCallback can be implemented and register to the player to execute a action for each event (play, pause, resume, at each note, ...).
 */
public interface PartitionPlayer {

    /** Play the given partition in real time.
     * If the partition is already playing, the player restart the partition from the beginning.
     *
     * @param partition the partition to play
     */
    void playPartition(MusicPartition partition);

    /** Stop to play the partition.
     */
    void stopPlaying();

    /** Pause the partition, with saving the current position in the partition.
     */
    void pausePlaying();

    /** Resume the partition, at the current position.
     */
    void resumePlaying();

    /**
     * Return true if the playing is playing.
     * "Playing" means the player is started and is not stopped or doesn't finish to play the partition, but a player "playing" can be "paused".
     * A player can be playing and paused at the same time.
     * @return if the playing is playing
     */
    boolean isPlaying();

    /**
     * Return true if the playing is paused.
     * A player can be paused and still playing.
     * @return if the playing is paused
     */
    boolean isPaused();

    /**
     * Add a PlayerCallback to the player.
     * @param callback the callback to add
     */
    void addPlayerCallback(PlayerCallback callback);

    /**
     * Remove a PlayerCallback to the player.
     * @param callback the callback to remove.
     */
    void removePlayerCallback(PlayerCallback callback);
}
