package overlay;

import javafx.util.Duration;

public class TimeUtility {
	
	public static String formatTime(Duration d){
		if (d.isIndefinite() || d.isUnknown()){
			return "UNKNOWN";
		}
		StringBuilder sb = new StringBuilder();
		int hours = (int) d.toHours(); //truncate hours
		int minutes = (int) (d.toMinutes() % 60.0);
		int seconds = (int) (d.toSeconds() % 60.0);
		String hString, mString, sString;
		
		if (hours < 10) {
			hString = "0"+hours;
		} else {
			hString = String.valueOf(hours);
		}
		
		if (minutes < 10) {
			mString = "0"+minutes;
		} else {
			mString = String.valueOf(minutes);
		}
		
		if (seconds < 10) {
			sString = "0"+seconds;
		} else {
			sString = String.valueOf(seconds);
		}
		
		String separator = ":";
		
		sb.append(hString).append(separator)
		.append(mString).append(separator)
		.append(sString);
	
		return sb.toString();
	}
}
