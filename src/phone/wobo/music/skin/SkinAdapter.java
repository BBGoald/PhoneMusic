package phone.wobo.music.skin;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SkinAdapter extends BaseAdapter {
	private static final String TAG = "liangbang";
	private Context context;
	private List<Skin> imgList;
	private BitmapUtils fb;

	public SkinAdapter(Context context, BitmapUtils fb) {
		Log.i(TAG, "......SkinAdapter");

		this.context = context;
		this.fb = fb;
	}

	public void setData(List<Skin> list) {
		Log.i(TAG, "	--->SkinAdapter--->setData");

		this.imgList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		Log.i(TAG, "	--->SkinAdapter--->getCount");
		if (imgList == null) {
			Log.i(TAG, "	--->SkinAdapter--->getCount imgList==null,return 0");
		}
		return imgList == null ? 0 : imgList.size();
	}

	@Override
	public Object getItem(int position) {
		Log.i(TAG, "	--->SkinAdapter--->getItem");

		return imgList == null ? null : imgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.i(TAG, "	--->SkinAdapter--->getItemId");

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "	--->SkinAdapter--->getView");

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_skin, null);
			holder.mv_poster = (ImageView) convertView
					.findViewById(R.id.poster);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Skin skin = (Skin) imgList.get(position);
		holder.mv_poster
		.setImageResource(R.drawable.long_movie_default);
		if (skin.getImgUrlSmall() != null && !skin.getImgUrlSmall().equals("")) {
			Log.i(TAG, "	--->SkinAdapter--->getView--->fb.display");
			fb.display(holder.mv_poster, skin.getImgUrlSmall());
		} 
		return convertView;
	}

	static class ViewHolder {
		private ImageView mv_poster;
	
	}

}
