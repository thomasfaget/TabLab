package tablab.partitionListener;

import tablab.MusicBar;

public interface LineStructureListener {

    void addedLineStructure(MusicBar source, int beat);
    void removedLineStructure(MusicBar source, int beat);

}
