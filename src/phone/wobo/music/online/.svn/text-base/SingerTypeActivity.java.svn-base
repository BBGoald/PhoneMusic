package phone.wobo.music.online;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.favorites.FavoritesSingerActivity;
import phone.wobo.music.favorites.SingerListAdapter;
import phone.wobo.music.model.MVLabel;
import phone.wobo.music.model.SingerFavorites;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.SharedFileHelper;

public class SingerTypeActivity extends BaseActivity
{
	private GridView singer_imgshow;
	private Context mContext;
	private ListView gv_rank;
	private BitmapUtils fb;
	private final static int EVNET_TYPE_FINISH = 1;
	private final static int SINGER_FAV_LOAD_FISISH = 1001;
	private RelativeLayout singer_fav, singer_fav_more;
	private List<MVLabel> list;
	private LabelGridAdapter adapter;
	private List<SingerFavorites> singerList;
	private SingerListAdapter singerFavAdapter;

	private TextView txt_msg;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_list_singertype);

		setTitle("歌手");
		mContext = SingerTypeActivity.this;
		list = new ArrayList<MVLabel>();
		fb = ImageCache.create(mContext, R.drawable.long_movie_default);
		loading.show();
		initView();
		loadData();
	}

	private void initView()
	{
		gv_rank = (ListView) findViewById(R.id.gv_rank);
		singer_fav = (RelativeLayout) findViewById(R.id.singer_fav);
		singer_imgshow = (GridView) findViewById(R.id.singer_fav_imgshow);
		singer_fav_more = (RelativeLayout) findViewById(R.id.singer_fav_more);
		singerList = new ArrayList<SingerFavorites>();
		adapter = new LabelGridAdapter(mContext, fb,R.layout.item_online_singertype_list);
		gv_rank.setAdapter(adapter);
		txt_msg = (TextView) findViewById(R.id.msg);
		singer_imgshow.setOnItemClickListener(itemclicklistener);
		gv_rank.setOnItemClickListener(itemclicklistener);

		singer_fav_more.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(mContext, FavoritesSingerActivity.class);
				startActivity(intent);
			}
		});
	}

	OnItemClickListener itemclicklistener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			if (parent.getId() == R.id.singer_fav_imgshow)
			{
				SingerFavorites label = singerList.get(position);
				if (label == null)
					return;
				Intent intent = new Intent();
				intent.setClass(SingerTypeActivity.this,SingerSongActivity.class);
				intent.putExtra("key", "" + label.getKeySinger());
				intent.putExtra("name", label.getSingerName());
				intent.putExtra("singerrImg", label.getImgUrl());
				startActivity(intent);
			}else if(parent.getId() == R.id.gv_rank){
				MVLabel label = list.get(position);
				if (label == null)
					return;
				Intent intent = new Intent();
				intent.setClass(SingerTypeActivity.this, SingerActivity.class);
				intent.putExtra("key", "" + label.getId());
				intent.putExtra("name", label.getName());
				startActivity(intent);
			}
		}
	};

	private void loadData()
	{

		new Thread()
		{
			@Override
			public void run()
			{
				String json = SharedFileHelper.getPreference(mContext,
						OnlineLogic.Online_Name,
						OnlineLogic.Online_SingerType_Key);
				if (json == null || json.equals(""))
				{
					list = OnlineLogic.getSingerType(mContext);
					handler.sendEmptyMessage(EVNET_TYPE_FINISH);
				} else
				{
					list = OnlineLogic.parseSingerTypeJson(json);
					handler.sendEmptyMessage(EVNET_TYPE_FINISH);
					OnlineLogic.updateSingerTypeCache(mContext);
				}

			}
		}.start();

	}

	private void loadSingerFavImg()
	{
		new Thread()
		{

			@Override
			public void run()
			{
				singerList = dbHelper.getSingerFavoritesList(4);
				handler.sendEmptyMessage(SINGER_FAV_LOAD_FISISH);
			}

		}.start();
	}

	protected Handler handler = new Handler(new Handler.Callback()
	{
		@Override
		public boolean handleMessage(Message msg)
		{
			if (msg.what == EVNET_TYPE_FINISH)
			{
				loading.close();
				refreshTypeView(msg);
			} else if (msg.what == SINGER_FAV_LOAD_FISISH)
			{
				refreshSingerFavImg();
			}
			return false;
		}
	});

	private void refreshSingerFavImg()
	{
		if (singerList != null && singerList.size() != 0)
		{
			singer_fav.setVisibility(View.VISIBLE);
			singerFavAdapter = new SingerListAdapter(mContext, singerList, fb);
			singer_imgshow.setAdapter(singerFavAdapter);
		} else
		{
			singer_fav.setVisibility(View.GONE);
		}
	}

	private void refreshTypeView(Message msg)
	{
		if (list == null || list.size() == 0)
		{
			if (isCloseNetwork())
			{
				txt_msg.setText(getResources().getString(
						R.string.network_connection_is_close));
			} else
			{
				txt_msg.setText(getResources().getString(
						R.string.data_load_failure));
			}
			txt_msg.setVisibility(View.VISIBLE);
			gv_rank.setVisibility(View.GONE);
		} else
		{
			txt_msg.setVisibility(View.GONE);
			gv_rank.setVisibility(View.VISIBLE);
			adapter.setListData(list);
		}
	}

	/*
	 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
	 * position, long arg3) {
	 * 
	 * MVLabel label = list.get(position); if (label == null) return; Intent
	 * intent = new Intent(); intent.setClass(SingerTypeActivity.this,
	 * SingerActivity.class); intent.putExtra("key", "" + label.getId());
	 * intent.putExtra("name", label.getName()); startActivity(intent);
	 * 
	 * }
	 */

	@Override
	protected void onResume()
	{
		super.onResume();
		loadSingerFavImg();
	}

}
