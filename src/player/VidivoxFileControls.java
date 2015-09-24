package player;

import java.io.File;
import java.net.URI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;

/**
 * The panel containing the file controls and other miscellaneous control items.
 * 
 */
public class VidivoxFileControls extends HBox {

	/* User-configurable fields for application */
	private boolean isAutoPlayEnabled = true;

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
				// Placeholder
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
		this.setSpacing(8.0);
		this.getChildren().addAll(openFileBtn, saveFileBtn, editPanelBtn);
	}
}
