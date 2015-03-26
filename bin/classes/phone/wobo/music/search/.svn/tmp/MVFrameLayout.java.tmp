package phone.wobo.music.search;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.Config;
import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.SearchInfo;
import phone.wobo.music.model.SearchMV;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.PullToRefreshView;
import phone.wobo.music.util.PullToRefreshView.OnFooterRefreshListener;
import phone.wobo.music.util.PullToRefreshView.OnHeaderRefreshListener;
import phone.wobo.music.util.Utils;
import phone.wobo.music.videoplayer.Video;
import phone.wobo.music.videoplayer.VideoResource;
import phone.wobo.music.videoplayer.VideoSet;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class MVFrameLayout extends FrameLayout implements OnItemClickListener,
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private Context mContext;
	private GridView gridView;
	private TextView txtMsg;
	private SearchMVAdapter adapter;
//	private FinalBitmap fb;
	private BitmapUtils fb;
	private String searchStr;
	private SearchInfo searchInfo;
	private final int refresh_view = 0x1001;
	private final int remove_refreshView = 0x1002;
	private final int network_isclose = 0x1003;
	private final int ADD_DATA_refreshView = 0x1004;
	private PullToRefreshView pullToRefreshView;
	private int currentPage = 1;
	TextView txtResult;
	public MVFrameLayout(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.lay_search_grid, this);
		initView(view);
	}

	public MVFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	private void initView(View view) {
		fb = ImageCache
				.create(mContext, R.drawable.long_movie_default);
		pullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.grid_pull_refresh_view);
		pullToRefreshView.setOnFooterRefreshListener(this);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		gridView = (GridView) view.findViewById(R.id.gv_rank);
		txtMsg = (TextView) view.findViewById(R.id.msg);
		txtResult = (TextView) view.findViewById(R.id.result);
		adapter = new SearchMVAdapter(mContext, fb);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
	}

	public void startSearch(String searchStr) {
		this.searchStr = searchStr;
		txtMsg.setVisibility(View.VISIBLE);
		txtMsg.setText("正在努力加载数据。。。");
		pullToRefreshView.setVisibility(View.GONE);
		if (OnlineLogic.isCloseNetwork(mContext)) {
			mHandler.sendEmptyMessage(network_isclose);
			return;
		}
		currentPage = 1;
		new LoadDataTheard(this.searchStr, currentPage).start();
	}

	@SuppressWarnings("unused")
	private class LoadDataTheard extends Thread {
		private String key;
		private int loadPage;

		public LoadDataTheard(String key, int loadPage) {
			this.key = key;
			this.loadPage = loadPage;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			searchInfo = SearchLogic.getSearchMV(key, loadPage);
			mHandler.sendEmptyMessage(refresh_view);
		}

	}

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

	private void refreshView() {
		if (checkData()) {			
			txtResult.setText("共匹配到"+searchInfo.getTotal()+"个结果");
			adapter.setData(searchInfo.getList());
		}
	}

	private void addListData() {
		if (checkData()) {
			adapter.addData(searchInfo.getList());
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
		VideoSet videoSet = new VideoSet();
		videoSet.setCurrentLocation(position);
		videoSet.setDefinition(Config.VIDEO_RES_SC);
		videoSet.setName("");
		videoSet.setPosterUrl("");
		List<SearchMV> searchMVList = adapter.getSearchMV();
		if (searchMVList == null || searchMVList.size() == 0) {
			return;
		}
		int listSize = searchMVList.size();
		for (int l = 0; l < listSize; l++) {
			Video video = new Video();
			VideoResource videoResource = new VideoResource();
			videoResource.setResourceValue("");
			videoResource.setUrl(searchMVList.get(l).getUrl());
			videoResource.setVid(-1);
			video.addResource(videoResource);
			video.setVideoName(searchMVList.get(l).getMusicName());
			video.setMid(-1);
			video.setCurrentResource(videoResource);
			videoSet.AddVideo(video);
		}
		String json = Utils.toJson(videoSet);
		Intent intent = new Intent();
		intent.setClass(mContext,
				phone.wobo.music.videoplayer.WoboPlayerActivity.class);
		intent.putExtra(
				phone.wobo.music.videoplayer.WoboPlayerActivity.VIDEO_JSON_KEY, json);
		mContext.startActivity(intent);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (searchInfo.isHasNext()) {
			currentPage += 1;
			new Thread(new Runnable() {

				@Override
				public void run() {
					searchInfo = SearchLogic.getSearchMV(searchStr, currentPage);
					mHandler.sendEmptyMessage(ADD_DATA_refreshView);
				}
			}).start();
		} else {
			pullToRefreshView.setMoreData(false);
			mHandler.sendEmptyMessage(remove_refreshView);
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		currentPage = 1;
		new LoadDataTheard(searchStr, currentPage).start();
	}

}
