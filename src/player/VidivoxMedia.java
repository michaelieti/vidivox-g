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
	protected VidivoxPlayer vp = VidivoxPlayer.getVidivoxPlayer();
	protected Slider mediaTimeline;
	
	protected static final double MINTIME = 0.0;
	protected static final double MAXTIME = 100.0;
	protected static final double STARTTIME = 0.0;
	
	
	public VidivoxMedia() {
		super();
		vp.setMediaPanel(this);
		MediaView mv = vp.getMediaView();
		mv.setFitHeight(500);
		mv.setFitWidth(800);
		mediaTimeline = new Slider(MINTIME, MAXTIME, STARTTIME);  //TODO: subclass this timeline. tie timeline to media file so it updates accordingly.		
		this.getChildren().addAll(mv,mediaTimeline);
	}
	
	public MediaView getMediaView() {
		return vp.getMediaView();
	}
	public VidivoxPlayer getPlayer(){
		return vp;
	}
	public Slider getTimeline(){
		return mediaTimeline;
	}
	
}

