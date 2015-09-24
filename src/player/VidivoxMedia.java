package player;

import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

/**
 * The panel holding the media player.
 * 
 */
public class VidivoxMedia extends VBox {
	protected VidivoxPlayer vp = VidivoxPlayer.getVidivoxPlayer();
	protected SliderVX mediaTimeline;

	protected static final double MINTIME = 0.0;
	protected static final double MAXTIME = 100.0;
	protected static final double STARTTIME = 0.0;

	public VidivoxMedia() {
		super();
		vp.setMediaPanel(this);
		MediaView mv = vp.getMediaView();
		mv.setFitHeight(500);
		mv.setFitWidth(800);
		mediaTimeline = new SliderVX(MINTIME, MAXTIME, STARTTIME);
		this.getChildren().addAll(mv, mediaTimeline);
	}

	public MediaView getMediaView() {
		return vp.getMediaView();
	}

	public VidivoxPlayer getPlayer() {
		return vp;
	}

	public SliderVX getTimeline() {
		return mediaTimeline;
	}

}
