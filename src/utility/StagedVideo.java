package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class StagedVideo extends StagedMedia {
	private static File emptymp4 = new File("empty.mp4");

	public StagedVideo() {
		InitMedia();
	}

	/**
	 * Currently Does not work
	 */
	@Override
	protected void createFile() {
		// ffmpeg -i sample.avi -f mp4 -strict -2 -c:v libx264 -t 0 out2.mp4
		System.out.println("empty: " + emptymp4.exists() + "other: "
				+ file.exists());
		String expression = "cp " + emptymp4.getAbsolutePath() + " "
				+ file.getAbsolutePath();
		String[] cmd = { "/bin/bash", "-c", expression };
		ProcessBuilder build = new ProcessBuilder(cmd);
		try {
			build.redirectErrorStream(true);
			Process p = build.start();
			BufferedReader b = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = b.readLine()) != null) {
				System.out.println("CMD>: " + line);
			}
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getExtension() {
		return ".mp4";
	}

}
