package player;

import java.io.File;

import editor.EditPanel;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import overlay.OverlayPanel;

/**
 * This class will act as the controller class for the application
 * 
 * @author adav194
 * 
 */
public class Main extends Application {

	/* Developer-configurable fields for application */
	public static final boolean GRID_IS_VISIBLE = false;
	public static final String DEFAULT_TITLE = "Vidivox V1";

	private MainStage ms;
	private EditPanel editorPanel;
	private OverlayPanel overlayPanel;
	
	public static void main(String[] args) throws Exception {
		/*Checks whether the temporary already exists. Cleans the directory if it does, and creates a new one */
		File p = new File(System.getProperty("user.dir") + "/.temp");
		int i = 0;
		while (p.exists()) {
			System.out.println(p.getCanonicalPath() + " already exists.");
			p = new File(System.getProperty("user.dir") + "/.temp" + i);
			System.out.println("New temp dir at "+ p.getCanonicalPath());
			i++;
		}
		if (!p.mkdirs()) {
			throw new Exception("Unable to create temporary directory at '" + p.getCanonicalPath() + "'.");
		}
		
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ms = new MainStage(this);
		editorPanel = new EditPanel(ms.getMediaPane().getMediaView());
		overlayPanel = new OverlayPanel();
		
		primaryStage = ms;
		
		/* SCREEN POSITIONAL SET UP */
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		editorPanel.setX(screenBounds.getMinX());
		editorPanel.setY(screenBounds.getMaxY() / 2 - 200);
		
		overlayPanel.setX(screenBounds.getMaxX() - 250);
		overlayPanel.setY(screenBounds.getMaxY() / 2 - 200);
		
		primaryStage.show();
		editorPanel.show();
		overlayPanel.show();
	}

	public EditPanel getEditor() {
		return editorPanel;
	}

	public OverlayPanel getOverlay() {
		return overlayPanel;
	}

	public MediaPanel getView() {
		return ms.getMediaPane();
	}

}
