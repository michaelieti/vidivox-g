package player;

import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * The panel holding the media player.
 * 
 */
public class VidivoxMedia extends VBox {
	private MediaView mv;
	private Slider mediaTimeline;
	
	public VidivoxMedia() {
		super();
		mv = new MediaView();
		mv.setFitHeight(500);
		mv.setFitWidth(800);
		mediaTimeline = new Slider(0.0,1.0,0.0);  //TODO: subclass this timeline. tie timeline to media file so it updates accordingly.
		this.getChildren().addAll(mv,mediaTimeline);
	}
	
	public MediaView getMediaView() {
		return mv;
	}
	
}

