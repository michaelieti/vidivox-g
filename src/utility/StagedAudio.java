package utility;

import java.io.File;
import java.io.IOException;

public class StagedAudio extends StagedMedia {

	/**
	 * Creates an Empty wav file using the linux command 'rec'.
	 */
	@Override
	protected void createFile() {
		String[] cmd = {"bash", "-c", "rec", file.getAbsolutePath(), "trim", "0", "0"};
		ProcessBuilder build = new ProcessBuilder(cmd);
		try {
			Process p = build.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO:This creates the file, but there is no way of checking if it was successful or not	
	}
	
	/**
	 * Required by the StagedMedia constructor
	 */
	@Override
	protected String getExtension() {
		return ".wav";
	}

}
