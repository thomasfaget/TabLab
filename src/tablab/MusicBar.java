package tablab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        for (int i = 0; i < settings.notesNumber; i++) {
            MusicBeat musicBeat = new MusicBeat();
            for (String lineType : settings.lineStructure) {
                musicBeat.put(lineType, new Notes());
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
            musicBeat.put(lineType, new Notes());
        }
    }

    /**
     * Add a note at a specific emplacement in the bar
     * @param noteNumber the number of the note (determine with the structure)
     * @param beatNumber the beat number
     * @param lineType the line
     */
    public void addNote(String lineType, int beatNumber, int noteNumber) {
        Notes notes = getNotesAt(lineType, beatNumber);
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
        Notes notes = getNotesAt(lineType, beatNumber);
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
        Notes notes = getNotesAt(lineType, beatNumber);
        return notes != null && notes.isNote(noteNumber);
    }

    /** Get the compressed notes data from at a specific emplacement in the bar
     *
     * @param beatNumber the beat number
     * @param lineType the line
     * @return the notes data
     */
    long getCompressedNotes(String lineType, int beatNumber) {
        Notes notes = getNotesAt(lineType, beatNumber);
        return notes == null ? 0 : notes.getNotes();
    }

    /** Set compressed notes data at a specific emplacement in the bar
     *
     * @param beatNumber the beat number
     * @param lineType the line
     * @param notes the compressed notes to set
     */
    void setCompressedNotes(String lineType, int beatNumber, long notes) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        if (musicBeat != null)
            musicBeat.put(lineType, new Notes(notes));
    }

    /**
     * Check if the music bar has an alternative beat structure for a specific beat number
     * @param beatNumber the beat number
     * @return true if alternative beat structure
     */
    public boolean hasSpecialBeatStructure(int beatNumber) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        return musicBeat != null && musicBeat.hasSpecialBeatStructure();
    }

    /**
     * Add a alternative beat structure in bar for a specific beat number
     * Override the previous structure if there is already a alternative structure
     * Use 'null' as parameter to remove the special structure
     * @param structure the alternative beat structure to use on the beat, set null the remove the current alternative structure
     * @param beatNumber the beat number
     */
    public void setSpecialBeatStructure(BeatStructure structure, int beatNumber) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        if (musicBeat != null) {
            BeatStructure oldBeatStructure = musicBeat.hasSpecialBeatStructure() ? musicBeat.specialBeatStructure : settings.beatStructure;
            musicBeat.specialBeatStructure = structure;

            // Update the notes, to match with the new structure
            if (structure != null && structure != oldBeatStructure) {
                for (String lineType : musicBeat.keySet()) {
                    musicBeat.put(lineType, changeNotesBeatStructure(musicBeat.get(lineType), oldBeatStructure, structure));
                }
            }
            else if (structure == null) {
                for (String lineType : musicBeat.keySet()) {
                    musicBeat.put(lineType, changeNotesBeatStructure(musicBeat.get(lineType), oldBeatStructure, settings.beatStructure));
                }
            }
        }
    }

    /**
     * Return the alternative beat structure of the bar for a specific beat number
     * @param beatNumber the beat number
     * @return the alternative structure
     */
    public BeatStructure getSpecialBeatStructure(int beatNumber) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        return musicBeat == null ? null : musicBeat.specialBeatStructure;
    }

    /**
     * Return the beat structure of the bar for a specific beat number, with checking the alternative structure
     * @param beatNumber the beat number
     * @return the structure of the beat
     */
    public BeatStructure getBeatStructure(int beatNumber) {
        if (hasSpecialBeatStructure(beatNumber)) {
            return getSpecialBeatStructure(beatNumber);
        }
        return settings.beatStructure;
    }


    /**
     * Check if the music bar has an alternative line structure for a specific beat number
     * @param beatNumber the beat number
     * @return true if alternative line structure
     */
    public boolean hasSpecialLineStructure(int beatNumber) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        return musicBeat != null && musicBeat.hasSpecialLineStructure();
    }

    /**
     * Add a alternative line structure in bar for a specific beat number
     * Override the previous structure if there is already a alternative structure
     * Use 'null' as parameter to remove the special structure
     * @param structure the alternative line structure to use on the beat, set null the remove the current alternative structure
     * @param beatNumber the beat number
     */
    public void setSpecialLineStructure(LineStructure structure, int beatNumber) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        if (musicBeat != null) {
            LineStructure oldBeatStructure = musicBeat.hasSpecialLineStructure() ? musicBeat.specialLineStructure : settings.lineStructure;
            MusicBeat newMusicBeat = changeNotesLineStructure(musicBeat, oldBeatStructure, structure);
            newMusicBeat.specialLineStructure = structure;
            musicBeats.set(beatNumber-1, newMusicBeat);
        }
    }

    /**
     * Return the alternative line structure of the bar for a specific beat number
     * @param beatNumber the beat number
     * @return the alternative structure
     */
    public LineStructure getSpecialLineStructure(int beatNumber) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        return musicBeat == null ? null : musicBeat.specialLineStructure;
    }

    /**
     * Return the line structure of the bar for a specific beat number, with checking the alternative structure
     * @param beatNumber the beat number
     * @return the structure of the beat
     */
    public LineStructure getLineStructure(int beatNumber) {
        if (hasSpecialLineStructure(beatNumber)) {
            return getSpecialLineStructure(beatNumber);
        }
        return settings.lineStructure;
    }


    /**
     * Get a copy of the music bar
     * @return A copy of the music bar
     */
    public MusicBar copyMusicBar() {
        MusicBar newMusicBar = new MusicBar(settings);

        for (MusicBeat musicBeat : musicBeats) {
            MusicBeat newMusicBeat = new MusicBeat();
            newMusicBeat.specialBeatStructure = musicBeat.specialBeatStructure;

            for (String lineType : musicBeat.keySet()) {
                Notes notes = musicBeat.get(lineType).copyNotes();
                newMusicBeat.put(lineType, notes);
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
        MusicBeat musicBeat1 = getBeatAt(beatNumberToCopy);
        MusicBeat musicBeat2 = getBeatAt(beatNumberToPaste);

        if (musicBeat1 != null && musicBeat2 != null) {
            for (String lineType : musicBeat1.keySet()) {
                if (musicBeat2.containsKey(lineType)) {
                    musicBeat2.put(lineType, changeNotesBeatStructure(musicBeat1.get(lineType), getBeatStructure(beatNumberToCopy), getBeatStructure(beatNumberToPaste)));
                }
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
        MusicBeat musicBeat1 = getBeatAt(beatNumberToCopy);
        MusicBeat musicBeat2 = getBeatAt(beatNumberToPaste);

        if (musicBeat1 != null && musicBeat2 != null && musicBeat2.containsKey(lineType)) {
            musicBeat2.put(lineType, changeNotesBeatStructure(musicBeat1.get(lineType), getBeatStructure(beatNumberToCopy), getBeatStructure(beatNumberToPaste)));
        }
    }

    /**
     * Get the music beat at a precise beat position
     * @param beatNumber the beat position
     * @return the MusicBeat, null if beatNumber greater than the number of notes
     */
    private MusicBeat getBeatAt(int beatNumber) {
        return beatNumber > settings.notesNumber ? null : musicBeats.get(beatNumber -1);
    }

    /**
     * Get the notes at a precise beat position and line
     * @param lineType the line
     * @param beatNumber the beat position
     * @return the Notes, null if unknown lineType or if beatNumber greater than the number of notes
     */
    private Notes getNotesAt(String lineType, int beatNumber) {
        MusicBeat musicBeat = getBeatAt(beatNumber);
        if (musicBeat == null) {
            return null;
        }
        return musicBeat.get(lineType);
    }

    /**
     * Change the notes to match with the new beat structure
     * @param oldNotes the notes to update
     * @param oldStructure the old structure
     * @param newStructure the new structure
     * @return the new notes
     */
    private Notes changeNotesBeatStructure(Notes oldNotes, BeatStructure oldStructure, BeatStructure newStructure) {

        List<Fraction> oldEvolution = oldStructure.getFractionEvolution(settings);
        List<Fraction> newEvolution = newStructure.getFractionEvolution(settings);
        Notes newNotes = new Notes();

        int oldIndex = 0;
        int newIndex = 0;

        while (oldIndex < oldEvolution.size() && newIndex < newEvolution.size()) {
            if (oldEvolution.get(oldIndex).equal(newEvolution.get(newIndex))) {
                if (oldNotes.isNote(oldIndex + 1)) {
                    newNotes.addNote(newIndex +1);
                }
                oldIndex ++;
                newIndex ++;
            }
            else if (oldEvolution.get(oldIndex).doubleValue() > newEvolution.get(newIndex).doubleValue()) {
                newIndex ++;
            }
            else {
                oldIndex ++;
            }
        }

        return newNotes;
    }

    /**
     * Change the music beat to match with the new line structure
     * @param musicBeat the music beat to update
     * @param oldStructure the old structure
     * @param newStructure the new structure
     * @return the new music beat
     */
    private MusicBeat changeNotesLineStructure(MusicBeat musicBeat, LineStructure oldStructure, LineStructure newStructure) {

        int oldIndex = 0;
        int newIndex = 0;
        MusicBeat newMusicBeat = new MusicBeat();

        while (oldIndex < oldStructure.size() && newIndex < newStructure.size()) {

            if (oldStructure.get(oldIndex).equals(newStructure.get(newIndex))) {
                newMusicBeat.put(oldStructure.get(oldIndex), musicBeat.get(oldStructure.get(oldIndex)));
                oldIndex++;
                newIndex++;
            }
            else if (!oldStructure.get(oldIndex).equals(newStructure.get(newIndex)) && !oldStructure.contains(newStructure.get(newIndex))) {
                newMusicBeat.put(newStructure.get(newIndex), new Notes());
                newIndex++;
            }
            else {
                oldIndex++;
            }
        }
        if (newIndex < newStructure.size()) {
            for (int i = newIndex; i < newStructure.size(); i++) {
                newMusicBeat.put(newStructure.get(i), new Notes());
            }
        }

        return newMusicBeat;
    }

    /**
     * The MusicBeat contains all the information of a music bar on the duration of a beat
     * Coutains Notes and alternative structures
     */
    private class MusicBeat extends HashMap<String, Notes> {

        LineStructure specialLineStructure = null;
        BeatStructure specialBeatStructure = null;

        boolean hasSpecialBeatStructure() {
            return specialBeatStructure != null;
        }

        boolean hasSpecialLineStructure() {
            return specialLineStructure != null;
        }
    }
}
