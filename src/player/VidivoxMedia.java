package player;

import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;

public class VidivoxMedia extends StackPane {
	private MediaView mv;
	
	public VidivoxMedia() {
		super();
		this.setMinHeight(800);
		this.setMinWidth(1000);
		mv = new MediaView();
		this.getChildren().add(mv);
	}
}

