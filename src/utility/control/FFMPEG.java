package utility.control;

import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;

/**
 * A helper class which handles FFMPEG commands. Commands are run on a
 * background worker thread and update a progress property appropriately.
 * 
 * @author adav194
 * 
 */
public class FFMPEG {

	private DoubleProperty progress;
	private Thread th;

	/**
	 * 
	 * @param progress
	 *            A progress property which will be updated according to FFMPEG
	 *            progress.
	 * @param input
	 *            The full FFMPEG bash command in string format.
	 * @param finalDuration
	 *            The duration of the desired output Media Source.
	 */
	public FFMPEG(DoubleProperty progress, String input, Double finalDuration) {
		this.progress = progress;
		Task<Void> ffmpegProcess = new FFmpegTask(progress, input,
				finalDuration);
		th = new Thread(ffmpegProcess);
		th.setDaemon(true);

	}

	/**
	 * Called to start the FFMPEG command.
	 */
	public void start() {
		th.start();
	}

	/**
	 * 
	 * @return A progress property which is manually updated by FFMPEG to
	 *         indicate progress completion.
	 */
	public DoubleProperty getProgress() {
		return progress;
	}

}
