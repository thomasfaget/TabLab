package tablab.partitionListener;

import tablab.MusicBar;

public interface BeatStructureListener {

    void addedBeatStructure(MusicBar source, int beat);
    void removedBeatStructure(MusicBar source, int beat);

}
