package main.control.viewcomponents;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import overlay.control.OverlayController;
import player.Main;

/**
 * This class holds the overarching javafx GUI code for the main window.
 * Essentially create a click dummy.
 * @author michael
 *
 */
public class MainView extends Stage {
	
	FilePanel filePanel;
	PlayerPanel playerPanel;
	ControlPanel ctrlPanel;
	
	public MainView(){
		//initialize all components
		filePanel = new FilePanel();
		playerPanel = new PlayerPanel();
		ctrlPanel = new ControlPanel();
		
		this.setTitle(Main.DEFAULT_TITLE);
		
		//set up a new borderpane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(0, 0, 15, 0));
		grid.setGridLinesVisible(Main.GRID_IS_VISIBLE);

		//add all components to border pane.
		grid.add(filePanel, 0, 0);
		grid.add(playerPanel, 0, 1);
		grid.add(ctrlPanel, 0, 2);
		
		Scene scene = new Scene(grid);
		
		//add the style sheet
		scene.getStylesheets().add(
			getClass().getResource("/skins/BlueSkin.css").toExternalForm()
		);
		
		//set on close/on open
		setOnCloseRequest(new TempCleanHandler());
		iconifiedProperty().addListener(new IconifiedListener());		
		//finally - set the scene
		setScene(scene);
		
	}
	
	public FilePanel getFilePanel(){
		return filePanel;
	}

	public PlayerPanel getPlayerPanel() {
		return playerPanel;
	}

	public ControlPanel getCtrlPanel() {
		return ctrlPanel;
	}
	
	
	/**
	 * Cleans up the temporary folder for the next launch.
	 */
	private class TempCleanHandler implements EventHandler<WindowEvent> {
		@Override
		public void handle(WindowEvent event) {		
			File p = new File(System.getProperty("user.dir") + "/.temp/");
			if (p.exists() && p.isDirectory()) {
				for (File f : p.listFiles()) {
					f.delete();
				}
			}
			p.delete();
			Platform.exit();
		}
	}
	
	private class IconifiedListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> ov,
				Boolean oldValue, Boolean newValue) {
			if (!newValue) {
				EditorController.getController().iconify(false);
				OverlayController.getController().iconify(false);
				MainController.getController.iconify(false);
			} else {
				EditorController.getController().iconify(true);
				OverlayController.getController().iconify(true);
				
			}
		}
	});

	
}
