package player;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import editor.EditPanel;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class will act as the controller class for the application
 * @author adav194
 *
 */
public class VidivoxLauncher extends Application {
	
	/*Developer-configurable fields for application*/
	public static final boolean GRID_IS_VISIBLE = false;
	public static final String DEFAULT_TITLE = "Vidivox V1";
	
	/*User-configurable fields for application */
	private boolean isAutoPlayEnabled = true;
	
	/*Status flags for the Application*/
	private boolean mediaEnded = false;
	
	protected final FileChooser fileChooser = new FileChooser();
	private Stage ms, editorPanel;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ms = new MainStage(this);
		editorPanel = new EditPanel();
		primaryStage = ms;
		primaryStage.show();
		editorPanel.show();
	}

}
