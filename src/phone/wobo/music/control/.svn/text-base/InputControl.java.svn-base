package phone.wobo.music.control;

import java.util.Timer;
import java.util.TimerTask;

import phone.wobo.music.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class InputControl extends Activity {
	private static final String TAG = "InputControl";
	public static EditText mEditText;
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(Helper.InputControl.receiveText != null){ 
			Log.d(TAG,"receiveText = "+Helper.InputControl.receiveText);
			int txtLen = Helper.InputControl.receiveText.length();
			Log.d(TAG,"receiveText length = "+txtLen);
		   mEditText.setText(Helper.InputControl.receiveText);  
		   try
		   {
		     
		     if(Helper.InputControl.selectionStart >= txtLen)
			   Helper.InputControl.selectionStart = 0;
		     if(Helper.InputControl.selectionEnd >= txtLen)
			   Helper.InputControl.selectionEnd = (txtLen-1)>0?(txtLen-1):0;
		     mEditText.setSelection(Helper.InputControl.selectionStart, Helper.InputControl.selectionEnd);
		   }catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_control_input_control);
		initView();
		setListenerEvent();
		
		getInputMethod();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	private void initView() {
		mEditText = (EditText) findViewById(R.id.input_msg);
	}

	private void setListenerEvent() {
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				if(Helper.InputControl.receiveText != null){
					Helper.InputControl.receiveText = null;
					return;
				}				

				if (TVClientActivity.mCtc == null) 
					return;
				
				byte[] cmd = new byte[1];
				cmd[0] = Protocol.input_method_content;
				byte[] content = null;
				content = s.toString().getBytes();
				TVClientActivity.mCtc.sendByteData(Helper.bytesLinkBytes(cmd, content));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void afterTextChanged(Editable s) {}
		});
	}

	@SuppressWarnings("static-access")
	private void getInputMethod() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(InputControl.mEditText,
				InputMethodManager.SHOW_IMPLICIT, null);		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed (){
		super.onBackPressed();		
		if (TVClientActivity.mCtc != null) {
			byte[] buffer = new byte[1];
			buffer[0] = Protocol.key_back;
			TVClientActivity.mCtc.sendByteData(buffer);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d(TAG,"onKeyUp keyCode = "+keyCode);
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			Thread sendEnter = new Thread() {
				@Override
				public void run() {
					super.run();
					byte[] cmd = new byte[1];
					cmd[0] = Protocol.input_method_enter;
					if (TVClientActivity.mCtc != null) {
						TVClientActivity.mCtc.sendByteData(cmd);
					}
				}
			};
			sendEnter.start();
		/*	Intent MjpegIntent = new Intent(InputControl.this,MjpegActivity.class);
			String url = "http://"+Helper.ConnectionInfo.TcpConnectIp +":8080/?action=stream";
			MjpegIntent.putExtra("url", url);
			startActivity(MjpegIntent);
			finish();*/
		}
		else if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			//if(Helper.SimulateTvHelper.getHandler()!=null)
			/*{
				Intent MjpegIntent = new Intent(InputControl.this,MjpegActivity.class);
				String url = "http://"+Helper.ConnectionInfo.TcpConnectIp +":8080/?action=stream";
				MjpegIntent.putExtra("url", url);
				startActivity(MjpegIntent);
				finish();
			}*/
		}
		return super.onKeyUp(keyCode, event);
	}
}
