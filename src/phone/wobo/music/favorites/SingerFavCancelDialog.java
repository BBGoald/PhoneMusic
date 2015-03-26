package phone.wobo.music.favorites;

import phone.wobo.music.R;
import phone.wobo.music.model.SingerFavorites;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SingerFavCancelDialog extends Dialog
{
	private TextView singer_fav_name,singer_fav_cancel;
	private SingerFavorites singerFav;
	private Context context;
	
	public SingerFavCancelDialog(Context context,SingerFavorites singerFav)
	{
		super(context, R.style.WoboDialog);
		this.singerFav = singerFav;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		initDialog();
		
	}
	
	private void initDialog()
	{
		View view = LayoutInflater.from(this.context).inflate(R.layout.act_dialog_singerfav, null);
		setContentView(view);
		singer_fav_name = (TextView) view.findViewById(R.id.singer_fav_dialog_name);
		singer_fav_name.setText(singerFav.getSingerName());
		singer_fav_cancel = (TextView) view.findViewById(R.id.singer_fav_cancel_tv);
		
		singer_fav_cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				favCancel();
			}
		});
	}
	
	public void favCancel(){}
	
	@Override
	public void dismiss()
	{
		super.dismiss();
	}

}
