package overlay.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.util.Duration;
import overlay.Commentary;
import player.Main;
import utility.control.BackgroundTask;
import utility.control.MediaHandler;
import utility.control.SchemeFile;
import utility.media.MediaFile;
import utility.media.MediaFormat;

public class OverlayCommitter extends Application {

	Media originalVideo;
	List<Commentary> commentaryList;
	DoubleProperty progressProperty = new SimpleDoubleProperty(0);
	private BackgroundTask queue = new BackgroundTask();

	/* for testing purposes! */
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Hello! Now begins the test....");
		// make a new overlay committer

		OverlayCommitter oc = new OverlayCommitter();

		String uriString = System.getProperty("user.home")
				+ "/SoftEng206/vidivox/vid.mp4";
		System.out.println(uriString);
		File file = new File(uriString);
		System.out.println();
		// make new commentary list
		// provide a new video
		List<Commentary> list = new ArrayList<Commentary>();
		list.add(new Commentary(Duration.valueOf("1s"), "Test commentary",
				OverlayType.TTS));
		list.add(new Commentary(Duration.valueOf("1s"), "How's it going?",
				OverlayType.TTS));
		list.add(new Commentary(Duration.valueOf("1s"), "Test commentary",
				OverlayType.TTS));

		Media video = new Media(file.toURI().toString());
		oc.addCommentaryList(list);
		oc.addVideo(video);
		oc.beginCommit();

	}

	public static void main(String[] args) throws Exception {
		Main.InitTemp();
		launch(args);
	}

	public OverlayCommitter addVideo(Media originalVideo) {
		this.originalVideo = originalVideo;
		return this;
	}

	public OverlayCommitter addCommentaryList(List<Commentary> commentaryList) {
		this.commentaryList = commentaryList;
		return this;
	}

	public OverlayCommitter addProgressProperty(DoubleProperty doubleprop) {
		this.progressProperty = doubleprop;
		System.out.println("Progress property added!");
		return this;
	}

	public void call() {
		Thread th = new Thread(queue);
		th.setDaemon(true);
		th.start();
	}
	
	public void setOnFinished(EventHandler<WorkerStateEvent> event) {
		queue.setOnSucceeded(event);
		
	}

	public MediaFile beginCommit() {
		if (originalVideo == null) {
			System.out.println("Media supplied was null!");
			return null;
		}
		if (commentaryList == null || commentaryList.size() == 0) {
			System.out.println("List size is zero");
			if (commentaryList.size() == 0) {
				return null;
			}
		}
		queue = new BackgroundTask();
		List<MediaFile> toBeMerged = new ArrayList<MediaFile>();
		boolean debug = true;
		try {
			MediaFile finalOutput = MediaFile
					.createMediaContainer(queue,MediaFormat.WAV);
			MediaFile finalVideo = MediaFile
					.createMediaContainer(queue,MediaFormat.MP4);
			if (debug)
				System.out.println("\nFile Info: FinalVideo\n\tLocation: "
						+ finalVideo.getQuoteOfAbsolutePath());
			MediaHandler finalHandler = new MediaHandler(finalOutput);
			for (Commentary c : commentaryList) {
				MediaFile blankFile = makeBlank(queue, c.getTime());
				MediaFile speechFile = simpleMakeSpeech(queue, c.getText());

				MediaFile commentFile = concat(queue, blankFile, speechFile);
				if (debug)
					System.out.println("\nFile Info:\n\tIndex: " + commentaryList.indexOf(c) +"\n\tLocation: "
							+ commentFile.getQuoteOfAbsolutePath()
							+ "\n\tBlank: "
							+ blankFile.getQuoteOfAbsolutePath()
							+ "\n\tSpeech: "
							+ speechFile.getQuoteOfAbsolutePath());
				toBeMerged.add(commentFile);
			}
			MediaFile[] array = new MediaFile[toBeMerged.size()];
			array = (MediaFile[]) toBeMerged.toArray(array);
			finalHandler.mergeAudio(array);
			finalHandler.printToConsole(true);
			MediaFile audio = finalHandler.getMediaFile();
			if (debug)
				System.out.println("\nFile Info:\n\tLocation: "
						+ finalHandler.getMediaFile().getQuoteOfAbsolutePath());
			MediaFile orginal = new MediaFile(originalVideo);

			
		
			MediaHandler videoHandler = new MediaHandler(finalVideo);
			videoHandler.mergeAudioAndVideo(orginal, audio);
			if (debug)
				System.out.println("\nFile Info:\n\tLocation: "
						+ videoHandler.getMediaFile().getQuoteOfAbsolutePath());
			return videoHandler.getMediaFile();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private MediaFile concat(BackgroundTask queue, MediaFile first,
			MediaFile second) throws Exception {
		MediaFile concatenatedFile = MediaFile
				.createMediaContainer(queue,MediaFormat.WAV);
		MediaHandler concatenatedHandler = new MediaHandler(progressProperty,
				concatenatedFile);
		concatenatedHandler.setBackgroundTask(queue);
		concatenatedHandler.setNameOfProcess("Concat");
		concatenatedHandler.concatAudio(first, second);

		return concatenatedHandler.getMediaFile();
	}

	private MediaFile makeBlank(BackgroundTask queue, Duration blankTime)
			throws Exception {
		MediaHandler blankMediaHandler;
		MediaFile blankFile = MediaFile.createMediaContainer(queue,MediaFormat.WAV);
		blankMediaHandler = new MediaHandler(progressProperty, blankFile);
		blankMediaHandler.setBackgroundTask(queue);
		blankMediaHandler.setNameOfProcess("Make Blank");
		blankMediaHandler.makeBlankAudio(blankTime);
		blankFile = blankMediaHandler.getMediaFile();
		// blankFile now holds a blank file
		return blankFile;
	}

	/**
	 * This is a convenience method for creating a Text to Speech MediaFile.
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	private MediaFile simpleMakeSpeech(BackgroundTask queue, String text)
			throws Exception {

		/* Initializing festival Scheme Settings */
		SchemeFile scmFile = new SchemeFile();
		scmFile.writeToDisk();

		/* Initializing MediaFile Container */
		MediaFile textMediaFile = MediaFile
				.createMediaContainer(queue,MediaFormat.WAV);
		MediaHandler textMediaHandler = new MediaHandler(progressProperty,
				textMediaFile);
		textMediaHandler.setBackgroundTask(queue);
		textMediaHandler.setNameOfProcess("Simple Text to Speech");

		/* Constructing text to speech file */
		textMediaHandler.textToSpeech(text, scmFile);
		textMediaFile = textMediaHandler.getMediaFile();
		return textMediaFile;
	}

}
