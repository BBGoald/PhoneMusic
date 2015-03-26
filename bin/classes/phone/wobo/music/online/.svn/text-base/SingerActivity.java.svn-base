package phone.wobo.music.online;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.MVLabel;
import phone.wobo.music.model.SearchInfo;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.LetterLayout;
import phone.wobo.music.util.PullToRefreshView;
import phone.wobo.music.util.PullToRefreshView.OnFooterRefreshListener;
import phone.wobo.music.util.PullToRefreshView.OnHeaderRefreshListener;

public class SingerActivity extends BaseActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	// private GridView gv_rank;
	private ListView gv_rank;
	private Context mContext;
	//private FinalBitmap fb;
	private BitmapUtils fb;
	private final static int EVNET_TYPE_FINISH = 1;
	private final static int remove_refreshView = 0x1002;
	private final static int ADD_DATA_refreshView = 0x1003;
	private SearchInfo<MVLabel> mvInfo;
	private LabelGridAdapter adapter;
	private String key = "";
	private int page = 1;
	private PullToRefreshView pullToRefreshView;
	private TextView txt_msg;
	private int currentPage = 1;
	
	LetterLayout letterLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.act_grid);
		setContentView(R.layout.act_online_singerlist);
		mContext = SingerActivity.this;
		mvInfo = new SearchInfo<MVLabel>();
		fb = ImageCache
				.create(mContext, R.drawable.online_grid_loading_default);
		initView();
		Bundle bundle = this.getIntent().getExtras();
		key = bundle.getString("key");
		String name = bundle.getString("name");
		if (name != null && !name.equals("")) {
			setTitle(name);
		}
		if (key != null && !key.equals("")) {
			loading.show();
			loadData(key, page);
		}

	}

	private void initView() {
		
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.listview_pull_refresh_view);
		pullToRefreshView.setOnFooterRefreshListener(this);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		txt_msg = (TextView) findViewById(R.id.msg);
		gv_rank = (ListView) findViewById(R.id.ls_song);
		adapter = new LabelGridAdapter(mContext, fb,
				R.layout.item_online_singer_list);
		gv_rank.setAdapter(adapter);
		gv_rank.setOnItemClickListener(this);
		
		//字母筛选
		letterLayout = (LetterLayout) findViewById(R.id.lay_letter);
		letterLayout.setOnclickOfObjectBtn(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String text = ((Button) view).getTag().toString();
				
				showToast(text);	
				
				 if (key.indexOf(",") > 0)
					key = key.substring(0, key.indexOf(",")) + "," + text;
				else
					key = key + "," + text;
				 if(text.equals("全部")){
					 key = key.substring(0, key.indexOf(","));
					}
				// loading.show();
				loadData(key, 1);
			}
		});

	}

	private void loadData(final String key, final int page) {
		new Thread() {
			@Override
			public void run() {
				mvInfo = OnlineLogic.getSingerList(key, page);
				handler.sendEmptyMessage(EVNET_TYPE_FINISH);
			}
		}.start();
	}

	protected Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {

			switch (msg.what) {
			case EVNET_TYPE_FINISH:
				loading.close();
				refreshTypeView(msg);
				break;
			case remove_refreshView:
				pullToRefreshView.refreshComplete();
				break;
			case ADD_DATA_refreshView:
				addListData();
			}
			return false;
		}
	});

	/******************************************************
	 * 左边的总分类
	 *******************************************************/
	private void refreshTypeView(Message msg) {
		if (checkData()) {
			adapter.setListData(mvInfo.getList());
		}
	}

	private void addListData() {
		if (checkData()) {
			adapter.addListData(mvInfo.getList());
		} else {
			currentPage = 1;
		}
	}

	private boolean checkData() {
		if (mvInfo == null || mvInfo.getList() == null
				|| mvInfo.getList().size() == 0) {
			txt_msg.setVisibility(View.VISIBLE);
			pullToRefreshView.setVisibility(View.GONE);
			handler.sendEmptyMessage(remove_refreshView);
			if (isCloseNetwork()) {
				txt_msg.setText(getResources().getString(
						R.string.network_connection_is_close));
			} else {
				txt_msg.setText("暂无数据");
			}
			return false;
		}
		if (mvInfo.isHasNext()) {
			pullToRefreshView.setMoreData(true);
		} else {
			pullToRefreshView.setMoreData(false);
		}
		txt_msg.setVisibility(View.GONE);
		pullToRefreshView.setVisibility(View.VISIBLE);
		handler.sendEmptyMessage(remove_refreshView);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		List<MVLabel> listMVLabel = adapter.getListData();
		MVLabel label = listMVLabel.get(position);
		if (label == null)
			return;
		Intent intent = new Intent();
		intent.setClass(SingerActivity.this, SingerSongActivity.class);
		intent.putExtra("key", "" + label.getId());
		intent.putExtra("name", label.getName());
		intent.putExtra("singerrImg", label.getThumb());
		startActivity(intent);

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (mvInfo.isHasNext()) {
			currentPage += 1;
			new Thread(new Runnable() {

				@Override
				public void run() {
					mvInfo = OnlineLogic.getSingerList(key, currentPage);
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
