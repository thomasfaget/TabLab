package tablab.partitionListener;

import tablab.MusicBar;

/** A listener interface for notes.
 * This listener can handle events based on LineStructure addition or deletion in the tab.
 */
public interface LineStructureListener {

    /** Method called when a LineStructure was added (or set) in the MusicBar

     * @param source the music bar where the LineStructure was added
     * @param beat the beat index
     */
    void addedLineStructure(MusicBar source, int beat);

    /** Method called when a LineStructure was removed in the MusicBar

     * @param source the music bar where the LineStructure was added
     * @param beat the beat index
     */
    void removedLineStructure(MusicBar source, int beat);


}
