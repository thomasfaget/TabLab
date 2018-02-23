package tablab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MusicBar represent a bar of partition.
 * Contains all the notes of bar, on all the lines
 */
public class MusicBar {

    private List<MusicBeat> musicBeats;
    private ScoreSettings settings;

    public MusicBar(ScoreSettings settings) {
        this.musicBeats = new ArrayList<>();
        this.settings = settings;
    }

    /** Create a empty MusicBar, with the data in the setting object
     */
    public void createEmptyBar() {
        for (int i = 0; i < settings.pitch; i++) {
            MusicBeat musicBeat = new MusicBeat();
            for (String lineType : settings.lineStructure) {
                musicBeat.beatNotes.put(lineType, new Notes());
            }
            musicBeats.add(musicBeat);
        }
    }

    /**
     * Add a empty line in the music bar
     * @param lineType the type of the line
     */
    public void addEmptyLine(String lineType) {
        for (MusicBeat musicBeat : musicBeats) {
            musicBeat.beatNotes.put(lineType, new Notes());
        }
    }

    /**
     * Add a note at a specific emplacement in the bar
     * @param noteNumber the number of the note (determine with the structure)
     * @param beatNumber the beat number
     * @param lineType the line
     */
    public void addNote(String lineType, int beatNumber, int noteNumber) {
        Notes notes = musicBeats.get(beatNumber - 1).beatNotes.get(lineType);
        if (notes != null) {
            notes.addNote(noteNumber);
        }
    }

    /**
     * Remove a note at a specific emplacement in the bar
     * @param noteNumber the number of the note (determine with the structure)
     * @param beatNumber the beat number
     * @param lineType the line
     */
    public void removeNote(String lineType, int beatNumber, int noteNumber) {
        Notes notes = musicBeats.get(beatNumber - 1).beatNotes.get(lineType);
        if (notes != null) {
            notes.removeNote(noteNumber);
        }
    }

    /**
     * Determine if there is a note at a specific emplacement in the bar
     * @param noteNumber the number of the note (determine with the structure)
     * @param beatNumber the beat number
     * @param lineType the line
     * @return If there is a note at the given position
     */
    public boolean isNote(String lineType, int beatNumber, int noteNumber) {
        Notes notes = musicBeats.get(beatNumber - 1).beatNotes.get(lineType);
        return notes != null && notes.isNote(noteNumber);
    }

    /** Get the compressed notes data from at a specific emplacement in the bar
     *
     * @param beatNumber the beat number
     * @param lineType the line
     * @return the notes data
     */
    long getCompressedNotes(String lineType, int beatNumber) {
        Notes notes = musicBeats.get(beatNumber - 1).beatNotes.get(lineType);
        return notes.getNotes();
    }

    /** Set compressed notes data at a specific emplacement in the bar
     *
     * @param beatNumber the beat number
     * @param lineType the line
     * @param notes the compressed notes to set
     */
    void setCompressedNotes(String lineType, int beatNumber, long notes) {
        musicBeats.get(beatNumber -1).beatNotes.put(lineType, new Notes(notes));
    }

    /**
     * Check if the music bar has an alternative structureor a specific beat number
     * @param beatNumber the beat number
     * @return true if alternative structure
     */
    public boolean hasSpecialStructure(int beatNumber) {
        return musicBeats.get(beatNumber - 1).hasSpecialStructure();

    }

    /**
     * Add a alternative structure in bar for a specific beat number
     * Override the previous structure if there is already a alternative structure
     * Use 'null' as parameter to remove the special structure
     * @param beatNumber the beat number
     */
    public void setSpecialStructure(BeatStructure structure, int beatNumber) {
        MusicBeat musicBeat = musicBeats.get(beatNumber - 1);
        BeatStructure oldBeatStructure = musicBeat.hasSpecialStructure() ? musicBeat.specialStructure : settings.beatStructure;
        musicBeat.specialStructure = structure;

        // Update the notes, to match with the new structure
        for (String lineType : musicBeat.beatNotes.keySet()) {
            musicBeat.beatNotes.put(lineType, changeNotesStructure(musicBeat.beatNotes.get(lineType), oldBeatStructure, structure));
        }
    }

    /**
     * Return the alternative structure of the bar for a specific beat number
     * @param beatNumber the beat number
     * @return the alternative structure
     */
    public BeatStructure getSpecialStructure(int beatNumber) {
        return musicBeats.get(beatNumber - 1).specialStructure;
    }

