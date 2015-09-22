package player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class VidivoxVideoControls extends HBox {

	private MediaView mediaView;
	private Button playBtn, stopBtn, skipBackBtn, skipFwdBtn; //Video Playback
	private Slider volumeBar;
	
	/*Status flags for the Application*/
	private boolean mediaEnded = false;
	
	/*Flags related to application view */
	private boolean currentlyPlaying; //returns true if video currently playing.
	
	final private static double minVolume = 0.0;
	final private static double maxVolume = 10.0;
	final private static double defaultVolume = 5.0;
	
	
	public VidivoxVideoControls(MediaView mv) {
		super();
		this.mediaView = mv;
		
		ToggleButton tb = new ToggleButton();
		//Buttons defined here (e.g. play button, pause button, stop button...)
		playBtn = new Button();
		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				updateCurrentlyPlaying();
				if ( ! currentlyPlaying) {
					playVideo(playBtn);
				}
				else {
					pauseVideo(playBtn);
				}
				updateControls();
			}
		});
		playBtn.setId("playBtn");

		stopBtn = new Button();
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				stopVideo();
				updateControls();
			}
		});
		stopBtn.setId("stopBtn");
		
		skipFwdBtn = new Button();
		skipFwdBtn.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent event) {
				ffwdVideo();
			}
		});
		skipFwdBtn.setId("skipFwdBtn");
		
		skipBackBtn = new Button();
		skipBackBtn.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent event) {
				rwdVideo();
			}
		});
		skipBackBtn.setId("skipBackBtn");
		//Initializing the Volume Slider

		volumeBar = new Slider(minVolume, maxVolume, defaultVolume);
		//the volumeBar's valueProperty registers a listener, that is notified when
		//	that property is invalidated. Invalidation is caused by e.g. the value
		//	property not being equal to the slider's position
		volumeBar.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				//if (volumeBar.isValueChanging()){
					double currentVol = volumeBar.getValue() / maxVolume;	//gets the current value represented by the slider
					mediaView.getMediaPlayer().setVolume(currentVol);	//updates the volume in mediaplayer to be equal to the slider value
				//}
			}
		});

		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.getChildren().addAll(skipBackBtn, stopBtn, playBtn, skipFwdBtn, volumeBar);
		this.getStylesheets().add(getClass().getResource("/skins/MainStage.css").toExternalForm());
	}
	

	protected void updateCurrentlyPlaying() {
		Status st = mediaView.getMediaPlayer().getStatus();
		currentlyPlaying = (st == Status.PLAYING);
		return;
	}
	
	protected void updateControls() {
		updateCurrentlyPlaying();
		if (currentlyPlaying){	//gg ez shit
			playBtn.setId("pauseBtn");
		}
		else {
			playBtn.setId("playBtn");
		}
	}
	
	/*
	 * Previously in VidivoxLauncher
	 * 
	 * 
	 * 
	 */
	
	
	/**
	 * An event handling method that dictates what occurs when the Play button is pushed.
	 * If the video is paused, stopped, or at the end of the video track, play resumes from current point or the start of the video track.
	 * If the video is currently playing, the video is paused, setting into motion the setOnPaused event handler.
	 * Other states - nothing happens (e.g. an error state is generated)
	 * @param src - the button that generated the event
	 */
	public void playVideo(Button src) {
		System.out.println("Pressed Play Wow!");
		//Refactored obtain the current media view
		Status status = mediaView.getMediaPlayer().getStatus();	// obtain current media player status.
				//Several checks are now carried out.
		//CHECK ONE: possible error? nothing is done in these states for now
		if (status == Status.UNKNOWN || status == Status.HALTED ){
			return;
		}
		//CHECK TWO: stopped/paused/ready states - begin playing video
		if (status == Status.STOPPED || status == Status.READY || status == Status.PAUSED ){
			//check if video track is at end: if so, reset the track.
			if (mediaEnded){
				mediaView.getMediaPlayer().seek(mediaView.getMediaPlayer().getStartTime());
				mediaEnded = false;
			}//continue playing from that point
			mediaView.getMediaPlayer().play();
		} else {
			mediaView.getMediaPlayer().pause();
		}
	}
	
	public void pauseVideo(Button src) {
		System.out.println("Pressed Pause Wow!");
		mediaView.getMediaPlayer().pause();
	}

	public void stopVideo() {
		System.out.println("Pressed Stop Wow!");
		mediaView.getMediaPlayer().stop();
	}

	public void ffwdVideo() {
		System.out.println("Pressed Fast Foward Wow!");
		MediaPlayer mp = mediaView.getMediaPlayer();
		mp.seek(mp.getCurrentTime().add(Duration.seconds(10)));
	}

	public void rwdVideo() {
		System.out.println("Pressed Rewind Wow!");
		MediaPlayer mp = mediaView.getMediaPlayer();
		mp.seek(mp.getCurrentTime().subtract(Duration.seconds(10)));
	}
	
}

