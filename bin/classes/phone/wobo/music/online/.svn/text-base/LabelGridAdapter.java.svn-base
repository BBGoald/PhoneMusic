//package phone.wobo.music.online;
//
//import java.util.List;
//
//import net.tsz.afinal.FinalBitmap;
//
//import phone.wobo.music.R;
//import phone.wobo.music.model.MVLabel;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
///**
// * @author heming
// * @since :JDK ?  
// * @version：1.0
// * Create at:2013-12-30
// * Description:  
// */
//public class LabelGridAdapter extends BaseAdapter {
//
//	private Context context;
//	private List<MVLabel> list;
//	private FinalBitmap fb; 
//
//	public LabelGridAdapter(Context context,FinalBitmap fb) {
//		this.context = context;
//		this.fb=fb;
//	}
//
//	public void setListData(List<MVLabel> list) {
//		this.list = list;
//		notifyDataSetChanged();
//	}
//	
//	public MVLabel getPositionData(int position){
//		if(null == list || list.size() <= position){
//			return null;
//		}
//		return list.get(position);
//	}
//
//	@Override
//	public int getCount() {
//		return list == null ? 0 : list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return 0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = LayoutInflater.from(context).inflate(R.layout.item_onlinerank_grid, null);
//			holder.poster=(ImageView) convertView.findViewById(R.id.poster);
//			holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		fb.display(holder.poster, ((MVLabel) list.get(position)).getThumb());
//		holder.tv_name.setText(((MVLabel) list.get(position)).getName());
//		return convertView;
//	}
//
//	static class ViewHolder {
//		private ImageView poster;
//		private TextView tv_name;
//	}
//
//	
//}
package phone.wobo.music.online;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import phone.wobo.music.model.MVLabel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-12-30
 * Description:  
 */
public class LabelGridAdapter extends BaseAdapter {

	private Context context;
	private List<MVLabel> list = new ArrayList<MVLabel>();
//	private FinalBitmap fb; 
	private BitmapUtils fb;
	int layout=R.layout.item_onlinerank_grid;
	public LabelGridAdapter(Context context,BitmapUtils fb) {
		this.context = context;
		this.fb=fb;
		layout=R.layout.item_onlinerank_grid;
	}
	public LabelGridAdapter(Context context,BitmapUtils fb,int layout) {
		this.context = context;
		this.fb=fb;
		this.layout=layout;
	}

	public void setListData(List<MVLabel> list) {
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}
	
	
	public void addListData(List<MVLabel> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	
	public List<MVLabel> getListData() {
		return list;
	}
	
	public MVLabel getPositionData(int position){
		if(null == list || list.size() <= position){
			return null;
		}
		return list.get(position);
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
			convertView = LayoutInflater.from(context).inflate(layout, null);
			holder.poster=(ImageView) convertView.findViewById(R.id.poster);
			holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MVLabel label=	(MVLabel) list.get(position);
		if(label.getThumb()!=null && !label.getThumb().equals("")){
			fb.display(holder.poster, label.getThumb());
		}else{
			holder.poster.setImageResource(R.drawable.online_grid_loading_default);
		}
		holder.tv_name.setText(label.getName());
		return convertView;
	}

	static class ViewHolder {
		private ImageView poster;
		private TextView tv_name;
	}

	
}
