package tablab;

import java.util.Arrays;
import java.util.Collections;

import static tablab.BeatStructure.NoteTime.*;

// Some standard beat structures
public class StandardBeatStructure {

    public static BeatStructure SIMPLE_NOTE_STRUCTURE = new BeatStructure(Collections.singletonList(
            SIMPLE_NOTE));
    public static BeatStructure EIGHTH_NOTE_STRUCTURE = new BeatStructure(Arrays.asList(
            EIGHTH_NOTE,
            EIGHTH_NOTE));
    public static BeatStructure SIXTEENTH_NOTE_STRUCTURE = new BeatStructure(Arrays.asList(
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE));
    public static BeatStructure THIRTY_SECOND_NOTE_STRUCTURE = new BeatStructure(Arrays.asList(
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE));
    public static BeatStructure TRIPLET_NOTE_STRUCTURE = new BeatStructure(Arrays.asList(
            TRIPLET_NOTE,
            TRIPLET_NOTE,
            TRIPLET_NOTE));
    public static BeatStructure QUAVER_TRIPLET_STRUCTURE = new BeatStructure(Arrays.asList(
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE));
}
