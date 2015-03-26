package phone.wobo.music.control;

public class Protocol {
	/**
	 * Client receive. The message is broadcasted from server, 
	 * which indicate the server is active, so client can show
	 * the server in the active list.
	 * 
	 * Format: message type(1 byte)
	 * Protocol: udp in port 6666
	 * */
	public static final byte server_online = 0x66;
	
	/**
	 * Client send. Client notify server to close the tcp connect,
	 * It is opportunity for server to cleanup.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte client_exit = 0x00;
	/**
	 * Client receive. Indicate tcp connect has been built successfully.
	 * And the client should send "version_validate" to the server.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte client_connected = 0x01;	
	
	
	/**
	 * Client send. Notify the server to hide mouse icon on TV screen.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte mouse_hide = 0x10;
	/**
	 * Client send. Notify the server to show mouse icon on TV screen.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte mouse_show = 0x11;
	/**
	 * Client send. Notify the server that the mouse is click.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte mouse_click = 0x12;
	/**
	 * Client send. Notify the server that the mouse is moved.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte) + deltaX(2 bytes) + deltaY(2 bytes)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte mouse_mov_ack = 0x13;
	/**
	 * unused
	 */
	public static final byte mouse_mov_syn = 0x14;
	
	/**
	 * Client send. Send touch motion event to server.
	 * For details about motion event, refer to 
	 * http://developer.android.com/reference/android/view/MotionEvent.html
	 * 
	 * Format: message length(4 bytes) + message type(1 byte) 
	 *  + down time(8 bytes) 
		+ event time(8 bytes)
		+ action type(4 bytes)
		+ pointer count(4 bytes)
		+ x scale(4 bytes)
		+ y scale(4 bytes)
		+ pressure(4 bytes)
		+ size(4 bytes)
		+ metaState(4 bytes)
		+ x precision(4 bytes)
		+ y precision(4 bytes)
		+ edgeFlags(4 bytes)
	 * 
	 * down time :
	 *     the time (in ms) when the user originally pressed down to start a 
	 *     stream of position events.
	 * event time :
	 *     the time (in ms) when this specific event was generated.
	 * action type :
	 *     the kind of action being performed. Include the following types:
	 *     ACTION_DOWN = 0 (A pressed gesture has started, the motion contains the initial starting location.)
     *     ACTION_UP = 1 ( A pressed gesture has finished)
     *     ACTION_MOVE = 2 (A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP(.)
     *     ACTION_CANCEL = 3 (The current gesture has been aborted.)
     *     ACTION_OUTSIDE = 4 (A movement has happened outside of the normal bounds of the UI element.)
     *     
     *     (the following type is unused currently)
     *     ACTION_POINTER_DOWN = 5 
     *     ACTION_POINTER_UP = 6  
     *     ACTION_HOVER_MOVE = 7 
     *     ACTION_SCROLL = 8
     *     ACTION_HOVER_ENTER = 9
     *     ACTION_HOVER_EXIT = 10
     * pointer count:
     *     The number of pointers of data contained in this event. Always >= 1.
     * x/y scale:
     *     The X/Y coordinate of this event is divided by the screen width/height of phone, in pixels.
     * pressure:
     *     The current pressure of this event, ranges from 0 (no pressure at all) to 1 (normal pressure).
     * size:
     *     This represents some approximation of the area of the screen being pressed.
     *     the actual value in pixels corresponding to the touch is normalized with the 
     *     device specific range of values and scaled to a value between 0 and 1.  
     * metaState:
     *     The state of any meta / modifier keys that were in effect when the event was generated.
     *     An integer in which each bit set to 1 represents a pressed meta key:
     *     META_CAP_LOCKED = 0x100;
     *     META_ALT_LOCKED = 0x200;
     *     META_SYM_LOCKED = 0x400;
     *     META_SELECTING = 0x800;
     *     META_ALT_ON = 0x02;
     *     META_ALT_LEFT_ON = 0x10;
     *     META_ALT_RIGHT_ON = 0x20;
     *     META_SHIFT_ON = 0x1;
     *     META_SHIFT_LEFT_ON = 0x40;
     *     META_SHIFT_RIGHT_ON = 0x80;
     *     META_SYM_ON = 0x4;
     *     META_FUNCTION_ON = 0x8;
     *     META_CTRL_ON = 0x1000;
     *     META_CTRL_LEFT_ON = 0x2000;
     *     META_CTRL_RIGHT_ON = 0x4000;
     *     META_META_ON = 0x10000; //This mask is used to check whether one of the META meta keys is pressed.
     *     META_META_LEFT_ON = 0x20000;
     *     META_META_RIGHT_ON = 0x40000;
     *     META_CAPS_LOCK_ON = 0x100000;
     *     META_NUM_LOCK_ON = 0x200000;
     *     META_SCROLL_LOCK_ON = 0x400000;
     * x precision:
     *     The precision of the X coordinates being reported. You can multiply this number with 
     *     getX to find the actual hardware value of the X coordinate.
     * y precision:
     *     The precision of the Y coordinates being reported. You can multiply this number with 
     *     getY to find the actual hardware value of the Y coordinate.
     * edgeFlags:
     *     A bit field indicating which edges, if any, were touched by this MotionEvent. 
     *     For touch events, clients can use this to determine if the user's finger was touching 
     *     the edge of the display. This property is only set for ACTION_DOWN events.
     *     EDGE_TOP = 0x00000001;
     *     EDGE_BOTTOM = 0x00000002;
     *     EDGE_LEFT = 0x00000004;
     *     EDGE_RIGHT = 0x00000008;
	 * Protocol: tcp in port 7777
	 * */
	public static final byte raw_motion_event = 0x15;
	// client send.
	public static final byte mouse_scroll = 0x16;
	
