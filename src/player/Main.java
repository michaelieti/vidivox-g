package player;

import java.io.File;

import main.control.MainController;
import main.control.MainControllable;
import main.model.MainModel;
import main.model.MainModelable;

import editor.EditPanel;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import overlay.control.OverlayController;
import overlay.control.OverlayView;
import overlay.model.OverlayModel;

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
	private OverlayView olView;
	
	public static void main(String[] args) throws Exception {
		/*Checks whether the temporary already exists. Cleans the directory if it does, and creates a new one */
		File p = new File(System.getProperty("user.dir") + "/.temp");
		if (p.exists()) {
			System.out.println("Temporary directory already exists");
			p.delete();
			System.out.println("Deleting (" + p.getCanonicalPath() + ")...");
		}
		if (!p.mkdir()) {
			throw new Exception("Unable to create temporary directory at '" + p.getCanonicalPath() + "'.");
		}
		
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainModelable model = new MainModel();
		MainControllable control = new MainController(model);
		ms = new MainStage(this,model, control);
		editorPanel = new EditPanel(ms.getMediaPane().getMediaView());
	
		/* sets up overlay panel*/
		OverlayModel olModel = new OverlayModel();	//initializes overlay data model
		OverlayController olController = new OverlayController(olModel);	//initializes the controller
		olView = new OverlayView();
		olController.setView(olView);	//sets the controller's associated view
		olController.initialize();		//initializes event handlers etc between view and model
		/* finish overlay set up*/
		
		primaryStage = ms;
		
		/* SCREEN POSITIONAL SET UP */
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		editorPanel.setX(screenBounds.getMinX());
		editorPanel.setY(screenBounds.getMaxY() / 2 - 200);
		
		olView.setX(screenBounds.getMaxX() - 250);
		olView.setY(screenBounds.getMaxY() / 2 - 200);
		
		primaryStage.show();
		editorPanel.show();
		olView.show();
	}

	public EditPanel getEditor() {
		return editorPanel;
	}

	public OverlayView getOverlay() {
		return olView;
	}

	public MediaPanel getView() {
		return ms.getMediaPane();
	}

}
