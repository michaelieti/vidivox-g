package player;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

/**
 * The panel containing the file controls and other miscellaneous control items.
 *
 */
public class VidivoxFileControls extends HBox {
	
	/*User-configurable fields for application */
	private boolean isAutoPlayEnabled = true;
	
	private MediaView mediaView;
	private Button openFileBtn, saveFileBtn, editPanelBtn;
	private File currentFile = null;
	
	protected final FileChooser fileChooser = new FileChooser();
	
	public VidivoxFileControls(final MainStage ms, MediaView mv) {
		super();
		this.setId("fileControls");
		
		mediaView = mv;
		
		//OPEN FILE BUTTON STARTS HERE
		openFileBtn = new Button("Open file");
		openFileBtn.setId("fileBtns");
		openFileBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				System.out.println("File Opened Wow!");
				URI mediaPath = null;
				currentFile = fileChooser.showOpenDialog(ms);
				if (currentFile != null) {
					mediaPath = currentFile.toURI();	//converts to URI object
					//next step retrieves the MediaView object from the main stage
					Media media = null;
					try{
						media = new Media(mediaPath.toString());
					} catch (Exception me){	//this is pretty hax tbh TODO: come up with a better way to do this
						String message = me.getMessage().trim();
						if (message.equals("Unrecognized file signature!")){
							try {	//second try
								media = editor.MediaConverter.convertToMP4(mediaPath.getPath(), mediaPath.getPath());
							} catch (InterruptedException | ExecutionException e) {
								System.out.println("wow u really screwed up");
								e.printStackTrace();
							}	//end second try	
							//TODO: reformats the media at mediaPath and provides a new URI.
							media = new Media(mediaPath.toString());
						} else {
							throw me;	//lol
						}
					}
					System.out.println(mediaPath.toString());
					if (mediaView.getMediaPlayer() != null){
						mediaView.getMediaPlayer().stop();
						mediaView.getMediaPlayer().dispose();
						System.out.println("Called mp disposal method");
					}
					//need to check media to make sure it is flv or mp4 or whatever
					//and then assigns a new MediaPlayer with a new Media to that MediaView 
					//object, but with the new file.
					mediaView.setMediaPlayer(new MediaPlayer(media));
					//sets video to play automatically
					mediaView.getMediaPlayer().setAutoPlay(isAutoPlayEnabled);
				}
			}
			
		});
		
		//SAVE FILE BUTTON STARTS HERE
		saveFileBtn = new Button("Save file");
		saveFileBtn.setId("fileBtns");
		saveFileBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				//Placeholder
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
