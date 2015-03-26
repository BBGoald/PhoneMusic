package phone.wobo.music.lrc;



public class Lrc {
	public Time start;
	public Time end;
	
	public String content="";
	public boolean isSing=false;
	public String getTextContent(){
		if(content == null){
			return "";
		}
		
		String text = content.replaceAll("<br />", "\n");
		text = text.replaceAll("\\<.*?\\>", "");
		return text;
	}
	
	public void add(Lrc lrc){
		if(this.start.mseconds <= lrc.start.mseconds){			
			content = content + "<br />" + lrc.content;
		}else{
			content = lrc.content + "<br />" + content;
			this.start.mseconds = lrc.start.mseconds;
		}
		
		if(this.end.mseconds < lrc.end.mseconds)
			this.end.mseconds = lrc.end.mseconds;
	}
}
