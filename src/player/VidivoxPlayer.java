package player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import overlay.TimeUtility;

/**
 * A wrapper class for the vidivox player, provides extra power in terms of
 * accessibility
 * 
 * @author michael
 * 
 */
public class VidivoxPlayer {

	// has a MediaView
	// sets up the event handlers for buttons and sliders in
	// - the VidivoxVideoControl class
	// - the VidivoxMedia class

	/* REQUIRES CONTROL CLASSES */
	private static VidivoxPlayer singletonPlayer;

	private MediaView mv;
	
	private Media originalMedia;
	
	private MediaPanel mediaPanel;
	private VidivoxVideoControls controlPanel;
	private VidivoxFileControls filePanel;

	private double initialVolume = VidivoxVideoControls.DEFAULTVOLUME;
	
	/* SINGLETON CONSTRUCTOR */
	public static VidivoxPlayer getVPlayer() {
		if (singletonPlayer == null) {
			singletonPlayer = new VidivoxPlayer();
		}
		return singletonPlayer;
	}

	/* CONSTRUCTORS */
	private VidivoxPlayer() {
		singletonPlayer = this;
		this.mv = new MediaView();
	}

	/* PANEL INTERACTORS */
	/* MUST BE CALLED DURING STAGE CONSTRUCTION */
	public VidivoxPlayer setMediaPanel(MediaPanel mediaPanel) {
		this.mediaPanel = mediaPanel;
		
		return this;
	}

	public VidivoxPlayer setControlPanel(VidivoxVideoControls controlPanel) {
		this.controlPanel = controlPanel;
		return this;
	}

	public VidivoxPlayer setFilePanel(VidivoxFileControls filePanel) {
		this.filePanel = filePanel;
		return this;
	}

	/* MEDIAVIEW/MEDIAPLAYER GETTER/SETTER */
	public MediaView getMediaView() {
		return mv;
	}

	public MediaPlayer getMediaPlayer() {
		return mv.getMediaPlayer();
	}

	public Media getMedia() {
		return mv.getMediaPlayer().getMedia();
	}

	public void setOriginalMedia(Media media){
		originalMedia = media;
	}
	
	public Media getOriginalMedia(){
		return originalMedia;
	}
	
	public void setMediaPlayer(MediaPlayer mp) {
		if (getMediaPlayer() != null){
			predisposal();
			getMediaPlayer().dispose();
		}
		mv.setMediaPlayer(mp);
		initialize();
	}

	public void setMedia(Media media) {
		if (getMediaPlayer() != null){
			predisposal();
			getMediaPlayer().dispose();
		}
		mv.setMediaPlayer(new MediaPlayer(media));
		initialize();
	}

	/* INITIALIZER METHOD - CALLED AFTER A MediaPlayer OBJECT IS ASSIGNED */
	public void initialize() {
		getMediaPlayer().setOnReady(new Runnable(){
			@Override
			public void run() {
				bindTimeline(controlPanel.getTimeline());
				bindButtonId(controlPanel.playBtn);
				setInitialVolume(controlPanel.volumeBar);
				bindCurrentTimeLabel(controlPanel.currentTimeLabel);
				setDurationLabel(controlPanel.totalDurationLabel);
			}
			
		});
		
	}
	
	/* DISPOSAL METHOD - CALLED JUST BEFORE DISPOSAL OF A MediaPlayer OBJECT */
	public void predisposal(){
		storeVolume(controlPanel.volumeBar);
	}

	/* METHODS CALLED FROM INITIALIZE */
	private void bindTimeline(final SliderVX mediaTimeline) {
		mediaTimeline.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				if (mediaTimeline.getSliderFlag()) {
					Duration mediaLength = getMedia().getDuration();
					getMediaPlayer().seek(
							mediaLength.multiply(mediaTimeline.getValue()
									/ VidivoxVideoControls.MAXTIME));
					mediaTimeline.resetSliderFlag();
				}
			}
		});
		getMediaPlayer().currentTimeProperty()
				.addListener(new InvalidationListener() {
					@Override
					public void invalidated(Observable observable) {
						if (!mediaTimeline.getSliderFlag()) {
							double timePassed = getMediaPlayer()
									.getCurrentTime().toMillis();
							double timeTotal = getMedia().getDuration()
									.toMillis();
							double newRatio = timePassed / timeTotal; 
							mediaTimeline.setValue(100 * newRatio); 
						}

					}
				});
	}
	private void bindButtonId(final Button button) {
		MediaPlayer mp = getMediaPlayer();
		// define both runnables
		Runnable setIdPlayRunnable = new Runnable() {
			@Override
			public void run() {
				button.setId("pauseBtn");
			}
		};
		Runnable setIdPausedRunnable = new Runnable() {
			@Override
			public void run() {
				button.setId("playBtn");
			}
		};
		// assign the runnables to event handlers
		mp.setOnPaused(setIdPausedRunnable);
		mp.setOnPlaying(setIdPlayRunnable);
		mp.setOnStopped(setIdPausedRunnable);

	}
	private void setInitialVolume(final SliderVX volumeBar){
		getMediaPlayer().setVolume(initialVolume);
	}
	
	private void bindCurrentTimeLabel(final Text t){
		//TODO: bind the time label to mediaPlayer.currentTimeProperty
		getMediaPlayer().currentTimeProperty()
		.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				Duration timePassed = getMediaPlayer().getCurrentTime();
				String timePassedAsString = TimeUtility.formatTime(timePassed);
				t.setText(timePassedAsString);
			}
		});
		
	}
	private void setDurationLabel(final Text t){
		Duration totalDuration = getMedia().getDuration();
		String totalDurationString = TimeUtility.formatTime(totalDuration);
		t.setText(totalDurationString);
	}
	
	
	/* METHODS CALLED FROM PREDISPOSAL */
	private void storeVolume(final SliderVX volumeBar){
		initialVolume = volumeBar.getValue() / VidivoxVideoControls.MAXVOLUME;
	}
}
