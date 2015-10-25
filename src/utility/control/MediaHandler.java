package utility.control;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;
import javafx.application.Application;
import utility.media.MediaFile;
import utility.media.MediaFormat;
import utility.media.MediaType;

/**
 * A utility class which allows Media objects to be converted and merged.
 * 
 * @author adav194
 * 
 */
public class MediaHandler extends Application {

	private DoubleProperty progress;
	private MediaFile destination;

	@Override
	public void start(Stage primaryStage) throws Exception {

		MediaFile outputFinal = MediaFile.createMediaContainer(MediaFormat.MP3,
				new File(System.getProperty("user.home")
						+ "/SoftEng206/ffmpeg/FinalDest.mp3"));
		MediaHandler mh = new MediaHandler(outputFinal);
		MediaFile out1 = new MediaFile(new File(System.getProperty("user.home")
				+ "/SoftEng206/ffmpeg/out1.mp3"));
		MediaFile out2 = new MediaFile(new File(System.getProperty("user.home")
				+ "/SoftEng206/ffmpeg/out2.mp3"));
		MediaFile out3 = new MediaFile(new File(System.getProperty("user.home")
				+ "/SoftEng206/ffmpeg/out3.mp3"));

		mh.mergeAudio(outputFinal, out1, out2, out3);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public MediaHandler(MediaFile destination) throws Exception {
		this(new SimpleDoubleProperty(0), destination);
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
	public MediaHandler(DoubleProperty progress, MediaFile destination)
			throws Exception {
		if (!destination.isValid()) {
			throw new Exception(
					"Cannot instantiate a MediaHandler on an invalid MediaFile");
		}
		this.progress = progress;
		this.destination = destination;
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
	public MediaFile getDestination() {
		return destination;
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
	public void convert(MediaFile source) {

		String expansion = "ffmpeg -y -i " + source.getAbsolutePath()
				+ " " + destination.getAbsolutePath();
		FFMPEG cmd = new FFMPEG(progress, expansion, source.getDuration());
		cmd.start();
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
	public void mergeAudio(MediaFile... files) throws Exception {
		Double longestDuration = 0.0;
		if (!destination.getType().equals(MediaType.Audio)) {
			throw new Exception("destination format '"
					+ destination.getFormat().toString()
					+ "' is not a valid Audio source");
		}
		String ffmpegCommand = "ffmpeg -y ";
		for (MediaFile f : files) {
			if (f.getType().equals(MediaType.Audio)) {
				ffmpegCommand = ffmpegCommand.concat("-i "
						+ f.getAbsolutePath() + " ");
				if (longestDuration < f.getDuration()) {
					longestDuration = f.getDuration();
				}
			}
		}
		ffmpegCommand = ffmpegCommand.concat("-filter_complex \"amix=inputs="
				+ files.length + "\" -ac 2 "
				+ destination.getAbsolutePath());
		FFMPEG cmd = new FFMPEG(progress, ffmpegCommand, longestDuration);
		cmd.start();
	}

	/**
	 * Strips the audio from a valid video source. The resulting video is stored
	 * in the destination MediaFile.
	 * 
	 * @param source
	 * @throws Exception
	 */
	public void stripAudio(MediaFile source) throws Exception {
		if (!destination.getType().equals(MediaType.Video)) {
			throw new Exception("destination format '"
					+ destination.getFormat().toString()
					+ "' is not a valid Video source");
		}
		String ffmpegCommand = "ffmpeg -y -i "
				+ source.getAbsolutePath() + " -an "
				+ destination.getAbsolutePath();
		FFMPEG cmd = new FFMPEG(progress, ffmpegCommand, source.getDuration());
		cmd.start();

	}

	/**
	 * Overlays an audio file onto a video.
	 * 
	 * @param video
	 *            A valid video source.
	 * @param audio
	 *            A valid audio source.
	 * @throws Exception
	 *             An Exception is thrown if any of the input Media containers
	 *             are of unexpected format.
	 */
	public void mergeAudioAndVideo(MediaFile video, MediaFile audio)
			throws Exception {
		if (!destination.getType().equals(MediaType.Video)) {
			throw new Exception("destination format '"
					+ destination.getFormat().toString()
					+ "' is not a valid Video source");
		} else if (!video.getType().equals(MediaType.Video)) {
			throw new Exception("input video format '"
					+ destination.getFormat().toString()
					+ "' is not a valid Video source");
		} else if (!audio.getType().equals(MediaType.Audio)) {
			throw new Exception("input audio format '"
					+ destination.getFormat().toString()
					+ "' is not a valid Audio source");
		}
		String ffmpegCommand = "ffmpeg -y -i "
				+ video.getAbsolutePath() + " -i "
				+ audio.getAbsolutePath()
				+ " -filter_complex amix=inputs=2 -shortest "
				+ destination.getAbsolutePath();

		FFMPEG cmd = new FFMPEG(progress, ffmpegCommand, video.getDuration());
		cmd.start();
	}

	public void textToSpeech(FestivalFile festival) {

	}
	//
	// String cmd = "text2wave -o " + filePath + " " + textFile + " -eval " +
	// scmFile;
	//
	// String scmFileText = "(" + voiceCommand + ")";
	//
	// scmFileText = scmFileText + "(set! duffint_params \'((start " + start +
	// ") (end " + end + ")))\n";
	// scmFileText = scmFileText + "(Parameter.set \'Int_Method \'DuffInt)\n";
	// scmFileText = scmFileText +
	// "(Parameter.set \'Int_Target_Method Int_Targets_Default)\n";

}
