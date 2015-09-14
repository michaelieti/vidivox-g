package player;

import javafx.application.Application;
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
		String userInputString = null;
		//get user input as a string
		//parse user input into a URI object
		URI mediaPath = new URI(userInputString);
		VidivoxMedia vm = ms.getMediaPane().getMediaView();
		vm.setMediaPlayer = new MediaPlayer(new Media(mediaPath));
		//TODO: test later
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
