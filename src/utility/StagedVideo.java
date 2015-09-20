package utility;

import java.io.IOException;

public class StagedVideo extends StagedMedia {
	
	/**
	 * Currently Does not work
	 */
	@Override
	protected void createFile() {
		//ffmpeg -i sample.avi -f mp4 -strict -2 -c:v libx264 -t 0 out2.mp4

		String[] cmd = {"bash", "-c", "ffmpeg", "-i", file.getAbsolutePath()}; 
		ProcessBuilder build = new ProcessBuilder(cmd);
		try {
			build.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getExtension() {
		return ".mp4";
	}

}
