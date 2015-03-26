package phone.wobo.music.online;

import java.util.List;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.MusicInfo;
import phone.wobo.music.model.SearchInfo;
import phone.wobo.music.model.SingerFavorites;
import phone.wobo.music.search.SearchActivity;
import phone.wobo.music.util.FuncUtils;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.PullToRefreshView;
import phone.wobo.music.util.PullToRefreshView.OnFooterRefreshListener;
import phone.wobo.music.util.PullToRefreshView.OnHeaderRefreshListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

public class SingerSongActivity extends BaseActivity implements
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
	private TextView tv_msg;
	private PullToRefreshView pullToRefreshView;
	private int currentPage = 1;
	//private ImageView singerImg;
	private ImageView singerFavSwitch;
	private String singerImgUrl;
	private BitmapUtils fb;
	private String name;
	private SingerFavorites singerFavorites;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_singer_song);

		mContext = SingerSongActivity.this;
		musicInfo = new SearchInfo<MusicInfo>();
		Bundle bundle = this.getIntent().getExtras();
		key = bundle.getString("key");
		name = bundle.getString("name");
		singerImgUrl = bundle.getString("singerrImg");
		// 初始化歌手收藏表
		singerFavorites = new SingerFavorites();
		singerFavorites.setImgUrl(singerImgUrl);
		singerFavorites.setKeySinger(key);
		singerFavorites.setSingerName(name);

		fb = ImageCache
				.create(mContext, R.drawable.online_grid_loading_default);

		initView();

		if (name != null && !name.equals("")) {
			setTitle(name);
		}
		if (key != null && !key.equals("")) {
			loading.show();
			loadData(key, pageIndex);
		}
	}
	
	protected void clickBack() {
		
		ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
		ImageButton btn_search = (ImageButton)findViewById(R.id.btn_search);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MobclickAgent.onEvent(getApplicationContext(), "btn_search");
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SearchActivity.class);
				startActivity(intent);
			}
		});
	}
	private void initView() {
	//	singerImg = (ImageView) findViewById(R.id.singer_img);
		singerFavSwitch = (ImageView) findViewById(R.id.singer_fav_switch);

		tv_msg = (TextView) findViewById(R.id.msg);
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.listview_pull_refresh_view);
		pullToRefreshView.setOnFooterRefreshListener(this);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		ls_song = (ListView) findViewById(R.id.ls_song);
		ls_song.setItemsCanFocus(true);

		// 加载歌手图像
		//fb.display(singerImg, singerImgUrl);
		// 参数为1表示没有被收藏
		if (DatabaseHelper.isExistSingerFavorites(singerFavorites)) {
			singerFavSwitch.setImageResource(R.drawable.list_btn_faved2);
		} else {
			singerFavSwitch.setImageResource(R.drawable.list_btn_fav);
		}

		singerFavSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DatabaseHelper.isExistSingerFavorites(singerFavorites)) {
					showToast("取消关注");
					singerFavSwitch.setImageResource(R.drawable.list_btn_fav);
					DatabaseHelper.deleteSingerFavorites(singerFavorites);
				} else {
					showToast("已关注");
					singerFavSwitch.setImageResource(R.drawable.list_btn_faved2);
					DatabaseHelper.addFavoritesSingers(singerFavorites);
				}

			}
		});

		adapter = new SongListAdapter(mContext) {
			protected void play(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					playMusic(listMusic, position);
					FuncUtils.beginClickAnimation(SingerSongActivity.this,
							(View) view.getParent(),
							phone.wobo.music.R.drawable.ic_anim_music);
				}
			}

			protected void add2List(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					add2PlayList(listMusic.get(position));
					FuncUtils.beginClickAnimation(SingerSongActivity.this,
							view, phone.wobo.music.R.drawable.ic_anim_add);
				}
			}

			protected void add2fav(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					handleFavorite(view, listMusic.get(position));
				}
			}

			protected void add2download(View view, int position) {
				List<MusicInfo> listMusic = adapter.getListData();
				if (isExistMusic(listMusic)) {
					startDownload(listMusic.get(position));
					FuncUtils.beginClickAnimation(SingerSongActivity.this,
							view, phone.wobo.music.R.drawable.btn_download);
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
				musicInfo = OnlineLogic.getSingerSongList(key, pageNumber);
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
				/*
				 * case EVENT_FAV_SWITCH_FINISH: //切换收藏< --- >取消收藏
				 * switchFavImg(); break;
				 */
			}
			return false;
		}
	});

	/*
	 * private void initSingerSwitchImg() { if(favSwitch % 2 == 0) { //falSwitch
	 * == 奇数 表示关注 //falSwitth == 偶数 表示取消关注
	 * singerFavSwitch.setImageDrawable(getResources
	 * ().getDrawable(R.drawable.fav_switch_2)); }else{
	 * singerFavSwitch.setImageDrawable
	 * (getResources().getDrawable(R.drawable.fav_switch_1)); } }
	 * 
	 * private void switchFavImg() { if(favSwitch % 2 == 0) { //falSwitch == 奇数
	 * 表示关注 //falSwitth == 偶数 表示取消关注 //图片切换到2,将歌手添加到歌手收藏
	 * singerFavSwitch.setImageDrawable
	 * (getResources().getDrawable(R.drawable.fav_switch_2));
	 * DatabaseHelper.addFavoritesSingers(singerFavorites); }else{
	 * //图片切换到1,将歌手从歌手收藏中取消
	 * singerFavSwitch.setImageDrawable(getResources().getDrawable
	 * (R.drawable.fav_switch_1));
	 * DatabaseHelper.deleteSingerFavorites(singerImgUrl); } }
	 */

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
					musicInfo = OnlineLogic.getSingerSongList(key, currentPage);
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
