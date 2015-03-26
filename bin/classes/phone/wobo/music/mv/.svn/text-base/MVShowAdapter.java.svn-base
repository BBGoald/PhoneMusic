package phone.wobo.music.mv;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import phone.wobo.music.model.MVPlayInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MVShowAdapter extends BaseAdapter {
	private List<MVPlayInfo> list = new ArrayList<MVPlayInfo>();
	private Context mContext;
//	private FinalBitmap fb;
	private BitmapUtils fb;

	public MVShowAdapter(Context context, BitmapUtils fb) {
		mContext = context;
		this.fb = fb;
	}
	public void setData(List<MVPlayInfo> list)
	{
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}


	public void addData(List<MVPlayInfo> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void clearData() {
		this.list.clear();
	}

	public List<MVPlayInfo> getMVPlayInfoList() {
		return this.list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_mv_grid3, null);
			holder.poster = (ImageView) convertView.findViewById(R.id.poster);
			holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MVPlayInfo mvInfo = (MVPlayInfo) list.get(position);

		if (mvInfo.getThumb() != null && !mvInfo.getThumb().equals("")) {
			fb.display(holder.poster, mvInfo.getThumb());
		} else {
			holder.poster
					.setImageResource(R.drawable.online_grid_loading_default);
		}
		holder.tv_name.setText(mvInfo.getName());
		return convertView;
	}

	static class ViewHolder {
		private ImageView poster;
		private TextView tv_name;
	}

}