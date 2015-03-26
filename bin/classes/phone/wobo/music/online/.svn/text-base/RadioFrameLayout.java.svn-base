package phone.wobo.music.online;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.Radio;
import phone.wobo.music.model.RadioSong;
import phone.wobo.music.model.Song;
import phone.wobo.music.player.MusicFileType;
import phone.wobo.music.player.MusicPlayActivity;
import phone.wobo.music.player.MusicPlayState;
import phone.wobo.music.player.MusicServiceManager;
import phone.wobo.music.radio.ExtractRadio;
import phone.wobo.music.radio.RadioAdapter;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.SharedFileHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class RadioFrameLayout extends SimpleFrameLayout implements
		OnItemClickListener {
	private Context mContext;
	//private FinalBitmap fb;
	private BitmapUtils fb;
	private GridView gvRadio;
	private TextView resultMsg;
	private List<Radio> list = new ArrayList<Radio>();
	private final int GetRadioSucc = 1;
	private final int GetRadioFail = 2;
	private final int GetSongSucc = 3;
	private final int GetSongFail = 4;
	private final int ClickRadioDelayed = 5;
	private final int PageSize = 60;
	private int curPlaySongIndex = 0;
	private int curPlayRadioIndex = 0;
	private RadioAdapter adapter;
	private String title = "";
	private ExtractRadio er = new ExtractRadio();
	private RadioSong curRadioSong = null;
	private MusicServiceManager mServiceManager;
	private List<MusicInfo> curPlayList = new ArrayList<MusicInfo>();
//	private LoadingDialog loading;

	public RadioFrameLayout(Context context) {
		this(context, null);
		mContext = context;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.lay_grid, this);
//		loading = new LoadingDialog(mContext);
		fb = ImageCache
				.create(mContext, R.drawable.long_movie_default);
		initView(view);
		loading.show();
		loadData();
	}

	public void setServiceManager(MusicServiceManager mServiceManager) {
		this.mServiceManager = mServiceManager;
	}

	/*
	 * public RadioFrameLayout(Context context, MusicServiceManager
	 * mServiceManager) { super(context); mContext = context;
	 * this.mServiceManager = mServiceManager; }
	 */

	public RadioFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	private void initView(View view) {
		resultMsg = (TextView) view.findViewById(R.id.msg);
		gvRadio = (GridView) view.findViewById(R.id.gv_rank);
		adapter = new RadioAdapter(mContext, null, fb);
		gvRadio.setAdapter(adapter);
		gvRadio.setOnItemClickListener(this);
	}

	private void loadData() {
		new Thread() {
			@Override
			public void run() {
				list = new ArrayList<Radio>();
				String json = SharedFileHelper.getPreference(mContext,
						ExtractRadio.Radio_Name, ExtractRadio.Radio_Key_Json);
				if (json == null || "".equals(json)) {
					list = er.getHotRadioList(mContext);
				} else {
					list = er.parseRadio(json);
				}
				if (list == null)
					handler.sendEmptyMessage(GetRadioFail);
				else
					handler.sendEmptyMessage(GetRadioSucc);
				er.getHotRadioList(mContext);
			}
		}.start();
	}

	protected Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == GetRadioSucc) {
				loading.close();
				if (list == null || list.size() == 0) {
					if (OnlineLogic.isCloseNetwork(mContext)) {
						resultMsg.setText(mContext.getResources().getString(
								R.string.network_connection_is_close));
					} else {
						resultMsg.setText(mContext.getResources().getString(
								R.string.data_load_failure));
					}
					resultMsg.setVisibility(View.VISIBLE);
					gvRadio.setVisibility(View.GONE);
					return false;
				}
				resultMsg.setVisibility(View.GONE);
				gvRadio.setVisibility(View.VISIBLE);
				adapter.setDataSource(list);

			} else if (msg.what == GetRadioFail) {
				loading.close();
				showErrorToast("无法连接电台");
			} else if (msg.what == GetSongFail) {
				loading.close();
				showErrorToast("无法连接电台");
			} else if (msg.what == GetSongSucc) {
				loading.close();
				if (curRadioSong == null || curRadioSong.getSonglist() == null
						|| curRadioSong.getSonglist().size() == 0) {
					showErrorToast("无法连接电台");
					return false;
				}
				String curFmid = list.get(curPlayRadioIndex).getFmid();
				if (curRadioSong.getFmid() == null
						|| !curRadioSong.getFmid().equals(curFmid)) {
					return false;
				}
				List<MusicInfo> playList = toMusicInfoLocal(curRadioSong
						.getSonglist());
				setCurPlaySongIndex(playList.size());
				playMusic(playList, curPlaySongIndex);

			} else if (msg.what == ClickRadioDelayed) {
				Radio sd = list.get(curPlayRadioIndex);
				if (msg.arg1 != curPlayRadioIndex)
					return false;
				String offset = sd.getOffset();
				if (offset != null && offset.indexOf("time") > 0)
					offset = offset.substring(0, offset.indexOf("time"))
							+ System.currentTimeMillis();
				else
					offset = "%7B%22offset%22%3A10%2C%22time%22%"
							+ System.currentTimeMillis();
				loadSongByRadio(sd.getFmid(), offset);

			}
			return false;
		}
	});

	private void setCurPlaySongIndex(int size) {
		if (size <= 10) {
			curPlaySongIndex = 0;
		}
		int index = (int) (Math.random() * 10);
		if (index == curPlaySongIndex) {
			curPlaySongIndex = index - 1 < 0 ? 1 : index - 1;
		} else
			curPlaySongIndex = index;
	}

	public void playMusic(List<MusicInfo> playList, int position) {
		if (mServiceManager != null) {
			mServiceManager.setPlayListAndPlay(playList, curPlaySongIndex);
		}
		Intent intent = new Intent();
		intent.setClass(mContext, MusicPlayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(MusicPlayActivity.FLAG_PLAY_LIST,
				(ArrayList<? extends Parcelable>) playList);
		bundle.putInt(MusicPlayState.PLAY_MUSIC_INDEX, position);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	private void loadSongByRadio(final String fmid, final String offset) {
		loading.show();
		new Thread() {
			@Override
			public void run() {
				curRadioSong = er.getRadionSong(fmid, offset, PageSize);
				if (curRadioSong == null)
					handler.sendEmptyMessage(GetSongFail);
				else
					handler.sendEmptyMessage(GetSongSucc);
			}
		}.start();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		for (Radio r : list) {
			r.setSelected(false);
		}
		curPlayRadioIndex = position;
		Radio sd = list.get(position);
		sd.setSelected(true);
		adapter.notifyDataSetChanged();

		handler.removeMessages(ClickRadioDelayed);

		Message msg = new Message();
		msg.what = ClickRadioDelayed;
		msg.arg1 = position;
		handler.sendMessageDelayed(msg, 0);

	}

	/**
	 * @param list
	 * @return
	 */
	private List<MusicInfo> toMusicInfoLocal(List<Song> list) {
		List<MusicInfo> playList = new ArrayList<MusicInfo>();
		String time = "";
		for (Song song : list) {
			MusicInfo lm = new MusicInfo();
			lm.setName(song.getName());
			lm.setPath(song.getHash());
			lm.setPluginPath(song.getHash());
			lm.setFileType(MusicFileType.MPM_RADIO);
			lm.setAlbum("");
			lm.setArtist("");
			time = song.getTime();
			if (song.getTime() == null || song.getTime().equals(""))
				time = "0";
			lm.setPlayTime(Integer.parseInt(time));

			playList.add(lm);
		}
		return playList;
	}

	// 显示错误提示对话框
	// 显示错误提示
	public void showErrorToast(String msg) {
	    showToast( msg);
	}
}
