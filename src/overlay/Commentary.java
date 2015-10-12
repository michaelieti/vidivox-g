package overlay;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class Commentary implements Comparable<Commentary> {
	
	private SimpleObjectProperty<Duration> timeProperty;
	private SimpleStringProperty textProperty;
	private SimpleStringProperty timeStringProperty;
	//can put in pitch/voice/stretch later
	
	public Commentary(Duration time, String text){
		this.timeProperty = new SimpleObjectProperty<>(time);
		this.textProperty = new SimpleStringProperty(text);
		this.timeStringProperty = new SimpleStringProperty(TimeUtility.formatTime(time));
		Bindings.bindBidirectional(timeStringProperty, timeProperty, new DurationConverter());
	}
	
	public void setTime(Duration time){
		this.timeProperty.set(time);
	}
	
	public void setText(String text){
		this.textProperty.set(text);
	}
	
	public void setTimeString(String timeString){
		//TODO: finish up the time string alteration
		System.out.println("Not yet implemented!");
	}
	
	public Duration getTime(){
		return timeProperty.get();
	}
	
	public String getTimeString(){
		return timeStringProperty.get();
	}
	public String getText(){
		return textProperty.getValue();
	}
	
	public String toFileFormattedString(){
		StringBuilder sb = new StringBuilder();
		sb.append(TimeUtility.formatTime(timeProperty.get()));
		sb.append("\n");
		sb.append(textProperty);
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Commentary o) {
		double thisDuration = timeProperty.get().toMillis();
		double otherDuration = o.timeProperty.get().toMillis();
		return (int)(thisDuration - otherDuration);
	}
	
	@Override
	public boolean equals(Object o){
		Commentary obj = (Commentary)o;
		if (timeProperty.get().compareTo(obj.timeProperty.get()) == 0 
				&& this.textProperty.equals(obj.textProperty))
		{
			return true;
		}
		else
			return false;
	}
	
	private class DurationConverter extends StringConverter<Duration>{

		@Override
		public String toString(Duration object) {
			return TimeUtility.formatTime(object);
		}

		@Override
		public Duration fromString(String string){
			//if the string is not in format xx:xx:xx reject the string.
			String[] timeStringArray = string.split(":");
			if (timeStringArray.length != 3) { 
				System.out.println("INCORRECT TIME ENTERED"); 
				return Duration.UNKNOWN; 
			}
			//else continue onwards
			Duration duration = Duration.ZERO;
			duration = duration.add(Duration.hours(Double.valueOf(timeStringArray[0])));
			duration = duration.add(Duration.minutes(Double.valueOf(timeStringArray[1])));
			duration = duration.add(Duration.seconds(Double.valueOf(timeStringArray[2])));
			return duration;
		}
		
	}

}
