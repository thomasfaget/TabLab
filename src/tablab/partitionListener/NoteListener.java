package tablab.partitionListener;

import tablab.MusicBar;

public interface NoteListener {

    void addedNote(MusicBar source, String lineType, int beat, int note);
    void removedNote(MusicBar source, String lineType, int beat, int note);

}
