package utility.media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import editor.MediaConverter;

public class FFmpegTask extends Task<Void> {

	private DoubleProperty progress;
	private Double totalWork;
	private String input;

	public FFmpegTask(DoubleProperty progress, String input,
			Double finalDuration) {
		super();
		this.progress = progress;
		totalWork = finalDuration;
		this.input = input;
		System.out.println(this.input);
	}

	protected void updateProgress(double workDone, double totalWork) {
		super.updateProgress(workDone, totalWork);
		progress.setValue(workDone / totalWork);
	}
	@Override
    protected Void call() throws Exception {

            ProcessBuilder procBulder = new ProcessBuilder("/bin/bash", "-c", input);
            procBulder.redirectErrorStream(true);
           
            System.out.println("Command:\n");
            System.out.println(input);

            try {
                    Process process = procBulder.start();

                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line = null;

                    while ((line = reader.readLine()) != null) {
                            System.out.println("SOX: " + line);
                    }

            } catch (Exception e) {
                    System.out.println("Failed to process sox thread");
                    e.printStackTrace();
            }

            return null;
    }
	/*
	@Override
	protected Void call() throws Exception {

		String[] cmd = { "/bin/bash", "-c", input };
		ProcessBuilder build = new ProcessBuilder(cmd);
		build.redirectErrorStream(true);
		Process p;
		try {
			p = build.start();
			MediaConverter.currentlyProcessed(p.getInputStream());
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
*/
	public void currentlyProcessed(InputStream in) {
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		String line;
		Boolean processingStarted = false;
		try {
			while ((line = bin.readLine()) != null) {;
				this.updateMessage(line);
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
