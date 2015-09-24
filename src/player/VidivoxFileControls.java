package player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
public class VidivoxFileControls extends HBox {

	/* User-configurable fields for application */
	private boolean isAutoPlayEnabled = true;

	protected MenuBar skinMenu;
	protected Button openFileBtn, saveFileBtn, editPanelBtn;
	private File currentFile = null;
	private final FileChooser fileChooser = new FileChooser();

	public VidivoxFileControls(final MainStage ms, final VidivoxMedia vvm) {
		super();
		VidivoxPlayer.getVidivoxPlayer().setFilePanel(this);
		this.setId("fileControls");
		// OPEN FILE BUTTON STARTS HERE
		openFileBtn = new Button("Open file");
		openFileBtn.setId("fileBtns");
		openFileBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				System.out.println("File Opened Wow!");
				URI mediaPath = null;
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
					VidivoxPlayer vp = vvm.getPlayer();
					vp.setMedia(media);

					vp.getMediaPlayer().setAutoPlay(isAutoPlayEnabled);
				}
			}

		});

		// SAVE FILE BUTTON STARTS HERE
		saveFileBtn = new Button("Save file");
		saveFileBtn.setId("fileBtns");
		saveFileBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Media toSave = VidivoxPlayer.getVidivoxPlayer()
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
						VidivoxPlayer.getVidivoxPlayer().setMedia(newMedia);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		});

		editPanelBtn = new Button("Edit file");
		editPanelBtn.setId("fileBtns");
		editPanelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ms.getLauncher().getEditor().show();
			}

		});
		MenuItem blueSkin = new MenuItem("Blue");
		blueSkin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ms.getVideoControls().getStyleClass().remove("blue");
				ms.getVideoControls().getStyleClass().add("green");
			}
			
		});
		MenuItem greenSkin = new MenuItem("Green");
		blueSkin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ms.getVideoControls().getStyleClass().remove("green");
				ms.getVideoControls().getStyleClass().add("blue");
			}
			
		});
		MenuItem orangeSkin = new MenuItem("Orange");
		blueSkin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ms.getScene().getStylesheets().clear();
				ms.getScene().getStylesheets().add(getClass().getResource("/skins/OrangeSkin.css")
						.toExternalForm());
			}
			
		});
		MenuItem purpleSkin = new MenuItem("Purple");
		blueSkin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ms.getScene().getStylesheets().clear();
				ms.getScene().getStylesheets().add(getClass().getResource("/skins/PurpleSkin.css")
						.toExternalForm());
			}
			
		});
		
		skinMenu = new MenuBar();
		Menu selectSkin = new Menu("Select Skin");
		skinMenu.getMenus().addAll(selectSkin);
		selectSkin.getItems().addAll(blueSkin, greenSkin, orangeSkin, purpleSkin);
		this.setSpacing(8.0);
		this.getChildren().addAll(openFileBtn, saveFileBtn, editPanelBtn, skinMenu);
	}
}
