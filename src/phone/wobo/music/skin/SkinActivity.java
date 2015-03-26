package phone.wobo.music.skin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import phone.wobo.music.BaseActivity;
import phone.wobo.music.R;
import phone.wobo.music.bll.OnlineLogic;
import phone.wobo.music.mv.MVUtil;
import phone.wobo.music.util.ImageCache;
import phone.wobo.music.util.SharedFileHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class SkinActivity extends BaseActivity implements OnItemClickListener
{

	private static final String TAG = "liangbang";
	private final int SKIN_IMG_LIST_LOAD_FINISH = 101;
	private Context mContext;
	private GridView skin_gv;
	private List<Skin> imgList;
	private SkinAdapter adapter;
	private BitmapUtils fb;
	private static  String skinPath="";
	private TextView txt_msg;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_grid_refresh_no);
		mContext = SkinActivity.this;
		initView();
		loadData();
	}

	private void initView()
	{
		Log.i(TAG, "	--->SkinActivity--->initView");

		RelativeLayout t1 = (RelativeLayout) findViewById(R.id.header);
		t1.setBackgroundResource(R.color.half_transparent);
		setTitle("换肤");
		txt_msg = (TextView) findViewById(R.id.msg);
		skin_gv = (GridView) findViewById(R.id.gv_rank);
		skin_gv.setNumColumns(3);
		
		fb = ImageCache.create(mContext, R.drawable.bg);//默认壁纸
		adapter = new SkinAdapter(mContext,fb);
		skin_gv.setAdapter(adapter);
		skin_gv.setOnItemClickListener(this);
	}

	private void loadData()
	{
		Log.i(TAG, "	--->SkinActivity--->loadData");

		new Thread()
		{
			@Override
			public void run()
			{
				try{
					String json = SharedFileHelper.getPreference(mContext,SkinUtil.Skin_Name, SkinUtil.Skin_key);
					Log.i(TAG, "	--->SkinActivity--->run json= " + json);

					if (json == null || json.equals("")) {
						json = SkinUtil.getSkinJson();
					}
					imgList = SkinUtil.getSkinList(json);
					Log.i(TAG, "	--->SkinActivity--->run imgList= " + imgList);

					handler.sendEmptyMessage(SKIN_IMG_LIST_LOAD_FINISH);
					SkinUtil.updateSkinCache(mContext);
				}catch (Exception e){
					Log.e("SkinError", e.toString());
					Log.e(TAG, "	--->SkinActivity--->run--->Exception SkinError");

				}
				/*if (imgList != null)
				{
					handler.sendEmptyMessage(SKIN_IMG_LIST_LOAD_FINISH);
				}*/
			}

		}.start();
	}

	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == SKIN_IMG_LIST_LOAD_FINISH)
			{
				refreshView();
			} 
		}

	};

	private void refreshView()
	{
		Log.i(TAG, "	--->SkinActivity--->refreshView");

		if (imgList == null || imgList.size() == 0) {
			if (OnlineLogic.isCloseNetwork(mContext)) {
				txt_msg.setText(mContext.getResources().getString(
						R.string.network_connection_is_close));
			} else {
				txt_msg.setText(mContext.getResources().getString(
						R.string.data_load_failure));
			}
			txt_msg.setVisibility(View.VISIBLE);
			skin_gv.setVisibility(View.GONE);
			return;
		}
		txt_msg.setVisibility(View.GONE);
		skin_gv.setVisibility(View.VISIBLE);
		
		adapter.setData(imgList);
	}
	
	
	private void loadBackground() {
		Log.i(TAG, "	--->SkinActivity--->loadBackground");

		FrameLayout frameLayout = (FrameLayout) findViewById(android.R.id.content);
		String url = SkinUtil.getSelectedImg(this);
		if(url.equals("")){
			frameLayout.setBackgroundResource(R.drawable.bg);
			return;
		}
		Drawable da = getDrawable(this, url, false);		
		if (da != null){
			Log.i(TAG, "	--->SkinActivity--->loadBackground--->setBackgroundDrawable");

			frameLayout.setBackgroundDrawable(da);
		}
		else
			frameLayout.setBackgroundResource(R.drawable.bg);
	}
	
	
	public static Drawable getDrawable(Context context,String path,boolean isAssets) { 
		Log.i(TAG, "	--->SkinActivity--->getDrawable");

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bitmap = null;
		try {
			if(!isAssets) {
				bitmap = BitmapFactory.decodeFile(path, options);
				if (bitmap == null) {
					return null;
				}else {
					Log.i(TAG, "	--->SkinActivity--->getDrawable--->BitmapDrawable");
					return new BitmapDrawable(bitmap);
				}				
			}		
			InputStream is = context.getAssets().open(path);
			bitmap = BitmapFactory.decodeStream(is); 
			is.close();
			if(bitmap!= null){
				return new BitmapDrawable(bitmap); 
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();
			}
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null; 
	}
	
	
	private void download(String downloadUrl, final String targetPath,
			final int position) {
		HttpUtils http = new HttpUtils();
		http.configTimeout(20*1000);
		showToast("开始下载");
		http.download(downloadUrl, targetPath, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
			    showToast("下载成功");
				SkinUtil.setSelectedImg(mContext, targetPath);
				loadBackground();
				imgList.get(position).setLocalPath(targetPath);
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				try {
				File file = new File(targetPath);
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				showToast("下载失败");
				
			}
		});

	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		Log.i(TAG, "	--->SkinActivity--->onItemClick");

		Skin sk = imgList.get(position);
		String filename=skinPath+SkinUtil.getFileName(sk.getImgUrlBig());	
		Log.i(TAG, "	--->SkinActivity--->onItemClick filename= " + filename);

		if(SkinUtil.isExistsLocalImg(filename)){
			SkinUtil.setSelectedImg(mContext, filename);
			loadBackground();
			showToast("已应用");
		}else{				
			download(sk.getImgUrlBig(),filename,position);
		}	
	}

	private void initDownload(){
		skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/wobo/skin/";
		File f = new File(skinPath);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initDownload();
		loadBackground();
	}
	
}
