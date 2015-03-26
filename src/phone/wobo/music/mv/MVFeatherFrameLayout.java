package phone.wobo.music.mv;

import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import phone.wobo.music.R;
import phone.wobo.music.bll.MVHelper;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.MV;
import phone.wobo.music.model.MVLabel;
import phone.wobo.music.online.SimpleFrameLayout;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.SharedFileHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class MVFeatherFrameLayout extends SimpleFrameLayout implements
		OnItemClickListener {
private String type="version";
	private Context mContext;
	private final static int MV_FEATHER_FINISH = 3;
	private final int MV_LOAD_FINISH = 2;
	private List<MVLabel> list;
	private GridView gv_type;
	private BitmapUtils fb;
	private MVGridAdapter adapter;
	private TextView txt_msg;
	
	private MV mv;
	private GridView  gv_mv;	
	private MVShowAdapter mvAdapter;

	public MVFeatherFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public MVFeatherFrameLayout(Context context) {
		this(context, null);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.lay_mv_grid, this);
		list = new ArrayList<MVLabel>();
		fb = ImageCache
				.create(mContext, R.drawable.long_movie_default);
		loading.show();
		initView(view);
		loadData();
	}

	private void initView(View view) {
		gv_type = (GridView) view.findViewById(R.id.gv_type);
		txt_msg = (TextView) view.findViewById(R.id.msg);
		adapter = new MVGridAdapter(mContext, fb);
		gv_type.setAdapter(adapter);
		gv_type.setOnItemClickListener(this);
		
		gv_mv = (GridView) view.findViewById(R.id.gv_mv);
		mvAdapter = new MVShowAdapter(mContext, fb);
		gv_mv.setAdapter(mvAdapter);
		gv_mv.setOnItemClickListener(this);
	}

	private void loadData() {
		new Thread() {
			@Override
			public void run() {
				String json = SharedFileHelper.getPreference(mContext,
						MVUtil.MV_Name, MVUtil.MV_Version_Key);
				if (json == null || json.equals("")) {
					json = MVUtil.loadLabelData("version");
				}
				list = MVUtil.getMvLabelList(json);
				if(list != null && list.size()>6){
					list=list.subList(0, 6);
				}
				handler.sendEmptyMessage(MV_FEATHER_FINISH);
				if (list != null && list.size() > 0) {
					int index=MVHelper.getRandom(0,list.size());
					//loadMV("" + list.get(index).getId(), MVHelper.getRandom(1,3));
					
					mv = MVUtil.getMVList(type, ""+list.get(index).getId(), MVHelper.getRandom(1,3),20);
					handler.sendEmptyMessage(MV_LOAD_FINISH);
				}
				MVUtil.updateMVCache(mContext, MVUtil.MV_Version_Key,
						MVUtil.loadLabelData("version"));
			}

		}.start();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MV_FEATHER_FINISH) {
				loading.close();
				refreshFeatherView();
				/*if (list != null && list.size() > 0) {
					int index=MVHelper.getRandom(0,list.size());
					loadMV("" + list.get(index).getId(), MVHelper.getRandom(1,3));
				}*/
			}else if (msg.what == MV_LOAD_FINISH) {				
				if (MVHelper.isExistMVData(mContext, mv, txt_msg)) {
					mvAdapter.setData(mv.getList());
				}
			}
		}

	};

	public void refreshFeatherView() {
		if (list == null || list.size() == 0) {
			if (OnlineLogic.isCloseNetwork(mContext)) {
				txt_msg.setText(mContext.getResources().getString(
						R.string.network_connection_is_close));
			} else {
				txt_msg.setText(mContext.getResources().getString(
						R.string.data_load_failure));
			}
			txt_msg.setVisibility(View.VISIBLE);
			gv_type.setVisibility(View.GONE);
			return;
		}
		txt_msg.setVisibility(View.GONE);
		gv_type.setVisibility(View.VISIBLE);
		adapter.setData(list);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getId() == R.id.gv_type) {
			MVHelper.toMVListActivity(mContext, list.get(position),type);			
		} else if (parent.getId() == R.id.gv_mv) {	
			MVHelper.playMV(mContext, position, mvAdapter.getMVPlayInfoList());
		}
	}

}
