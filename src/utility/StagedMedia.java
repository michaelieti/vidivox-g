package utility;

import java.io.File;
import javafx.scene.media.Media;

/**
 * Creates an empty file in
 * @author adav194
 *
 */
abstract public class StagedMedia {
	/**
	 * This field represents the physical location of the StagedMedia. The URI and path to
	 * this location can be extracted from the File object.
	 */
	protected File file; //Path to physical file in vidivox/.temp
	/**
	 * The Media which is linked to the piece of StagedMedia being examined. Media objects
	 * can be handled by MediaPlayer and MediaView.
	 */
	protected Media media; //To change media, use ffmpeg from processbuilder and output to file.getPath()
	
	public StagedMedia() {
		file = new File(System.getProperty("user.dir") + "/.temp/" + this.hashCode() + "." + getExtension());
		while (file.exists()) {
			file = new File(System.getProperty("user.dir") + "/.temp/" + file.hashCode() + "." + getExtension());
		}
		createFile();
		media = new Media(file.toURI().toString());
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
			throw new Exception("This StagedMedia has not been assigned yet."); //B
		} else {
			return media;
		}
	}
	
	public void setMedia(String URI) {
		media = new Media(URI);
	}
	
	public File getFile() { 
		return file;
	}
	
}
