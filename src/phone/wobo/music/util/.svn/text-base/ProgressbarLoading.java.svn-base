package phone.wobo.music.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import phone.wobo.music.R;

/*********************************************************
 * @Title: ProgressbarLoading.java
 * @Package phone.wobo.music.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  Administrator
 * @date 2014-10-31 -- 下午02:06:40
 * @version V1.0
 *********************************************************/
public class ProgressbarLoading extends android.widget.LinearLayout{

	public static Object Tag = "ProgressbarLoading";
	private android.widget.ProgressBar pb;
	private android.widget.TextView tvMessage;
	
	/*******************************************************
	 * @param context
	 ******************************************************/
	public ProgressbarLoading(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setTag(Tag);
		
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		createProgressBar();
		createTextView();
	}
	
    private void createTextView() {
        tvMessage = new android.widget.TextView(getContext());
        tvMessage.setSingleLine(true);
        tvMessage.setTextAppearance(getContext(), R.style.font_small_gray);
//        tvMessage.setTextSize(getResources().getDimension(R.dimen.font_s1));
//        tvMessage.setTextColor(Color.BLACK);
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(tvMessage, params);
        
        tvMessage.setSingleLine(false);
        tvMessage.setMaxLines(2);
        tvMessage.setGravity(Gravity.CENTER);
    }

    private void createProgressBar() {
        pb = new android.widget.ProgressBar(getContext(), null, 
                android.R.attr.progressBarStyleSmallInverse);
//        pb = new android.widget.ProgressBar(getContext());
        addView(pb);
    }
	
	public void show(){
	    show(R.string.txt_loading_data);
	}
	public void show(int resId){
	    show(getContext().getString(resId));
	}
	public void show(String text){
	    android.view.ViewParent viewParent = getParent();
        if(null != viewParent){
            viewParent.bringChildToFront(this);
        }
        if(null == text){
            text = "";
        }
        tvMessage.setText(text);
        setVisibility(android.view.View.VISIBLE);
    }
	public void close(){
	    setVisibility(android.view.View.GONE);
	}
	public boolean isShowing(){
		return getVisibility() == View.VISIBLE;
	}

}
