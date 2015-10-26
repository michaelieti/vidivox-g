package utility.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

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
	private FFmpegTask ffmpegProcess;

	/**
	 * 
	 * @param progress
	 *            A progress property which will be updated according to FFMPEG
	 *            progress.
	 * @param input
	 *            The full FFMPEG bash command in string format.
	 * @param finalDuration
	 *            The duration of the desired output Media Source in seconds.
	 */
	public FFMPEG(DoubleProperty progress, String input, Double finalDuration) {
		this.progress = progress;
		ffmpegProcess = new FFmpegTask(progress, input,
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
	
	
	public void setOnFinished(EventHandler<WorkerStateEvent> event) {
		ffmpegProcess.setOnSucceeded(event);
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
