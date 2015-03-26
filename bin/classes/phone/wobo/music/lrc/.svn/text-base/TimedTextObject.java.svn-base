package phone.wobo.music.lrc;
import java.util.TreeMap;

//import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;

/**
 * These objects can (should) only be created through the implementations of parseFile() in the {@link TimedTextFileFormat} interface
 * <br><br>
 * Copyright (c) 2012 J. David Requejo <br>
 * j[dot]david[dot]requejo[at] Gmail
 * <br><br>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * <br><br>
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 * <br><br>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * 
 * @author J. David Requejo
 *
 */
public class TimedTextObject {
	
	public static class TimedIndex implements Comparable<TimedIndex>{
		public int start;
		public int end;
		public TimedIndex(int start, int end){
			this.start = start;
			this.end = end;
		}
		public TimedIndex(int start){
			this.start = start;
			this.end = start;
		}
		public TimedIndex(Lrc  lrc){
			this.start = lrc.start.mseconds;
			this.end = lrc.end.mseconds;
		}
		@Override
		public int compareTo(TimedIndex o) {
			if((this.end >= o.end && this.start <= o.start)
				|| (this.end <= o.end && this.start >= o.start))
				return 0;
			if(this.start > o.start)
				return 1;
			
			return -1;
		}
	}
	
	// 歌名
	private String title = "";
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// 歌手名
	private String artist = "";
	// 专辑名
	private String album = "";
	// 最大时间
	//private long maxTime = 0;
	
	// 歌词内容
	//private static Map<Long, String> lrcContents = new HashMap<Long, String>();
	// 验证是否通过
//	private boolean valid = false;

//	private TreeMap<Integer, LrcLine> lrc_map = new TreeMap<Integer, LrcLine>();
	
	//list of captions (begin time, reference)
	//represented by a tree map to maintain order
	//TreeMap<TimedIndex, Lrc> lrcs;	
	TreeMap<TimedIndex, Lrc> lrcs;
	//**** OPTIONS *****
	//to know whether file should be saved as .ASS or .SSA
	boolean useASSInsteadOfSSA = false;
	//to delay or advance the subtitles, parsed into +/- milliseconds
	int offset = 0;	// 偏移时间	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Protected constructor so it can't be created from outside
	 */
	protected TimedTextObject(){
		lrcs = new TreeMap<TimedIndex, Lrc>();		
	}
	
	public Lrc getLrc(TimedIndex index){
		return lrcs.get(index);
	}
	
	public String getTitle(){
		return title;
	}
	

	
	


}
