package overlay;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import javafx.util.StringConverter;
import overlay.control.OverlayType;

public class Commentary implements Comparable<Commentary> {
	
	private SimpleObjectProperty<OverlayType> typeProperty;
	private SimpleObjectProperty<Duration> timeProperty;
	private SimpleStringProperty textProperty;
	private SimpleStringProperty timeStringProperty;
	//can put in pitch/voice/stretch later
	
	public Commentary() {
		this(Duration.ZERO, "<no text>", OverlayType.NO_TYPE); 
	}
	
	/**
	 * Create a new Commentary object. Requires a Duration object with the time for insertion, and the text to be inserted.
	 * @param time - Duration object of where the commentary begins.
	 * @param text - String of what is to be said.
	 */
	public Commentary(Duration time, String text, OverlayType type){
		this.timeProperty = new SimpleObjectProperty<Duration>(time);
		this.textProperty = new SimpleStringProperty(text);
		this.timeStringProperty = new SimpleStringProperty(TimeUtility.formatTime(time));
		this.typeProperty = new SimpleObjectProperty<>(type);
		Bindings.bindBidirectional(timeStringProperty, timeProperty, new DurationConverter());
	}
	
	/* Property getters/setters */
	/**
	 * Sets the time for insertion.
	 * @param time
	 */
	public void setTime(Duration time){
		timeProperty.set(time);
		setTimeString("");
	}
	
	public void setType(OverlayType type){
		typeProperty.set(type);
	}
	
	/**
	 * Sets the text for the commentary
	 * @param text
	 */
	public void setText(String text){
		textProperty.set(text);
	}
	/**
	 * Sets the timestring. 
	 * **note that the string to be passed doesn't have any effect on the timestring,
	 * but rather will force it to be updated.
	 * @param timeString
	 */
	public void setTimeString(String timeString){
		timeStringProperty.set(TimeUtility.formatTime(getTime()));
	}
	/**
	 * Returns a duration object representing the time of insertion.
	 * @return
	 */
	public Duration getTime(){
		return timeProperty.get();
	}
	public String getTimeString(){
		return timeStringProperty.get();
	}
	public OverlayType getType(){
		return typeProperty.get();
	}
	public String getText(){
		return textProperty.getValue();
	}
	
	/**
	 * Returns a string for this object, which is formatted in the following way:
	 * - time (in format hh:mm:ss)
	 * - text (in single line)
	 * @return string representing a formatted commentary for a file
	 */
	public String toFileFormattedString(){
		StringBuilder sb = new StringBuilder();
		sb.append(TimeUtility.formatTime(timeProperty.get()));
		sb.append(System.getProperty("line.separator"));
		sb.append(textProperty);
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

	/**
	 * Returns a negative integer if the compared object comes before this object, with respect to timeline.
	 * 	i.e. if the compared object has a shorter duration, it comes before this object.
	 * Returns 0 if the compared object occurs at the same time.
	 * Returns a positive integer if the compared object comes after this object.
	 */
	@Override
	public int compareTo(Commentary o) {
		double thisDuration = timeProperty.get().toMillis();
		double otherDuration = o.timeProperty.get().toMillis();
		return (int)(otherDuration - thisDuration);
	}

	
/*	@Override
	public boolean equals(Object o){
		Commentary obj = (Commentary)o;
		//TODO: occasional crash on below comparator. how to reproduce?
		System.out.println("time property of this: "+ TimeUtility.formatTime(timeProperty.get()));
		System.out.println("text property of this: "+ textProperty.get());
		System.out.println("time property of other: "+ TimeUtility.formatTime(obj.timeProperty.get()));
		System.out.println("text property of other: " + obj.textProperty.get());
		if (timeProperty.get().
				compareTo(
						obj.timeProperty.get()) == 0 
				&& this.textProperty.equals(obj.textProperty))
		{
			return true;
		}
		else
			return false;
	}
*/
	
	/**
	 * A class required for the use of bi-directional bindings.
	 * Converts formatted strings to durations and vice versa.
	 * @author michael
	 *
	 */
	public class DurationConverter extends StringConverter<Duration>{

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
