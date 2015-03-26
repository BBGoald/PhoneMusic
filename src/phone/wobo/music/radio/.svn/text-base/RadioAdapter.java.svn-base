package phone.wobo.music.radio;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import phone.wobo.music.R;
import phone.wobo.music.model.Radio;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RadioAdapter extends BaseAdapter {
	Context mContext;
	List<Radio> mList;
//	private FinalBitmap fb;
	private BitmapUtils fb;
	
	public RadioAdapter(Context context,List<Radio> list,BitmapUtils fb) {
		this.mContext = context;
		this.mList=list;
		this.fb=fb;
	}
	
	@Override
	public int getCount() {
		if(mList!=null)return mList.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if(mList!=null)return mList.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
public void setDataSource(List<Radio> list){	
	this.mList=list;
	notifyDataSetChanged();
}
public void clearView() {
	if (mList != null ) {
		mList.clear();			
	}
}
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Radio mp=mList.get(position);	
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_onlinerank_grid, null);
			holder.poster=(ImageView) convertView.findViewById(R.id.poster);
			holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(mp.getImgurl()!=null && !mp.getImgurl().equals("")){
			fb.display(holder.poster, mp.getImgurl());
		}else{
			holder.poster.setImageResource(R.drawable.online_grid_loading_default);
		}
		holder.tv_name.setText(mp.getFmname());
		
		
		
	
		return convertView;
	}
	static class ViewHolder {
		private ImageView poster;
		private TextView tv_name;
	}
}
