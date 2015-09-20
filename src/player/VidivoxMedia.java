package player;

import javafx.scene.layout.VBox;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 * The panel holding the media player.
 * 
 */
public class VidivoxMedia extends VBox {
	private MediaView mv;
	
	/* Media time line properties*/
	private Slider mediaTimeline;
	final private static double STARTTIME = 0.0;
	final private static double ENDTIME = 100.0;
	final private static double DEFAULTTIME = 0.0;
	
	public VidivoxMedia() {
		super();
		mv = new MediaView();
		mv.setFitHeight(500);
		mv.setFitWidth(800);
		
		mediaTimeline = new Slider(STARTTIME,ENDTIME,DEFAULTTIME);  
		//TODO: subclass this timeline. tie timeline to media file so it updates accordingly.
		mediaTimeline.valueProperty().addListener(new InvalidationListener(){
			@Override
			public void invalidated(Observable observable) {
				//When the value property is invalidated (i.e. the slider value has changed and no longer
				//matches the value saved) this method in this handler is called.
				if (mediaTimeline.isValueChanging()){
					//calculate duration via current slider position divided by max slider position
					double sliderRatio = ( mediaTimeline.getValue() / ENDTIME );			//gets the current ratio represented by slider
					Duration mediaLength = mv.getMediaPlayer().getMedia().getDuration();	//gets the duration of the media
					mv.getMediaPlayer().seek(mediaLength.multiply(sliderRatio));			//seeks to new position based on media
				}
			}
		});
		
		
		this.getChildren().addAll(mv,mediaTimeline);
	}
	
	public MediaView getMediaView() {
		return mv;
	}
	
}

