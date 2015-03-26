package phone.wobo.music.lrc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import phone.wobo.music.lrc.TimedTextObject.TimedIndex;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.util.NetTools;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class ParseLrcThread extends Thread {
	private static long interval = 400;
	private List<String> mListUrl;
	private boolean mFinish;
	private String mText1;
	private String mEncode = "UTF-8";
	MusicInfo info;
	private Handler mHandler;
	private TextView lrcView1;
	private TextView lrcView2;
	
	private List<String>  loadLrcUrl(String source,String songName,String singer){	
		List<String> list_url = new ArrayList<String>();
		ExtractLrc extractlrc = new ExtractLrc();
		if (source == null || source.equals("")){	//本地音乐				
			list_url = extractlrc.getLrcOfString("search", "", songName, singer);//搜索
			
		}else if (source.contains("qqmusic") || source.contains("qq.com")) {
			String mid = source.substring(
					source.lastIndexOf("%3D") + 3, source.length());
			list_url = extractlrc.getLrcOfString(source, mid,
					removeSpecChar(songName), singer);
		}

		if (list_url == null || list_url.size() == 0) {// 电台
												// 是kugou的源，songName包含了歌名和歌手
			songName = removeSpecChar(songName);
			int spil = songName.indexOf("-");
			if (spil > -1) {
				singer = songName.substring(0, spil).trim();
				songName = songName.substring(spil + 1).trim();
				songName = removeSpecChar(songName);
				singer = removeSpecChar(singer);
			}
			list_url = extractlrc.getLrcOfString("search", "", songName, singer);//搜索
		}
		return list_url;
	}
	public ParseLrcThread(MusicInfo info,Handler mHandler,TextView lrcView1,TextView lrcView2) {
		this.info=info;		
		mFinish = false;
		this.mHandler=mHandler;
		this.lrcView1=lrcView1;
		this.lrcView2=lrcView2;
	}

	private InputStream getInputStream() {
		String url="";
		for (int i = 0; i < mListUrl.size(); i++) {
			 url=mListUrl.get(i);
			 Log.d("url列表", url);
			try {
				InputStream is = NetTools.getHttpStream(url, 20);
				
				if (is != null) {					
					if (url.indexOf("http://lrc.aspxp.net/") > -1 ||url.indexOf("http://box.zhangmen.baidu.com") > -1 )
						mEncode = "GB2312";
					Log.d("url显示", url);					
					return is;
				}
			}catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return null;
	}

	public void run() {
		mListUrl = loadLrcUrl(info.getPluginPath(),info.getName(),info.getArtist());
		if(mListUrl==null|| mListUrl.size()==0){
			Lrc lrc=new Lrc();
			lrc.content="~~~~~~";
			Lrc lrc2=new Lrc();
			lrc.content="暂无歌词文件";
			handleSubtitleChange(lrc, lrc2);
			return;
		}
		TimedTextObject timedTextObject;
		try {
			InputStream is = getInputStream();
			if (is == null){
				Lrc lrc=new Lrc();
				lrc.content="~~~~~~";
				Lrc lrc2=new Lrc();
				lrc.content="暂无歌词文件";
				handleSubtitleChange(lrc, lrc2);
				return;
			}
				
			timedTextObject = FormatLRC.parseFile(is, mEncode);
			TimedIndex timedIndex = new TimedIndex(0);
			while (!mFinish) {
				timedIndex.start = getCurrentPosition();
				timedIndex.end = timedIndex.start;
				Lrc lrc1 = timedTextObject.getLrc(timedIndex);
				
				Lrc lrc2 = null;
				if (lrc1 != null) {
					timedIndex.start = lrc1.end.mseconds;
					timedIndex.end = timedIndex.start+2000;
					lrc2 = timedTextObject.getLrc(timedIndex);
					
				}
				String title1 = "";
				if (lrc1 != null) {
					lrc1.isSing=true;
					title1 = lrc1.getTextContent() == null ? "" : lrc1
							.getTextContent().trim();
				}
				if (lrc2 != null) {
					lrc2.isSing=false;				
				}
			
				if (!title1.equals(mText1) && !title1.equals("")&& lrc1!=null) {
					mText1 = title1;				
					handleSubtitleChange(lrc1, lrc2);				
				}

				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void finish() {
		this.mFinish = true;
	}

	public void handleSubtitleChange(final Lrc lrc1, final Lrc lrc2) {
		mHandler.post(new Runnable() {
			public void run() {
				showLrcView(lrc1, lrc2);
			}
		});
	}
	public Lrc createDefaultLrc(String content){
		return createLrc(content,0,0);
	}
	
	public Lrc createLrc(String content, int timeStart, int timeEnd) {
		Lrc lrc = new Lrc();
		lrc.start = new Time(timeStart);
		lrc.end = new Time(timeEnd);
		lrc.content = content;
		return lrc;
	}
	public int getCurrentPosition() {
		return 0;
	}
	private String removeSpecChar(String str) {
		Matcher match = Pattern.compile("\\(.*?\\)").matcher(str);
		str = match.replaceAll("");
		match = Pattern.compile("【.*?】").matcher(str);
		str = match.replaceAll("");
		return str;
	}
	
	public  void showLrcView(Lrc lrc1, Lrc lrc2) {	
		String ot2 = lrcView2.getText().toString();
		String nt1 = lrc1 == null ? "" : lrc1.getTextContent();// 新的
		String nt2 = lrc2 == null ? "" : lrc2.getTextContent();
		if (nt1.equals(ot2)) {
			lrcView1.setText(nt2);
			if (lrc1.isSing) {
				lrcView1.setTag("");
				lrcView2.setTag("isSing");
			}

		} else {
			lrcView1.setText(nt1);
			lrcView2.setText(nt2);
			if (lrc1.isSing) {
				lrcView1.setTag("isSing");
				lrcView2.setTag("");
			}
		}
		String flag = lrcView1.getTag() == null ? "" : lrcView1.getTag()
				.toString();

		if (flag.equals("isSing")) {
			lrcView1.setTextColor(Color.YELLOW);
			lrcView2.setTextColor(Color.WHITE);

		} else {
			lrcView1.setTextColor(Color.WHITE);
			lrcView2.setTextColor(Color.YELLOW);
		}
	}
}
