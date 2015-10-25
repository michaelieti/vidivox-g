package utility.media;

import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;

public class FFMPEG {

	private String inputCommand;
	private DoubleProperty progress;
	private Thread th;

	public FFMPEG(DoubleProperty progress, String input, Double finalDuration) {
		this.progress = progress;
		Task<Void> ffmpegProcess = new FFmpegTask(progress, input,
				finalDuration);
		th = new Thread(ffmpegProcess);
		th.setDaemon(true);
		
		
	}

	public void start() {
		th.start();
	}

	public DoubleProperty getProgress() {
		return progress;
	}

}

