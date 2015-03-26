package phone.wobo.music.bll;

import java.util.Locale;

import phone.wobo.music.R;
import phone.wobo.music.model.MusicInfo;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicPlayerHelper {
	Context context;

	public MusicPlayerHelper(Context context) {
		this.context = context;
	}

	// 设置暂停按钮的图片
	public static void setPauseBtnImg(ImageButton btn) {
		btn.setImageResource(R.drawable.btn_pause);
	}

	public static void setPlayBtnImg(ImageButton btn) {
		btn.setImageResource(R.drawable.btn_play);
	}

	public static void updateFavorites(boolean isTrue, ImageButton btn_favorite) {
		if (isTrue) {
			btn_favorite.setImageResource(R.drawable.btn_favroity);
		} else {
			btn_favorite.setImageResource(R.drawable.btn_favroity);
		}
	}

	// 更新进度条
	public static void updateProgress(SeekBar player_seekbar,
			TextView txt_curTime, TextView txt_totalTime) {
		updateProgress(0, 0, player_seekbar, txt_curTime, txt_totalTime);
	}

	public static void updateProgress(long curTime, long totalTime,
			android.widget.ProgressBar player_seekbar, TextView txt_curTime, TextView txt_totalTime) {
		curTime /= 1000;
		totalTime /= 1000;
		long curminute = curTime / 60;
		long cursecond = curTime % 60;

		String curTimeString = String.format(Locale.getDefault(), "%02d:%02d",
				curminute, cursecond);

		long totalminute = totalTime / 60;
		long totalsecond = totalTime % 60;
		String totalTimeString = String.format(Locale.getDefault(),
				"%02d:%02d", totalminute, totalsecond);

		int rate = 0;
		if (totalTime != 0) {
			rate = (int) ((float) curTime / totalTime * 100);
		}
        if (null != player_seekbar) {
            player_seekbar.setProgress(rate);
        }
		if(null != txt_curTime){
			txt_curTime.setText(curTimeString);
		}
		if(null != txt_totalTime){
			txt_totalTime.setText(totalTimeString);
		}
	}

	// 处理收藏
	public static void handleFavorite(DatabaseHelper dbHelper, MusicInfo info,
			ImageButton btn) {
		if (null == info) {
			return;
		}
		if (dbHelper.isExistFavorites(info)) {
			dbHelper.deleteFavorites(info);
			btn.setImageResource(R.drawable.btn_favroity);
		} else {
			dbHelper.addFavorites(info);
			btn.setImageResource(R.drawable.btn_favroity);
		}
	}
	// 添加历史记录
	public static void addHistory(DatabaseHelper dbHelper, MusicInfo info) {
		if(info==null)return;
		try {
			dbHelper.addHistory(info);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void handlePlayMessage(Handler mHandler,int msg){
		mHandler.removeMessages(msg);
		mHandler.sendEmptyMessageDelayed(msg, 1000);
	}

}
