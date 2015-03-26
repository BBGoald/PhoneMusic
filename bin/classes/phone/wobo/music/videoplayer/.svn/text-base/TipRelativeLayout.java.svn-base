package phone.wobo.music.videoplayer;

import java.lang.reflect.Method;

import phone.wobo.music.R;
import phone.wobo.music.TVBoxApplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TipRelativeLayout extends RelativeLayout {

	public static final int LOADING = -1;
	public int what = -1;
	private int mPercent;

	private ImageView mImageView;
	private TextView mTextView;

	public TipRelativeLayout(Context context) {
		super(context);
		mImageView = new ImageView(context);
		mImageView.setId(1000);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(mImageView, lp);
		mTextView = new TextView(context);
		mTextView.setTextSize(30);
		mTextView.setTextColor(getResources().getColor(
				android.R.color.white));
		mTextView.setSingleLine(true);

		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, mImageView.getId());
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(mTextView, lp);
	}

	public void setPercent(int percent) {
		mPercent = percent;
		if (mPercent <= 0) {
			mTextView.setText(TVBoxApplication.getAppContext().getResources().getString(R.string.wobo_player_other_buffering1));
			return;
		}
		mTextView.setText(TVBoxApplication.getAppContext().getResources().getString(R.string.wobo_player_other_buffering2) + percent + "%...");
		if(percent == 100) {
			setVisibility(View.INVISIBLE);
		}
	} 
	public void setMessage(String message){
		mTextView.setText(message);
	}

	public int getPercent() {
		return mPercent;
	}

	public synchronized void show(int what, int resId) {
		this.what = what; 
		mTextView.setText("");
		show(this.what,resId,false);  
	}

	public synchronized void show(int what, int resId, boolean isAnimated) {
		this.what = what;
		mImageView.setImageResource(resId);
		setVisibility(View.VISIBLE);
		if (!isAnimated) {
			return;
		}
		try {
			Drawable drawable = mImageView.getDrawable();
		/*	Method method = ((Class)drawable.getClass()).getMethod("start", null);
			method.invoke(drawable, null);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void hide(int what) {
		if (this.what != what) {
			return;
		}
		setVisibility(View.INVISIBLE);
	}
}
