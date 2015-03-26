package phone.wobo.music.online;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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

public class TypeSongActivity extends BaseActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private Context mContext;
	private ListView ls_song;
	private SearchInfo<MusicInfo> musicInfo;
	private final static int EVNET_LIST_FINISH = 1;
	private final static int remove_refreshView = 0x1002;
	private final static int ADD_DATA_refreshView = 0x1003;
	private int pageIndex = 1;
	private String key = "";
	private SongListAdapter adapter;
	private PullToRefreshView pullToRefreshView;
	private int currentPage = 1;
	private TextView tv_msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_list);

		mContext = TypeSongActivity.this;
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
			loadData(key, pageIndex);
		}

	}

	private void initView() {
		tv_msg = (TextView) findViewById(R.id.msg);
		pullToRefreshView = (PullToRefreshView)findViewById(R.id.listview_pull_refresh_view);
		pullToRefreshView.setOnFooterRefreshListener(this);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		ls_song = (ListView) findViewById(R.id.ls_song);
		ls_song.setItemsCanFocus(true);
		adapter = new SongListAdapter(mContext) {
			protected void play(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					playMusic(listMusic, position);
					FuncUtils.beginClickAnimation(TypeSongActivity.this, (View) view.getParent(), phone.wobo.music.R.drawable.ic_anim_music);
				}
			}

			protected void add2List(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					add2PlayList(listMusic.get(position));
					FuncUtils.beginClickAnimation(TypeSongActivity.this, view, phone.wobo.music.R.drawable.ic_anim_add);
				}
			}

			protected void add2fav(View view,int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
				    showToast("已收藏");
					handleFavorite(view,listMusic.get(position));
				}
			}

			protected void add2download(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					startDownload(listMusic.get(position));
					FuncUtils.beginClickAnimation(TypeSongActivity.this, view, phone.wobo.music.R.drawable.btn_download);
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
		ls_song.setOnItemClickListener(this);
	}

	private boolean loadData(final String key, final int pageNumber) {

		new Thread() {
			public void run() {
				musicInfo = OnlineLogic.getTypeSong(key, pageNumber);
				handler.sendEmptyMessage(EVNET_LIST_FINISH);
			};
		}.start();
		return true;
	}

	protected Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			
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
			default:
				break;
			}
			return false;
		}
	});

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	private void refreshListView() {
		if (musicInfo == null || musicInfo.getList() == null
				|| musicInfo.getList().size() == 0) {
			handler.sendEmptyMessage(remove_refreshView);
			pullToRefreshView.setVisibility(View.GONE);
			tv_msg.setVisibility(View.VISIBLE);
			if (isCloseNetwork()) {
				tv_msg.setText(getResources().getString(R.string.network_connection_is_close));
			}else {
				tv_msg.setText("暂无数据");
			}
			return;
		}
		if (musicInfo.isHasNext()) {
			pullToRefreshView.setMoreData(true);
		} else {
			pullToRefreshView.setMoreData(false);
		}
		tv_msg.setVisibility(View.GONE);
		pullToRefreshView.setVisibility(View.VISIBLE);
		adapter.setListData(musicInfo.getList());
		handler.sendEmptyMessage(remove_refreshView);
	}
	
	private void addListData() {
		if (musicInfo == null || musicInfo.getList() == null
				|| musicInfo.getList().size() == 0) {
			currentPage = 1;
			tv_msg.setVisibility(View.VISIBLE);
			pullToRefreshView.setVisibility(View.GONE);
			handler.sendEmptyMessage(remove_refreshView);
			if (isCloseNetwork()) {
				tv_msg.setText(getResources().getString(
						R.string.network_connection_is_close));
			} else {
				tv_msg.setText("暂无数据");
			}
			return;
		}
		if (musicInfo.isHasNext()) {
			pullToRefreshView.setMoreData(true);
		} else {
			pullToRefreshView.setMoreData(false);
		}
		tv_msg.setVisibility(View.GONE);
		pullToRefreshView.setVisibility(View.VISIBLE);
		adapter.addListData(musicInfo.getList());
		handler.sendEmptyMessage(remove_refreshView);
	}
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (musicInfo.isHasNext()) {
			currentPage += 1;
			new Thread(new Runnable() {

				@Override
				public void run() {
					musicInfo = OnlineLogic.getTypeSong(key, currentPage);
					handler.sendEmptyMessage(ADD_DATA_refreshView);
				}
			}).start();
		}else {
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
