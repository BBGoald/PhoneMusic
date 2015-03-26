package phone.wobo.music.bll;

import java.util.List;

import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.SongPlayInfo;
import phone.wobo.music.radio.ExtractRadio;


/**
 * @author heming
 * @since :JDK ?
 * @version：1.0 Create at:2014-3-7 Description:
 */
public class ParsePluginPlayList {

	/**
	 * 把playList中第index位置元素的地址解析出来 第index位置的元素用initMusicInfoLocal()初始化
	 * 其他位置用setDefaultMusicInfoLocal()初始化
	 * 
	 * @param playList
	 * @return
	 */
	public static List<MusicInfo> getLocalPlayListInfo(
			List<MusicInfo> playList, int index) {
		//List<MusicInfoLocal> list = new ArrayList<MusicInfoLocal>();
		ExtractRadio er = new ExtractRadio();
		String hash = playList.get(index).getPluginPath();
		SongPlayInfo sp = er.getSongPlayInfo(hash);
		if (sp == null)
			return playList;
		playList.get(index).setPath(sp.getUrl());
		playList.get(index).setPlayTime(sp.getTimeLength()*1000);
	
		return playList;
	}
	


	/*private static MusicInfoLocal initMusicInfoLocal(String artist,
			String path, String name, String mMusicAlbum, int d) {
		MusicInfoLocal info = new MusicInfoLocal();
		info.setPath(path);
		info.setAlbum(mMusicAlbum);
		info.setPlayTime(d*1000);
		info.setName(name);
		
		return info;
	}

	private static MusicInfoLocal setDefaultMusicInfoLocal(String path) {
		MusicInfoLocal info = new MusicInfoLocal();
		info.setPath(path);
		return info;
	}*/

}
