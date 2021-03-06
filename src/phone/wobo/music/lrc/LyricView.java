package phone.wobo.music.lrc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
/**
 * @author root
 * 
 */
public class LyricView extends TextView {
	private Paint mPaint;
	private float mX;
	//private static Lyric mLyric;
	private Paint mPathPaint;
	public String test = "test";
	public int index = 0;
	//private List<Sentence> list;
	public float mTouchHistoryY;
	private int mY;
	//private long currentDunringTime; // 当前行歌词持续的时间，用该时间来sleep
	private float middleY;// y轴中间
	private static final int DY = 50; // 每一行的间隔
	private static final String TAG = "liangbang";
	 
	TimedTextObject lrcObject;
	List<Lrc> list;
	public LyricView(Context context) {
		super(context);
		init();
	}
	public LyricView(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}
	public LyricView(Context context, AttributeSet attr, int i) {
		super(context, attr, i);
		init();
	}
	public void setLrcObject(TimedTextObject lrcObject){
		Log.i(TAG, "	--->LyricView--->setLrcObject");
		this.lrcObject=lrcObject;
		list=new ArrayList<Lrc>();
		Iterator titer=lrcObject.lrcs.entrySet().iterator();
		Log.i(TAG, "	LyricView lrcObject.lrcs.size= " + lrcObject.lrcs.size());
		  //System.out.println("lrcObject.lrcs.size"+lrcObject.lrcs.size()); 
		int i=0;
        while(titer.hasNext()){  
            Map.Entry ent=(Map.Entry )titer.next();  
            String keyt=ent.getKey().toString();  
            Lrc valuet=(Lrc) ent.getValue();
            list.add(valuet);
            i++;           
        } 
		Log.i(TAG, "	LyricView llrcObject.lrcs.length= " + i);
        //System.out.println("lrcObject.lrcs.length"+i); 
	}
	private void init() {
		Log.i(TAG, "******instance LyricView--->init");
		// 非高亮部分
		mPaint = new Paint();		
		mPaint.setAntiAlias(true);	
		mPaint.setTextSize(24);
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.SERIF);
		
		// 高亮部分 当前歌词		
		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setColor(Color.YELLOW);
		mPathPaint.setTextSize(32);
		mPathPaint.setTypeface(Typeface.SANS_SERIF);
	}
	  @Override
	protected void onDraw(Canvas canvas) {
			Log.i(TAG, "	--->LyricView--->onDraw");

		  if (canvas == null)
				return;
		  if(list==null || list.size()==0)return;
		super.onDraw(canvas);
		Paint p = mPaint;
		Paint p_curIndex = mPathPaint;
		p.setTextAlign(Paint.Align.CENTER);		
		if (index == -1)
			return;
		p_curIndex.setTextAlign(Paint.Align.CENTER);
		
		// 先画当前行，之后再画他的前面和后面，这样就保持当前行在中间的位置
		if(list.get(index)==null)return;
		canvas.drawText(list.get(index).getTextContent(), mX, middleY, p_curIndex);	
		
        
		float tempY = middleY;
		// 画出本句之前的句子
		for (int i = index - 1; i >= 0; i--) {
			// Sentence sen = list.get(i);
			// 向上推移
			tempY = tempY - DY;
			if (tempY < 0) {
				break;
			}
			// System.out.println("index前"+index+":"+list.get(i).getTextContent());  
			canvas.drawText(list.get(i).getTextContent(), mX, tempY, p);
			// canvas.translate(0, DY);
		}
		tempY = middleY;
		// 画出本句之后的句子
		for (int i = index + 1; i < list.size(); i++) {
			// 往下推移
			tempY = tempY + DY;
			if (tempY > mY) {
				break;
			}
			// System.out.println("index前"+index+":"+list.get(i).getTextContent());  
			canvas.drawText(list.get(i).getTextContent(), mX, tempY, p);
			// canvas.translate(0, DY);
		}
	}
	  
	public void update() {
          // 更新界面
		Log.i(TAG, "	--->LyricView--->update--->postInvalidate");
          postInvalidate();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		mX = w * 0.5f; // remember the center of the screen
		mY = h;
		middleY = h * 0.5f;
	}
	//
	/**
	 * @param time
	 *            当前歌词的时间轴
	 * 
	 * @return currentDunringTime 歌词只需的时间
	 */
	public long updateIndex(Lrc lrc) {	
		if(lrc==null)return -1;
		Log.i(TAG, "	--->LyricView--->updateIndex--->index= " + index);
		index=getCurIndex(lrc);
		Log.i(TAG, "	--->LyricView--->updateIndex--->index= " + index);
		return lrc.end.mseconds-lrc.start.mseconds;
	}
	
	private int getCurIndex(Lrc lrc1 ){
		if(list==null || list.size()==0)return 0;//list在setLrcObject中初始化
		for(int i=0;i<list.size();i++){
			Lrc lrc=list.get(i);
			if(lrc!=lrc1)continue;
			else return i;				
		}
		return 0;		
	}
} 