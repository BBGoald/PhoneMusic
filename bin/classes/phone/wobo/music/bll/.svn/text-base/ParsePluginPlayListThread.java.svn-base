package phone.wobo.music.bll;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.SongPlayInfo;
import phone.wobo.music.radio.ExtractRadio;


public class ParsePluginPlayListThread extends Thread {
	private List<MusicInfo> mPlayList;
	private int mIndex;
	private boolean mFinish;
	public ParsePluginPlayListThread(List<MusicInfo> playList,int index){
		mFinish = false;
		this.mPlayList=playList;
		this.mIndex=index;
	}
@Override
public void run() {
	while (!mFinish) {
	ExtractRadio er = new ExtractRadio();
	String hash = mPlayList.get(mIndex).getPath();
	SongPlayInfo sp = er.getSongPlayInfo(hash);
	if (sp == null){
		handlePlay(null);
		return;
	}
		
	MusicInfo mi = initMusicInfoLocal("", sp.getUrl(),
			sp.getFileName(), "", sp.getTimeLength());
	List<MusicInfo> list=new ArrayList<MusicInfo>();
	for (int i = 0; i < mPlayList.size(); i++) {
		if (i != mIndex) {
			list.add(mPlayList.get(i));
		} else {
			list.add(mi);
		}
	}
	handlePlay(list);
	}
}

public void handlePlay(List<MusicInfo> list){
	
}
private static MusicInfo initMusicInfoLocal(String artist,
		String path, String name, String mMusicAlbum, int d) {
	MusicInfo info = new MusicInfo();
	info.setPath(path);
	info.setAlbum(mMusicAlbum);
	info.setPlayTime(d*1000);
	info.setName(name);
	
	return info;
}
public void finish() {
	this.mFinish = true;
}
}
