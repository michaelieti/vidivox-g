package utility.media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import editor.MediaConverter;

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

class FFmpegTask extends Task<Void> {

	private DoubleProperty progress;
	private Double totalWork;
	private String input;

	public FFmpegTask(DoubleProperty progress, String input,
			Double finalDuration) {
		this.progress = progress;
		totalWork = finalDuration;
		this.input = input;
	}

	protected void updateProgress(double workDone, double totalWork) {
		super.updateProgress(workDone, totalWork);
		progress.setValue(workDone / totalWork);

	}

	@Override
	protected Void call() throws Exception {
		//String[] cmd = { "bash", "-c", input };
		ProcessBuilder build = new ProcessBuilder(input);
		build.redirectErrorStream(true);
		Process p = build.start();
		currentlyProcessed(p.getInputStream());
		p.waitFor();
		System.out.println("done");
		return null;
	}

	public void currentlyProcessed(InputStream in) {
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		String line;
		Boolean processingStarted = false;
		try {
			while ((line = bin.readLine()) != null) {
				if (line.equals("Press [q] to stop, [?] for help")) {
					processingStarted = true;
				} else if (processingStarted & (line.indexOf("time=") != -1)) {
					line = line.substring(line.indexOf("time=") + 5,
							line.indexOf(" bitrate"));
					this.updateProgress(MediaConverter.timeToSeconds(line),
							totalWork);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
