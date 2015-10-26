package utility.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import editor.MediaConverter;

/**
 * Utility class to create FFMPEG Tasks. This particular Task is package
 * protected and should not be called elsewhere.
 * 
 * @author adav194
 * 
 */
public class FFmpegTask extends Task<Void> {

	private DoubleProperty progress;
	private Double totalWork;
	private String input;
	private Process process;
	private boolean printOutput = false;

	protected FFmpegTask(DoubleProperty progress, String input,
			Double finalDuration) {
		super();
		this.progress = progress;
		totalWork = finalDuration;
		this.input = input;
	}

	protected void updateProgress(double workDone, double totalWork) {
		super.updateProgress(workDone, totalWork);
		//progress.setValue(workDone / totalWork);
	}
	@Override
	protected Void call() throws Exception {
		System.out.println(">>> " + input);

		ProcessBuilder procBulder = new ProcessBuilder("/bin/bash", "-c", input);
		procBulder.redirectErrorStream(true);
		try {
			process = procBulder.start();
			currentlyProcessed(process.getInputStream());

		} catch (Exception e) {
			System.out.println("Failed to process FFMPEG command.");
			e.printStackTrace();
		}

		return null;
	}
	
	public void waitFor() throws InterruptedException {
		if (process != null) {
			process.waitFor();
		}
	}

	public void setPrint(boolean print) {
		printOutput = print;
	}
	
	public Process getProcess() {
		return process;
	}
	/*
	 * A helper function which processes the output stream from an FFMPEG
	 * command. Parsing of this output allows an indication of progress to be
	 * made.
	 */
	private void currentlyProcessed(InputStream in) {
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		String line;
		int i = 0;
		Boolean processingStarted = false;
		if (printOutput) System.out.println("\n~~~~~~~~~Printing Output for FFMPEG Command~~~~~~~~~\n");
		try {
			while ((line = bin.readLine()) != null) {
				i++;
				if (printOutput) System.out.println(i  + " | " + line);
				if (line.equals("Press [q] to stop, [?] for help")) {
					processingStarted = true;
				} else if (processingStarted & (line.indexOf("time=") != -1)) {
					line = line.substring(line.indexOf("time=") + 5,
							line.indexOf(" bitrate"));
					this.updateProgress(MediaConverter.timeToSeconds(line),
							totalWork);
				}
			}
			if (printOutput) System.out.println("~~~~~~~~~~~~~~~~Finish~~~~~~~~~~~~~~~~");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
