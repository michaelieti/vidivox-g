package main.control;

import player.VidivoxPlayer;
import main.MainView;
import main.model.MainModel;
import main.model.MainModelable;
import overlay.control.OverlayController;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import skins.SkinColor;

public class MainController implements MainControllable {
	
	/* object fields */
	MainView view;
	MainModel model;
	MediaView mediaView;
	private static MainController singletonObject;
	
	/* application state variables */
	private static final double PLAY_RATE_INCREMENT = 4.0;
	private double rwdRate;
	
	/* constructors */
	public MainController(){
		super();
		singletonObject = this;
	}
	
	public MainController(MainModel model){
		super();
		this.model = model;
		singletonObject = this;
	}
	
	/* singleton accessor */
	public static MainController getController(){
		if (singletonObject == null){
			new MainController();
		}
		return singletonObject;
	}
	
	/* initializers */
	
	public void setView(MainView view){
		this.view = view;
	}
	public void setModel(MainModel model){
		this.model = model;
	}
	
	@Override
	public boolean initialize() {
		if (model == null || view == null){
			return false;
		}
		/* binding: main view */
		view.titleProperty().bind(model.getTitleProperty());			//set title
		view.iconifiedProperty().addListener(new IconifiedListener());	//set listener for iconified
		view.setOnCloseRequest(new TempCleanHandler());					//set handler for closure
		
		//bind in each file button
		//bind in player properties
		//bind in each player control button
		return true;
	}
	
	@Override
	public void setMedia(Media media) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAutoPlay(boolean auto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void play() {
		if (model.hasMedia()){
			mediaView.getMediaPlayer().play();
		}
	}

	@Override
	public void ffwd() {
		play();
		MediaPlayer mp = mediaView.getMediaPlayer();
		Double currentRate = mp.getRate();
		mp.setRate(currentRate + PLAY_RATE_INCREMENT);
		setMute(true);
	}


	@Override
	public void rwd() {
		if (rwdRate == 0) {
			rwdRate = 1;
		} else if (rwdRate < 8.0) {
			rwdRate++;
			Task<Void> t = new RewindTask();	//reclassed this down to bottom
			Thread th = new Thread(t);
			th.setDaemon(true);
			th.start();
		}
	}
	
	@Override
	public void stop() {
		if (model.getMediaPlayer() != null) {
			model.getMediaPlayer().stop();
		}
	}

	@Override
	public void setTime(Duration time) {
		model.getMediaPlayer().seek(time);
	}

	@Override
	public void setVolume(double vol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMute(boolean mute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSkinColor(SkinColor color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BooleanProperty hasMediaProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModel(MainModelable model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(main.control.viewcomponents.MainView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIconified(boolean b) {
		view.setIconified(b);
	}

	/**
	 * Rewinds the video on an alternate thread to allow the GUI to remain responsive.
	 */
	private class RewindTask extends Task<Void> {
		@Override
		protected Void call() throws Exception {
			MediaPlayer mp = model.getMediaPlayer();
			mp.setRate(0.0001);
			mp.setMute(true);
			while (mp.getRate() < 1.0) {
				mp.seek(mp.getCurrentTime().subtract(
						Duration.seconds(rwdRate)));
				Thread.sleep(500);
			}
			mp.setMute(false);
			mp.play();
			mp.setRate(1.0);
			return null;
		}
	};
	
	/**
	 * Cleans the temporary folder on closing.
	 */
	private class TempCleanHandler implements EventHandler<WindowEvent> {
		@Override
		public void handle(WindowEvent event) {		
			File p = new File(System.getProperty("user.dir") + "/.temp/");
			if (p.exists() && p.isDirectory()) {
				for (File f : p.listFiles()) {
					f.delete();
				}
			}
			p.delete();
			Platform.exit();
		}
	}
	
	/**
	 * Listens for a change to the iconified property, which is true
	 * when the program is shrunk or false when it is not.
	 * When the value is changed, it changes the value of all other windows.
	 */
	private class IconifiedListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> ov,
				Boolean oldValue, Boolean newValue) {
			if (!newValue) {
				EditorController.getController().setIconified(false);
				OverlayController.getController().setIconified(false);
				MainController.getController.setIconified(false);
			} else {
				EditorController.getController().setIconified(true);
				OverlayController.getController().setIconified(true);
				
			}
		}
	}
	
}
