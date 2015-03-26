package phone.wobo.music.hotmv;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.Config;
import phone.wobo.music.R;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.PullToRefreshView;
import phone.wobo.music.util.PullToRefreshView.OnFooterRefreshListener;
import phone.wobo.music.util.PullToRefreshView.OnHeaderRefreshListener;
import phone.wobo.music.util.Utils;
import phone.wobo.music.videoplayer.Video;
import phone.wobo.music.videoplayer.VideoResource;
import phone.wobo.music.videoplayer.VideoSet;
import phone.wobo.music.videoplayer.WoboPlayerActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class HotMVActivity extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	private Context mContext;
	private List<HotMV> mvList = new ArrayList<HotMV>();
	private GridView gridView;
	private TextView txtMsg;
	private PullToRefreshView pullToRefreshView;
//	private FinalBitmap fb;
	private BitmapUtils fb;
	private final int REFRESH_VIEW = 0x1000;
	private final int remove_refreshView = 0x1002;
	private HotMVGridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = HotMVActivity.this;
		setContentView(R.layout.act_grid);
		gridView = (GridView) findViewById(R.id.gv_rank);
		txtMsg = (TextView) findViewById(R.id.msg);
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.grid_pull_refresh_view);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterRefreshListener(this);
		gridView.setOnItemClickListener(itemClickListener);
		fb = ImageCache.create(this, R.drawable.long_movie_default);
		adapter = new HotMVGridAdapter(this, fb);
		gridView.setAdapter(adapter);
		setTitle(getString(R.string.wobo_music_hotmv_title));
		loading.show();
		loadData();
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			play(position);
		}
	};

	private void loadData() {
		new Thread() {
			@Override
			public void run() {
				pullToRefreshView.setMoreData(false);
				HotMVInfo hotMVInfo = new HotMVInfo(mContext);
				String json = hotMVInfo.getHotMVJson(0);
				mvList = getHotMV(json);
				myhandler.sendEmptyMessage(REFRESH_VIEW);
			}
		}.start();
	}

	private List<HotMV> getHotMV(String json) {
		if (json == null || json == "") {
			return null;
		}
		List<HotMV> list = new ArrayList<HotMV>();
		try {
			JSONTokener jsonParser = new JSONTokener(json);
			JSONArray jsonArray = (JSONArray) jsonParser.nextValue();

			for (int i = 0; i < jsonArray.length(); i++) {
				HotMV hotMV = new HotMV();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				hotMV.setDate(jsonObject.getString("date"));
				hotMV.setName(jsonObject.getString("name"));
				hotMV.setPlayurl(jsonObject.getString("playurl"));
				hotMV.setSinger(jsonObject.getString("singer"));
				hotMV.setThumb(jsonObject.getString("thumb"));
				list.add(hotMV);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	private Handler myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_VIEW:
				loading.close();
				refreshView();
				break;
			case remove_refreshView:
				pullToRefreshView.refreshComplete();
				break;
			}
		}

	};

	private void refreshView() {
		if (mvList != null && mvList.size() != 0) {
			pullToRefreshView.setVisibility(View.VISIBLE);
			txtMsg.setVisibility(View.GONE);
			adapter.setData(mvList);
			myhandler.sendEmptyMessage(remove_refreshView);
		} else {
			pullToRefreshView.setVisibility(View.GONE);
			if (isCloseNetwork()) {
				txtMsg.setText(getResources().getString(
						R.string.network_connection_is_close));
			} else {
				txtMsg.setText("暂无数据");
			}
			txtMsg.setVisibility(View.VISIBLE);
			myhandler.sendEmptyMessage(remove_refreshView);
		}
	}

	private void play(int location) {
		VideoSet videoSet = new VideoSet();
		videoSet.setCurrentLocation(location);
		videoSet.setDefinition(Config.VIDEO_RES_SC);
		videoSet.setName("");
		videoSet.setPosterUrl("");
		int listSize = mvList.size();
		for (int l = 0; l < listSize; l++) {
			Video video = new Video();
			VideoResource videoResource = new VideoResource();
			videoResource.setResourceValue("");
			videoResource.setUrl(mvList.get(l).getPlayurl());
			videoResource.setVid(-1);
			video.addResource(videoResource);
			video.setVideoName(mvList.get(l).getName());
			video.setMid(-1);
			video.setCurrentResource(videoResource);
			videoSet.AddVideo(video);
		}
		String json = Utils.toJson(videoSet);
		Intent intent = new Intent();
		intent.setClass(this, WoboPlayerActivity.class);
		intent.putExtra(WoboPlayerActivity.VIDEO_JSON_KEY, json);
		startActivity(intent);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		myhandler.sendEmptyMessage(remove_refreshView);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		loadData();
	}

}
