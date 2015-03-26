package phone.wobo.music.search;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import phone.wobo.music.model.SearchMV;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchMVAdapter extends BaseAdapter {
	private Context context;
	private List<SearchMV> list = new ArrayList<SearchMV>();
//	private FinalBitmap fb;
	private BitmapUtils fb;
	
	public SearchMVAdapter(Context context, BitmapUtils fb) {
		super();
		this.context = context;
		this.fb = fb;
	}

	public void setData(List<SearchMV> list) {
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}

	public void addData(List<SearchMV> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	
	public SearchMV getPositionData(int position){
		if(null == list || list.size() <= position){
			return null;
		}
		return list.get(position);
	}
	
	public List<SearchMV> getSearchMV() {
		return list;
	}
	
	public void clearData() {
		this.list.clear();
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mv_grid3, null);
			holder.poster=(ImageView) convertView.findViewById(R.id.poster);
			holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SearchMV label=	(SearchMV) list.get(position);
		if(label.getThumb()!=null && !label.getThumb().equals("")){
			fb.display(holder.poster, label.getThumb());
		}else{
			holder.poster.setImageResource(R.drawable.online_grid_loading_default);
		}
		holder.tv_name.setText(label.getMusicName()+"-"+label.getmArtist());
		return convertView;
	}
	static class ViewHolder {
		private ImageView poster;
		private TextView tv_name;
	}
}
