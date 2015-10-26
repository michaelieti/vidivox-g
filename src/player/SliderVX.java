package player;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

/**
 * Class extending the JavaFX implementation of Slider with some further mouse
 * event functionality. Slightly modified from
 * http://stackoverflow.com/a/21686813.
 * 
 * @author michael
 * 
 */
public class SliderVX extends Slider {

	private boolean sliderFlag = false; // returns true if mouse was clicked on
										// it.

	public SliderVX() {
		super();
	}

	public SliderVX(double minValue, double maxValue, double defaultValue) {
		super(minValue, maxValue, defaultValue);
	}

	/* Post-constructor initializer block */
	{
		// adds the event filter, which processes an event before the event
		// handlers.
		addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						sliderFlag = true;
					}
				});
	}

	/* end post constructor initializer */

	public boolean getSliderFlag() {
		return sliderFlag;
	}

	public void resetSliderFlag() {
		sliderFlag = false;
	}

}
