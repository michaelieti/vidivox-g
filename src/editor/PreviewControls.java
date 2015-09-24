package editor;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class PreviewControls extends HBox {

	protected Button rewind, ffwd;
	protected Slider volumeSlider;
	
	final private static double MINVOL = 0.0;
	final private static double MAXVOL = 100.0 ;
	final private static double DEFAULTVOL = 50.0;
	
	public PreviewControls(){
		rewind = new Button("<<");
			rewind.setId("prev-rewind");
		ffwd = new Button(">>");
			ffwd.setId("prev-ffwd");
		volumeSlider = new Slider(MINVOL, MAXVOL, DEFAULTVOL);
			volumeSlider.setId("prev-vol");
		this.getChildren().addAll(rewind, ffwd, volumeSlider);
	}
	
}
