package main.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import skins.SkinColor;

public class MainModel implements MainModelable {

	private MediaView view;
	private BooleanProperty mediaProperty;
	private ObjectProperty<SkinColor> skinProperty;

	public MainModel() {
		view = new MediaView();

		ObjectBinding<MediaPlayer.Status> Status = new When(view
				.mediaPlayerProperty().isNull()).then(
				MediaPlayer.Status.UNKNOWN).otherwise(getStatus());
		BooleanProperty sb = new SimpleBooleanProperty(false);
		BooleanBinding b = new When(view.mediaPlayerProperty().isNull()).then(
				true).otherwise(false);
		System.out.println(Status.getValue() + "    \n    "
				+ view.getMediaPlayer());
		BooleanBinding correctMedia = view.mediaPlayerProperty().isNotNull()
				.and(Status.isNotEqualTo(MediaPlayer.Status.UNKNOWN));

		BooleanBinding mediaBinding = new When(correctMedia).then(true)
				.otherwise(false);

		mediaProperty = new SimpleBooleanProperty(false);
		mediaProperty.bind(mediaBinding);

		skinProperty = new SimpleObjectProperty<SkinColor>(SkinColor.BLUE);
	}

	public boolean hasMedia() {
		return mediaProperty.getValue();
	}

	@Override
	public BooleanProperty hasMediaProperty() {
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
		return skinProperty.getValue();
	}

	@Override
	public ObjectProperty<SkinColor> getCurrentSkinColorProperty() {
		return skinProperty;
	}

	public ObservableObjectValue<Status> getStatus() {
		if (getMediaPlayer() == null) {
			return new When(new SimpleBooleanProperty(true)).then(
					MediaPlayer.Status.UNKNOWN).otherwise(
					MediaPlayer.Status.UNKNOWN);
		}
		return getMediaPlayer().statusProperty();
	}

}
