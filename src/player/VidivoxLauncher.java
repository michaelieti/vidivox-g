package player;

public class VidivoxLauncher {
	
	private static MainScreen ms;
	
	
	public static void main(String[] args) {
		
		//insert other initiation related stuff
		new VidivoxLauncher(args);
	}
	
	private VidivoxLauncher(String[] args){
		ms = new MainScreen();
		ms.initiate(args, this);
	}

}
