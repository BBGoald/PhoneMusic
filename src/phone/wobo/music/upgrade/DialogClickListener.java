package phone.wobo.music.upgrade;


/*********************************************************
 * @Title: DialogClickListener.java
 * @Package phone.wobo.music.upgrade
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  Administrator
 * @date 2014-10-17 -- 上午10:08:38
 * @version V1.0
 *********************************************************/
public interface DialogClickListener {
	
	public void onPositiveClick(int flag, Object object, android.content.DialogInterface dialog);
	public void onNegativeClick(int flag, Object object, android.content.DialogInterface dialog);

}
