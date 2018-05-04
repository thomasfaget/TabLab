package tablab;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.transform.dom.DOMSource;
import java.io.*;
import java.util.*;

public class PartitionFileManager {

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
    public static void exportToXml(String path, MusicPartition musicPartition) throws IOException {

        Element racine = getPartition(musicPartition);
        Document document = new Document(racine);


        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileOutputStream(path));

    }

    /** Return a string with the partition parsed as a xml file
     *
     * @param musicPartition the music partition to write
     */
    public static String exportToXmlString(MusicPartition musicPartition) {

        Element racine = getPartition(musicPartition);
        Document document = new Document(racine);

        return new XMLOutputter().outputString(document);
    }

    private static Element getPartition(MusicPartition musicPartition) {
        Element partition = new Element(PARTITION);

        partition.setAttribute(TITLE, musicPartition.getTitle());
        partition.setAttribute(AUTHOR, musicPartition.getAuthor());


        Element settings = getSettings(musicPartition.getSettings());
        partition.addContent(settings);

        Element bars = new Element(BARS);
        for (int i = 0; i < musicPartition.getMusicBarNumber(); i++) {
            Element bar = getMusicBar(musicPartition.getMusicBar(i), musicPartition.getSettings());
            bar.setAttribute(NUMBER, String.valueOf(i+1));
            bars.addContent(bar);
        }
        partition.addContent(bars);

        return partition;
    }

    private static Element getSettings(PartitionSettings partitionSettings) {
        Element settings = new Element(SETTINGS);

        settings.setAttribute(NOTES_NUMBER, String.valueOf(partitionSettings.notesNumber));
        settings.setAttribute(NOTES_VALUE, String.valueOf(partitionSettings.notesValue));
        settings.setAttribute(TEMPO, String.valueOf(partitionSettings.tempo));

        Element bs = getBeatStructure(partitionSettings.beatStructure);
        settings.addContent(bs);

        Element ls = getLineStructure(partitionSettings.lineStructure);
        settings.addContent(ls);

        return settings;
    }

    private static Element getBeatStructure(BeatStructure beatStructure) {
        Element bs = new Element(BEAT_STRUCTURE);
        bs.addContent(getBeatStructureAsString(beatStructure));
        return bs;
    }

    private static Element getLineStructure(LineStructure lineStructure) {
        Element ls = new Element(LINE_STRUCTURE);
        ls.addContent(getLineStructureAsString(lineStructure));
        return ls;
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

    private static String getLineStructureAsString(LineStructure lineStructure) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < lineStructure.size(); i++) {
            if (i > 0)
                str.append(SEP);
            str.append(lineStructure.get(i));
        }
        return String.valueOf(str);
    }


    private static Element getMusicBar(MusicBar musicBar, PartitionSettings partitionSettings) {
        Element bar = new Element(MUSIC_BAR);

        for (int beat = 1; beat <= partitionSettings.notesNumber; beat++) {
            Element b = new Element(BEAT);
            b.setAttribute(NUMBER, String.valueOf(beat));

            if (musicBar.hasSpecialBeatStructure(beat)) {
                b.setAttribute(BEAT_STRUCTURE, getBeatStructureAsString(musicBar.getSpecialBeatStructure(beat)));
            }

            if (musicBar.hasSpecialLineStructure(beat)) {
                b.setAttribute(LINE_STRUCTURE, getLineStructureAsString(musicBar.getSpecialLineStructure(beat)));
            }

            for (String lineType : musicBar.getLineStructure(beat)) {
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
    public static MusicPartition importFromXml(String path) throws JDOMException, IOException {
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(new File(path));

        Element racine = document.getRootElement();
        return createPartition(racine);
    }

    private static MusicPartition createPartition(Element partition) {

        String title = partition.getAttributeValue(TITLE);
        String author = partition.getAttributeValue(AUTHOR);
        PartitionSettings partitionSettings = createSettings(partition.getChild(SETTINGS));

        MusicPartition musicPartition = new MusicPartition(title, author, partitionSettings);

        List<Element> bars = partition.getChild(BARS).getChildren(MUSIC_BAR);
        Collections.sort(bars, new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                return Integer.parseInt(o1.getAttributeValue(NUMBER)) - Integer.parseInt(o2.getAttributeValue(NUMBER));
            }
        });

        for (Element bar : bars) {
            musicPartition.addMusicBar(createMusicBar(bar, partitionSettings));
        }

        return musicPartition;
    }

    private static PartitionSettings createSettings(Element settings) {
        PartitionSettings partitionSettings = new PartitionSettings();
        partitionSettings.notesNumber = Integer.parseInt(settings.getAttributeValue(NOTES_NUMBER));
        partitionSettings.notesValue = Integer.parseInt(settings.getAttributeValue(NOTES_VALUE));
        partitionSettings.tempo = Float.parseFloat(settings.getAttributeValue(TEMPO));

        partitionSettings.lineStructure = createLineStructure(settings.getChild(LINE_STRUCTURE));
        partitionSettings.beatStructure = createBeatStructure(settings.getChild(BEAT_STRUCTURE));

        return partitionSettings;
    }

    private static LineStructure createLineStructure(Element structure) {
        String line = structure.getContent(0).getValue();
        return new LineStructure(Arrays.asList(line.split(SEP)));
    }

    private static BeatStructure createBeatStructure(Element structure) {
        String beat = structure.getContent(0).getValue();
        return createBeatStructureFromString(beat);
    }

    private static BeatStructure createBeatStructureFromString(String structure) {
        List<BeatStructure.NoteTime> beatStructure = new ArrayList<>();
        for (String s : structure.split(SEP)) {
            beatStructure.add(BeatStructure.NoteTime.valueOf(s));
        }
        return new BeatStructure(beatStructure);
    }

    private static LineStructure createLineStructureFromString(String structure) {
        return new LineStructure(Arrays.asList(structure.split(SEP)));
    }

    private static MusicBar createMusicBar(Element bar, PartitionSettings partitionSettings) {
        MusicBar musicBar = new MusicBar(partitionSettings);

        List<Element> beats = bar.getChildren(BEAT);
        Collections.sort(beats, new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                return Integer.parseInt(o1.getAttributeValue(NUMBER)) - Integer.parseInt(o2.getAttributeValue(NUMBER));
            }
        });
        for (int i = 0; i < beats.size(); i++) {
            Element beat = beats.get(i);
            Attribute beatAtt = beat.getAttribute(BEAT_STRUCTURE);
            if (beatAtt != null) {
                musicBar.setSpecialBeatStructure(createBeatStructureFromString(beatAtt.getValue()), i+1);
            }
            Attribute lineAtt = beat.getAttribute(LINE_STRUCTURE);
            if (lineAtt != null) {
                musicBar.setSpecialLineStructure(createLineStructureFromString(lineAtt.getValue()), i+1);
            }

            for (Element line : beat.getChildren(LINE)) {
                long notes = Long.parseLong(line.getContent(0).getValue());
                musicBar.setCompressedNotes(line.getAttributeValue(PART), i+1, notes);
            }
        }

        return musicBar;
    }

    /** write the partition into a txt file
     *
     * @param path the path of the partition
     * @param musicPartition the partition
     * @throws FileNotFoundException if error with creation or print of the file
     */
    public static void exportToTxt(String path, MusicPartition musicPartition) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(path);
        writer.print(musicPartition.toString());
        writer.close();
    }
}