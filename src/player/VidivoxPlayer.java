package player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Button;
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
		bindButtonId(controlPanel.playBtn);
	}
	
	/* METHODS CALLED FROM INITIALIZE */
	public void bindTimeline(final SliderVX mediaTimeline){
		mediaTimeline.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				if (mediaTimeline.getSliderFlag()){
					Duration mediaLength = getMedia().getDuration();
					getMediaPlayer().seek(mediaLength.multiply(mediaTimeline.getValue() / VidivoxMedia.MAXTIME));
					mediaTimeline.resetSliderFlag();
				}
			}
		});
		mv.getMediaPlayer().currentTimeProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				if ( ! mediaTimeline.getSliderFlag()){
					double timePassed = getMediaPlayer().getCurrentTime().toMillis();	//get current time value
					double timeTotal = getMedia().getDuration().toMillis();				//get total length
					double newRatio = timePassed/timeTotal; 		//divide total length by current time = ratio
					mediaTimeline.setValue(100*newRatio);			//set slider value = ratio * 100
				}
				
			}
		});
	}
	private void bindButtonId(final Button button){
		MediaPlayer mp = getMediaPlayer();
		//define both runnables
		Runnable setIdPlayRunnable = new Runnable() {
			@Override
			public void run() {
				button.setId("pauseBtn");
			}
		};
		Runnable setIdPausedRunnable = new Runnable(){
			@Override
			public void run() {
				button.setId("playBtn");
			}
		};
		//assign the runnables to event handlers
		mp.setOnPaused(setIdPausedRunnable);
		mp.setOnPlaying(setIdPlayRunnable);
		mp.setOnStopped(setIdPausedRunnable);
		
		
	}
}
