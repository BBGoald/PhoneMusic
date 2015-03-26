package phone.wobo.music.videoplayer; 

import java.util.List;

import phone.wobo.music.Config;
 

public class ExtractThread extends Thread { 
	private String mUrl;
	private String mDefinition;
	private String mLanguage;

	public ExtractThread(String url, String definition, String language) {
		mUrl = url;
		mDefinition = definition;
		mLanguage = language;
	}

	public void handlePlay(RealUrlInfo info) {

	}

	public void run() {
		try {
			load();
		} catch (Exception e) {
			handlePlay(null);
			e.printStackTrace();
		}
	}

	private void load() throws Exception {  
		if (mLanguage == null
				|| "".equals(mLanguage)) {
			mLanguage = Config.LANGUAGE_CHINESE ;
		}
		RealUrlInfo info = new ExtractRealUrl().getPlayInfo(
				mUrl, mDefinition, mLanguage);

		List<String> playlist = info.getPlayList();
		if (!info.getStatus().equalsIgnoreCase("ok") || playlist == null
				|| playlist.size() == 0) {
			handlePlay(null);
			return;
		} 
		handlePlay(info);
	}
} 