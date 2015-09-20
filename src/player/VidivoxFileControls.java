package player;

import java.io.File;
import java.net.URI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private Button openFileBtn, saveFileBtn;
	private File currentFile = null;
	
	protected final FileChooser fileChooser = new FileChooser();
	
	public VidivoxFileControls(final MainStage ms, MediaView mv) {
		super();
		mediaView = mv;
		openFileBtn = new Button("Open file");
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
					} catch (MediaException me){	//this is pretty hax tbh TODO: come up with a better way to do this
						String message = me.getMessage().trim();
						if (message.equals("MEDIA_UNSUPPORTED : Unrecognized file signature!")){
						//	mediaPath = MediaFormatter.transformMedia(mediaPath, MediaFormatter.MP4);	//TODO: reformats the media at mediaPath and provides a new URI.
							media = new Media(mediaPath.toString());
						} else {
							throw me;	//lol
						}
					}
					System.out.println(mediaPath.toString());
					if (mediaView.getMediaPlayer() != null){	//if a media object is already playing...
						mediaView.getMediaPlayer().stop();		//stop the media player
						mediaView.getMediaPlayer().dispose();	//dispose of the media player
						System.out.println("Called mp disposal method");
					}
					mediaView.setMediaPlayer(new MediaPlayer(media));	//then, set a new mediaPlayer object with
					//sets video to play automatically					  the associated video to play.
					mediaView.getMediaPlayer().setAutoPlay(isAutoPlayEnabled);
				}
			}
			
		});
		saveFileBtn = new Button("Save file");
		saveFileBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				//Placeholder
			}
		});
		this.getChildren().addAll(openFileBtn, saveFileBtn);
	}
}
