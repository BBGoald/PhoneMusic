package phone.wobo.music.online;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.model.TypeLabel;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.SharedFileHelper;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TypeFrameLayout extends SimpleFrameLayout {

	private ListView listview;
	private TextView txt_msg;
	private Context mContext;
//	private FinalBitmap fb;
	private BitmapUtils fb;
	private final static int EVNET_TYPE_FINISH = 1;
	private List<TypeLabel> list;
	private TypeListAdapter adapter;
//	private LoadingDialog loading;

	public TypeFrameLayout(Context context) {
		this(context, null);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.lay_list, this);
//		loading = new LoadingDialog(mContext);
		list = new ArrayList<TypeLabel>();
		fb = ImageCache
				.create(mContext, R.drawable.long_movie_default);
		loading.show();
		initView(view);
		loadData();
	}

	public TypeFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	private void initView(View view) {
		txt_msg = (TextView) view.findViewById(R.id.msg);
		listview = (ListView) view.findViewById(R.id.ls_song);
		listview.setItemsCanFocus(true);
		adapter = new TypeListAdapter(mContext, fb);
		listview.setAdapter(adapter);
	}

	private void loadData() {
		new Thread() {
			@Override
			public void run() {
				String json = SharedFileHelper.getPreference(mContext,
						OnlineLogic.Online_Name, OnlineLogic.Online_Type_Key);
				if (json == null ||json.equals("")) {
					list = OnlineLogic.getType(mContext);
					handler.sendEmptyMessage(EVNET_TYPE_FINISH);
				}else {
					list = OnlineLogic.parseTypeLabelJson(json);
					handler.sendEmptyMessage(EVNET_TYPE_FINISH);
					OnlineLogic.updateTypeCache(mContext);
				}
				
			}
		}.start();
	}

	protected Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == EVNET_TYPE_FINISH) {
				loading.close();
				refreshTypeView(msg);
			}
			return false;
		}
	});

	/******************************************************
	 * 左边的总分类
	 *******************************************************/
	private void refreshTypeView(Message msg) {
		if (list == null || list.size() == 0) {
			if (OnlineLogic.isCloseNetwork(mContext)) {
				txt_msg.setText(mContext.getResources().getString(
						R.string.network_connection_is_close));
			} else {
				txt_msg.setText(mContext.getResources().getString(
						R.string.data_load_failure));
			}
			txt_msg.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			return;
		}
		txt_msg.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);
		adapter.setListData(list);
	}

}
