package tablab;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileOutputStream;
import java.io.IOException;

public class XmlManager {

    private static String SEP = "//";

    // Index title
    private static String PARTITION = "partition";
    private static String TITLE = "title";
    private static String AUTHOR = "author";
    private static String BARS = "bars";
    private static String SETTINGS = "settings";
    private static String PITCH = "pitch";
    private static String PITCH_VALUE = "pitch_value";
    private static String TEMPO = "tempo";
    private static String LINE_STRUCTURE = "line_struct";
    private static String BEAT_STRUCTURE = "beat_struct";
    private static String MUSIC_BAR = "bar";
    private static String BEAT = "beat";
    private static String NUMBER = "number";
    private static String PART = "part";
    private static String LINE = "line";


    /** Create a xml file and write the music partition
     *
     * @param path the path (path+file name) of the xml file to create
     * @param musicPartition the music partition to write
     * @throws IOException is thrown if problem with the given path
     */
    public static void writePartitionToXmlFile(String path, MusicPartition musicPartition) throws IOException {

        Element racine = getPartition(musicPartition);
        Document document = new Document(racine);

        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileOutputStream(path));

    }

    private static Element getPartition(MusicPartition musicPartition) {
        Element partition = new Element(PARTITION);

        partition.setAttribute(TITLE, musicPartition.getTitle());
        partition.setAttribute(AUTHOR, musicPartition.getAuthor());


        Element settings = getSettings(musicPartition.getSettings());
        partition.addContent(settings);

        Element bars = new Element(BARS);
        for (int i = 0; i < musicPartition.getMusicBars().size(); i++) {
            Element bar = getMusicBar(musicPartition.getMusicBar(i), musicPartition.getSettings());
            bar.setAttribute(NUMBER, String.valueOf(i+1));
            bars.addContent(bar);
        }
        partition.addContent(bars);

        return partition;
    }

    private static Element getSettings(ScoreSettings scoreSettings) {
        Element settings = new Element(SETTINGS);

        settings.setAttribute(SETTINGS, String.valueOf(scoreSettings.pitch));
        settings.setAttribute(PITCH_VALUE, String.valueOf(scoreSettings.noteValue));
        settings.setAttribute(TEMPO, String.valueOf(scoreSettings.tempo));

        Element bs = getBeatStructure(scoreSettings.beatStructure);
        settings.addContent(bs);

        Element ls = getLineStructure(scoreSettings.lineStructure);
        settings.addContent(ls);

        return settings;
    }

    private static Element getLineStructure(LineStructure lineStructure) {
        Element ls = new Element(LINE_STRUCTURE);
        ls.addContent(String.join(SEP, lineStructure));
        return ls;
    }

    private static Element getBeatStructure(BeatStructure beatStructure) {
        Element bs = new Element(BEAT_STRUCTURE);
        bs.addContent(getBeatStructureAsString(beatStructure));
        return bs;
    }

    private static String getBeatStructureAsString(BeatStructure beatStructure) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < beatStructure.size(); i++) {
            if (i > 0)
                str.append(SEP);
            str.append(beatStructure.get(i));
        }
        return String.valueOf(str);
    }

    private static Element getMusicBar(MusicBar musicBar, ScoreSettings scoreSettings) {
        Element bar = new Element(MUSIC_BAR);

        for (int beat = 1; beat <= scoreSettings.pitch; beat++) {
            Element b = new Element(BEAT);
            b.setAttribute(NUMBER, String.valueOf(beat));

            if (musicBar.hasSpecialStructure(beat)) {
                b.setAttribute(LINE_STRUCTURE, getBeatStructureAsString(musicBar.getSpecialStructure(beat)));
            }

            for (String lineType : scoreSettings.lineStructure) {
                Element line = new Element(LINE);
                line.setAttribute(PART, lineType);

                line.addContent(String.valueOf(musicBar.getCompressedNotes(lineType, beat)));
                b.addContent(line);
            }
            bar.addContent(b);
        }

        return bar;
    }
}