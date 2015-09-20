package utility;

import java.io.IOException;

import javafx.scene.media.Media;

public class StagedVideo extends StagedMedia {

	/**
	 * The original Media object from which this video is staged from
	 */
	private Media original;
	
	public StagedVideo(Media origin) {
		super();
		original = origin;
	}
	
	/**
	 * Currently Does not work
	 */
	@Override
	protected void createFile() {
		//ffmpeg -i sample.avi -f mp4 -strict -2 -c:v libx264 -t 0 out2.mp4

		String[] cmd = {"bash", "-c", "ffmpeg", "-i", file.getAbsolutePath()}; 
		ProcessBuilder build = new ProcessBuilder(cmd);
		try {
			Process p = build.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getExtension() {
		return ".mp4";
	}

}
