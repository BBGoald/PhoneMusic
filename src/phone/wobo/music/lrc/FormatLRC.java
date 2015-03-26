package phone.wobo.music.lrc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phone.wobo.music.lrc.TimedTextObject.TimedIndex;

import android.util.Log;

public class FormatLRC {
	public static TimedTextObject parseFile(InputStream is,String encode) throws IOException {
		TimedTextObject tto = new TimedTextObject();
		List<String> listStr=new ArrayList<String>();	
		String line = "";
		if(encode==null || encode.equals(""))
			encode="UTF-8";
		try {
			
		/*	 File saveFile = new File(file); FileInputStream stream = new
			 FileInputStream(saveFile);// context.openFileInput(file);
			 
			 BufferedReader br = new BufferedReader(new InputStreamReader(
			 stream, "GB2312"));*/
			 
			InputStreamReader in = new InputStreamReader(is, encode);
			BufferedReader br = new BufferedReader(in);
		
			int lineCounter = 0;
			line = br.readLine();
			while ( line!= null) {
				line = line.trim();
				if (line.indexOf("[ti:") > -1) {// 解析歌名
					String title = line.substring(line.indexOf(":") + 1,
							line.length() - 1);
					tto.setTitle(title);
				} else if (line.indexOf("[ar:") > -1) {// 解析歌手
					String artist = line.substring(line.indexOf(":") + 1,
							line.length() - 1);
					tto.setArtist(artist);
				} else if (line.indexOf("[al:") > -1) {// 解析专辑
					String album = line.substring(line.indexOf(":") + 1,
							line.length() - 1);
					tto.setAlbum(album);
				} else if (line.indexOf("[offset:") > -1) {
					String offset = line.substring(line.indexOf(":") + 1,
							line.length() - 1);
					tto.setOffset(Integer.parseInt(offset));
				}else if(line.indexOf("[by:")>-1){
					
				}
				else if (line.indexOf("[") > -1 && line.indexOf("]") > -1) {
					listStr.add(line);
				}
				lineCounter++;
				line = br.readLine();
				if(line==null){
					line = br.readLine();
					lineCounter++;
				}
			}
			br.close();
			in.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}

		Map<Integer, Lrc> temp=toHaspMap(listStr);
		return toTimeIndex(temp, tto);
	}
	
	private static Map<Integer, Lrc> toHaspMap(List<String> list) {
		if (list == null) {
			return null;
		}
		int length = list.size();
		Map<Integer, Lrc> temp = new HashMap<Integer, Lrc>();
		String line = "";
		for (int i = 0; i < length; i++) {
			line = list.get(i);
			String[] timesplits = line.split("]");
			if (timesplits.length < 2)
				continue;		
			String content=line.substring(line.lastIndexOf("]")+1);
			if(content==null || content.equals(""))continue;
			for (int j = timesplits.length - 2; j >= 0; j--) {
				String start = timesplits[j].substring(1);
				Time time = toTime(start);
				if (!temp.containsKey(time.mseconds)) {
					Lrc lrc = new Lrc();
					lrc.start = time;
					lrc.content = content;				
					temp.put(time.mseconds, lrc);				
				}
			}
		}
		return temp;
	}
	
	private static TimedTextObject toTimeIndex(Map<Integer, Lrc> temp,
			TimedTextObject tto) {
		//排序
		Object[] key_arr = temp.keySet().toArray();     
		Arrays.sort(key_arr);  
		List<Lrc> templist=new ArrayList<Lrc>();
		for  (Object key : key_arr) {     
			Lrc lrc = temp.get(key); 
			templist.add(lrc);			
		}
		
		//设置lrc的end,然后转为TimedIndex
		for(int i=0;i<templist.size();i++){
			Lrc lrc = templist.get(i); 
			if(i==key_arr.length-1){
				Time time=new Time(lrc.start.mseconds+2000); 
				lrc.end=time;
			}else{
				Lrc lrc2=temp.get(key_arr[i+1]); ;
				lrc.end=lrc2.start;
			}
			
			TimedIndex index = new TimedIndex(lrc);
			if (!tto.lrcs.containsKey(index)) {			
				tto.lrcs.put(index, lrc);
			}			
		}
		
		return tto;
		
	}
	private static Time toTime(String start){
		String timeformat="mm:ss.cs";
		if(start.length()<=5){
			timeformat="mm:ss";
		}
		return new Time(timeformat, start);
	}

}
