package overlay.control;

import java.util.Iterator;
import java.util.List;

import javafx.scene.media.Media;
import javafx.util.Duration;
import overlay.Commentary;
import utility.control.MediaHandler;
import utility.control.SchemeFile;
import utility.media.MediaFile;
import utility.media.MediaFormat;

public class OverlayCommitter {
	
	Media originalVideo;
	List<Commentary> commentaryList;
	
	
	public OverlayCommitter addVideo(Media originalVideo){
		this.originalVideo = originalVideo;
		return this;
	}
	
	public OverlayCommitter addCommentaryList(List<Commentary> commentaryList){
		this.commentaryList = commentaryList;
		return this;
	}
	
	public void beginCommit(){
		if (originalVideo == null){
			System.out.println("Media supplied was null!");
			return;
		}
		if (commentaryList == null || commentaryList.size() == 0){
			System.out.println("List size is zero");
			if (commentaryList.size() == 0) { return; }
			//check all other fields in the future and set flags as appropriate
		}

		// TODO: creates a new video, overlaid with the commentary.
		// iterate through list
		// first commentary object in list: text2wav into mediaFile_text
		// get time property, use MediaFile -> MediaHandler.blankAudio -> extract to mediaFile_blankOffset
		// new MediaFile -> MediaHandler.concatAudio(mediaFile_blankOffset, mediaFile_text )
		//						`-------> extract to new MediaFile

		
		//first comment - get duration, make blank wav, make text2wav, concat both.
		//store this somewhere.
		Iterator<Commentary> cliterator = commentaryList.iterator();
		Commentary comment1 = cliterator.next();
		SchemeFile scmFile = new SchemeFile();
		try{	
		scmFile.writeToDisk();
				// 1 - make the blank audio
		Duration blankTime = comment1.getTime();
		MediaHandler blankMediaHandler;
		MediaFile blankFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		blankMediaHandler = new MediaHandler(blankFile);
		blankMediaHandler.makeBlankAudio(blankTime);
		//do we need to wait for it to make the audio?
		blankFile = blankMediaHandler.getMediaFile();
		//blankFile now holds a blank file
		
				// 2 - make the text to speech
		MediaFile textMediaFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		MediaHandler textMediaHandler = new MediaHandler(textMediaFile);
		textMediaHandler.textToSpeech(comment1.getText(), scmFile);
		//do we need to wait for this?
		textMediaFile = textMediaHandler.getMediaFile();
		//textMediaFile now holds the speech wav
		
				// 3 - concatenate the blank file and the speech file
		MediaFile concatenatedFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		MediaHandler concatenatedHandler = new MediaHandler(concatenatedFile);
		concatenatedHandler.concatAudio(blankFile, textMediaFile);
		//do we need to wait for concatenation? yes probably
		
		MediaFile firstComment = concatenatedHandler.getMediaFile();
		
		//now we have the first comment with the audio offset.
		
		//iterate through rest of list and append onto this first comment.
		
		
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
