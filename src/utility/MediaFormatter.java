package utility;

import java.net.URI;

public class MediaFormatter {
	
	static int MP4 = 1;

	/**
	 * Checks if a media located at a URI object is acceptable to be played
	 * by JavaFX's MediaPlayer and its related components.
	 * @param mediaPath - a URI object pointing to the media to be transformed
	 * @return - boolean on whether it is MP4 or FLV format
	 */
	public static boolean isFormatAcceptable(URI mediaPath){
		
		//TODO: attempt to check extension
		//if extension is mp4 or flv
		
		return false;
	}
	
	
	/**
	 * Takes a media file at the specified URI and transforms it to a specified file.
	 * @param mediaPath - a URI object to be transformed
	 * @param mediaType - MediaFormatter.MP4 or MediaFormatter.FLV
	 * @return URI object pointing to the reformatted file.
	 */
	public static URI transformMedia(URI mediaPath, int mediaType){
		
		//converts the file at the URI to an acceptable file type
		
		return null;
	}
	
	
}
