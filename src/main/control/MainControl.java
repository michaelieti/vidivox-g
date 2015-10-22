package main.control;

import player.VidivoxPlayer;
import main.model.MainModelable;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import skins.SkinColor;

public class MainControl implements MainControllable {

	MainModelable model;
	private Double playRateIncrement = 3.5;
	private double rwdRate = 0;

	public MainControl(MainModelable model) {
		this.model = model;
	}

	@Override
	public void setMedia(Media media) {
		VidivoxPlayer.getVPlayer(model).setMedia(media);

	}

	@Override
	public void play() {
		if (model.getMediaPlayer() != null) {
			model.getMediaPlayer().play();
		}
	}

	@Override
	public void ffwd() {
		play();
		MediaPlayer mp = model.getMediaPlayer();
		Double currentRate = mp.getRate();
		mp.setRate(currentRate + playRateIncrement);
		setMute(true);
	}

	@Override
	public void rwd() {
		if (rwdRate == 0) {
			rwdRate = 1;
		} else if (rwdRate < 8.0) {
			rwdRate++;
			Task<Void> t = new Task<Void>() {
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
		model.getMediaPlayer().setVolume(vol);
	}

	@Override
	public void setMute(boolean mute) {
		model.getMediaPlayer().setMute(mute);
	}

	@Override
	public void setSkinColor(SkinColor color) {
		model.getCurrentSkinColorProperty().setValue(color);
	}

	@Override
	public BooleanProperty hasMediaProperty() {
		return model.hasMediaProperty();
	}

	@Override
	public void setAutoPlay(boolean auto) {
		model.getMediaPlayer().setAutoPlay(auto);
		
	}

}
