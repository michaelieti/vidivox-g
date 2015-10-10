package player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class VidivoxVideoControls extends VBox {

	private MediaView mediaView;
	protected Button playBtn, stopBtn, skipBackBtn, skipFwdBtn;
	protected SliderVX volumeBar;
	protected SliderVX mediaTimeline; 
	protected Text currentTimeLabel, totalDurationLabel;


	/* Status flags for the Application */
	private boolean mediaEnded = false;
	private boolean rwd = false;

	final public static double MINVOLUME = 0.0;
	final public static double MAXVOLUME = 10.0;
	final public static double DEFAULTVOLUME = 5.0;

	protected static final double MINTIME = 0.0;
	protected static final double MAXTIME = 100.0;
	protected static final double STARTTIME = 0.0;

	final private double playRateIncrement = 3.5;
	
	final public static String DEFAULT_SKIN = "blue";

	public VidivoxVideoControls(MediaView mv) {
		super();
		VidivoxPlayer.getVidivoxPlayer().setControlPanel(this);
		this.mediaView = mv;
		
		HBox timeSliderPanel = new HBox();
		HBox videoControlPanel = new HBox();

		// Buttons defined here (e.g. play button, pause button, stop button...)
		playBtn = new Button();
		playBtn.setId("playBtn");
		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				rwd = false;
				MediaPlayer mp = mediaView.getMediaPlayer();
				// if the rate is not currently 1.0, set to 1.0 and then return
				// to pause.
				if (mp.getRate() != 1.0) {
					mp.setRate(1.0);
					mp.setMute(false);
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
				rwd = false;
				stopVideo();
				playBtn.setId("playBtn");
			}
		});
		stopBtn.setId("stopBtn");

		skipFwdBtn = new Button();
		skipFwdBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				rwd = false;
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
		
		volumeBar = new SliderVX(MINVOLUME, MAXVOLUME, DEFAULTVOLUME);
		volumeBar.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				double currentVol = volumeBar.getValue() / MAXVOLUME;
				mediaView.getMediaPlayer().setVolume(currentVol);
			}
		});
		
		/* VOLUME SLIDER HBOX (next to main controls) */
		HBox volSlide = new HBox();
		volSlide.setAlignment(Pos.CENTER);
		volSlide.setSpacing(5);
		Text t = new Text("Volume");
		t.setFill(Color.LIGHTGRAY);
		volSlide.getChildren().addAll(t,volumeBar);
		
		/* TIME SLIDER PANEL SET UP */
		mediaTimeline = new SliderVX(MINTIME, MAXTIME, STARTTIME);
		currentTimeLabel = new Text("00:00:00");
		currentTimeLabel.setFill(Color.LIGHTGRAY);
		totalDurationLabel = new Text("99:99:99");
		totalDurationLabel.setFill(Color.LIGHTGRAY);
		
		/* TIME SLIDER PANEL POSITIONAL SETTINGS */
		timeSliderPanel.setAlignment(Pos.CENTER);
		timeSliderPanel.setSpacing(10);
		HBox.setHgrow(mediaTimeline, Priority.ALWAYS);
		HBox.setMargin(currentTimeLabel, new Insets(2,8,2,8));
		HBox.setMargin(totalDurationLabel, new Insets(2,8,2,8));
		timeSliderPanel.getChildren().addAll(currentTimeLabel, mediaTimeline, totalDurationLabel);
	//	timeSliderPanel.setStyle("-fx-background-color: black");
		
		/* VIDEO CONTROL PANEL POSITIONAL SETTINGS */
		videoControlPanel.setAlignment(Pos.CENTER);
		videoControlPanel.setSpacing(10);
		videoControlPanel.getChildren().addAll(skipBackBtn, stopBtn, playBtn, skipFwdBtn,
				volSlide);
		
		/* MAIN COMPONENT POSITIONAL SETTINGS */
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.getChildren().addAll(timeSliderPanel, videoControlPanel);
		this.getStyleClass().add(DEFAULT_SKIN);
		// This can be changed to BlueSkin GreenSkin PurpleSkin or OrangeSkin
		//this.getStylesheets().add(getClass().getResource("/skins/BlueSkin.css").toExternalForm());
	}

	/* end placement of classes */

	public void stopVideo() {
		System.out.println("Pressed Stop Wow!");
		mediaView.getMediaPlayer().stop();
	}

	public void ffwdVideo() {
		mediaView.getMediaPlayer().play();
		System.out.println("Pressed Fast Foward Wow!");
		MediaPlayer mp = mediaView.getMediaPlayer();
		Double currentRate = mp.getRate();
		mp.setRate(currentRate + playRateIncrement);
		mp.setMute(true);
	}

	public void rwdVideo() {
		if (rwd) {
			rwd = false;
		} else {
			rwd = true;
			Task<Void> t = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					MediaPlayer mp = mediaView.getMediaPlayer();
					mp.setRate(0.0001);
					mp.setMute(true);
					while (rwd) {
						mp.seek(mp.getCurrentTime().subtract(Duration.seconds(1)));
						Thread.sleep(500);
					}
					mp.setMute(false);
					mp.play();
					mp.setRate(1.0);
					return null;
				}
			};
			Thread th = new Thread(t);
			th.setDaemon(true);
			th.start();
		}
	}
	
	public SliderVX getTimeline() {
		return mediaTimeline;
	}

}
