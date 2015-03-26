package phone.wobo.music.search;

import java.util.List;

import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.SearchInfo;
import phone.wobo.music.online.RankSongActivity;
import phone.wobo.music.online.SongListAdapter;
import phone.wobo.music.util.FuncUtils;
import phone.wobo.music.util.PullToRefreshView;
import phone.wobo.music.util.PullToRefreshView.OnFooterRefreshListener;
import phone.wobo.music.util.PullToRefreshView.OnHeaderRefreshListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SongFrameLayout extends FrameLayout implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private Context mContext;
	private ListView listView;
	private TextView txtMsg;
//	private SearchSongAdapter adapter;
	private SongListAdapter adapter;
	private final int refresh_view = 0x1001;
	private final int remove_refreshView = 0x1002;
	private final int network_isclose = 0x1003;
	private final int ADD_DATA_refreshView = 0x1004;
	private String searchStr;
	private SearchActivity searchActivity;
	private PullToRefreshView pullToRefreshView;
	private SearchInfo searchInfo;
	private int currentPage = 1;
	TextView txtResult;
	public SongFrameLayout(SearchActivity searchActivity) {
		super(searchActivity);
		this.searchActivity = searchActivity;
		mContext = searchActivity.getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.lay_search_list, this);
		initView(view);
	}

	public SongFrameLayout(SearchActivity context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

	}

	private void initView(View view) {
		txtMsg = (TextView) view.findViewById(R.id.msg);
		pullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.main_pull_refresh_view);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterRefreshListener(this);
		listView = (ListView) view.findViewById(R.id.ls_song);
		txtResult = (TextView) view.findViewById(R.id.result);
		adapter = new SongListAdapter(mContext) {

			@Override
			protected void play(View view, int position) {
				super.play(view, position);				
				searchActivity.playMusic(adapter.getListData(), position);				
				FuncUtils.beginClickAnimation(searchActivity, view, phone.wobo.music.R.drawable.ic_anim_music);
			}
			@Override
			protected void add2download(View view, int position) {
				searchActivity.startDownload(adapter.getListData().get(position));
				FuncUtils.makeText(mContext, "开始下载");
				FuncUtils.beginClickAnimation(searchActivity, view, phone.wobo.music.R.drawable.btn_download);
			}
			@Override
			protected void add2List(View view, int position) {				
				FuncUtils.beginClickAnimation(searchActivity, view, phone.wobo.music.R.drawable.ic_anim_add);
				List<MusicInfo> listMusic = adapter.getListData();
				if (searchActivity.isExistMusic(listMusic)) {				
					searchActivity.add2PlayList(listMusic.get(position));
				}
			}
			protected void add2fav(View view,int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (searchActivity.isExistMusic(listMusic)) {
					searchActivity.showToast("已收藏");
					searchActivity.handleFavorite( view,listMusic.get(position));
				}
				
			}

			protected void play2TV(int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (searchActivity.isExistMusic(listMusic)) {
					searchActivity.showToast("在电视上播放");
					searchActivity.playMusicOnTV(listMusic, position);
				}
			}

		};
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public void startSearch(String searchStr) {
		this.searchStr = searchStr;
		txtMsg.setVisibility(View.VISIBLE);
		txtMsg.setText("正在为您努力加载数据。。。");
		pullToRefreshView.setVisibility(View.GONE);
		if (OnlineLogic.isCloseNetwork(mContext)) {
			mHandler.sendEmptyMessage(network_isclose);
			return;
		}
		currentPage = 1;
		new LoadDataThread(searchStr, currentPage).start();
	}

	private class LoadDataThread extends Thread {
		private String key;
		private int loadPage;

		public LoadDataThread(String key, int loadPage) {
			this.key = key;
			this.loadPage = loadPage;
		}

		@Override
		public void run() {
			searchInfo = SearchLogic.getSearchSong(key, loadPage);
			mHandler.sendEmptyMessage(refresh_view);
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case refresh_view:
				refreshView();
				break;
			case remove_refreshView:
				pullToRefreshView.refreshComplete();
				break;
			case network_isclose:
				pullToRefreshView.setVisibility(View.GONE);
				txtMsg.setText("网络连接未打开");
				txtMsg.setVisibility(View.VISIBLE);
				break;
			case ADD_DATA_refreshView:
				addListData();
				break;
			}
		}

	};

	@SuppressWarnings("unchecked")
	private void refreshView() {
		if (checkData()) {
			txtResult.setText("共匹配到"+searchInfo.getTotal()+"个结果");
			adapter.setListData(searchInfo.getList());
		}

	}

	@SuppressWarnings("unchecked")
	private void addListData() {
		if (checkData()) {		
			adapter.addListData(searchInfo.getList());
		} else {
			currentPage = 1;
		}
	}

	private boolean checkData() {
		if (searchInfo == null || searchInfo.getList() == null
				|| searchInfo.getList().size() == 0) {
			txtMsg.setVisibility(View.VISIBLE);
			pullToRefreshView.setVisibility(View.GONE);
			mHandler.sendEmptyMessage(remove_refreshView);
			txtResult.setText("共匹配到0个结果");
			if (OnlineLogic.isCloseNetwork(mContext)) {
				txtMsg.setText(getResources().getString(
						R.string.network_connection_is_close));
			} else {
				txtMsg.setText("暂无数据");
			}
			return false;
		}
		if (searchInfo.isHasNext()) {
			pullToRefreshView.setMoreData(true);
		} else {
			pullToRefreshView.setMoreData(false);
		}
		txtMsg.setVisibility(View.GONE);
		pullToRefreshView.setVisibility(View.VISIBLE);
		mHandler.sendEmptyMessage(remove_refreshView);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		currentPage = 1;
		new LoadDataThread(searchStr, currentPage).start();
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (searchInfo.isHasNext()) {
			currentPage += 1;
			new Thread(new Runnable() {

				@Override
				public void run() {
					searchInfo = SearchLogic.getSearchSong(searchStr,
							currentPage);
					mHandler.sendEmptyMessage(ADD_DATA_refreshView);
				}
			}).start();
		} else {
			pullToRefreshView.setMoreData(false);
			mHandler.sendEmptyMessage(remove_refreshView);
		}

	}

}
