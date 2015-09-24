package editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import utility.*;


/**
 * The purpose of this class will be to provide all the necessary functionality to edit a video.
 * 
 * Each object represents a video to be editted.
 * Dependant on ffmpeg
 * @author adav194
 *
 */
public class MediaConverter {
	private URI video;

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	
	public MediaConverter(String path) throws IOException {
		File f = new File(path);
		video = f.toURI();
		Path p = Paths.get(video);
		String output = Files.probeContentType(p);
		System.out.println(output); //TODO: Use this to determine whether a conversion is required
		
	}
	
	/**
	 * Uses festival to create an mp3 file from a text file. A File object is returned, representing this mp3 file.
	 * @param msg
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static File textToSpeech(String msg) throws IOException, InterruptedException {
		String expansion = "echo " + msg + " hello | text2wave > .temp/speech.wav";
		String[] cmd = {"bash","-c",expansion};
		ProcessBuilder build = new ProcessBuilder(cmd);
		Process p = build.start();
		p.waitFor(); //TODO: Implement concurrency, this code is potentially slow.	
		return new File(System.getProperty("user.dir") + "/.temp/speech.wav");
	}
	
	public static StagedMedia mergeVideoAndAudio(Media video, StagedAudio audio, ProgressBar prog, MediaView mv) {
		
		
		InBackground task = new InBackground(video, audio, prog, mv);
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
		StagedMedia output = null;
		try {
			output = task.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;	//TODO
	}
	
	
	/**
	 * Version 0.1 - not yet tested.
	 * Converts a file (given via a path string) to an mp4 that will be compatible with
	 * vidivox. 
	 * @param inputPath - the complete absolute input path, including the extension.
	 * @param outputPath - the absolute output path, including the name of the file. The extension
	 * is automatically assigned as .mp4 and does not need to be included.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static Media convertToMP4(String inputPath, final String outputPath) throws InterruptedException, ExecutionException {
	/*	//ProcessBuilder builds the process below
		//ffmpeg -y -i <inputPath> -f mp4 -strict -2 -c:v libx264 -t 0 <outputPath>.mp4
		//TODO: check that Path.toString() works properly here
		System.out.println("Beginning conversion...");
		String command = buildFFMPEGCommand(inputPath.toString(), outputPath.toString());
		final ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
		//TODO: test this shit out
		
		//CREATE TASK
		final Task task;
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	Process p = pb.start();
            	p.waitFor();
            	//TODO: finish this shet
            	return null;
            }
        };
     */   
        return null;
	}
	
	
	public static void currentlyProcessed(InputStream in, Task task) {
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		String line;
		Boolean processingStarted = false;
		try {
			while ((line = bin.readLine()) != null) {
				if (line.equals("Press [q] to stop, [?] for help")) {
					processingStarted = true;
				} else if (processingStarted & (line.indexOf("time=") != -1)) {
					line = line.substring(line.indexOf("time=") + 5, line.indexOf(" bitrate"));
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
		
	}
	/**
	 * This helper function takes a string in the form "hh:mm:ss" and returns
	 * the amount of seconds this amounts to.
	 * @param time
	 * @return
	 */
	public static Double timeToSeconds(String time) {
		time = time.replace(":", " ");
		time = time.replace(".", " ");
		Scanner scan = new Scanner(time);
		//Calculating hours
		int seconds = scan.nextInt() * 3600;
		//Calculating minutes
		seconds += scan.nextInt() * 60;
		//Calculating seconds
		seconds += scan.nextInt();
		scan.close();
		return (double) seconds;
	}
	/**
	 * A utility method for convertToMP4(). Not for public use.
	 */
	private static String buildFFMPEGCommand(String in, String out){
		StringBuilder sb = new StringBuilder("ffmpeg -y -i ");
		sb.append(in);
		sb.append(" -f mp4 -strict -2 -c:v libx264 -t 0 ");
		sb.append(out);
		sb.append(".mp4");
		return sb.toString();
	}
	
	public static Double HowLongToGo() {
		return null;
		//TODO: Parse the output of ffmpeg to see how long it has to go
	}

}
