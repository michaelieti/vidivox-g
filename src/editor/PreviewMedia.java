package editor;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;

public class PreviewMedia {
	
	private static PreviewMedia singleton;
	
	private PreviewControls pc;
	private MediaView mv;
	private MediaPlayer mp;
	private boolean playerIsSet = false;	//flag for whether this MediaView has been assigned a player yet or not
	
	public static PreviewMedia getPreviewMedia(){
		if (singleton == null){
			singleton = new PreviewMedia();
		}
		return singleton;
	}
	
	private PreviewMedia(){
		mv = new MediaView();
		playerIsSet = false;
	}
	
	public Media getMedia(){
		if (playerIsSet) {
			return mv.getMediaPlayer().getMedia();
		}
		else return null;
	}
	public MediaPlayer getPlayer(){
		if (playerIsSet) {
			return mp;
		}
		else return null;
	}
	public MediaView getView(){
		return mv;
	}
	public void setMedia(Media media){
		if (playerIsSet){
			mp.dispose();
			mp = null;
			playerIsSet = false;
		}
		mv.setMediaPlayer(mp = new MediaPlayer(media));
		playerIsSet = true;
		initialize();
	}
	
	/* call this during parent construction, before setting media */
	public void setControls(PreviewControls previewControls){
		this.pc = previewControls;
	}
	
	private void initialize(){
		bindFFWD();
		bindRWD();
		bindVolumeSlider();
	}
	private void bindFFWD(){
		pc.ffwd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mp.seek(mp.getCurrentTime().subtract(new Duration(10000)));
			}
		});
	}
	private void bindRWD(){
		pc.rewind.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mp.seek(mp.getCurrentTime().add(new Duration(10000)));
			}
		});
	}
	private void bindVolumeSlider(){
		Slider volSlider = pc.volumeSlider;
		volSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				double currentVol = volSlider.getValue()/100;
				mp.setVolume(currentVol);
			}
		});
	}
	
}
