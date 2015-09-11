package player;

/**
 * This class will act as the controller class for the application
 * @author adav194
 *
 */
public class VidivoxLauncher {
	
	private static MainScreen ms;
	
	
	public static void main(String[] args) {
		initialize(args);
		
	}
	
	private static void initialize(String[] args) {
		ms = new MainScreen();
		ms.initiate(args);
	}

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
