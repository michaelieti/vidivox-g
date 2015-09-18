package player;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	public static File TextToSpeech(String msg) throws IOException, InterruptedException {
		String expansion = "echo " + msg + " hello | text2wave > .temp/speech.wav";
		String[] cmd = {"bash","-c",expansion};
		ProcessBuilder build = new ProcessBuilder(cmd);
		Process p = build.start();
		p.waitFor(); //TODO: Implement concurrency, this code is potentially slow.	
		return new File(System.getProperty("user.dir") + "/.temp/speech.wav");
	}
	

}

