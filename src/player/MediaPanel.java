package player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

/**
 * The panel holding the media player.
 * 
 */
public class MediaPanel extends VBox {
	protected VidivoxPlayer vp = VidivoxPlayer.getVPlayer();


	public MediaPanel() {
		super();
		vp.setMediaPanel(this);
		MediaView mv = vp.getMediaView();
		setAlignment(Pos.CENTER);
		mv.setFitHeight(500);
		mv.setFitWidth(800);
		VBox.setMargin(mv, new Insets(0,8,0,8));
		this.getChildren().add(mv);
	}

	public MediaView getMediaView() {
		return vp.getMediaView();
	}

	public VidivoxPlayer getPlayer() {
		return vp;
	}

	

}
