package main.control.viewcomponents;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import main.control.MainController;
import overlay.control.OverlayController;
import player.VidivoxPlayer;
import skins.SkinColor;

/**
 * This class holds the javafx code for the file control bar,
 * normally located at the top of the screen.
 * @author michael
 *
 */
public class FilePanel extends MenuBar{

	/**
	 * This is a the top level Menu containers. Use Menu.getItems() to access
	 * the children of this node rather than getChildren().
	 */
	protected Menu fileMenu, windowMenu, custMenu;
	protected MenuItem open, save;
	protected CheckMenuItem editor, overlay;
	protected CheckMenuItem blueSkin, orangeSkin, purpleSkin, greenSkin;
	
	/* refactor these fields out into control */
	FileChooser fileChooser;
	
	public FilePanel(){
		this.setId("File Controls");
		
		/* top level items */
		fileMenu = new Menu("File");
		fileMenu.setId("fileMenu");
		
		windowMenu = new Menu("Window");
		windowMenu.setId("fileMenu");
		//TODO: refactor into controller class later
		windowMenu.setOnShowing(new WindowCheckBoxHandler());
		
		custMenu = new Menu("Customize");
		custMenu.setId("fileMenu");
		
		/* file menu items: open file, save file */
		open = new MenuItem("Open file");
			open.setId("fileBtns");
			open.setOnAction(new OpenFileHandler());
		save = new MenuItem("Save file");
			save.setId("fileBtns");
			save.setOnAction(new SaveFileHandler());
		
		/* window menu items: editor panel, overlay panel */
			editor = new CheckMenuItem("Editing Panel");
			editor.setId("fileBtns");
			editor.setOnAction(new EditorOpenStatusHandler());		
			
			overlay = new CheckMenuItem("Overlay Panel");
			overlay.setId("fileBtns");
			overlay.setOnAction(new OverlayOpenStatusHandler());
			

			blueSkin = new CheckMenuItem("Blue");
			blueSkin.setId("fileBtns");
			blueSkin.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					control.setSkinColor(SkinColor.BLUE);
					// Insures that only one skin is shown as selected at a time.
					for (MenuItem m : custMenu.getItems()) {
						if (!m.equals(blueSkin)) {
							((CheckMenuItem) (m)).setSelected(false);
						}
					}
				}

			});
			blueSkin.setSelected(ms.getCurrentSkinColor().equals(
					SkinColor.BLUE));

			greenSkin = new CheckMenuItem("Green");
			greenSkin.setId("fileBtns");
			greenSkin.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					control.setSkinColor(SkinColor.GREEN);
					for (MenuItem m : custMenu.getItems()) {
						if (!m.equals(greenSkin)) {
							((CheckMenuItem) (m)).setSelected(false);
						}
					}
				}

			});
			greenSkin.setSelected(ms.getCurrentSkinColor().equals(
					SkinColor.GREEN));
			orangeSkin = new CheckMenuItem("Orange");
			orangeSkin.setId("fileBtns");
			orangeSkin.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					control.setSkinColor(SkinColor.ORANGE);
					for (MenuItem m : custMenu.getItems()) {
						if (!m.equals(orangeSkin)) {
							((CheckMenuItem) (m)).setSelected(false);
						}
					}
				}

			});
			orangeSkin.setSelected(ms.getCurrentSkinColor().equals(
					SkinColor.ORANGE));
			purpleSkin = new CheckMenuItem("Purple");
			purpleSkin.setId("fileBtns");
			purpleSkin.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					control.setSkinColor(SkinColor.PURPLE);
					for (MenuItem m : custMenu.getItems()) {
						if (!m.equals(purpleSkin)) {
							((CheckMenuItem) (m)).setSelected(false);
						}
					}
				}

			});
			purpleSkin.setSelected(ms.getCurrentSkinColor().equals(
					SkinColor.PURPLE));

			
	}
	
	/* refactor into controller class later */
	public class WindowCheckBoxHandler implements EventHandler<Event> {

		@Override
		public void handle(Event arg0) {
			for (MenuItem m : windowMenu.getItems()) {
				if (m.equals(editor)) {
					((CheckMenuItem) m).setSelected(EditorController.getController().isShowing());
				} else if (m.equals(overlay)) {
					((CheckMenuItem) m).setSelected(OverlayController.getController().isShowing());
				}
			}
		}
	}
	
	public class OpenFileHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			System.out.println("File Opened Wow!");
			URI mediaPath = null;
			File currentFile = null;
			currentFile = fileChooser.showOpenDialog(ms);
			if (currentFile != null) {
				mediaPath = currentFile.toURI(); // converts to URI object
				// next step retrieves the MediaView object from the main
				// stage
				Media media = null;
				try {
					media = new Media(mediaPath.toString());
				} catch (Exception me) { // this is pretty hax tbh TODO:
											// come up with a better way to
											// do this
					me.printStackTrace();
					System.out.println("wrong file format");
				}
				// file check!
				System.out.println(mediaPath.toString());
				// get the wrapper class, set the media
				control.setMedia(media);
				control.setAutoPlay(isAutoPlayEnabled);
			}
		}

	}

	public class SaveFileHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			Media toSave = VidivoxPlayer.getVPlayer(null)
					.getMediaPlayer().getMedia();
			FileChooser f = new FileChooser();
			f.setTitle("Save");
			File file = new FileChooser().showSaveDialog(null);
			if (file != null) {
				System.out.println(toSave.getSource());
				URI u = null;
				try {
					u = new URI(toSave.getSource());
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
				String expansion = "mv " + u.getPath() + " "
						+ file.getAbsolutePath();
				String[] cmd = { "bash", "-c", expansion };
				ProcessBuilder build = new ProcessBuilder(cmd);
				build.redirectErrorStream(true);
				Process p = null;
				try {
					p = build.start();
					BufferedReader bout = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = bout.readLine()) != null) {
						System.out.println(line);
					}
					p.waitFor();
					System.out.println(file.toURI().toString());
					Media newMedia = new Media(file.toURI().toString());
					VidivoxPlayer.getVPlayer(null).setMedia(newMedia);
				} catch (IOException | InterruptedException e) {		
					e.printStackTrace();
				}

			}

		}
	}

	public class EditorOpenStatusHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			EditorController.getController().setVisible(
					editor.isSelected());
		}

	}

	public class OverlayOpenStatusHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
				OverlayController.getController().setVisible(
						overlay.isSelected());
		}
	}

	public class SingularSelectionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			MainController.getController().setSkinColor(SkinColor.BLUE);
			// Insures that only one skin is shown as selected at a time.
			for (MenuItem m : custMenu.getItems()) {
				if (!m.equals(blueSkin)) {
					((CheckMenuItem) (m)).setSelected(false);
				}
			}
		}

	});
}
