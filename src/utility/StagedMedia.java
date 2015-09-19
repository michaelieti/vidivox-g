package utility;

import java.io.File;
import java.net.URI;

/**
 * A StagedMedia is linked to a physical video located in the local .temp folder.
 * StagedMedia allow the user to preview the effects of certain changes on the original media
 * @author adav194
 *
 */
public class StagedMedia {
	private URI uri;
	private File file;
	
	public StagedMedia(File path) {
		uri = path.toURI();
		this.file = path;
	}
	
	/*
	 * To add functionality
	 */

}
