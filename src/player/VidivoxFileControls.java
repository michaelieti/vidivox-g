package player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import editor.EditPanel;

import player.MainStage.SkinColor;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

/**
 * The panel containing the file controls and other miscellaneous control items.
 * 
 */
public class VidivoxFileControls extends MenuBar {

	/* User-configurable fields for application */
	private boolean isAutoPlayEnabled = true;

	protected MenuBar skinMenu;
	private final FileChooser fileChooser = new FileChooser();

	/**
	 * This is a the top level Menu containers. Use Menu.getItems() to access
	 * the children of this node rather than getChildren().
	 */
	protected Menu fileMenu, windowMenu, custMenu;
	/**
	 * This node belongs to the Menu container. In principle it has the same
	 * features as a button.
	 */
	protected MenuItem open, save;
	/**
	 * A CheckMenuItem which represents whether a window is visible.
	 */
	protected CheckMenuItem edittor, overlay;
	/**
	 * A CheckMenuItem which represents whether a skin is currently being used.
	 */
	protected CheckMenuItem blueSkin, orangeSkin, purpleSkin, greenSkin;
	private final MainStage ms;
	
	
	public VidivoxFileControls(final MainStage ms, final MediaPanel vvm) {
		super();
		this.ms = ms;
		VidivoxPlayer.getVPlayer().setFilePanel(this);
		this.setId("fileControls");
		// OPEN FILE BUTTON STARTS HERE

		fileMenu = new Menu("File");
		fileMenu.setId("fileMenu");
		
		windowMenu = new Menu("Window");
		windowMenu.setId("fileMenu");
		/*
		 * This checks which windows are open before showing the menu. This particular implementation is inflexible.
		 */
		windowMenu.setOnShowing(new WindowMenuHandler(ms));
		
		custMenu = new Menu("Customize");
		custMenu.setId("fileMenu");
		//TODO: Consider similar structures for this menu

		open = new MenuItem("Open file");
		open.setId("fileBtns");
		open.setOnAction(new OpenFileHandler());
		
		// SAVE FILE BUTTON STARTS HERE
		save = new MenuItem("Save file");
		save.setId("fileBtns");
		save.setOnAction(new SaveFileHandler());
		
		edittor = new CheckMenuItem("Editing Panel");
		edittor.setId("fileBtns");
		edittor.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (edittor.isSelected()) {
					ms.getLauncher().getEditor().show();
				} else {
					ms.getLauncher().getEditor().hide();
				}
			}

		});

		overlay = new CheckMenuItem("Overlay Panel");
		overlay.setId("fileBtns");
		overlay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (overlay.isSelected()) {
					ms.getLauncher().getOverlay().show();
				} else {
					ms.getLauncher().getOverlay().hide();
				}
			}

		});

		blueSkin = new CheckMenuItem("Blue");
		blueSkin.setId("fileBtns");
		blueSkin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ms.changeSkin(MainStage.SkinColor.BLUE);
				// Insures that only one skin is shown as selected at a time.
				for (MenuItem m : custMenu.getItems()) {
					if (!m.equals(blueSkin)) {
						((CheckMenuItem) (m)).setSelected(false);
					}
				}
			}

		});
		blueSkin.setSelected(ms.getCurrentSkinColor().equals(
				MainStage.SkinColor.BLUE));

		greenSkin = new CheckMenuItem("Green");
		greenSkin.setId("fileBtns");
		greenSkin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ms.changeSkin(MainStage.SkinColor.GREEN);
				for (MenuItem m : custMenu.getItems()) {
					if (!m.equals(greenSkin)) {
						((CheckMenuItem) (m)).setSelected(false);
					}
				}
			}

		});
		greenSkin.setSelected(ms.getCurrentSkinColor().equals(
				MainStage.SkinColor.GREEN));
		orangeSkin = new CheckMenuItem("Orange");
		orangeSkin.setId("fileBtns");
		orangeSkin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ms.changeSkin(MainStage.SkinColor.ORANGE);
				for (MenuItem m : custMenu.getItems()) {
					if (!m.equals(orangeSkin)) {
						((CheckMenuItem) (m)).setSelected(false);
					}
				}
			}

		});
		orangeSkin.setSelected(ms.getCurrentSkinColor().equals(
				MainStage.SkinColor.ORANGE));
		purpleSkin = new CheckMenuItem("Purple");
		purpleSkin.setId("fileBtns");
		purpleSkin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ms.changeSkin(MainStage.SkinColor.PURPLE);
				for (MenuItem m : custMenu.getItems()) {
					if (!m.equals(purpleSkin)) {
						((CheckMenuItem) (m)).setSelected(false);
					}
				}
			}

		});
		purpleSkin.setSelected(ms.getCurrentSkinColor().equals(
				MainStage.SkinColor.PURPLE));

		
		fileMenu.getItems().addAll(open, save);
		windowMenu.getItems().addAll(edittor, overlay);
		custMenu.getItems().addAll(blueSkin, greenSkin, orangeSkin, purpleSkin);
		this.getMenus().addAll(fileMenu, windowMenu, custMenu);

	}
	
	/* handlers */
	
	private class WindowMenuHandler implements EventHandler<Event> {
		
		MainStage ms;
		
		WindowMenuHandler(MainStage ms){
			super();
			this.ms = ms;
		}
		
		@Override
		public void handle(Event arg0) {
			for (MenuItem m : windowMenu.getItems()) {
				if (m.equals(edittor)) {
					((CheckMenuItem) m).setSelected(ms.getLauncher()
							.getEditor().isShowing());
				} else if (m.equals(overlay)) {
					((CheckMenuItem) m).setSelected(ms.getLauncher()
							.getOverlay().isShowing());
				}
			}
		}
	}

	private class OpenFileHandler implements EventHandler<ActionEvent> {
		
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
				VidivoxPlayer vp = VidivoxPlayer.getVPlayer();
				vp.setMedia(media);
				vp.setOriginalMedia(media);
				//sets the original media for later use in overlay

				vp.getMediaPlayer().setAutoPlay(isAutoPlayEnabled);
			}
		}

	}

	private class SaveFileHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			Media toSave = VidivoxPlayer.getVPlayer()
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
					VidivoxPlayer.getVPlayer().setMedia(newMedia);
				} catch (IOException | InterruptedException e) {		
					e.printStackTrace();
				}

			}

		}
	}

	
	
}
