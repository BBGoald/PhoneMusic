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

import java.util.List;
import phone.wobo.music.R;
import phone.wobo.music.model.MVLabel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * @author huayanlan
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-12-30
 * Description:  
 */
public class SubTypeAdapter extends BaseAdapter {

	private Context context;
	private List<MVLabel> list;


	public SubTypeAdapter(Context context,List<MVLabel> list) {
		this.context = context;
		this.list = list;
	}

	public void setListData(List<MVLabel> list) {
		this.list = list;
		notifyDataSetChanged();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_type_button, null);
			holder.txt_typename=(TextView) convertView.findViewById(R.id.txt_typename);		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MVLabel label=	(MVLabel) list.get(position);		
		holder.txt_typename.setText(label.getName());
		holder.txt_typename.setTag(label.getId());
		
		return convertView;
	}

	static class ViewHolder {
		private TextView txt_typename;
		
	}
	
}
