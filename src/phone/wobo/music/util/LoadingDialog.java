package phone.wobo.music.util;

import phone.wobo.music.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class LoadingDialog extends Dialog{

	public LoadingDialog(Context context) {	
		super(context, R.style.loadingDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_loading_dialog);
		setCanceledOnTouchOutside(false);
	}
	
	
	
	@Override
	public void show() {
		super.show();
	}

	
	public void close() {
		dismiss();
	}

}