	/**
	 * Client send. Notify the server that key OK is pressed.
	 * The same format is used for other key event.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte key_ok = 0x20;
	public static final byte key_left = 0x21;
	public static final byte key_right = 0x22;
	public static final byte key_up = 0x23;
	public static final byte key_down = 0x24;
	
	public static final byte key_home = 0x30;
	public static final byte key_back = 0x31;
	public static final byte key_menu = 0x32;
	public static final byte key_voladd = 0x33;
	public static final byte key_voldel = 0x34;
	
	
	/**
	 * Client receive. Indicate server has finished input.
	 * Client should hide soft input(soft keyboard and so on).
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte input_method_hide = 0x40;
	/**
	 * Client receive. Indicate server need input.
	 * Client should show soft input(soft keyboard and so on) with initial text.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte) + initial text(non fixed)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte input_method_show = 0x41;
	/**
	 * Client send. trigger enter key event on server.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte input_method_enter = 0x42;
	/**
	 * Client send. Send changed part of text to server.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte) + changed part of text(non fixed)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte input_method_content= 0x43;
	
	/**
	 * Client send. Send G-sensor event to server.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte) 
	 * + Gx(4 bytes)
	   + Gy(4 bytes)
	   + Gz(4 bytes)
	 * Protocol: tcp in port 7777
	 * */
	public static final byte G_Sensor = 0x50;
	
	/**
	 * Client send and receive. Client sends the message to request screenshot.
	 * And then receive the message with the screenshot. 
	 * 
	 * Format of sending: message length(4 bytes) + message type(1 byte)
	 * Format of receiving: message length(4 bytes) + message type(1 byte) 
	   + screenshot in jpeg(non fixed)
	  
	 * Protocol: tcp in port 7777
	 * 
	 * Note: the resolution of jpeg is 800x480 at present.
	 * */
	public static final byte screen_frame_syn = 0x60;
	/**
	 * Client send. Notify server to stop sending screenshot.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte) 
	 * Protocol: tcp in port 7777
	 * */
	public static final byte screen_frame_syn_stop = 0x61;
	/**
	 * unused
	 */
	public static final byte screen_frame_resolution = 0x62; 
	
	public static final byte screen_frame_sync_started = 0x63;
	
	/**
	 * Client send and receive. Send the message to query version name of sever.
	 * And then receive the message with server's version name(eg, "2.1.2").
	 * 
	 * Format of sending: message length(4 bytes) + message type(1 byte) 
	 * Format of receiving: message length(4 bytes) + message type(1 byte) + version name(non fixed)
	 * Protocol: tcp in port 7777
	 * 
	 * Note: send this message when client receives "client_connected".
	 * */
	public static final byte version_validate = 0x70;
	
	/**
	 * Client send. Send video uri to server to play.
	 * 
	 * Format: message length(4 bytes) + message type(1 byte) + uri json string(non fixed)
	 * Protocol: tcp in port 7777
	 * 
	 * Uri json format: {"mid":"", "name":"", "source": "", "url":""}
	 * 
	 * */
	public static final byte video_play = 0x71;
	/**
	 * unused
	 */
	public static final byte video_collect  = 0x72;
	//添加于2014.10.13---huayanlan
	public static final byte music_play_single = 0x73;          // 播放单首歌
	public static final byte music_play_list  = 0x74;      // 播放整个列表的歌
	public static final byte mv_play_single = 0x75;          // 播放单个mv
	public static final byte mv_play_list  = 0x76;      // 播放整个列表的mv
}
