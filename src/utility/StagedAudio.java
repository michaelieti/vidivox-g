package utility;

import java.io.File;
import java.io.IOException;

import javafx.scene.media.Media;

public class StagedAudio extends StagedMedia {

	public enum MediaTypes {
		MP3(".mp3"), WAV(".wav"), NONE("");
		private String type;

		private MediaTypes(String type) {
			this.type = type;
		}

		public String getExtension() {
			return type;
		}
	}

	/**
	 * The format of this particular instance of StagedMedia. Valid formats
	 * currently include: mp3, wav.
	 */
	private MediaTypes format;

	public StagedAudio(MediaTypes type) {
		format = type;
		InitMedia();
	}

	public StagedAudio(Media audio) {
		media = audio;
		file = new File(audio.getSource());
		format = MediaTypes.NONE;
	}

	/**
	 * Creates an Empty wav file using the linux command 'rec'. Does not work
	 * for mp3
	 */
	@Override
	protected void createFile() {
		String[] cmd = { "bash", "-c", "rec", file.getAbsolutePath(), "trim",
				"0", "0" };
		ProcessBuilder build = new ProcessBuilder(cmd);
		try {
			build.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO:This creates the file, but there is no way of checking if it was
		// successful or not
	}

	/**
	 * Required by the StagedMedia constructor
	 */
	@Override
	protected String getExtension() {
		return ".mp3";
	}

}
