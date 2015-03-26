package phone.wobo.music.online;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.SearchInfo;
import phone.wobo.music.util.FuncUtils;

import phone.wobo.music.util.PullToRefreshView;
import phone.wobo.music.util.PullToRefreshView.OnFooterRefreshListener;
import phone.wobo.music.util.PullToRefreshView.OnHeaderRefreshListener;

public class RankSongActivity extends BaseActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private Context mContext;
	private ListView ls_song;
	private SearchInfo<MusicInfo> musicInfo;
	private final static int EVNET_LIST_FINISH = 0x1001;
	private final static int remove_refreshView = 0x1002;
	private final static int ADD_DATA_refreshView = 0x1003;
	private int pageIndex = 1;
	private String key = "";
	private SongListAdapter adapter;
	private TextView tv_msg;
	private PullToRefreshView pullToRefreshView;
	private int currentPage = 1;
	
	//private String TAG = "RankSongActivity";
	private String TAG = "liangbang";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "******instance RankSongActivity");
		Log.d(TAG, "this= " + this.hashCode());
		Log.d(TAG, "RankSongActivity.this= " + RankSongActivity.this.hashCode());
		Log.d(TAG, "current Thread= " + Thread.currentThread().getId());
		Log.d(TAG, "current Process= " + android.os.Process.myPid());

		setContentView(R.layout.act_list);
		mContext = RankSongActivity.this;
		musicInfo = new SearchInfo<MusicInfo>();
		initView();
		Bundle bundle = this.getIntent().getExtras();
		key = bundle.getString("key");
		String name = bundle.getString("name");
		if (name != null && !name.equals("")) {
			setTitle(name);
		}
		if (key != null && !key.equals("")) {
			loading.show();
	        Log.d(TAG, "	--->RankSongActivity--->loading.hashCode()= " + loading.hashCode());
			loadData(key, pageIndex);
		}

	}

	private void initView() {
		tv_msg = (TextView) findViewById(R.id.msg);
		ls_song = (ListView) findViewById(R.id.ls_song);
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.listview_pull_refresh_view);
		pullToRefreshView.setOnFooterRefreshListener(this);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		ls_song.setItemsCanFocus(true);
		adapter = new SongListAdapter(mContext) {
			protected void play(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
		        Log.d(TAG, "	--->RankSongActivity--->listMusic= " + listMusic);

				if (isExistMusic(listMusic)) {
					FuncUtils.beginClickAnimation(RankSongActivity.this, view, phone.wobo.music.R.drawable.ic_anim_music);
					playMusic(listMusic, position);
				}
			}

			protected void add2List(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					FuncUtils.beginClickAnimation(RankSongActivity.this, view, phone.wobo.music.R.drawable.ic_anim_add);
					add2PlayList(listMusic.get(position));
				}
			}

			protected void add2fav(View view,int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
				    showToast("已收藏");
					handleFavorite( view,listMusic.get(position));
				}
				
			}

			protected void add2download(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					FuncUtils.beginClickAnimation(RankSongActivity.this, view, phone.wobo.music.R.drawable.btn_download);
					startDownload(listMusic.get(position));
					showToast("开始下载");
				}
			}

			protected void play2TV(int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
				    showToast("在电视上播放");
					playMusicOnTV(musicInfo.getList(), position);
				}
			}
		};
		ls_song.setAdapter(adapter);
		ls_song.setOnItemClickListener(this);//trigger click events in SongListAdapter class
		//Log.d(TAG, "ls_song= " + ls_song.hashCode());
		//Log.d(TAG, "this= " + this.hashCode());
		//Log.d(TAG, "RankSongActivity.this= " + RankSongActivity.this.hashCode());//RankSongActivity.this=this=1113441368
	}

	private boolean loadData(final String key, final int pageNumber) {

		new Thread() {
			public void run() {
				musicInfo = OnlineLogic.getRankSongList(key, pageNumber);
				handler.sendEmptyMessage(EVNET_LIST_FINISH);
			};
		}.start();
		return true;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVNET_LIST_FINISH:
				loading.close();
				refreshListView();
				break;
			case remove_refreshView:
				pullToRefreshView.refreshComplete();
				break;
			case ADD_DATA_refreshView:
				addListData();
				break;
			}
		}

	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	/************************************************************************
	 * 更新ListView数据
	 * 
	 * @param msg
	 *************************************************************************/
	private void refreshListView() {

		if (checkData()) {
			adapter.setListData(musicInfo.getList());
		}

	}

	private void addListData() {
		if (checkData()) {
			adapter.addListData(musicInfo.getList());
		} else {
			currentPage = 1;
		}

	}

	private boolean checkData() {
		if (musicInfo == null || musicInfo.getList() == null
				|| musicInfo.getList().size() == 0) {
			tv_msg.setVisibility(View.VISIBLE);
			pullToRefreshView.setVisibility(View.GONE);
			handler.sendEmptyMessage(remove_refreshView);
			if (isCloseNetwork()) {
				tv_msg.setText(getResources().getString(
						R.string.network_connection_is_close));
			} else {
				tv_msg.setText("暂无数据");
			}
			return false;
		}
		if (musicInfo.isHasNext()) {
			pullToRefreshView.setMoreData(true);
		} else {
			pullToRefreshView.setMoreData(false);
		}
		tv_msg.setVisibility(View.GONE);
		pullToRefreshView.setVisibility(View.VISIBLE);
		handler.sendEmptyMessage(remove_refreshView);
		return true;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (musicInfo.isHasNext()) {
			currentPage += 1;
			new Thread(new Runnable() {
				@Override
				public void run() {
					musicInfo = OnlineLogic.getRankSongList(key, currentPage);
					handler.sendEmptyMessage(ADD_DATA_refreshView);
				}
			}).start();

		} else {
			pullToRefreshView.setMoreData(false);
			handler.sendEmptyMessage(remove_refreshView);
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		currentPage = 1;
		loadData(key, currentPage);
	}

}
