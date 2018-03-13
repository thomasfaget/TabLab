package tablab;


import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class XmlManager {

    private static String SEP = "//";

    // Index title
    private static String PARTITION = "partition";
    private static String TITLE = "title";
    private static String AUTHOR = "author";
    private static String BARS = "bars";
    private static String SETTINGS = "settings";
    private static String NOTES_NUMBER = "notesNumber";
    private static String NOTES_VALUE = "notes_value";
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

        settings.setAttribute(NOTES_NUMBER, String.valueOf(scoreSettings.notesNumber));
        settings.setAttribute(NOTES_VALUE, String.valueOf(scoreSettings.notesValue));
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

        for (int beat = 1; beat <= scoreSettings.notesNumber; beat++) {
            Element b = new Element(BEAT);
            b.setAttribute(NUMBER, String.valueOf(beat));

            if (musicBar.hasSpecialStructure(beat)) {
                b.setAttribute(BEAT_STRUCTURE, getBeatStructureAsString(musicBar.getSpecialStructure(beat)));
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

    /** Read a music partition from a xml file
     *
     * @param path the path of the partition
     * @return A music partition
     * @throws JDOMException is thrown if problem with the given path
     * @throws IOException is thrown if problem with the given path
     */
    public static MusicPartition readPartitionFromXmlFile(String path) throws JDOMException, IOException {
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(new File(path));

        Element racine = document.getRootElement();
        return createPartition(racine);
    }

    private static MusicPartition createPartition(Element partition) {

        String title = partition.getAttributeValue(TITLE);
        String author = partition.getAttributeValue(AUTHOR);
        ScoreSettings scoreSettings = createSettings(partition.getChild(SETTINGS));

        MusicPartition musicPartition = new MusicPartition(title, author, scoreSettings);

        List<Element> bars = partition.getChild(BARS).getChildren(MUSIC_BAR);
        bars = bars.stream().sorted(Comparator.comparingInt(bar -> Integer.parseInt(bar.getAttributeValue(NUMBER)))).collect(Collectors.toList());
        for (Element bar : bars) {
            musicPartition.addMusicBar(createMusicBar(bar, scoreSettings));
        }

        return musicPartition;
    }

    private static ScoreSettings createSettings(Element settings) {
        ScoreSettings scoreSettings = new ScoreSettings();
        scoreSettings.notesNumber = Integer.parseInt(settings.getAttributeValue(NOTES_NUMBER));
        scoreSettings.notesValue = Integer.parseInt(settings.getAttributeValue(NOTES_VALUE));
        scoreSettings.tempo = Float.parseFloat(settings.getAttributeValue(TEMPO));

        scoreSettings.lineStructure = createLineStructure(settings.getChild(LINE_STRUCTURE));
        scoreSettings.beatStructure = createBeatStructure(settings.getChild(BEAT_STRUCTURE));

        return scoreSettings;
    }

    private static LineStructure createLineStructure(Element structure) {
        String line = structure.getContent(0).getValue();
        LineStructure lineStructure = new LineStructure();
        lineStructure.addAll(Arrays.asList(line.split(SEP)));
        return lineStructure;
    }

    private static BeatStructure createBeatStructure(Element structure) {
        String beat = structure.getContent(0).getValue();
        return createBeatStructureFromString(beat);
    }

    private static BeatStructure createBeatStructureFromString(String structure) {
        BeatStructure beatStructure = new BeatStructure();
        for (String s : structure.split(SEP)) {
            beatStructure.add(BeatStructure.NoteTime.valueOf(s));
        }
        return beatStructure;
    }

    private static MusicBar createMusicBar(Element bar, ScoreSettings scoreSettings) {
        MusicBar musicBar = new MusicBar(scoreSettings);
        musicBar.createEmptyBar();

        List<Element> beats = bar.getChildren(BEAT);
        beats = beats.stream().sorted(Comparator.comparingInt(beat -> Integer.parseInt(beat.getAttributeValue(NUMBER)))).collect(Collectors.toList());
        for (int i = 0; i < beats.size(); i++) {
            Element beat = beats.get(i);
            Attribute att = beat.getAttribute(BEAT_STRUCTURE);
            if (att != null) {
                musicBar.setSpecialStructure(createBeatStructureFromString(att.getValue()), i+1);
            }
            for (Element line : beat.getChildren(LINE)) {
                long notes = Long.parseLong(line.getContent(0).getValue());
                musicBar.setCompressedNotes(line.getAttributeValue(PART), i+1, notes);
            }
        }

        return musicBar;
    }
}