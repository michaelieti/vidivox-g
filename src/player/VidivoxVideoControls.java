package player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class VidivoxVideoControls extends HBox {

	private MediaView mediaView;
	protected Button playBtn, stopBtn, skipBackBtn, skipFwdBtn; // Video
																// Playback
	protected Slider volumeBar;

	/* Status flags for the Application */
	private boolean mediaEnded = false;

	final private static double minVolume = 0.0;
	final private static double maxVolume = 10.0;
	final private static double defaultVolume = 5.0;

	final private double playRateIncrement = 3.5;

	public VidivoxVideoControls(MediaView mv) {
		super();
		VidivoxPlayer.getVidivoxPlayer().setControlPanel(this);
		this.mediaView = mv;

		// Buttons defined here (e.g. play button, pause button, stop button...)
		playBtn = new Button();
		playBtn.setId("playBtn");
		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				MediaPlayer mp = mediaView.getMediaPlayer();
				// if the rate is not currently 1.0, set to 1.0 and then return
				// to pause.
				if (mp.getRate() != 1.0) {
					mp.setRate(1.0);
				}
				Status status = mp.getStatus();
				// CHECK ONE: possible error? nothing is done in these states
				// for now
				if (status == Status.UNKNOWN || status == Status.HALTED) {
					playBtn.setId("playBtn");
					return;
				}
				// CHECK TWO: stopped/paused/ready states - begin playing video
				if (status == Status.STOPPED || status == Status.READY
						|| status == Status.PAUSED) {
					// check if video track is at end: if so, reset the track.
					if (mediaEnded) {
						mp.seek(mp.getStartTime());
						mediaEnded = false;
					}// continue playing from that point
					mp.play();
					playBtn.setId("pauseBtn");
				} else {
					mp.pause();
					playBtn.setId("playBtn");
				}
			}
		});

		stopBtn = new Button();
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				stopVideo();
				playBtn.setId("playBtn");
			}
		});
		stopBtn.setId("stopBtn");

		skipFwdBtn = new Button();
		skipFwdBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				ffwdVideo();
			}
		});
		skipFwdBtn.setId("skipFwdBtn");

		skipBackBtn = new Button();
		skipBackBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				rwdVideo();
			}
		});
		skipBackBtn.setId("skipBackBtn");

		volumeBar = new Slider(minVolume, maxVolume, defaultVolume);
		// the volumeBar's valueProperty registers a listener, that is notified
		// when
		// that property is invalidated. Invalidation is caused by e.g. the
		// value
		// property not being equal to the slider's position
		volumeBar.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				double currentVol = volumeBar.getValue() / maxVolume;
				// gets the current value represented by the slider
				mediaView.getMediaPlayer().setVolume(currentVol);
				// updates the volume in mediaplayer to be equal to the slider
				// value
			}
		});

		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.getChildren().addAll(skipBackBtn, stopBtn, playBtn, skipFwdBtn,
				volumeBar);
		
		this.getStyleClass().add("blue");
		// This can be changed to BlueSkin GreenSkin PurpleSkin or OrangeSkin
		//this.getStylesheets().add(getClass().getResource("/skins/BlueSkin.css").toExternalForm());
	}

	/* end placement of classes */

	public void stopVideo() {
		System.out.println("Pressed Stop Wow!");
		mediaView.getMediaPlayer().stop();
	}

	public void ffwdVideo() {
		System.out.println("Pressed Fast Foward Wow!");
		MediaPlayer mp = mediaView.getMediaPlayer();
		Double currentRate = mp.getRate();
		mp.setRate(currentRate + playRateIncrement);
	}

	public void rwdVideo() {
		MediaPlayer mp = mediaView.getMediaPlayer();
		if (mp.getRate() != 1.0) {
			mp.setRate(1.0);
			return;
		}
		mp.seek(mp.getCurrentTime().subtract(Duration.seconds(10)));
	}

}
