package tablab;

import java.util.List;

/** The settings of the score
 */
public class ScoreSettings {

    public int pitch;
    public int noteValue;
    public float tempo;
    public BeatStructure beatStructure;
    public List<LineType> lineTypes;


    public int getLinesNumber() {
        return lineTypes.size();
    }
}
