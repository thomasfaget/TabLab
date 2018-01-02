package tablab;

public class Notes {

    // The notes of a beat are encoding in an integer (long)
    // Each bit of the integer indicate if there is or not a note at the position
    // The number of note (i.e. the number of bits used) is determined uphill
    private long notes = 0b0;

    /**
     * Add a note the position given
     * Do nothing id already present at the position
     * @param noteNumber the position > 0
     */
    public void addNote(int noteNumber) {
        long note = (long) Math.pow(2,noteNumber-1);
        notes |= note;

    }

    /**
     * remove a note the position given
     * Do nothing if any note at the position
     * @param noteNumber the position > 0
     */
    public void removeNote(int noteNumber) {
        long note = (long) Math.pow(2,noteNumber-1);
        notes &= (~note);
    }


    /**
     * Determine if there is a note at the given position
     * @param noteNumber the position > 0
     * @return if there is a note at the given position
     */
    public boolean isNote(int noteNumber) {
        long note = (long) Math.pow(2,noteNumber-1);
        return (notes & note) != 0;
    }

    /**
     * Get a copy of the notes
     * @return A copy of the notes
     */
    public Notes copyNotes() {
        Notes n = new Notes();
        n.notes = this.notes;
        return n;
    }
}
