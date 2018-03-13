package tablab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicPartition {

    private String title;
    private String author;
    private ScoreSettings settings;
    private List<MusicBar> musicBars;

    public MusicPartition(String title, String author, ScoreSettings settings) {
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
    public ScoreSettings getSettings() {
        return settings;
    }

    /**
     * Add a music bar in the partition
     * @param musicBar the bar to add
     */
    public void addMusicBar(MusicBar musicBar) {
        musicBars.add(musicBar);
    }

    /**
     * Add a music bar in the partition
     * @param musicBar the bar to add
     */
    public void addMusicBar(int index, MusicBar musicBar) {
        musicBars.add(index, musicBar);
    }

    /**
     * Get the music bar at a precise position in the partition
     * @param index the index of the music bar
     * @return the music bar
     */
    public MusicBar getMusicBar(int index) {
        return musicBars.get(index);
    }

    /** Get all the music bars
     * @return the music bars
     */
    public List<MusicBar> getMusicBars() {
        return musicBars;
    }

    /** Set a music bar in the partition
     * @param index the index of the bar to set
     * @param musicBar the music bar to set
     */
    public void setMusicBar(int index, MusicBar musicBar) {
        musicBars.set(index, musicBar);
    }

    /**
     * Remove a music bar in the partition
     * @param musicBar the bar to remove
     */
    public void removeMusicBar(MusicBar musicBar) {
        musicBars.remove(musicBar);
    }

    /**
     * Remove a music bar in the partition
     * @param index the index of the bar to remove
     */
    public void removeMusicBar(int index) {
        musicBars.remove(index);
    }


    /**
     * Print the partition
     * Use this method for debugging
     */
    public void printPartition() {
        StringBuilder upperLowerString = new StringBuilder("---");
        StringBuilder title1 = new StringBuilder();
        StringBuilder title2 = new StringBuilder();
        StringBuilder notesString = new StringBuilder("--|");
        Map<String, StringBuilder> parts = new HashMap<>();

        // Set the titles :
        title1.append("# Title : ").append(title).append(" -- Author : ").append(author);
        title2.append("# Structure : ").append(settings.notesValue).append("/").append(settings.notesNumber).append(" -- Tempo : ").append((int) (settings.tempo)).append(" bpm");

        // Set the part name :
        for (int i = 0; i < settings.getLinesNumber(); i++) {
            parts.put(settings.lineStructure.get(i), new StringBuilder(settings.lineStructure.get(i).substring(0, 2) + '|'));
        }

        // Fill the tab
        for (MusicBar musicBar : musicBars) {
            for (int beat = 1; beat <= settings.notesNumber; beat++) {
                for (int note = 1; note <= musicBar.getBeatStructure(beat).size(); note++) {

                    notesString.append(note == 1 ? beat : "-");
                    upperLowerString.append("-");
                    for (String lineType : parts.keySet()) {
                        String s = musicBar.isNote(lineType, beat, note) ? "x" : "-";
                        parts.get(lineType).append(s);
                    }
                }
            }
            notesString.append("|");
            upperLowerString.append("-");
            for (StringBuilder part : parts.values()) {
                part.append('|');
            }
        }

        System.out.println(upperLowerString);
        System.out.println(title1);
        System.out.println(title2);
        System.out.println(notesString);
        for (int i = 0; i < settings.getLinesNumber(); i++) {
            System.out.println(parts.get(settings.lineStructure.get(i)));
        }
        System.out.println(upperLowerString);

    }
}