    /**
     * Return the structure of the bar for a specific beat number, with checking the alternative structure
     * @param beatNumber the beat number
     * @return the structure of the beat
     */
    public BeatStructure getBeatStructure(int beatNumber) {
        if (hasSpecialStructure(beatNumber)) {
            return getSpecialStructure(beatNumber);
        }
        return settings.beatStructure;
    }

    /**
     * Get a copy of the music bar
     * @return A copy of the music bar
     */
    public MusicBar copyMusicBar() {
        MusicBar newMusicBar = new MusicBar(settings);

        for (MusicBeat musicBeat : musicBeats) {
            MusicBeat newMusicBeat = new MusicBeat();
            newMusicBeat.specialStructure = musicBeat.specialStructure;

            for (String lineType : musicBeat.beatNotes.keySet()) {
                Notes notes = musicBeat.beatNotes.get(lineType).copyNotes();
                newMusicBeat.beatNotes.put(lineType, notes);
            }

            newMusicBar.musicBeats.add(newMusicBeat);
        }
        return newMusicBar;
    }

    /**
     * Copy a beat to a specific position
     * Copy all the lines.
     * Do not copy the alternative structure !
     * @param beatNumberToCopy The beat position to copy
     * @param beatNumberToPaste The beat position to paste
     */
    public void copyBeat(int beatNumberToCopy, int beatNumberToPaste) {
        MusicBeat musicBeat1 = musicBeats.get(beatNumberToCopy - 1);
        MusicBeat musicBeat2 = musicBeats.get(beatNumberToPaste - 1);

        for (String lineType : musicBeat1.beatNotes.keySet()) {
            if (musicBeat2.beatNotes.containsKey(lineType)) {
                musicBeat2.beatNotes.put(lineType, changeNotesStructure(musicBeat1.beatNotes.get(lineType), getBeatStructure(beatNumberToCopy), getBeatStructure(beatNumberToPaste)));
            }
        }
    }

    /**
     * Copy a beat to a specific position and a specific line
     * Do not copy the alternative structure !
     * @param lineType the line to copy
     * @param beatNumberToCopy The beat position to copy
     * @param beatNumberToPaste The beat position to paste
     */
    public void copyBeat(String lineType, int beatNumberToCopy, int beatNumberToPaste) {
        MusicBeat musicBeat1 = musicBeats.get(beatNumberToCopy - 1);
        MusicBeat musicBeat2 = musicBeats.get(beatNumberToPaste - 1);

        if (musicBeat2.beatNotes.containsKey(lineType)) {
            musicBeat2.beatNotes.put(lineType, changeNotesStructure(musicBeat1.beatNotes.get(lineType), getBeatStructure(beatNumberToCopy), getBeatStructure(beatNumberToPaste)));
        }
    }

    /**
     * Change the notes to match with the new structure
     * @param oldNotes the notes to update
     * @param oldStructure the old structure
     * @param newStructure the new structure
     * @return the new notes
     */
    private Notes changeNotesStructure(Notes oldNotes, BeatStructure oldStructure, BeatStructure newStructure) {
        int nbTimes = newStructure.size();
        int oldTimeInd = 0;
        int nbSkipInd = 0;

        Notes newNotes = new Notes();

        for (int timeInd = 0; timeInd < nbTimes; timeInd++) {

            BeatStructure.NoteTime newTime = newStructure.get(timeInd);
            BeatStructure.NoteTime oldTime = oldStructure.get(oldTimeInd);
            double delta = oldTime.getTime();

            // Copy the note at the current emplacement (if present)
            if (nbSkipInd == 0 && oldNotes.isNote(oldTimeInd + 1)) {
                newNotes.addNote(timeInd + 1);
            }

            // Find the next note in the old structure (update the index)
            for (int skipInd = 1; skipInd <= nbSkipInd; skipInd++) {
                if (oldTimeInd + skipInd < oldStructure.size())
                    delta += oldStructure.get(oldTimeInd + skipInd).getTime();
            }
            delta /= newTime.getTime();

            if (delta == Math.floor(delta)) {
                oldTimeInd += (delta);
                nbSkipInd = 0;
            } else {
                nbSkipInd++;
            }

        }
        return newNotes;

    }

    // All the notes (all the lines) on the duration of a beat
    private class MusicBeat {

        BeatStructure specialStructure = null;
        Map<String, Notes> beatNotes = new HashMap<>();

        boolean hasSpecialStructure() {
            return specialStructure != null;
        }
    }
}
