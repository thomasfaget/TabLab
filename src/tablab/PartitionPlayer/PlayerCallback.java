package tablab.PartitionPlayer;

public interface PlayerCallback {

    void onStart();
    void onFinish();
    void onPause();
    void onResume();
    void onStop();

    void onNextNote(int barNumber, int beatNumber, int noteNumber);
}
