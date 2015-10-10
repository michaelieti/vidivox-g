package player;

import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

/**
 * The panel holding the media player.
 * 
 */
public class MediaPanel extends VBox {
	protected VidivoxPlayer vp = VidivoxPlayer.getVidivoxPlayer();


	public MediaPanel() {
		super();
		vp.setMediaPanel(this);
		MediaView mv = vp.getMediaView();
		mv.setFitHeight(500);
		mv.setFitWidth(800);
		this.getChildren().add(mv);
	}

	public MediaView getMediaView() {
		return vp.getMediaView();
	}

	public VidivoxPlayer getPlayer() {
		return vp;
	}

	

}
