package main.model;

import player.VidivoxPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import skins.SkinColor;

/**
 * A model of data from which MainStage is built off. MainModel is primarily
 * concerned with retrieving data from Model, not changing it. (See
 * MainControl).
 * 
 * @author adav194
 * 
 */
public class MainModel implements MainModelable {

	/** The Default Skin for this window. */
	private static SkinColor DEFAULT_SKIN = SkinColor.BLUE;
	/** The Default Title for this window */
	private static String DEFAULT_TITLE = "Vidivox V1";

	/* The Data from which this Model is based */
	private MediaView view;
	private BooleanProperty mediaProperty;
	private ObjectProperty<SkinColor> skinProperty;
	private StringProperty titleProperty;

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

		/*
		 * Initializing the title property to be set to Default Title.
		 */
		titleProperty = new SimpleStringProperty(DEFAULT_TITLE);
	}

	/**
	 * Returns true if the current MediaPlayer has a valid Media Source (one
	 * which has loaded and is non null).
	 */
	@Override
	public boolean hasMedia() {
		return mediaProperty.getValue();
	}

	/**
	 * A Boolean Property whose state is True if and only if the current
	 * MediaPlayer has a valid Media Source, which is loaded and ready to use.
	 */
	@Override
	public BooleanProperty hasMediaProperty() {
		return mediaProperty;
	}

	/**
	 * Returns the MediaView for this Model.
	 */
	@Override
	public MediaView getMediaView() {
		return view;
	}

	/**
	 * Returns the current MediaPlayer. If there is no valid Media (i.e.
	 * hasMedia returns false), this method will return null.
	 */
	@Override
	public MediaPlayer getMediaPlayer() {
		return getMediaView().getMediaPlayer();
	}

	/**
	 * Returns the current Media, loaded into the MediaPlayer.
	 * 
	 * @throws NullPointerException
	 *             In the case where hasMedia() returns false.
	 */
	@Override
	public Media getMedia() {
		return getMediaPlayer().getMedia();
	}

	/**
	 * Returns the current Duration of the loaded Media.
	 * 
	 * @throws NullPointerException
	 *             In the case where hasMedia() returns false.
	 */
	@Override
	public Duration getDuration() {
		return getMedia().getDuration();
	}

	/**
	 * A property representing the current time of the loaded Media.
	 * 
	 * @throws NullPointerException
	 *             In the case where hasMedia() returns false.
	 */
	@Override
	public ReadOnlyObjectProperty<Duration> getCurrentTimeProperty() {
		return getMediaPlayer().currentTimeProperty();
	}

	/**
	 * A property representing the current volume of the loaded Media.
	 * 
	 * @throws NullPointerException
	 *             In the case where hasMedia() returns false.
	 */
	@Override
	public DoubleProperty getVolumeProperty() {
		return getMediaPlayer().volumeProperty();
	}
	
	/**
	 * Returns the current color scheme.
	 */
	@Override
	public SkinColor getCurrentSkinColor() {
		return skinProperty.getValue();
	}

	/**
	 * A property representing the current color scheme.
	 */
	@Override
	public ObjectProperty<SkinColor> getCurrentSkinColorProperty() {
		return skinProperty;
	}

	/**
	 * Returns the title of the MainStage window.
	 */
	@Override
	public String getTitle() {
		return titleProperty.getValue();
	}
	
	/**
	 * A property representing the current
	 */
	@Override
	public StringProperty getTitleProperty() {
		return titleProperty;
	}

}
