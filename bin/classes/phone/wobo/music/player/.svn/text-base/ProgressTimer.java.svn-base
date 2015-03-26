package phone.wobo.music.player;

import java.util.Timer;
import java.util.TimerTask;

import phone.wobo.music.BuildConfig;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ProgressTimer {

	//private int mEventID1;
	private int mEventID2;
	private Handler mHandler;
	private Timer mTimer;
	private TimerTask mTimerTask; // 定时器任务
	private int mTimerInterval = 1000; // 定时器触发间隔时间(ms)
	private boolean mBStartTimer; // 定时器是否已开启
	private int count = 0;

	public ProgressTimer(Handler handler,  int eventID2) {
		initParam(handler, eventID2);
	}

	private void initParam(Handler handler, int eventID2) {
		mHandler = handler;
		mEventID2 = eventID2;
		mBStartTimer = false;
		mTimerTask = null;
		mTimer = new Timer();
	}

	public void startTimer() {		
		if (mHandler == null || mBStartTimer) { return; }
		mBStartTimer = true;
		mTimerTask = new MusicTimerTask();
		mTimer.schedule(mTimerTask, mTimerInterval, mTimerInterval);
		count = 0;
		
	}

	public void stopTimer() {
		if (!mBStartTimer) { return; }
		mBStartTimer = false;
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
			count = 0;
		}
		
	}

	class MusicTimerTask extends TimerTask {
		@Override
		public void run() {
			if (mHandler != null) {				
				int	what =mEventID2;
				if (BuildConfig.DEBUG) {
					Log.d("debug", "sendmessage to update progressbar");
				}
				
				Message msgMessage = mHandler.obtainMessage(what);
				msgMessage.sendToTarget();
				count++;
			}
		}
	}
	
	

}
