package main.control;

import main.MainView;
import main.model.MainModelable;
import skins.SkinColor;
import javafx.beans.property.BooleanProperty;
import javafx.scene.media.Media;
import javafx.util.Duration;

public interface MainControllable {

	public void setMedia(Media media);
	
	public void setAutoPlay(boolean auto);
	
	public void play();
	
	public void ffwd();
	
	public void rwd();
	
	public void stop();

	public void setTime(Duration time);
	
	public void setVolume(double vol);
	
	public void setMute(boolean mute);
	
	public void setSkinColor(SkinColor color);
	
	public BooleanProperty hasMediaProperty();
	
	public void setModel(MainModelable model);
	
	public void setView(MainView view);
	
	public boolean initialize();

}
