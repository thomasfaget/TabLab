package tablab;

/** The settings of the score
 */
public class ScoreSettings {

    public int pitch;
    public int noteValue;
    public float tempo;
    public BeatStructure beatStructure;
    public LineStructure lineStructure;


    public int getLinesNumber() {
        return lineStructure.size();
    }
}
