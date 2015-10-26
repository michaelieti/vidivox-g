package utility.control;

import javafx.beans.property.DoubleProperty;

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
	private String input;
	private String name = "";

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
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void printOutput(boolean print) {
		ffmpegProcess.setPrint(print);
	}
	/**
	 * Called to start the FFMPEG command.
	 */
	public void queueTo(BackgroundTask queue) {
		System.out.println("Called");
		queue.addTask(this);
	}
	
	public void waitFor() {
		try {
			ffmpegProcess.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public FFmpegTask getProcess() {
		return ffmpegProcess;
	}
	

	/**
	 * 
	 * @return A progress property which is manually updated by FFMPEG to
	 *         indicate progress completion.
	 */
	public DoubleProperty getProgress() {
		return progress;
	}
	
	@Override
	public String toString() {
		if (name.equals("")) {
			return super.toString();
		} else {
			return name;
		}
		
	}

}
