package player;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.scene.media.Media;
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
	private Stage ms;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ms = new MainStage(this);
		primaryStage = ms;
		primaryStage.show();
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
			//basically, retrieves the MediaView object from the main stage
			MediaView mediaView = ((MainStage)ms).getMediaPane().getMediaView();
			//and then assigns a new MediaPlayer with a new Media to that MediaView 
			//object, but with the new file.
			mediaView.setMediaPlayer(new MediaPlayer(new Media(mediaPath.toString())));
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


}
