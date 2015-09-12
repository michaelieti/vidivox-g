package player;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class will act as the controller class for the application
 * @author adav194
 *
 */
public class VidivoxLauncher extends Application {
	
	public static final boolean GRID_IS_VISIBLE = true;
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
	public static void openFile() {
		System.out.println("File Opened Wow!");
	}

	public static void playVideo() {
		System.out.println("Pressed Play Wow!");
		
	}

	public static void pauseVideo() {
		System.out.println("Pressed Pause Wow!");
		
	}

	public static void stopVideo() {
		System.out.println("Pressed Stop Wow!");
		
	}

	public static void ffwdVideo() {
		System.out.println("Pressed Fast Foward Wow!");
		
	}

	public static void rwdVideo() {
		System.out.println("Pressed Rewind Wow!");
		
	}


}
