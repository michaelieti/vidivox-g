package player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 * A wrapper class for the vidivox player, provides extra power in terms of accessibility
 * @author michael
 *
 */
public class VidivoxPlayer {

	//has a MediaView
	//sets up the event handlers for buttons and sliders in
	//	- the VidivoxVideoControl class
	//	- the VidivoxMedia class
	
	
	/* REQUIRES CONTROL CLASSES*/
	private static VidivoxPlayer singletonPlayer;
	
	private MediaView mv;
	
	private VidivoxMedia mediaPanel;
	private VidivoxVideoControls controlPanel;
	private VidivoxFileControls filePanel;
	
	/* SINGLETON CONSTRUCTOR */
	public static VidivoxPlayer getVidivoxPlayer(){
		if (singletonPlayer == null){
			singletonPlayer = new VidivoxPlayer();
		}
		return singletonPlayer;
	}
	
	/* CONSTRUCTORS */
	private VidivoxPlayer(){
		singletonPlayer = this;
		this.mv = new MediaView();
	}
	
	/* PANEL INTERACTORS*/
	/* MUST BE CALLED DURING STAGE CONSTRUCTION */
	public void setMediaPanel(VidivoxMedia mediaPanel){
		this.mediaPanel = mediaPanel;
	}
	public void setControlPanel(VidivoxVideoControls controlPanel){
		this.controlPanel = controlPanel;
	}
	public void setFilePanel(VidivoxFileControls filePanel){
		this.filePanel = filePanel;
	}
	
	
	/* MEDIAVIEW/MEDIAPLAYER GETTER/SETTER */
	public MediaView getMediaView(){
		return mv;
	}
	public MediaPlayer getMediaPlayer(){
		return mv.getMediaPlayer();
	}
	public Media getMedia(){
		return mv.getMediaPlayer().getMedia();
	}
	
	public void setMediaPlayer(MediaPlayer mp){
		mv.setMediaPlayer(mp);
		initialize();
	}
	public void setMedia(Media media){
		mv.setMediaPlayer(new MediaPlayer(media));
		initialize();
	}
	
	/* INITIALIZER METHOD - CALLED AFTER A MediaPlayer OBJECT IS ASSIGNED */
	public void initialize(){
		bindTimeline(mediaPanel.getTimeline());		//timeline changes iff the video time changes
		
	}
	
	/* METHODS CALLED FROM INITIALIZE */
	public void bindTimeline(Slider mediaTimeline){
		// TODO: hook up this timeline shit
		mediaTimeline.valueProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				if (mediaTimeline.isValueChanging()){
					MediaPlayer mp = mv.getMediaPlayer();
					Duration mediaLength = mp.getMedia().getDuration();
					mp.seek(mediaLength.multiply(mediaTimeline.getValue() / VidivoxMedia.MAXTIME));
				}
			}
		});
	}
	
}
