package utility;

import java.io.IOException;

public class StagedAudio extends StagedMedia {

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
	 * The format of this particular instance of StagedMedia. 
	 * Valid formats currently include: mp3, wav.
	 */
	private MediaTypes format;
	
	
	public StagedAudio(MediaTypes type) {
		super();
		format = type;
	}
	/**
	 * Creates an Empty wav file using the linux command 'rec'.
	 * Does not work for mp3
	 */
	@Override
	protected void createFile() {
		String[] cmd = {"bash", "-c", "rec", file.getAbsolutePath(), "trim", "0", "0"};
		ProcessBuilder build = new ProcessBuilder(cmd);
		try {
			build.start();
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
		return format.getExtension();
	}

	
	
}
