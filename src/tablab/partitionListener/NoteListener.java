package tablab.partitionListener;

import tablab.MusicBar;

/** A listener interface for notes.
 * This listener can handle events based on Note addition or deletion in the tab.
 */
public interface NoteListener {

    /** Method called when a note was added (or set) in the MusicBar

     * @param source the music bar where the note was added
     * @param lineType the line of the note
     * @param beat the beat of the note
     * @param note the note index
     */
    void addedNote(MusicBar source, String lineType, int beat, int note);

    /** Method called when a note was removed in the MusicBar

     * @param source the music bar where the note was added
     * @param lineType the line of the note
     * @param beat the beat of the note
     * @param note the note index
     */
    void removedNote(MusicBar source, String lineType, int beat, int note);

}
