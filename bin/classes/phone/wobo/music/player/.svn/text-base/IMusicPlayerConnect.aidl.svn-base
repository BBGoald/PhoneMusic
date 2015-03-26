package phone.wobo.music.player;

import phone.wobo.music.model.MusicInfo;

interface IMusicPlayerConnect {
   void setPlayListAndPlay(in List<MusicInfo> playList, int index);
   void setPlayList(in List<MusicInfo> playList);
   void getFileList(out List<MusicInfo> playList);
   MusicInfo getCurrentMusicInfo();
   
   boolean rePlay();
   boolean play(int position);
   boolean pause();
   boolean playNext();
   boolean playPre();
   boolean seekTo(int rate);
   boolean stop();
   void exit();
   void destroy();
   
   int getCurPosition();
   int getDuration();
   int getPlayState();
   int getPlayMode();
   int getAudioSessionId();
   int getCurPlayIndex();
   int getMusicFileType();
   void startDownLoad(in MusicInfo mp3);
   
   void setPlayMode(int mode);
   void sendPlayStateBrocast();
}


