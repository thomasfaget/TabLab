package tablab.partitionListener;

import tablab.MusicBar;

/** A listener interface for notes.
 * This listener can handle events based on BeatStructure addition or deletion in the tab.
 */
public interface BeatStructureListener {

    /** Method called when a BeatStructure was added (or set) in the MusicBar

     * @param source the music bar where the BeatStructure was added
     * @param beat the beat index
     */
    void addedBeatStructure(MusicBar source, int beat);

    /** Method called when a BeatStructure was removed in the MusicBar

     * @param source the music bar where the BeatStructure was added
     * @param beat the beat index
     */
    void removedBeatStructure(MusicBar source, int beat);


}