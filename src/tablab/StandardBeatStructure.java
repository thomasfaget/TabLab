package tablab;

import java.util.Arrays;
import java.util.Collections;

import static tablab.BeatStructure.NoteTime.*;

// Some standard beat structures
public class StandardBeatStructure {

    // Structures for half note as note value (2)
    public static BeatStructure HALF_NOTE_STRUCTURE_HALF_NOTE_STRUCTURE = new BeatStructure(Collections.singleton(
            HALF_NOTE));
    public static BeatStructure QUARTER_NOTE_STRUCTURE_HALF_NOTE_SIGNATURE = new BeatStructure(Arrays.asList(
            QUARTER_NOTE,
            QUARTER_NOTE));
    public static BeatStructure EIGHTH_NOTE_STRUCTURE_HALF_NOTE_SIGNATURE = new BeatStructure(Arrays.asList(
            EIGHTH_NOTE,
            EIGHTH_NOTE,
            EIGHTH_NOTE,
            EIGHTH_NOTE));
    public static BeatStructure SIXTEENTH_NOTE_STRUCTURE_HALF_NOTE_SIGNATURE = new BeatStructure(Arrays.asList(
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE));
    public static BeatStructure TRIPLET_NOTE_STRUCTURE_HALF_NOTE_SIGNATURE = new BeatStructure(Arrays.asList(
            TRIPLET_NOTE,
            TRIPLET_NOTE,
            TRIPLET_NOTE,
            TRIPLET_NOTE,
            TRIPLET_NOTE,
            TRIPLET_NOTE));



    // Structures for quarter note as note value (4)
    public static BeatStructure QUARTER_NOTE_STRUCTURE_QUARTER_SIGNATURE = new BeatStructure(Collections.singletonList(
            QUARTER_NOTE));
    public static BeatStructure EIGHTH_NOTE_STRUCTURE_QUARTER_SIGNATURE = new BeatStructure(Arrays.asList(
            EIGHTH_NOTE,
            EIGHTH_NOTE));
    public static BeatStructure SIXTEENTH_NOTE_STRUCTURE_QUARTER_SIGNATURE = new BeatStructure(Arrays.asList(
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE));
    public static BeatStructure THIRTY_SECOND_NOTE_STRUCTURE_QUARTER_SIGNATURE = new BeatStructure(Arrays.asList(
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE));
    public static BeatStructure TRIPLET_NOTE_STRUCTURE_QUARTER_SIGNATURE = new BeatStructure(Arrays.asList(
            TRIPLET_NOTE,
            TRIPLET_NOTE,
            TRIPLET_NOTE));
    public static BeatStructure QUAVER_TRIPLET_STRUCTURE_QUARTER_SIGNATURE = new BeatStructure(Arrays.asList(
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE));



    // Structures for eighth note as note value (8)
    public static BeatStructure EIGHTH_NOTE_STRUCTURE_EIGHTH_SIGNATURE = new BeatStructure(Collections.singletonList(
            EIGHTH_NOTE));
    public static BeatStructure SIXTEENTH_NOTE_STRUCTURE_EIGHTH_SIGNATURE = new BeatStructure(Arrays.asList(
            SIXTEENTH_NOTE,
            SIXTEENTH_NOTE));
    public static BeatStructure THIRTY_SECOND_NOTE_STRUCTURE_EIGHTH_SIGNATURE = new BeatStructure(Arrays.asList(
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE));
    public static BeatStructure QUAVER_TRIPLET_STRUCTURE_EIGHTH_SIGNATURE = new BeatStructure(Arrays.asList(
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE,
            QUAVER_TRIPLET_NOTE));



    // Structures for sixteenth note as note value (16)
    public static BeatStructure SIXTEENTH_NOTE_STRUCTURE_SIXTEENTH_SIGNATURE = new BeatStructure(Collections.singletonList(
            SIXTEENTH_NOTE));
    public static BeatStructure THIRTY_SECOND_NOTE_STRUCTURE_SIXTEENTH_SIGNATURE = new BeatStructure(Arrays.asList(
            THIRTY_SECOND_NOTE,
            THIRTY_SECOND_NOTE));


}

