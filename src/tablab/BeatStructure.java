package tablab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static tablab.BeatStructure.NoteTime.*;

public class BeatStructure {

    // The time of a note
    public enum NoteTime {
        SIMPLE_NOTE(1),
        EIGHTH_NOTE(2),
        SIXTEENTH_NOTE(4),
        THIRTY_SECOND_NOTE(8),
        TRIPLET_NOTE(3),
        QUAVER_TRIPLET_NOTE(6);
        
        private int time;

        NoteTime(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }
    }
    
    
    // The array structure represents all the notes and their respective time in a beat
    // The time of a note are represented with the inverse of the duration of a note (for example 4 is 1/4 of a beat)
    // the sum of the inverse of the number in structure has to be equal to 1
    private List<NoteTime> structure;
    
    public BeatStructure() {
        structure = new ArrayList<>();
    }

    public BeatStructure(List<NoteTime> structure) {
        this.structure = structure;
    }

    /**
     * Set the structure
     * @param structure the structure
     */
    public void setStructure(List<NoteTime> structure) {
        this.structure = structure;
    }

    /**
     * Get the structure
     * @return the structure
     */
    public List<NoteTime> getStructure() {
        return structure;
    }

    /**
     * Get a note at a specific position
     * @param index index
     * @return the note time
     */
    public NoteTime getNoteTime(int index) {
        return structure.get(index);
    }

    /**
     * Get the total of the notes in the structure
     * @return the number of notes
     */
    public int getNotesNumber() {
        return structure.size();
    }

    /**
     * Check the structure integrity, i.e. if the total duration of the notes is equal to 1 (the duration of a beat)
     * @return true if the structure is correct
     */
    public boolean checkStructureIntegrity() {
        float eps = 0.01f;
        float sum = 0;
        for (NoteTime noteTime : structure) {
            sum +=  1/(float) noteTime.getTime();
        }
        return Math.abs(sum - 1.0f) <= eps;
    }
}

