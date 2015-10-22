package main.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import skins.SkinColor;

public class MainModel implements MainModelable {

	private MediaView view;
	private SkinColor currentColor = SkinColor.BLUE;

	public MainModel() {
		view = new MediaView();
	}

	@Override
	public BooleanProperty hasMedia() {
		BooleanBinding validMediaBinding = new When(getMediaPlayer()
				.statusProperty().isEqualTo(MediaPlayer.Status.UNKNOWN)).then(
				false).otherwise(true);
		BooleanBinding mediaBinding = new When(getMediaView()
				.mediaPlayerProperty().isNull()).then(false).otherwise(
				validMediaBinding);
		BooleanProperty mediaProperty = new SimpleBooleanProperty(false);
		mediaProperty.bind(mediaBinding);
		return mediaProperty;
	}

	@Override
	public MediaView getMediaView() {
		return view;
	}

	@Override
	public MediaPlayer getMediaPlayer() {
		return getMediaView().getMediaPlayer();
	}

	@Override
	public Media getMedia() {
		return getMediaPlayer().getMedia();
	}

	@Override
	public Duration getDuration() {
		return getMedia().getDuration();
	}

	@Override
	public ReadOnlyObjectProperty<Duration> getCurrentTimeProperty() {
		return getMediaPlayer().currentTimeProperty();
	}

	@Override
	public DoubleProperty getVolumeProperty() {
		return getMediaPlayer().volumeProperty();
	}

	@Override
	public SkinColor getCurrentSkinColor() {
		return currentColor;
	}

}
