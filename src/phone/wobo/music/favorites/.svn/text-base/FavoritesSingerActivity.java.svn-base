package phone.wobo.music.favorites;

import java.util.ArrayList;
import java.util.List;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.bll.DatabaseHelper;
import phone.wobo.music.model.SingerFavorites;
import phone.wobo.music.online.LabelGridAdapter;
import phone.wobo.music.online.SingerSongActivity;
import phone.wobo.music.util.ImageCache;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

public class FavoritesSingerActivity extends BaseActivity implements
		OnItemClickListener
{

	private GridView gv_rank;
	private Context mContext;
	private List<SingerFavorites> list;
	private final static int EVNET_LIST_FINISH = 1;
	private final static int EVNET_LIST_FRESH = 10010;
	private BitmapUtils fb;
	//private LabelGridAdapter adapter;
	private SingerListAdapter adapter;
	private TextView tv_msg;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_grid_refresh_no);
		mContext = FavoritesSingerActivity.this;
		setTitle("已关注歌手");
		initView();

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		loadData();
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
	}

	private void initView()
	{
		tv_msg = (TextView) findViewById(R.id.msg);
		fb = ImageCache
				.create(mContext, R.drawable.online_grid_loading_default);
		list = new ArrayList<SingerFavorites>();
		gv_rank = (GridView) findViewById(R.id.gv_rank);
		gv_rank.setNumColumns(3);
		adapter = new SingerListAdapter(mContext,null,fb,R.layout.item_mv_grid3);
		gv_rank.setAdapter(adapter);
		
		gv_rank.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				SingerFavorites label = list.get(position);
				if (label == null)
					return;
				Intent intent = new Intent();
				intent.setClass(FavoritesSingerActivity.this, SingerSongActivity.class);
				intent.putExtra("key", "" + label.getKeySinger());
				intent.putExtra("name", label.getSingerName());
				intent.putExtra("singerrImg", label.getImgUrl());
				startActivity(intent);
				
			}
		});
		gv_rank.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				final SingerFavorites singerFav = list.get(position);
				new SingerFavCancelDialog(mContext, singerFav)
				{

					@Override
					public void favCancel()
					{
						DatabaseHelper.deleteSingerFavorites(singerFav);
						handler.sendEmptyMessage(EVNET_LIST_FRESH);
						dismiss();
					}

					@Override
					public void dismiss()
					{
						super.dismiss();
					}
					
				}.show();
				return true;
			}
		});
		
	}

	private boolean loadData()
	{
		new Thread()
		{
			public void run()
			{
				list = dbHelper.getSingerFavoritesList();
				handler.sendEmptyMessage(EVNET_LIST_FINISH);
			};
		}.start();
		return true;
	}

	protected Handler handler = new Handler(new Handler.Callback()
	{
		@Override
		public boolean handleMessage(Message msg)
		{
			if (msg.what == EVNET_LIST_FINISH)
			{
				loading.close();
				refreshListView();
			}else if(msg.what == EVNET_LIST_FRESH){
				loadData();
			}
			return false;
		}
	});

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		SingerFavorites label = list.get(arg2);
		if (label == null)
			return;
		Intent intent = new Intent();
		intent.setClass(FavoritesSingerActivity.this, SingerSongActivity.class);
		intent.putExtra("key", "" + label.getKeySinger());
		intent.putExtra("name", label.getSingerName());
		intent.putExtra("singerrImg", label.getImgUrl());
		startActivity(intent);
	}

	/************************************************************************
	 * 更新ListView数据
	 * 
	 * @param msg
	 *************************************************************************/
	private void refreshListView()
	{
		if (list == null || list.size() == 0)
		{
			tv_msg.setVisibility(View.VISIBLE);
			gv_rank.setVisibility(View.GONE);
			tv_msg.setText("暂无数据");
			return;
		}
		tv_msg.setVisibility(View.GONE);
		gv_rank.setVisibility(View.VISIBLE);
		adapter.setListData(list);
	}

}
