package tablab;

import java.util.*;

/** The array structure represents all the notes and their respective time in a beat
 * The time of a note are represented with the inverse of the duration of a note (for example 4 is 1/4 of a beat)
 * the sum of the inverse of the number in structure has to be equal to 1
 */
public class BeatStructure extends ArrayList<BeatStructure.NoteTime> {

    // The time of a note
    public enum NoteTime {
        WHOLE_NOTE(1),
        HALF_NOTE(2),
        QUARTER_NOTE(4),
        EIGHTH_NOTE(8),
        SIXTEENTH_NOTE(16),
        THIRTY_SECOND_NOTE(32),
        TRIPLET_NOTE(12),
        QUAVER_TRIPLET_NOTE(24);
        
        private int time;

        NoteTime(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }
    }

    
    public BeatStructure() {
        super();
    }

    public BeatStructure(int initialCapacity) {
        super(initialCapacity);
    }

    public BeatStructure(Collection<? extends NoteTime> c) {
        super();
        this.addAll(c);
    }

    /**
     * Get the evolution of the structure from 0 to 1
     * There is one fraction for each NoteTime
     * Each fraction in the result represent the relative time of the associated NoteTime
     * @return a list of fraction from 0 to 1
     */
    List<Fraction> getFractionEvolution(ScoreSettings settings) {
        List<Fraction> fractions = new ArrayList<>();
        fractions.add(new Fraction(0,1));

        for (int i = 0; i < size()-1; i++ ) {
            Fraction fraction = new Fraction(settings.notesValue, get(i).getTime());
            fraction.add(fractions.get(fractions.size()-1));
            fractions.add(fraction);
        }
        return fractions;
    }

    /**
     * Check the structure integrity, i.e. if the total duration of the notes is equal to 1 (the duration of a beat)
     * @return true if the structure is correct
     */
    public boolean checkStructureIntegrity(ScoreSettings settings) {
        float eps = 0.01f;
        float sum = 0;
        for (NoteTime noteTime : this) {
            sum +=  (float) settings.notesValue / (float) noteTime.getTime();
        }
        return Math.abs(sum - 1.0f) <= eps;
    }
}

