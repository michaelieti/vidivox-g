package editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import player.VidivoxPlayer;

import utility.StagedAudio;
import utility.StagedMedia;
import utility.StagedVideo;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class InBackground extends Task<StagedMedia> {
	private Media video;
	private StagedAudio audio;
	private ProgressBar bar;
	private DoubleProperty progress = new SimpleDoubleProperty(0);
	private MediaView view;
	
	public InBackground(Media video, StagedAudio audio, ProgressBar prog, MediaView mv) {
		super();
		this.video = video;
		this.audio = audio;
		view = mv;
		bar = prog;
		bar.progressProperty().bind(progress);
	}
	
	 @Override
	protected void updateProgress(double workDone, double totalWork) {
		super.updateProgress(workDone, totalWork);
		progress.set(workDone / totalWork);
	}
	
	@Override
	protected StagedMedia call() throws Exception {
		String pathVideo = video.getSource();
		String pathAudio = audio.getFile().getPath();
		StagedVideo output = new StagedVideo();
		String expansion = "ffmpeg -y -i " + pathVideo + " -i " + pathAudio + " -filter_complex amix=inputs=2 -shortest " + output.getFile().getAbsolutePath();
		String[] cmd = {"/bin/bash", "-c", expansion };
		ProcessBuilder pb = new ProcessBuilder(cmd);
		try {
			pb.redirectErrorStream(true);
			Process p = pb.start();
			bar.setVisible(true);
			this.updateProgress(0, video.getDuration().toSeconds());
			currentlyProcessed(p.getInputStream());
			p.waitFor();
			this.updateProgress(video.getDuration().toSeconds(), video.getDuration().toSeconds());
			VidivoxPlayer.getVidivoxPlayer().setMediaPlayer(new MediaPlayer(output.getMedia()));
			VidivoxPlayer.getVidivoxPlayer().getMediaPlayer().play();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		bar.setVisible(false);
		return output;	//TODO
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
					line = line.substring(line.indexOf("time=") + 5, line.indexOf(" bitrate"));
					this.updateProgress(MediaConverter.timeToSeconds(line), video.getDuration().toSeconds());
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
		

}
