package player;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class will act as the controller class for the application
 * @author adav194
 *
 */
public class VidivoxLauncher extends Application {
	
	/*Configurable Fields for Application*/
	public static final boolean GRID_IS_VISIBLE = true;
	public static final String DEFAULT_TITLE = "Vidivox V1";
	
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

	/*
	 * Placeholder Functions
	 */
	public void openFile() {
		System.out.println("File Opened Wow!");
		URI mediaPath = null;
		File file = fileChooser.showOpenDialog(ms);
		if (file != null){
			mediaPath = file.toURI();	//converts to URI object
			//next step retrieves the MediaView object from the main stage
			MediaView mediaView = ((MainStage)ms).getMediaPane().getMediaView();
			Media media = null;
			try{
				media = new Media(mediaPath.toString());
			} catch (MediaException me){	//this is pretty hax tbh TODO: come up with a better way to do this
				String message = me.getMessage().trim();
				if (message.equals("MEDIA_UNSUPPORTED : Unrecognized file signature!")){
				//	mediaPath = MediaFormatter.transformMedia(mediaPath, MediaFormatter.MP4);	//reformats the media at mediaPath and provides a new URI.
					media = new Media(mediaPath.toString());
				} else {
					throw me;	//lol
				}
			}
			System.out.println(mediaPath.toString());
			//need to check media to make sure it is flv or mp4 or whatever
			//and then assigns a new MediaPlayer with a new Media to that MediaView 
			//object, but with the new file.
			mediaView.setMediaPlayer(new MediaPlayer(media));
			//sets video to play automatically
			mediaView.getMediaPlayer().setAutoPlay(true);
		}
	}
	
	public void saveFile() {
		System.out.println("Save this shit yo");
	}

	public void playVideo() {
		System.out.println("Pressed Play Wow!");
		
	}

	public void pauseVideo() {
		System.out.println("Pressed Pause Wow!");
		
	}

	public void stopVideo() {
		System.out.println("Pressed Stop Wow!");
		
	}

	public void ffwdVideo() {
		System.out.println("Pressed Fast Foward Wow!");
		
	}

	public void rwdVideo() {
		System.out.println("Pressed Rewind Wow!");
		
	}
	
	public void speech() {
		System.out.println("Pressed Speech");
	}

	public void sub() {
		System.out.println("Pressed Sub");
	}
	
	public void mp3() {
		System.out.println("Pressed mp3");
	}
}
