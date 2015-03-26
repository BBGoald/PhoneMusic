package phone.wobo.music.setting;

import phone.wobo.music.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WoboDialog extends Dialog
{
	private static final String TAG = "liangbang";
	private Context context;
	private String dialogMsg = "";
	private String title = "";
	private TextView tv_title, tv_msg, positiveTxv, negativeTxv;
	private CheckBox checkBox;
	private LinearLayout lay_checkbox;
	public WoboDialog(Context context)
	{
		super(context, R.style.WoboDialog);
	}

	public WoboDialog(Context context, String dialogMsg)
	{
		super(context, R.style.WoboDialog);
		this.context = context;
		this.dialogMsg = dialogMsg;
	}

	public WoboDialog(Context context, String title, String dialogMsg)
	{
		super(context, R.style.WoboDialog);
		this.context = context;
		this.title = title;
		this.dialogMsg = dialogMsg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.d(TAG, "******instance WoboDialog");
/*		Log.d(TAG, "this= " + this.hashCode());
		Log.d(TAG, "WoboDialog.this= " + WoboDialog.this.hashCode());
		Log.d(TAG, "current Thread= " + Thread.currentThread().getId());
		Log.d(TAG, "current Process= " + android.os.Process.myPid());
		*/
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		initDialog(context);
	}

	private void initDialog(Context context)
	{
		final View view = LayoutInflater.from(context).inflate(
				R.layout.act_dialog, null);
		setContentView(view);
		tv_title = (TextView) view.findViewById(R.id.setting_cache_title);
		tv_msg = (TextView) view.findViewById(R.id.setting_cache_msg);
		lay_checkbox=(LinearLayout) view.findViewById(R.id.lay_checkbox);
		checkBox = (CheckBox) view.findViewById(R.id.dialog_checkbox);
		positiveTxv = (TextView) view.findViewById(R.id.dialog_ok);
		negativeTxv = (TextView) view.findViewById(R.id.dialog_cancle);

		tv_title.setText(title);
		tv_msg.setText(dialogMsg);
		
		positiveTxv.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				setOnPositiveListener();
				
			}
		});
		
		negativeTxv.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				setOnNegativeListener();
				dismiss();
			}
		});
		
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				OnCheckBoxListener(isChecked);
			}
		});
		
		
		
		setCheckBoxVisibility(false);
		
	}

	/*
	 * CheckBox Listener
	 */
	public void OnCheckBoxListener(boolean isChecked)
	{
		
	}
	
	
	/*
	 * 设置CheckBox可见性
	 */
	public void setCheckBoxVisibility(boolean visibility)
	{
		if(visibility)
		{
			lay_checkbox.setVisibility(View.VISIBLE);
		}else{
			lay_checkbox.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置确定键文字
	 * 
	 * @param text
	 */
	public void setPositiveText(String text)
	{
		positiveTxv.setText(text);
	}

	/**
	 * 设置取消键文字
	 * 
	 * @param text
	 */
	public void setNegativeText(String text)
	{
		negativeTxv.setText(text);
	}

	/**
	 * 确定键监听器
	 * 
	 * @param listener
	 */
	public void setOnPositiveListener()
	{
		
	}

	/**
	 * 取消键监听器
	 * 
	 * @param listener
	 */
	public void setOnNegativeListener()
	{
		dismiss();
	}

	
	@Override
	public void dismiss()
	{
		super.dismiss();
	}

}