package phone.wobo.music.mv;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.model.Singer;
import phone.wobo.music.model.SingerDetail;
import phone.wobo.music.util.LetterLayout;
import phone.wobo.music.util.NetTools;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SingerListActivity extends BaseActivity implements
		OnItemClickListener {
	private Context mContext;
	private String labelName;
	private String key;
	private SingerListAdapter adapter;
	private final int SINGER_LIST_LOADED_FINISH = 110;
	private Singer newSinger;
	private String singerName;
	LetterLayout letterLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_singer_list);
		mContext = SingerListActivity.this;

		Bundle bundle = this.getIntent().getExtras();
		labelName = bundle.getString("name");
		key = bundle.getString("key");
		setTitle(labelName);
		initView();
		loadData("all");
	}

	private void initView() {
		final ListView lv = (ListView) findViewById(R.id.singerlist);
		adapter = new SingerListAdapter(mContext);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);	
		//字母筛选
		letterLayout = (LetterLayout) findViewById(R.id.lay_letter);
		letterLayout.setOnclickOfObjectBtn(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String text = ((Button) view).getTag().toString();				
				showToast(text);
				 loadData(text);
			}
		});
	}

	private void loadData(final String letKey) {
		new Thread() {

			@Override
			public void run() {
				if (key == null || key.equals("")) {
					Log.e("获取歌手类型ID失败", "获取歌手类型ID失败");
					return;
				}
				String url = "mv/sigerlist.html?initial=" + letKey
						+ "&singertype=" + key;
				try {
					String json = NetTools.getHtml(url);
					if ("".equals(json)) {
						// sendMessage(GetSingerListFail);
						return;
					}
					newSinger = MVUtil.getSinger(json);
					if (newSinger == null) {
						// sendMessage(GetSingerListFail);
						return;
					}
					handler.sendEmptyMessage(SINGER_LIST_LOADED_FINISH);
				} catch (Exception e) {
					e.printStackTrace();
					// sendMessage(GetSingerListFail);
				}
			};

		}.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == SINGER_LIST_LOADED_FINISH) {
				refreshView();
			} else {
				super.handleMessage(msg);
			}
		}

	};

	private void refreshView() {
		adapter.setData(newSinger);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (newSinger == null) {
			return;
		}
		SingerDetail singer = newSinger.getList().get(position);
		singerName = singer.getName();
		int key = singer.getId();

		Intent intent = new Intent();
		intent.setClass(mContext, MVShowActivity.class);
		intent.putExtra("name", singerName);
		intent.putExtra("key", String.valueOf(key));
		intent.putExtra("type", "singerid");
		mContext.startActivity(intent);
	}

}