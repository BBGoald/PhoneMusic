package phone.wobo.music.hotmv;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotMVGridAdapter extends BaseAdapter{

	private Context context;
	//private FinalBitmap fb;
	private BitmapUtils fb;
	private List<HotMV> list = new ArrayList<HotMV>();
	
	public HotMVGridAdapter(Context context,BitmapUtils fb) {
		this.context = context;
		this.fb = fb;
	}
	public void setData(List<HotMV> list) {
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}
	
	public List<HotMV> getData(){
		return list;
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		if (list.size() == 0 || list == null){
			return null;
		}else{
			return list;
		}
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mv_grid3, null);
			holder.poster = (ImageView) convertView.findViewById(R.id.poster);
			holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);
			//holder.singer = (TextView) convertView.findViewById(R.id.hot_mv_singer);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		HotMV mvItem = list.get(position);
		holder.tv_name.setText(mvItem.getName());
	//	holder.singer.setText(mvItem.getSinger());
		if(holder.poster != null){
			fb.display(holder.poster, mvItem.getThumb());
		}else{
			holder.poster.setImageResource(R.drawable.online_grid_loading_default);
		}
		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView poster;
		private TextView tv_name;
		//private TextView singer;
	}

}
