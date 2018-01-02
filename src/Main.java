import tablab.*;

import java.util.Arrays;


/** Created by thomas on 05/06/2017.
 */
public class Main {

    public static void main(String[] args) {

        ScoreSettings settings = new ScoreSettings();
        settings.pitch = 4;
        settings.noteValue = 4;
        settings.tempo = 120;
        settings.beatStructure = BeatStructure.StandardBeatStructure.SIXTEENTH_NOTE_STRUCTURE.getBeatStructure();
        settings.lineTypes = Arrays.asList(LineType.HIT_HAT, LineType.SNARE, LineType.BASS);

        MusicPartition partition = new MusicPartition("Test", "Thomas Faget", settings);

        MusicBar musicBar1 = new MusicBar(settings);
        musicBar1.createEmptyBar();
        musicBar1.addNote(LineType.HIT_HAT,1,1);
        musicBar1.addNote(LineType.HIT_HAT,1,3);
        musicBar1.addNote(LineType.HIT_HAT,2,1);
        musicBar1.addNote(LineType.HIT_HAT,2,3);
        musicBar1.addNote(LineType.HIT_HAT,3,1);
        musicBar1.addNote(LineType.HIT_HAT,3,3);
        musicBar1.addNote(LineType.HIT_HAT,4,1);
        musicBar1.addNote(LineType.HIT_HAT,4,3);
        musicBar1.addNote(LineType.BASS,1,1);
        musicBar1.addNote(LineType.SNARE,2,1);
        musicBar1.addNote(LineType.BASS,3,1);
        musicBar1.addNote(LineType.SNARE,4,1);
        partition.addMusicBar(musicBar1);
        musicBar1.setSpecialStructure(BeatStructure.StandardBeatStructure.THIRTY_SECOND_NOTE_STRUCTURE.getBeatStructure(), 2);

        MusicBar musicBar2 = musicBar1.copyMusicBar();
        musicBar2.addNote(LineType.BASS,1,2);
        partition.addMusicBar(musicBar2);

        partition.printPartition();


//        BeatStructure structure1 = BeatStructure.StandardBeatStructure.SIMPLE_NOTE_STRUCTURE.getBeatStructure();
//        BeatStructure structure2 = BeatStructure.StandardBeatStructure.EIGHTH_NOTE_STRUCTURE.getBeatStructure();
//        BeatStructure structure3 = BeatStructure.StandardBeatStructure.SIXTEENTH_NOTE_STRUCTURE.getBeatStructure();
//        BeatStructure structure4 = BeatStructure.StandardBeatStructure.THIRTY_SECOND_NOTE_STRUCTURE.getBeatStructure();
//        BeatStructure structure5 = BeatStructure.StandardBeatStructure.TRIPLET_NOTE_STRUCTURE.getBeatStructure();
//        BeatStructure structure6 = BeatStructure.StandardBeatStructure.QUAVER_TRIPLET_STRUCTURE.getBeatStructure();
//
//        System.out.println("structure1 = " + structure1.getStructure());
//        System.out.println("structure2 = " + structure2.getStructure());
//        System.out.println("structure3 = " + structure3.getStructure());
//        System.out.println("structure4 = " + structure4.getStructure());
//        System.out.println("structure5 = " + structure5.getStructure());
//        System.out.println("structure6 = " + structure6.getStructure());



//        int a = 0b10101;
//        System.out.println("a = " + a);
//        int b = 0b11101;
//        System.out.println("b = " + b);
//
//        double t1 = System.currentTimeMillis();
//
//        int max = b <= 2 ? b : (int) Math.ceil( Math.log(b)/Math.log(2) );
//        System.out.println("max = " + max);
//        for (int i = 0; i < max; i++) {
//            boolean res = (b & (int) Math.pow(2,i)) != 0;
//            System.out.println("res (" + i + ") = " + res);
//        }
//        double t2 = System.currentTimeMillis();
//        System.out.println("time = " + (t2-t1));

        // 11
        // 1 + 2 + 0 + 8

    }

    // A music bar :
    //
    // 1   2   3   4   |
    // x-x-x-x-x-x-x-x-| <- A music line
    // ----o-------o---|
    // 0-------0-------|
    //         ^
    //         \
    //          -- A music note


    // 1---
    // <normal>

    // 1-&2---
    // xxx-x
    // <4,4,2>
    // <6
}

