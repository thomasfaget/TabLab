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
            for (LineType lineType : settings.lineTypes) {
                musicBeat.beatNotes.put(lineType, new Notes());
            }
            musicBeats.add(musicBeat);
        }
    }

    /**
     * Add a empty line in the music bar
     * @param lineType the type of the line
     */
    public void addEmptyLine(LineType lineType) {
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
    public void addNote(LineType lineType, int beatNumber, int noteNumber) {
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
    public void removeNote(LineType lineType, int beatNumber, int noteNumber) {
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
    public boolean isNote(LineType lineType, int beatNumber, int noteNumber) {
        Notes notes = musicBeats.get(beatNumber - 1).beatNotes.get(lineType);
        return notes != null && notes.isNote(noteNumber);
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
        BeatStructure oldBeatStructure = settings.beatStructure == null ? musicBeats.get(beatNumber - 1).specialStructure : settings.beatStructure;
        musicBeats.get(beatNumber - 1).specialStructure = structure;

        // Update the note, to match with the new structure
        // TODO
        /*
        for (LineType lineType : musicBeats.get(beatNumber).beatNotes.keySet()) {
            Notes notes = musicBeats.get(beatNumber).beatNotes.get(lineType);
            Notes newNotes = new Notes();
            for (int i = 0; i < structure.getNotesNumber(); i++) {

            }
        }
        */
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

            for (LineType lineType : musicBeat.beatNotes.keySet()) {
                Notes notes = musicBeat.beatNotes.get(lineType).copyNotes();
                newMusicBeat.beatNotes.put(lineType, notes);
            }

            newMusicBar.musicBeats.add(newMusicBeat);
        }
        return newMusicBar;
    }


    // All the notes (all the lines) on the duration of a beat
    private class MusicBeat {

        BeatStructure specialStructure = null;
        Map<LineType, Notes> beatNotes = new HashMap<>();

        boolean hasSpecialStructure() {
            return specialStructure != null;
        }
    }
}
