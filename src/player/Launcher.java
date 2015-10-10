package player;

import editor.EditPanel;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class will act as the controller class for the application
 * 
 * @author adav194
 * 
 */
public class Launcher extends Application {

	/* Developer-configurable fields for application */
	public static final boolean GRID_IS_VISIBLE = false;
	public static final String DEFAULT_TITLE = "Vidivox V1";

	private MainStage ms;
	private EditPanel editorPanel;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ms = new MainStage(this);
		editorPanel = new EditPanel(ms.getMediaPane().getMediaView());
		primaryStage = ms;
		primaryStage.show();
		editorPanel.show();
	}

	public EditPanel getEditor() {
		return editorPanel;
	}

	public MediaPanel getView() {
		return ms.getMediaPane();
	}

}
