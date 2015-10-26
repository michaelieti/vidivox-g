package overlay.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.util.Duration;
import overlay.Commentary;
import player.Main;
import utility.control.MediaHandler;
import utility.control.SchemeFile;
import utility.media.MediaFile;
import utility.media.MediaFormat;

public class OverlayCommitter extends Application {
	
	Media originalVideo;
	List<Commentary> commentaryList;
	DoubleProperty progressProperty = new SimpleDoubleProperty(0);
	
	/* for testing purposes! */
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Hello! Now begins the test....");
		//make a new overlay committer
		
		OverlayCommitter oc = new OverlayCommitter();
		
		String uriString = System.getProperty("user.home") + "/SoftEng206/vidivox/vid.mp4";
		System.out.println(uriString);
		File file = new File(uriString);
		System.out.println();
		//make new commentary list
		//provide a new video
		List<Commentary> list = new ArrayList<Commentary>();
			list.add(new Commentary (Duration.valueOf("1s"), "Test commentary", OverlayType.TTS));
			list.add(new Commentary (Duration.valueOf("1s"), "How's it going?", OverlayType.TTS));
			list.add(new Commentary (Duration.valueOf("1s"), "Test commentary", OverlayType.TTS));
			
		Media video = new Media(file.toURI().toString());
		oc.addCommentaryList(list);
		oc.addVideo(video);
		oc.beginCommit();
		
	}

	public static void main(String[] args) throws Exception {
		Main.InitTemp();
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
	
	public OverlayCommitter addProgressProperty(DoubleProperty doubleprop){
		this.progressProperty = doubleprop;
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
		MediaFile allComments = null;
		MediaFile mergedVideo = null;
		List<MediaFile> toBeMerged = new ArrayList<MediaFile>();
		try {
			MediaFile finalOutput = MediaFile.createMediaContainer(MediaFormat.WAV);
			MediaHandler finalHandler = new MediaHandler(finalOutput);
			for (Commentary c: commentaryList) {
				MediaFile blankFile = makeBlank(c.getTime());
				MediaFile speechFile = simpleMakeSpeech(c.getText());
				
				MediaFile commentFile = concat(blankFile, speechFile);
				System.out.println("File Info:\n\tLocation: " + commentFile.getQuoteOfAbsolutePath() + "\n\tBlank: " + blankFile.getQuoteOfAbsolutePath() + "\n\tSpeech: " + speechFile.getQuoteOfAbsolutePath());
				toBeMerged.add(commentFile);
			}
			MediaFile[] array = new MediaFile[toBeMerged.size()];
			array = (MediaFile[]) toBeMerged.toArray(array);
			Thread.sleep(1000);
			finalHandler.mergeAudio(array);
			MediaFile audio = finalHandler.getMediaFile();
			System.out.println("File Info:\n\tLocation: " + finalHandler.getMediaFile().getQuoteOfAbsolutePath());
			MediaFile orginal = new MediaFile(originalVideo);
			
			MediaFile finalVideo = MediaFile.createMediaContainer(MediaFormat.MP4);
			System.out.println("File Info:\n\tLocation: " + finalVideo.getQuoteOfAbsolutePath());
			MediaHandler videoHandler = new MediaHandler(finalVideo);
			videoHandler.mergeAudioAndVideo(orginal, audio);
			System.out.println("File Info:\n\tLocation: " + videoHandler.getMediaFile().getQuoteOfAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
		try{	
			/*
					// 1 - make the blank audio
			Duration blankTime = comment1.getTime();
			MediaFile blankFile = makeBlank(blankTime);
			System.out.println("######################Blank File for " + blankTime + " > " + blankFile.getQuoteOfAbsolutePath());
			
					// 2 - make the text to speech
			MediaFile speechFile = simpleMakeSpeech(comment1.getText());
			System.out.println("######################Speech File > " + speechFile.getQuoteOfAbsolutePath());
			
					// 3 - concatenate the blank file and the speech file
			MediaFile firstComment = concat(blankFile, speechFile);
			System.out.println("######################Comment File > " + firstComment.getQuoteOfAbsolutePath());
			
			//now we have the first comment with the audio offset.
			
					// 4 - iterate through rest of list and append onto this first comment.
			allComments = firstComment;
			*/
//			while (cliterator.hasNext()){
//				Commentary currentCommentary = cliterator.next();
//				//get previous media duration
//				double prevDuration = allComments.getDuration();
//				//get current commentary start time
//				Duration timeOffset = currentCommentary.getTime();
//				// use these two to get the amount of blank media in between
//				Duration fillerDuration = Duration.seconds(timeOffset.toSeconds() - prevDuration);
//				// make the blank media
//				MediaFile fillerMediaFile = makeBlank(fillerDuration);
//				// make the speech file
//				MediaFile currentSpeechFile = simpleMakeSpeech(currentCommentary.getText());
//				Thread.sleep(2000);
//				// concatenate blank media and speech file
//				MediaFile fillerSpeechConcatenated = concat(fillerMediaFile, currentSpeechFile);
//				System.out.println("######################Comment File > " + fillerSpeechConcatenated.getQuoteOfAbsolutePath());
//				// concatenate previous file and current file
//				Thread.sleep(2000);
//				MediaFile prevAndCurrentConcat = concat(allComments, fillerSpeechConcatenated);
//				Thread.sleep(2000);
//				System.out.println("######################Concat File > " + prevAndCurrentConcat.getQuoteOfAbsolutePath());
//				// store as new previous file
//				allComments = prevAndCurrentConcat;
//			}	//end while
//			
//			//the MediaFile object 'allComments' now holds the audio for all comments spaced out
//			// by appropriately lengthed intervals.
//			
//					// 5 - merge video (from originalVideo, a Media object)
//					// and audio (from all 
//			System.out.println("########### All Comment  " + allComments.getQuoteOfAbsolutePath());
//			// create new media container (video)
//			MediaFile mergedVideoContainer = MediaFile.createMediaContainer(MediaFormat.MP4);
//			//  pass into new handler (with property)
//			MediaHandler mergedVideoHandler = new MediaHandler(progressProperty, mergedVideoContainer);
//			// mergeVideoAndAudio(video, audio)
//			MediaFile originalMediaFile = new MediaFile(originalVideo);
//			mergedVideoHandler.mergeAudioAndVideo(originalMediaFile, allComments);
//			
//			mergedVideo = mergedVideoHandler.getMediaFile();
//			System.out.println("########### " + mergedVideo.getQuoteOfAbsolutePath());
			
			//get allComments MediaFile
			//get the media file from originalVideo
				//turn that into a MediaFile object
			//merge the two using a new media file
			//return the media file
			
			//assume that the MediaFile object mergedVideo holds the video overlaid with audio

		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return mergedVideo;
		
	}
	
	private MediaFile concat(MediaFile first, MediaFile second) throws Exception{
		MediaFile concatenatedFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		MediaHandler concatenatedHandler = new MediaHandler(progressProperty, concatenatedFile);
		concatenatedHandler.concatAudio(first, second);
		concatenatedHandler.waitFor();
		//do we need to wait for concatenation? yes, probably ;_;
		
		return concatenatedHandler.getMediaFile();
	}
	
	
	private MediaFile makeBlank(Duration blankTime) throws Exception{
		MediaHandler blankMediaHandler;
		MediaFile blankFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		blankMediaHandler = new MediaHandler(progressProperty, blankFile);
		blankMediaHandler.makeBlankAudio(blankTime);
		blankMediaHandler.waitFor();
		//do we need to wait for it to make the audio?
		blankFile = blankMediaHandler.getMediaFile();
		//blankFile now holds a blank file
		return blankFile;	
	}
	
	private MediaFile simpleMakeSpeech(String text) throws Exception{

		SchemeFile scmFile = new SchemeFile();
		scmFile.writeToDisk();
		
		MediaFile textMediaFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		MediaHandler textMediaHandler = new MediaHandler(progressProperty, textMediaFile);
		textMediaHandler.textToSpeech(text, scmFile);
		textMediaHandler.waitFor();
		textMediaFile = textMediaHandler.getMediaFile();
		//textMediaFile now holds the speech wav
		return textMediaFile;
	}

}
