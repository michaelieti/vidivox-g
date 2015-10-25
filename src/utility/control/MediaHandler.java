package utility.control;

import java.io.File;
import java.io.IOException;

import editor.MediaConverter;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;

import utility.media.FFMPEG;
import utility.media.MediaFile;
import utility.media.MediaFormat;

/**
 * A utility class which allows Media objects to be converted and merged.
 * 
 * @author adav194
 * 
 */
public class MediaHandler {

	private DoubleProperty progress;
	
	public static void main(String[] args) {
		MediaHandler mh = new MediaHandler();
		MediaFile out1 = new MediaFile(new File(System.getProperty("user.home") + "/SoftEng206/ffmpeg/out1.mp3"));
		MediaFile out2 = new MediaFile(new File(System.getProperty("user.home") + "/SoftEng206/ffmpeg/out2.mp3"));
		MediaFile out3 = new MediaFile(new File(System.getProperty("user.home") + "/SoftEng206/ffmpeg/out3.mp3"));
		MediaFile outputFinal = MediaFile.createMediaContainer(MediaFormat.MP3, new File(System.getProperty("user.home") + "/SoftEng206/ffmpeg/FinalDest.mp3"));
		mh.mergeAudio(outputFinal, out1, out2, out3);
	}

	public MediaHandler() {
		progress = new SimpleDoubleProperty(0);
	}

	public MediaHandler(DoubleProperty progress) {
		this.progress = progress;
	}

	public DoubleProperty getProgress() {
		return progress;
	}

	/**
	 * Takes a source MediaFile and converts its contents to match the formating
	 * of the destination MediaFile.
	 * 
	 * @param source
	 * @param destination
	 */
	public void convert(MediaFile source, MediaFile destination) {

		String expansion = "ffmpeg -y -i " + source.getPath().getAbsolutePath()
				+ " " + destination.getPath().getAbsolutePath();
		FFMPEG cmd = new FFMPEG(progress, expansion, source.getLength());
		cmd.start();
	}

	/**
	 * Merges a list of MediaFiles and stores the result in a destination
	 * MediaFile.
	 * 
	 * @param destination
	 * @param files
	 * @return
	 */
	public void mergeAudio(MediaFile destination, MediaFile... files) {
		Double longestDuration = 0.0; 
		String ffmpegCommand = "ffmpeg -y ";
		for (MediaFile f : files) {
			ffmpegCommand = ffmpegCommand.concat("-i " + f.getPath().getAbsolutePath() + " ");
			if (longestDuration < f.getLength()) {
				longestDuration = f.getLength();
			}
		}
		ffmpegCommand = ffmpegCommand.concat("-filter_complex \"amix=inputs=" + files.length
				+ "\" " + destination.getPath().getAbsolutePath());
		System.out.println(ffmpegCommand);
		FFMPEG cmd = new FFMPEG(progress, ffmpegCommand, longestDuration);
		cmd.start(); 

		System.out.println("done");
		
	}

}
