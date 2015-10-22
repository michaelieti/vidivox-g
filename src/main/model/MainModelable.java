package main.model;

import skins.SkinColor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public interface MainModelable {
	
	public BooleanProperty hasMedia();
	
	public Media getMedia();
	
	public MediaPlayer getMediaPlayer();
	
	public MediaView getMediaView();
	
	public Duration getDuration();
	
	public ReadOnlyObjectProperty<Duration> getCurrentTimeProperty();
	
	public DoubleProperty getVolumeProperty();
	
	public SkinColor getCurrentSkinColor();

}
