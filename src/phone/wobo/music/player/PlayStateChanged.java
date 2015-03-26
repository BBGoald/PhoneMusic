package phone.wobo.music.player;

public interface PlayStateChanged {
    public void onPause();
    public void onPlaying();
    public void onCompletion();
    
    public void onStop();
    public void onNoFile();
    public void onDisConnectNet();
    public void onPrepare();
    public void onPrepareing();
    public void onErrorAddr();
    public void onErrorPlay();
}
