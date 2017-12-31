package tablab.partitions;

/**
 * Created by thomas on 04/06/2017.
 */
public class Note {

    private NoteType noteType;
    private PlayType playType;
    private int time;

    public Note(NoteType noteType, PlayType playType, int time) {
        this.noteType = noteType;
        this.playType = playType;
        this.time = time;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }
    public void setPlayType(PlayType playType) {
        this.playType = playType;
    }
    public void setTime(int time) {
        this.time = time;
    }

    public NoteType getNoteType() {
        return noteType;
    }
    public PlayType getPlayType() {
        return playType;
    }
    public int getTime() {
        return time;
    }
}
