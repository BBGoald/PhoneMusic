package phone.wobo.music.videoplayer; 


import phone.wobo.music.R;
import android.app.Dialog;
import android.content.Context; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message; 
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class TimeoutDialog extends Dialog {
	private static final int MESSAGE_TIMEOUT = 0;	
	private static final long DEFAULT_TIMEOUT = 3 * 1000;
	private long mTimeout ;
	
	public TimeoutDialog(Context context) {
		this(context, R.style.loadingDialog,DEFAULT_TIMEOUT);
	} 
	public TimeoutDialog(Context context, long timeout) {
		this(context, R.style.loadingDialog,timeout);  
	}
	public TimeoutDialog(Context context, int theme,long timeout) {
		super(context, theme);
		mTimeout = timeout;
	} 
	//
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | 0x80000000;
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(flags, flags);
		} catch (Exception e) {

		}

		LayoutParams lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0.0f; // 去背景遮盖
		getWindow().setAttributes(lp);
	} 
	
	public void show() {
		sendTimeoutMessage();
		super.show(); 
	} 

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			dismiss();
		}
		return super.onKeyDown(keyCode, event);
	}
	public void dismiss() {
		removeTimeoutMessage();
		super.dismiss();
	}
	protected void sendTimeoutMessage() {
		mHandler.removeMessages(MESSAGE_TIMEOUT);
		mHandler.sendEmptyMessageDelayed(MESSAGE_TIMEOUT,mTimeout);
	}
	protected void removeTimeoutMessage() {
		mHandler.removeMessages(MESSAGE_TIMEOUT);
	}
    private Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		dismiss();
        }
    }; 
}
