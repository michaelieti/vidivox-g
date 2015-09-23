package player;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

/**
 * Class extending the JavaFX implementation of Slider with some further mouse event functionality.
 * Slightly modified from http://stackoverflow.com/a/21686813.
 * @author michael
 *
 */
public class SliderVX extends Slider {

	static final private long TIME_THRESHOLD = 100;
	
	private boolean sliderFlag = false;	//returns true if mouse was clicked on it.
	private long mouseEventTime = 0;	//time in milliseconds
	
	public SliderVX(){
		super();
	}
	
	public SliderVX(double minValue, double maxValue, double defaultValue){
		super(minValue, maxValue, defaultValue);
	}
	
	/*Post-constructor initializer block */
	{
		//adds the event filter, which processes an event before the event handlers.
		addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				/* mouseEventTime = System.currentTimeMillis(); */
				sliderFlag = true;
			}
		});
	} /* end post constructor initializer */
	
	/*
	public boolean wasMousePressedOnSlider(){
		if ((System.currentTimeMillis() - mouseEventTime) <= TIME_THRESHOLD )
	}
	*/
	
	public boolean getSliderFlag(){
		return sliderFlag;
	}
	
	public void resetSliderFlag(){
		sliderFlag = false;
	}
	
	//mouse is clicked
	//currentTimeMillis is called, and the mouseEventTime is set e.g. to 100.
	//wasMousePressedOnSlider returns true as long as currenttime - mouseEventTime < = threshold
	
	//need to return the mouseevent time to false;
	
}
