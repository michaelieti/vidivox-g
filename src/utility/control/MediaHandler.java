package utility.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import utility.media.MediaFile;
import utility.media.MediaType;

/**
 * A utility class which allows Media objects to be converted and merged.
 * 
 * @author adav194
 * 
 */
public class MediaHandler extends Application {

	private DoubleProperty progress;
	private MediaFile mediaFile;
	private FFMPEG cmd;
	private BackgroundTask queueTasks;

	@Override
	public void start(Stage primaryStage) throws Exception {

		System.out.println("Making media files");
		/*
		MediaFile out1 = MediaFile.createMediaContainer(MediaFormat.MP4, new File(System.getProperty("user.home") + "/tempvids/"), "out1.avi");
		MediaFile out2 = MediaFile.createMediaContainer(MediaFormat.MP4, new File(System.getProperty("user.home") + "/tempvids/"),"out2.mp4");
		MediaFile out3 = MediaFile.createMediaContainer(MediaFormat.MP3, new File(System.getProperty("user.home") + "/tempvids/"),"out2.mp3");
		
		System.out.println("Made media files");
		System.out.println("out1 made in " + out1.getQuoteOfAbsolutePath());
		
		MediaFile out1 = new MediaFile(new File(System.getProperty("user.home")
				+ "/SoftEng206/ffmpeg/out1.mp3"));
		MediaFile out2 = new MediaFile(new File(System.getProperty("user.home")
				+ "/SoftEng206/ffmpeg/out2.mp3"));
		MediaFile out3 = new MediaFile(new File(System.getProperty("user.home")
				+ "/SoftEng206/ffmpeg/out3.mp3"));

		mh.mergeAudio(outputFinal, out1, out2, out3);*/
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public MediaHandler() {
		super();
	}

	
	public MediaHandler(BackgroundTask queue ,MediaFile destination) throws Exception {
		this(queue, new SimpleDoubleProperty(0), destination);

	}
	
	public void setBackgroundTask(BackgroundTask queue) {
		queueTasks = queue;
	}

	/**
	 * Constructor for the MediaHandler object.
	 * 
	 * @param progress
	 *            A progress property which will be dynamically updated when a
	 *            MediaHandling event is called.
	 * @param destination
	 *            A valid MediaFile.
	 * @throws Exception
	 *             An exception is thrown if an invalid MediaFile is passed to
	 *             the constructor.
	 */
	public MediaHandler(BackgroundTask queue ,DoubleProperty progress, MediaFile destination)
			throws Exception {
		if (!destination.isValid()) {
			throw new Exception(
					destination.getQuoteOfAbsolutePath() + " is an invalid MediaFile");
		}
		this.progress = progress;
		this.mediaFile = destination;
		this.queueTasks = queue;
	}

	/**
	 * @return The progress property for this handler. When a Media Handler
	 *         event is processing, this property will indicate the progress of
	 *         the event.
	 */
	public DoubleProperty getProgress() {
		return progress;
	}

	/**
	 * @return The assigned destination file for this handler. Most Media
	 *         Handler events will store their output into the destination file.
	 */
	public MediaFile getMediaFile() {
		return mediaFile;
	}
	
	public FFMPEG getProcess() {
		return cmd;
	}
	
	public void printToConsole(boolean print) {
		cmd.printOutput(print);
	}
	public void waitFor() {
		cmd.waitFor();
	}
	/**
	 * Takes a source MediaFile and converts its contents to match the formating
	 * of the destination MediaFile.
	 * <P>
	 * 
	 * By setting the destination format to the same type as the source format,
	 * this method can be used as a means of creating copies of media.
	 * 
	 * @param source
	 */
	public BackgroundTask convert(MediaFile source) {

		String expansion = "ffmpeg -y -i " + source.getQuoteOfAbsolutePath()
				+ " " + mediaFile.getQuoteOfAbsolutePath();
		cmd = new FFMPEG(progress, expansion, source.getDuration());
		cmd.queueTo(queueTasks);
		cmd.setName("Convert");
		return queueTasks;
	}
	

	/**
	 * Merges a list of MediaFiles and stores the result in the destination
	 * MediaFile.
	 * 
	 * @param files
	 * @return
	 * @throws Exception
	 *             If the destination container is not of type Audio.
	 */
	public BackgroundTask mergeAudio(MediaFile... files) throws Exception {
		Double longestDuration = 0.0;
		if (!mediaFile.getType().equals(MediaType.Audio)) {
			throw new Exception("destination format '"
					+ mediaFile.getFormat().toString()
					+ "' is not a valid Audio source");
		}
		String ffmpegCommand = "ffmpeg -y ";
		for (MediaFile f : files) {
			if (f.getType().equals(MediaType.Audio)) {
				ffmpegCommand = ffmpegCommand.concat("-i "
						+ f.getQuoteOfAbsolutePath() + " ");
				if (longestDuration < f.getDuration()) {
					longestDuration = f.getDuration();
				}
			}
		}
		ffmpegCommand = ffmpegCommand.concat("-filter_complex \"amix=inputs="
				+ files.length + "\" "
				+ mediaFile.getQuoteOfAbsolutePath());
		cmd = new FFMPEG(progress, ffmpegCommand, longestDuration);
		cmd.queueTo(queueTasks);
		cmd.setName("Merge Audio");
		return queueTasks;
	}

	/**
	 * Strips the audio from a valid video source. The resulting video is stored
	 * in the destination MediaFile.
	 * 
	 * @param source
	 * @return 
	 * @throws Exception
	 */
	public BackgroundTask stripAudio(MediaFile source) throws Exception {
		if (!mediaFile.getType().equals(MediaType.Video)) {
			throw new Exception("destination format '"
					+ mediaFile.getFormat().toString()
					+ "' is not a valid Video source");
		}
		String ffmpegCommand = "ffmpeg -y -i "
				+ source.getQuoteOfAbsolutePath() + " -an "
				+ mediaFile.getQuoteOfAbsolutePath();
		cmd = new FFMPEG(progress, ffmpegCommand, source.getDuration());
		cmd.queueTo(queueTasks);
		cmd.setName("Strip Audio");
		return queueTasks;

	}

	/**
	 * Overlays an audio file onto a video.
	 * 
	 * @param video
	 *            A valid video source.
	 * @param audio
	 *            A valid audio source.
	 * @return 
	 * @throws Exception
	 *             An Exception is thrown if any of the input Media containers
	 *             are of unexpected format.
	 */
	public BackgroundTask mergeAudioAndVideo(MediaFile video, MediaFile audio)
			throws Exception {
		if (!mediaFile.getType().equals(MediaType.Video)) {
			throw new Exception("destination format '"
					+ mediaFile.getFormat().toString()
					+ "' is not a valid Video source");
		} else if (!video.getType().equals(MediaType.Video)) {
			throw new Exception("input video format '"
					+ video.getFormat().toString()
					+ "' is not a valid Video source");
		} else if (!audio.getType().equals(MediaType.Audio)) {
			throw new Exception("input audio format '"
					+ audio.getFormat().toString()
					+ "' is not a valid Audio source");
		}
		String ffmpegCommand = "ffmpeg -y -i "
				+ video.getQuoteOfAbsolutePath() + " -i "
				+ audio.getQuoteOfAbsolutePath()
				+ " -filter_complex \"amix=inputs=2\" -ac 2 -shortest "
				+ mediaFile.getQuoteOfAbsolutePath();
		cmd = new FFMPEG(progress, ffmpegCommand, video.getDuration());
		cmd.queueTo(queueTasks);
		cmd.setName("Merge A/V");
		return queueTasks;
	}
	
	/**
	 * Creates a blank audio file of the given duration,
	 *  which will be located at the destination stored in this object.
	 * @param audio
	 * @param duration
	 * @return 
	 * @throws Exception
	 */
	public BackgroundTask makeBlankAudio(Duration duration) throws Exception{
		
		StringBuilder sb = new StringBuilder("ffmpeg -y -f lavfi -i aevalsrc=0:0:0:0:0:0::duration=");
		sb.append(duration.toSeconds());
		sb.append(" ");
		sb.append(mediaFile.getQuoteOfAbsolutePath());
		
		cmd = new FFMPEG(progress, sb.toString(), duration.toSeconds());
		cmd.queueTo(queueTasks);
		cmd.setName("Blank Audio");
		return queueTasks;
	}
	
	/**
	 * Concatenates two audio files, audio 1 comes before audio 2.
	 * @param audio1
	 * @param audio2
	 * @return 
	 * @throws Exception
	 */
	public BackgroundTask concatAudio(MediaFile audio1, MediaFile audio2) throws Exception{
		if (!audio1.getType().equals(MediaType.Audio) || 
				!audio2.getType().equals(MediaType.Audio)) {
			throw new Exception("input audio format");
		}
		
		StringBuilder sb = new StringBuilder("ffmpeg -y -i ");
		sb.append(audio1.getQuoteOfAbsolutePath())
		.append(" -i " + audio2.getQuoteOfAbsolutePath())
		.append(" -filter_complex '[0:0] [1:0] concat =n=2:v=0:a=1' ")
		.append(mediaFile.getQuoteOfAbsolutePath());
		
		Double totalDuration = audio1.getDuration() + audio2.getDuration();
		cmd = new FFMPEG(progress, sb.toString(), totalDuration);
		cmd.queueTo(queueTasks);
		cmd.setName("Concat Audio");
		return queueTasks;
	
	}

	public BackgroundTask textToSpeech(String text, SchemeFile festival) {
		String ffmpegCommand = "`echo \"" + text + "\" | text2wave -o "
				+ mediaFile.getQuoteOfAbsolutePath() + " -eval " + festival.getAbsolutePath() + "`";
		cmd = new FFMPEG(progress, ffmpegCommand, 1.0);
		cmd.queueTo(queueTasks);
		cmd.setName("Text to Speech");
		return queueTasks;
	}
	

}
