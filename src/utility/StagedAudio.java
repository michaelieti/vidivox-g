package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.scene.media.Media;

public class StagedAudio extends StagedMedia {

	private String ext;

	/**
	 * Currently unnecessary. The purpose of this enum is to specify the
	 * formatting type of the audio when it is initialized.
	 * 
	 * @author adav194
	 * 
	 */
	public enum MediaTypes {
		MP3(".mp3"), WAV(".wav");
		private String type;

		private MediaTypes(String type) {
			this.type = type;
		}

		public String getExtension() {
			return type;
		}
	}

	/**
	 * Use this constructor if you wish to create a new StagedAudio container to
	 * store audio not yet made. You must select the type of audio which will be
	 * stored in this container.
	 * 
	 * @param type
	 */
	public StagedAudio(MediaTypes type) {
		ext = type.getExtension();
		InitMedia();
	}

	/**
	 * Use this constructor if you wish to create a new StagedAudio container to
	 * store audio from a specific location.
	 * 
	 * @param type
	 * @param location
	 */
	public StagedAudio(MediaTypes type, File location) {
		file = location;
		ext = type.getExtension();
		createFile();
	}

	/**
	 * Use this constructor if you wish to create a StagedAudio from Media which
	 * already exists. If you do not know what the extension is it is ok to use
	 * an empty string.
	 * 
	 * @param audio
	 * @param extension
	 */
	public StagedAudio(Media audio, String extension) {
		media = audio;
		file = new File(audio.getSource());
		ext = extension;
	}

	/**
	 * Creates an Empty wav file using the linux command 'rec'. Does not work
	 * for mp3
	 */
	@Override
	protected void createFile() {
		String[] cmd = { "rec", file.getAbsolutePath(), "trim",
				"0", "0" };
		String l = "";
		for (String s:cmd) {
			l += " " +s;
		}
		System.out.println(l);
		ProcessBuilder build = new ProcessBuilder(cmd);
		build.redirectErrorStream(true);
		try {
			Process p =	build.start();
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = b.readLine()) != null) {
				System.out.println(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Required by the StagedMedia constructor
	 */
	@Override
	protected String getExtension() {
		return ext;
	}

}
