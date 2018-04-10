package tablab;

import tablab.partitionListener.PartitionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class MusicPartition {

    private String title;
    private String author;
    private PartitionSettings settings;
    private List<MusicBar> musicBars;
    private PartitionListenerList listenerList = new PartitionListenerList();

    public MusicPartition(String title, String author, PartitionSettings settings) {
        this.title = title;
        this.author = author;
        this.settings = settings;
        this.musicBars = new ArrayList<>();
    }

    /**
     * Get the title of the partition
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the author of the partition
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Get the settings of the partition
     * @return the settings
     */
    public PartitionSettings getSettings() {
        return settings;
    }

    /**
     * Get the number of Music bars
     * @return the number of music bar
     */
    public int getMusicBarNumber() {
        return musicBars.size();
    }

    /**
     * Add a music bar in the partition
     * @param musicBar the bar to add
     */
    public void addMusicBar(MusicBar musicBar) {
        musicBars.add(musicBar);
        listenerList.notifyAllAddedMusicBar(musicBars.size());
    }

    /**
     * Add a music bar in the partition
     * @param musicBar the bar to add
     */
    public void addMusicBar(int index, MusicBar musicBar) {
        musicBars.add(index-1, musicBar);
        listenerList.notifyAllAddedMusicBar(index);

    }

    /**
     * Get the music bar at a precise position in the partition
     * @param index the index of the music bar
     * @return the music bar
     */
    public MusicBar getMusicBar(int index) {
        return musicBars.get(index-1);
    }

    /** Set a music bar in the partition
     * @param index the index of the bar to set
     * @param musicBar the music bar to set
     */
    public void setMusicBar(int index, MusicBar musicBar) {
        musicBars.set(index-1, musicBar);
        listenerList.notifyAllAddedMusicBar(index);
    }

    /**
     * Remove a music bar in the partition
     * @param musicBar the bar to remove
     */
    public void removeMusicBar(MusicBar musicBar) {
        int index = musicBars.indexOf(musicBar);
        if (musicBars.remove(musicBar)) {
            listenerList.notifyAllRemovedMusicBar(index);
        }
    }

    /**
     * Remove a music bar in the partition
     * @param index the index of the bar to remove
     */
    public void removeMusicBar(int index) {
        musicBars.remove(index-1);
        listenerList.notifyAllRemovedMusicBar(index);
    }

    /**
     * Add a listener to the partition, the listener is called when a music bar is added or removed
     * @param listener the listener to add
     */
    public void addPartitionListener(PartitionListener listener) {
        listenerList.addListener(listener);
    }

    /**
     * Remove a listener to the partition, the listener is called when a music bar is added or removed
     * @param listener the listener to remove
     */
    public void removePartitionListener(PartitionListener listener) {
        listenerList.removeListener(listener);
    }


    @Override
    public String toString() {
        StringBuilder upperLowerString = new StringBuilder("---");
        StringBuilder title1 = new StringBuilder();
        StringBuilder title2 = new StringBuilder();
        StringBuilder notesString = new StringBuilder("--|");
        Map<String, StringBuilder> parts = new HashMap<>();

        // Create the full line structure :
        LineStructure commonLineStructure = new LineStructure(Collections.<String>emptyList());
        for (MusicBar musicBar : musicBars) {
            for (int i = 1; i <= settings.notesNumber; i++) {
                LineStructure structure = musicBar.getLineStructure(i);
                commonLineStructure = commonLineStructure.getUnion(structure);
            }
        }

        // Set the titles :
        title1.append("# Title : ").append(title).append(" -- Author : ").append(author);
        title2.append("# Structure : ").append(settings.notesNumber).append("/").append(settings.notesValue).append(" -- Tempo : ").append((int) (settings.tempo)).append(" bpm");

        // Set the part name :
        for (String line : commonLineStructure) {
            parts.put(line, new StringBuilder(line.substring(0, 2) + '|'));
        }

        // Fill the tab
        for (MusicBar musicBar : musicBars) {
            upperLowerString.append("-");
            for (int beat = 1; beat <= settings.notesNumber; beat++) {
                for (int note = 1; note <= musicBar.getBeatStructure(beat).size(); note++) {

                    notesString.append(note == 1 ? beat : "-");
                    upperLowerString.append("-");
                    for (String lineType : commonLineStructure) {
                        String s = musicBar.isNote(lineType, beat, note) ? "x" : "-";
                        parts.get(lineType).append(s);
                    }
                }
            }
            notesString.append("|");
            for (StringBuilder part : parts.values()) {
                part.append('|');
            }
        }

        StringBuilder string = new StringBuilder(upperLowerString).append('\n');
        string.append(title1).append('\n');
        string.append(title2).append('\n');
        string.append(notesString).append('\n');
        for (String line : commonLineStructure) {
            string.append(parts.get(line)).append('\n');
        }
        string.append(upperLowerString).append('\n');

        return String.valueOf(string);

    }

    /** A class to handle the listeners
     */
    private class PartitionListenerList {

        private List<PartitionListener> partitionListeners = new ArrayList<>();

        void addListener(PartitionListener listener) {
            partitionListeners.add(listener);
        }

        void removeListener(PartitionListener listener) {
            partitionListeners.remove(listener);
        }

        void notifyAllAddedMusicBar(int index) {
            for (PartitionListener listener : partitionListeners) {
                listener.addedMusicBar(index);
            }
        }
        void notifyAllRemovedMusicBar(int index) {
            for (PartitionListener listener : partitionListeners) {
                listener.removedMusicBar(index);
            }
        }
    }
}
