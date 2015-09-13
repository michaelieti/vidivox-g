package player;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * The panel containing the file controls and other miscellaneous control items.
 *
 */
public class VidivoxFileControls extends HBox {
	
	private VidivoxLauncher launcher;
	private Button openFileBtn;
	
	public VidivoxFileControls(VidivoxLauncher vl) {
		super();
		launcher = vl;
		openFileBtn = new Button("Open file");
		openFileBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				launcher.openFile();
			}
			
		});
		this.getChildren().add(openFileBtn);
	}
}
