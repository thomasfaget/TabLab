package tablab;

import tablab.partitionListener.BeatStructureListener;
import tablab.partitionListener.LineStructureListener;
import tablab.partitionListener.NoteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MusicBar represent a bar of partition.
 * Contains all the notes of bar, on all the lines
 */
public class MusicBar {

    private List<MusicBeat> musicBeats;
    private PartitionSettings settings;
    private MusicBarListenerList listenerList = new MusicBarListenerList();

    public MusicBar(PartitionSettings settings) {
        this.musicBeats = new ArrayList<>();
        this.settings = settings;
        createEmptyBar();
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
            listenerList.notifyAllAddedNote(this, lineType, beatNumber, noteNumber);
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
            listenerList.notifyAllRemovedNote(this, lineType, beatNumber, noteNumber);
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
        if (musicBeat != null) {
            Notes oldNotes = getNotesAt(lineType, beatNumber);
            Notes newNotes = new Notes(notes);
            musicBeat.put(lineType, newNotes);

            // Listeners
            handleListenersOnNotesCopy(oldNotes, newNotes, lineType, beatNumber, getBeatStructure(beatNumber).size());
        }
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
            BeatStructure oldBeatStructure = getBeatStructure(beatNumber);
            musicBeat.specialBeatStructure = structure;

            // Update the notes, to match with the new structure
            if (structure != null && structure != oldBeatStructure) {
                for (String lineType : musicBeat.keySet()) {
                    musicBeat.put(lineType, changeNotesBeatStructure(musicBeat.get(lineType), oldBeatStructure, structure));
                }
            } else if (structure == null) {
                for (String lineType : musicBeat.keySet()) {
                    musicBeat.put(lineType, changeNotesBeatStructure(musicBeat.get(lineType), oldBeatStructure, settings.beatStructure));
                }
            }

            // Handle listeners
            if (structure != null && !structure.equals(oldBeatStructure)) {
                // structure added or a new is set
                listenerList.notifyAllAddedBeatStructure(this, beatNumber);
            }
            if (structure == null && oldBeatStructure != null) {
                // the structure is removed
                listenerList.notifyAllRemovedBeatStructure(this, beatNumber);
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
            LineStructure oldLineStructure = getLineStructure(beatNumber);
            MusicBeat newMusicBeat = changeNotesLineStructure(musicBeat, oldLineStructure, structure);
            newMusicBeat.specialLineStructure = structure;
            musicBeats.set(beatNumber-1, newMusicBeat);

            // Handle listeners
            if (structure != null && !structure.equals(oldLineStructure)) {
                // structure added or a new is set
                listenerList.notifyAllAddedLineStructure(this, beatNumber);
            }
            if (structure == null) {
                // the structure is removed
                listenerList.notifyAllRemovedLineStructure(this, beatNumber);
            }
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

        List<String> lines = new ArrayList<>();
        List<Notes> oldNotesList = new ArrayList<>();
        List<Notes> newNotesList = new ArrayList<>();

        if (musicBeat1 != null && musicBeat2 != null) {
            for (String lineType : getLineStructure(beatNumberToPaste)) {
                if (getLineStructure(beatNumberToCopy).contains(lineType) && musicBeat2.containsKey(lineType) && musicBeat1.containsKey(lineType)) {
                    Notes oldNotes = getNotesAt(lineType, beatNumberToPaste);
                    Notes newNotes = changeNotesBeatStructure(musicBeat1.get(lineType), getBeatStructure(beatNumberToCopy), getBeatStructure(beatNumberToPaste));

                    // Add a copy of old et new notes (to handle the listeners)
                    if (oldNotes != null && newNotes != null) {
                        lines.add(lineType);
                        oldNotesList.add(oldNotes.copyNotes());
                        newNotesList.add(newNotes.copyNotes());
                    }

                    musicBeat2.put(lineType, newNotes);
                }
            }
            // Handle listeners
            for (int i = 0; i < lines.size(); i++) {
                String lineType = lines.get(i);
                Notes oldNotes = oldNotesList.get(i);
                Notes newNotes = newNotesList.get(i);
                handleListenersOnNotesCopy(oldNotes, newNotes, lineType, beatNumberToPaste, getBeatStructure(beatNumberToPaste).size());
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
            Notes oldNotes = getNotesAt(lineType, beatNumberToPaste);
            Notes newNotes = changeNotesBeatStructure(musicBeat1.get(lineType), getBeatStructure(beatNumberToCopy), getBeatStructure(beatNumberToPaste));
            musicBeat2.put(lineType, newNotes);
            // Handle listeners :
            handleListenersOnNotesCopy(oldNotes, newNotes, lineType, beatNumberToPaste, getBeatStructure(beatNumberToPaste).size());
        }
    }

    public void addBeatStructureListener(BeatStructureListener listener) {
        listenerList.addListener(listener);
    }

    public void removeBeatStructureListener(BeatStructureListener listener) {
        listenerList.removeListener(listener);
    }

    public void addLineStructureListener(LineStructureListener listener) {
        listenerList.addListener(listener);
    }

    public void removeLineStructureListener(LineStructureListener listener) {
        listenerList.removeListener(listener);
    }

    public void addNoteListener(NoteListener listener) {
        listenerList.addListener(listener);
    }

    public void removeNoteListener(NoteListener listener) {
        listenerList.removeListener(listener);
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

    /** handle the listener calls when copy notes on a entire beat
     *
     * @param oldNotes the notes before the copy
     * @param newNotes the notes after the copy
     * @param lineType the line type
     * @param beatNumber the beat index
     * @param nbNotes the number of notes in the beat
     */
    private void handleListenersOnNotesCopy(Notes oldNotes, Notes newNotes, String lineType, int beatNumber, int nbNotes) {
        for (int note = 1; note <= nbNotes; note++) {
            // Note addition
            if (!oldNotes.isNote(note) && newNotes.isNote(note)) {
                listenerList.notifyAllAddedNote(this, lineType, beatNumber, note);
            }
            // Note deletion
            else if (oldNotes.isNote(note) && !newNotes.isNote(note)) {
                listenerList.notifyAllRemovedNote(this, lineType, beatNumber, note);
            }
        }
    }

    /**
     * Create a empty MusicBar, according to the data contained in the settings (number of beats, types of line, ect..)
     */
    private void createEmptyBar() {
        for (int i = 0; i < settings.notesNumber; i++) {
            MusicBeat musicBeat = new MusicBeat();
            for (String lineType : settings.lineStructure) {
                musicBeat.put(lineType, new Notes());
            }
            musicBeats.add(musicBeat);
        }
    }

    /**
     * The MusicBeat contains all the information of a music bar on the duration of a beat
     * Contains Notes and alternative structures
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

    /** A class to handle the listeners
     */
    private class MusicBarListenerList {

        private List<BeatStructureListener> beatStructureListeners = new ArrayList<>();
        private List<LineStructureListener> lineStructureListeners = new ArrayList<>();
        private List<NoteListener> noteListeners = new ArrayList<>();

        void addListener(BeatStructureListener listener) {
            beatStructureListeners.add(listener);
        }
        void removeListener(BeatStructureListener listener) {
            beatStructureListeners.remove(listener);
        }

        void addListener(LineStructureListener listener) {
            lineStructureListeners.add(listener);
        }
        void removeListener(LineStructureListener listener) {
            lineStructureListeners.remove(listener);
        }

        void addListener(NoteListener listener) {
            noteListeners.add(listener);
        }
        void removeListener(NoteListener listener) {
            noteListeners.remove(listener);
        }

        void notifyAllAddedBeatStructure(MusicBar source, int beat) {
            for (BeatStructureListener listener : beatStructureListeners) {
                listener.addedBeatStructure(source, beat);
            }
        }
        void notifyAllRemovedBeatStructure(MusicBar source, int beat) {
            for (BeatStructureListener listener : beatStructureListeners) {
                listener.removedBeatStructure(source, beat);
            }
        }

        void notifyAllAddedLineStructure(MusicBar source, int beat) {
            for (LineStructureListener listener : lineStructureListeners) {
                listener.addedLineStructure(source, beat);
            }
        }
        void notifyAllRemovedLineStructure(MusicBar source, int beat) {
            for (LineStructureListener listener : lineStructureListeners) {
                listener.addedLineStructure(source, beat);
            }
        }

        void notifyAllAddedNote(MusicBar source, String lineType, int beat, int note) {
            for (NoteListener listener : noteListeners) {
                listener.addedNote(source, lineType, beat, note);
            }
        }
        void notifyAllRemovedNote(MusicBar source, String lineType, int beat, int note) {
            for (NoteListener listener : noteListeners) {
                listener.removedNote(source, lineType, beat, note);
            }
        }
    }
}
