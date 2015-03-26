package phone.wobo.music.videoplayer;

import phone.wobo.music.R;
import phone.wobo.music.TVBoxApplication;
import phone.wobo.music.player.DensityUtil;
import phone.wobo.music.videoplayer.MediaController.onProgressBarRefreshListener;
import android.content.Intent;
import android.media.AudioManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class WoboPlayerActivity extends VideoPlayerActivity {

	protected static final String TAG = "WoboPlayerActivity";
	private int maxVolume, currentVolume;
	private static final float STEP_PROGRESS = 10f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快
	private static final float STEP_VOLUME = 10f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
	private long palyerCurrentPosition = 0;// 模拟进度播放的当前标志，毫秒
	private long playerDuration = 0;// 模拟播放资源总时长，毫秒
	private int seek_pec = 3 * 1000;

	/** 当前亮度 */
	private float mBrightness = -1f;
	private GestureDetector mGestureDetector;

	private onProgressBarRefreshListener monProgressBarRefreshListener = new onProgressBarRefreshListener() {

		@Override
		public void onProgressBarRefresh(long progress, int duration) {
			palyerCurrentPosition = progress;
			playerDuration = duration;
		}

	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMediaController
				.setMonProgressBarRefreshListener(monProgressBarRefreshListener);
		mMediaController.setMediaDialog(mMediaDialog);
		mSurfaceView.setLongClickable(true);

		mGestureDetector = new GestureDetector(this,
				new GestureDetector.OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent arg0) {
						// 轻击触摸屏后，弹起。如果这个过程中产onLongPress、onScroll和onFling事件，就不会
						// 产生onSingleTapUp事件。
						if (mMediaController == null) {
							return false;
						}

						if (isPauseState()) {
							switchPlay();
							showMediaController();
							return true;
						}

						if (!mMediaController.isShowing()) {
							showMediaController();
						} else {
							hideMediaController();
						}
						return false;
					}

					@Override
					public void onShowPress(MotionEvent arg0) {
						// 点击了触摸屏，但是没有移动和弹起的动作onShowPress和onDown的区别在于
						// onDown是，一旦触摸屏按下，就马上产生onDown事件，但是onShowPress是onDown事件产生后，
						// 一段时间内，如果没有移动鼠标和弹起事件，就认为是onShowPress事件。

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// 当手在屏幕上滑动离开屏幕时触发，参数跟onFling一样（注意两者的区别）

						if (mMediaController == null) {
							return true;
						}

						if (mMediaController != null
								&& !mMediaController.isShowing()) {
							showMediaController();
							return true;
						}

						int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量
						int GESTURE_MODIFY_PROGRESS = 1;
						int GESTURE_MODIFY_VOLUME = 2;

						if (Math.abs(velocityX) >= Math.abs(velocityY)) {
							GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
						} else {
							GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
						}

						if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
							int resId = -1;

							if (velocityX >= DensityUtil.dip2px(
									getApplicationContext(), STEP_PROGRESS)) {
								// 快退，用步长控制改变速度，可微调
								if (palyerCurrentPosition > seek_pec) {// 避免为负
									palyerCurrentPosition -= seek_pec;// scroll方法执行一次快退3秒
								} else {
									palyerCurrentPosition = seek_pec;
								}

								// mMediaController.

								//resId = R.drawable.media_controller_rewind;
							} else if (velocityX <= -DensityUtil.dip2px(
									getApplicationContext(), STEP_PROGRESS)) {
								// 快进
								if (palyerCurrentPosition < playerDuration - 16 * 1000) {// 避免超过总时长
									palyerCurrentPosition += seek_pec;// scroll执行一次快进3秒
								} else {
									palyerCurrentPosition = playerDuration - 10 * 1000;
								}

								//resId = R.drawable.media_controller_forward;
							}

							//getTipView().show(102, resId);

							if (palyerCurrentPosition > 0
									&& palyerCurrentPosition < playerDuration
									&& playerDuration > 0) {
								seekTo((int) palyerCurrentPosition);
							}
						} else {
							if (velocityY >= DensityUtil.dip2px(
									getApplicationContext(), STEP_VOLUME)) {
								// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
								adjustVolume(AudioManager.ADJUST_RAISE);
								if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
									currentVolume++;
								}
							} else if (velocityY <= -DensityUtil.dip2px(
									getApplicationContext(), STEP_VOLUME)) {// 音量调小
								adjustVolume(AudioManager.ADJUST_LOWER);
								if (currentVolume > 0) {
									currentVolume--;
									if (currentVolume == 0) {// 静音，设定静音独有的图片
									}
								}
							}							
						}

						return false;
					}

					@Override
					public void onLongPress(MotionEvent arg0) {
						// 长按屏幕时触发

					}

					@Override
					public boolean onFling(MotionEvent arg0, MotionEvent arg1,
							float arg2, float arg3) {
						// 当手在屏幕上滑动但手未离开屏幕时触发
						return false;
					}

					@Override
					public boolean onDown(MotionEvent arg0) {
						// 一旦触摸屏按下，就马上产生onDown事件
						return false;
					}
				});
	}

	protected void handlePlay(RealUrlInfo playInfo) {
		if (playInfo == null) {
			String msg = getString(R.string.mv_cannot_play_prompt);
			showMessage(msg);
			if (mVideoSet.getCurrentLocation() < mVideoSet.getVideoCount() - 1) {
				playMedia(mVideoSet.getCurrentLocation() + 1);
			} else {
				finish();
			}
			return;
		}
		super.handlePlay(playInfo);
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		startActivity(intent);
		finish();
	}

	@Override
	protected boolean handleSurfaceViewEvent(int keyCode, KeyEvent event) {
		if (event.getAction() != KeyEvent.ACTION_DOWN) {
			return super.handleSurfaceViewEvent(keyCode, event);
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:

			return true;
		default:
			break;
		}
		return super.handleSurfaceViewEvent(keyCode, event);
	}

	@Override
	protected boolean handleSurfaceViewMotion(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return false;
	}

	protected void play(MediaSet mediaSet) {
		super.play(mediaSet);
		if (null != mMediaController) {
			mMediaController.setMediaSet(mediaSet);
		}
		
	}

	@Override
	public void switchPlay() {
		super.switchPlay();

		mMediaController.refreshPlayButtonStatus(isPlaying());
	}

	@Override
	protected void showLoading() {
		if (!mConnected) {
			return;
		}
		TipRelativeLayout view = this.getTipView();
		if (mBytes == 0) {
			view.setMessage("");
			mBytes = TrafficStats.getTotalRxBytes();
		} else {
			long rate = (TrafficStats.getTotalRxBytes() - mBytes) / 1024L;
			if (rate > 0) {
				view.setMessage(rate + " KB/秒 ");
				mBytes = TrafficStats.getTotalRxBytes();
			}
		}
		view.show(TipRelativeLayout.LOADING, R.anim.loading_progress, true);
		if (!hasMessages(TipRelativeLayout.LOADING)) {
			sendMessageDelayed(TipRelativeLayout.LOADING, 1000);// 计算速度，所以每秒计算一次
		}
	}

	// 重载加载框
	protected void playMedia(int location) {
		int loc = mVideoSet.getCurrentLocation();
		mVideoSet.setCurrentLocation(location);
		Log.d("playMedia", "location:" + location);
		if (loc != location) {
			showLoadTip();
		}
		loadAndPlay();
		hideTipView();
	}

	private void showLoadTip() {
		TipRelativeLayout view = this.getTipView();
		view.setMessage(TVBoxApplication.getAppContext().getResources()
				.getString(R.string.wobo_player_loading_tip)
				+ mVideoSet.getCurrentName());
		view.show(TipRelativeLayout.LOADING, R.anim.loading_progress, true);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	protected void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getWindow().setAttributes(lpa);
	}

	private boolean isPauseState() {
		TipRelativeLayout view = this.getTipView();
		if (null == view || view.getVisibility() == View.GONE) {
			return false;
		}
		for (int i = 0; i < view.getChildCount(); i++) {
			View v = view.getChildAt(i);
			if (v instanceof android.widget.ImageView) {
				ImageView iv = (ImageView) v;
				try {
					if (iv.getDrawable()
							.getConstantState()
							.equals(getResources().getDrawable(
									R.drawable.media_controller_resume)
									.getConstantState())) {
						return true;
					}
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

}