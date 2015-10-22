package main.model;

import player.VidivoxPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import skins.SkinColor;

public class MainModel implements MainModelable {

	private MediaView view;
	private BooleanProperty mediaProperty;
	private ObjectProperty<SkinColor> skinProperty;

	public MainModel() {
		view = VidivoxPlayer.getVPlayer(null).getMediaView();

		/*
		 * Creates a property initially set to false. This is set to true
		 * whenever MediaViews media player is changed
		 */
		mediaProperty = new SimpleBooleanProperty(false);
		view.mediaPlayerProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> property, Object oldValue,
					Object newValue) {
				MediaPlayer v1 = (MediaPlayer) oldValue;
				MediaPlayer v2 = (MediaPlayer) newValue;
				/*
				 * Given the old value is null, then we should be changing to a
				 * new mediaplayer.
				 */
				if (v1 == null) {
					mediaProperty.setValue(true);
				}
				/*
				 * However this change could be from null -> null (when v2 is
				 * null)
				 */
				if (v2 == null) {
					mediaProperty.setValue(false);
					return;
				}
			}
		});

		/*
		 * Initializing the Skin Property Ideally all skins should be syched
		 * with this property
		 */
		skinProperty = new SimpleObjectProperty<SkinColor>(SkinColor.BLUE);
		skinProperty.addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> property,
					Object oldValue, Object newValue) {
				
			} 
			
		});
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

}
