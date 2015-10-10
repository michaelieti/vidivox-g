package overlay;

import javafx.util.Duration;

public class Commentary implements Comparable<Commentary> {
	
	private Duration time;
	private String text;
	//can put in pitch/voice/stretch later
	
	public Commentary(Duration time, String text){
		this.time = time;
		this.text = text;
	}
	
	public void setTime(Duration time){
		this.time = time;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getTimestring(){
		return TimeUtility.formatTime(time);
	}
	public String getText(){
		return text;
	}
	
	public String toFileFormattedString(){
		StringBuilder sb = new StringBuilder();
		sb.append(TimeUtility.formatTime(time));
		sb.append("\n");
		sb.append(text);
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Commentary o) {
		double thisDuration = time.toMillis();
		double otherDuration = o.time.toMillis();
		return (int)(thisDuration - otherDuration);
	}
	
	@Override
	public boolean equals(Object o){
		Commentary obj = (Commentary)o;
		if (this.time == obj.time && this.text.equals(obj.text)){
			return true;
		}
		else
			return false;
	}
	

}
