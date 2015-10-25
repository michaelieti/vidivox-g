package overlay.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.util.Duration;
import overlay.Commentary;
import utility.control.MediaHandler;
import utility.control.SchemeFile;
import utility.media.MediaFile;
import utility.media.MediaFormat;

public class OverlayCommitter extends Application {
	
	Media originalVideo;
	List<Commentary> commentaryList;
	
	/* for testing purposes! */
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Hello! Now begins the test....");
		//make a new overlay committer
		
		OverlayCommitter oc = new OverlayCommitter();
		
		String uriString = System.getProperty("user.home") + "/workspace/vidivox/vidivox/vid.mp4";
		System.out.println(uriString);
		File file = new File(uriString);
		//make new commentary list
		//provide a new video
		List<Commentary> list = new ArrayList<Commentary>();
			list.add(new Commentary (Duration.valueOf("5s"), "Hello", OverlayType.TTS));
			list.add(new Commentary (Duration.valueOf("10s"), "How's it going?", OverlayType.TTS));
			list.add(new Commentary (Duration.valueOf("15s"), "Test commentary", OverlayType.TTS));
		Media video = new Media(file.toURI().toString());
		
		
		oc.addCommentaryList(list);
		oc.addVideo(video);
		
		oc.beginCommit();
		//begin the commit
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	/* end testing purposes ;_; */
	
	public OverlayCommitter addVideo(Media originalVideo){
		this.originalVideo = originalVideo;
		return this;
	}
	
	public OverlayCommitter addCommentaryList(List<Commentary> commentaryList){
		this.commentaryList = commentaryList;
		return this;
	}
	
	public MediaFile beginCommit(){
		if (originalVideo == null){
			System.out.println("Media supplied was null!");
			return null;
		}
		if (commentaryList == null || commentaryList.size() == 0){
			System.out.println("List size is zero");
			if (commentaryList.size() == 0) { return null; }
			//check all other fields in the future and set flags as appropriate
		}

		// TODO: creates a new video, overlaid with the commentary.
		// iterate through list
		// first commentary object in list: text2wav into mediaFile_text
		// get time property, use MediaFile -> MediaHandler.blankAudio -> extract to mediaFile_blankOffset
		// new MediaFile -> MediaHandler.concatAudio(mediaFile_blankOffset, mediaFile_text )
		//						`-------> extract to new MediaFile

		
		//define the comment list iterator
		Iterator<Commentary> cliterator = commentaryList.iterator();
		Commentary comment1 = cliterator.next();
		MediaFile prevMedia = null;
		
		try{	
					// 1 - make the blank audio
			Duration blankTime = comment1.getTime();
			MediaFile blankFile = makeBlank(blankTime);
			
					// 2 - make the text to speech
			MediaFile speechFile = simpleMakeSpeech(comment1.getText());
			
					// 3 - concatenate the blank file and the speech file
			MediaFile firstComment = concat(blankFile, speechFile);
			
			//now we have the first comment with the audio offset.
			
					// 4 - iterate through rest of list and append onto this first comment.
			prevMedia = firstComment;
			while (cliterator.hasNext()){
				Commentary currentCommentary = cliterator.next();
				//get previous media duration
				double prevDuration = prevMedia.getDuration();
				//get current commentary start time
				Duration timeOffset = currentCommentary.getTime();
				// use these two to get the amount of blank media in between
				Duration fillerDuration = Duration.seconds(timeOffset.toSeconds() - prevDuration);
				// make the blank media
				MediaFile fillerMediaFile = makeBlank(fillerDuration);
				// make the speech file
				MediaFile currentSpeechFile = simpleMakeSpeech(currentCommentary.getText());
				// concatenate blank media and speech file
				MediaFile fillerSpeechConcatenated = concat(fillerMediaFile, currentSpeechFile);
				// concatenate previous file and current file
				MediaFile prevAndCurrentConcat = concat(prevMedia, fillerSpeechConcatenated);
				// store as new previous file
				prevMedia = prevAndCurrentConcat;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return prevMedia;
		
	}
	
	private MediaFile concat(MediaFile first, MediaFile second) throws Exception{
		MediaFile concatenatedFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		MediaHandler concatenatedHandler = new MediaHandler(concatenatedFile);
		concatenatedHandler.concatAudio(first, second);
		//do we need to wait for concatenation? yes, probably ;_;
		
		return concatenatedHandler.getMediaFile();
	}
	
	private MediaFile makeBlank(Duration blankTime) throws Exception{
		MediaHandler blankMediaHandler;
		MediaFile blankFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		blankMediaHandler = new MediaHandler(blankFile);
		blankMediaHandler.makeBlankAudio(blankTime);
		//do we need to wait for it to make the audio?
		blankFile = blankMediaHandler.getMediaFile();
		//blankFile now holds a blank file
		return blankFile;	
	}
	
	private MediaFile simpleMakeSpeech(String text) throws Exception{

		SchemeFile scmFile = new SchemeFile();
		scmFile.writeToDisk();
		
		MediaFile textMediaFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		MediaHandler textMediaHandler = new MediaHandler(textMediaFile);
		textMediaHandler.textToSpeech(text, scmFile);
		//do we need to wait for this?
		textMediaFile = textMediaHandler.getMediaFile();
		//textMediaFile now holds the speech wav
		return textMediaFile;
	}

}
