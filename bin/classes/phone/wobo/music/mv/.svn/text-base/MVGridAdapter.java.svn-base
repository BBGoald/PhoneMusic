package phone.wobo.music.mv;

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

public class MVGridAdapter extends BaseAdapter
{
	
	private List<MVLabel> list;
	private Context context;
//	private FinalBitmap fb;
	private BitmapUtils fb;

	
	public MVGridAdapter(Context context,BitmapUtils fb) {
		this.context = context;
		this.fb=fb;
	}
	
	@Override
	public int getCount()
	{
		if (list == null || list.size() == 0){
			return 0;
		}else{
			return list.size();
		}
	}

	public void setData(List<MVLabel> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public Object getItem(int position)
	{
		return (list == null || list.size() == 0 )?null:list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mv_type_grid, null);
			holder.poster=(ImageView) convertView.findViewById(R.id.poster);
			//holder.tv_name = (TextView) convertView.findViewById(R.id.txtName);
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
		//holder.tv_name.setText(label.getName());
		return convertView;
	}

	static class ViewHolder {
		private ImageView poster;
	//	private TextView tv_name;
	}
	

}
