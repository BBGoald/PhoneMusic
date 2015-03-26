package phone.wobo.music.util;
import phone.wobo.music.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LetterLayout extends ScrollView implements
		android.view.View.OnClickListener {
	private String[] keyArr = {"全部", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private Context context;
	public android.view.View.OnClickListener _objectBtnOnclick;

	public LetterLayout(Context context) {
		super(context);
		this.context = context;
	}

	public LetterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		createLetterView();
	}

	private void createLetterView() {
		ScrollView scrollview = new ScrollView(context);
		LinearLayout layout_letter = new LinearLayout(context);
		LinearLayout.LayoutParams lp_letter = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout_letter.setOrientation(LinearLayout.VERTICAL);
		layout_letter.setLayoutParams(lp_letter);
		scrollview.addView(layout_letter);

		for (String key : keyArr) {
			Button btnObj = createButton(key);
			layout_letter.addView(btnObj);

		}
		addView(scrollview);
	}

	// 创建键盘上的按钮
	private Button createButton(String key) {
		Button button = new Button(context);
		button.setText(key);
		button.setPadding(0, 0, 0, 0);
		button.setTag(key);		
		button.setGravity(Gravity.CENTER);
		button.setTextAppearance(context, R.style.font_small_black);
		button.setBackgroundResource(R.drawable.btn_selector2);
		
		button.setOnClickListener(this);
		// SelectControl.setDefaultTextColor(button);
		// SelectControl.setKeyboardButtonDefaultBg(button);
		 button.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View btn, boolean hasFocus) {
			if(hasFocus){
				Log.d("onFocusChange", "true");
				((Button)btn).setTextAppearance(context, R.style.font_middle_blue);
			//	btn.setBackgroundResource(R.drawable.btn_selector1);
			}else{
				Log.d("onFocusChange", "false:");
				((Button)btn).setTextAppearance(context, R.style.font_middle_black);
				//btn.setBackgroundResource(R.drawable.btn_selector2);
			}
				
			}
		});
		

		return button;
	}

	@Override
	public void onClick(View v) {
		if (null != _objectBtnOnclick) {
			_objectBtnOnclick.onClick(v);
		}
	}

	public void setOnclickOfObjectBtn(android.view.View.OnClickListener onclick) {
		_objectBtnOnclick = onclick;
	}
}
