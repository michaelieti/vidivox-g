package player;

import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;

/**
 * The panel holding the media player.
 * 
 */
public class VidivoxMedia extends StackPane {
	private MediaView mv;
	
	
	public VidivoxMedia() {
		super();
		this.setMinHeight(500);
		this.setMinWidth(800);
		mv = new MediaView();
		this.getChildren().add(mv);
	}
}

