package utility;

import java.io.File;
import java.net.URI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

/**
 * Creates an empty file in
 * @author adav194
 *
 */
abstract public class StagedMedia {
	protected URI uri;
	protected File file;
	protected Media media = null;
	
	
	public StagedMedia() {
		file = new File(System.getProperty("user.dir") + "/.temp/" + this.hashCode() + "." + getExtension());
		while (file.exists()) {
			file = new File(System.getProperty("user.dir") + "/.temp/" + file.hashCode() + "." + getExtension());
		}
		createFile();
		uri = file.toURI();
	}
	
	/**
	 * Creates an empty file with the correct name in .temp.
	 * Depending on which class extends this abstract class the type of created file will be different.
	 * e.g. StagedAudio creates an empty .wav
	 * @param path
	 */
	abstract protected void createFile();
	
	/**
	 * Depending on which class extends this abstract class this will return different values.
	 * e.g. StagedAudio will return ".wav"
	 * @return
	 */
	abstract protected String getExtension();
	
	public Media getMedia() throws Exception {
		if (media == null) {
			throw new Exception("This StagedMedia has not been assigned yet."); //Beca
		} else {
			return media;
		}
	}
	
	public void setMedia(String URI) {
		media = new Media(URI);
	}
	
}
