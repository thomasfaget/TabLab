package tablab.partitionListener;


/** A listener interface for MusicBar insertion or deletion in the partition.
 * This listener can handle an addition or a deletion of a music bar in the partition.
 */
public interface PartitionListener {

    /** Method called when a MusicBar was added (or set) in the partition

     * @param index the index of the music bar
     */
    void addedMusicBar(int index);

    /** Method called when a MusicBar was removed in the partition
     *
     * @param index the index of the music bar
     */
    void removedMusicBar(int index);
}
