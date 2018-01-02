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
     * print the partition
     */
    public void printPartition() {
        StringBuilder upperLowerString = new StringBuilder("---");
        StringBuilder notesString = new StringBuilder("--|");
        Map<LineType, StringBuilder> parts = new HashMap<>();

        // Set the part name :
        for (int i = 0; i < settings.getLinesNumber(); i++) {
            parts.put(settings.lineTypes.get(i), new StringBuilder(settings.lineTypes.get(i).toString().substring(0, 2) + '|'));
        }

        // Fill the tab
        for (MusicBar musicBar : musicBars) {
            for (int beat = 1; beat <= settings.pitch; beat++) {
                for (int note = 1; note <= musicBar.getBeatStructure(beat).getNotesNumber(); note++) {

                    notesString.append(note == 1 ? beat : "-");
                    upperLowerString.append("-");
                    for (LineType lineType : parts.keySet()) {
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
        System.out.println(notesString);
        for (int i = 0; i < settings.getLinesNumber(); i++) {
            System.out.println(parts.get(settings.lineTypes.get(i)));
        }
        System.out.println(upperLowerString);

    }
}