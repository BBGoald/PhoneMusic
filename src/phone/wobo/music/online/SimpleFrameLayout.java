
package phone.wobo.music.online;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import phone.wobo.music.util.FuncUtils;
import phone.wobo.music.util.ProgressbarLoading;

public class SimpleFrameLayout extends FrameLayout {

    protected ProgressbarLoading loading;
    
    private String TAG = "SimpleFrameLayout";

    public SimpleFrameLayout(Context context) {
//        super(context);
        // TODO Auto-generated constructor stub
        this(context, null);
    }

    public SimpleFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        // TODO Auto-generated constructor stub
    }

    public SimpleFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initLoading();
    }
    @Override
    public void addView(View child) {
        // TODO Auto-generated method stub
        super.addView(child);
    }
    
    @Override
    public void addView(View child, int index) {
        // TODO Auto-generated method stub
        super.addView(child, index);
    }
    
    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        // TODO Auto-generated method stub
        super.addView(child, index, params);
        initLoading();
    }
    
    @Override
    public void addView(View child, int width, int height) {
        // TODO Auto-generated method stub
        super.addView(child, width, height);
    }
    
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        // TODO Auto-generated method stub
        super.addView(child, params);
    }
    
    @Override
    protected boolean addViewInLayout(View child, int index,
            android.view.ViewGroup.LayoutParams params) {
        // TODO Auto-generated method stub
        initLoading();
        return super.addViewInLayout(child, index, params);
    }
    
    @Override
    protected boolean addViewInLayout(View child, int index,
            android.view.ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        // TODO Auto-generated method stub
        initLoading();
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }
    
    public void initLoading(){
        if(null == loading){
            loading = new ProgressbarLoading(getContext());
            Log.d(TAG, "liangbang loading= " + loading.hashCode());//1111710264 1112000800
            loading.close();
        }
        android.view.View v = findViewWithTag(ProgressbarLoading.Tag);
        if (v == null) {
            android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            
           params.gravity = Gravity.CENTER;
           addView(loading, params);
        }
    }
    
    protected void showToast(String text){
        FuncUtils.makeText(getContext(), text);
    }
}
