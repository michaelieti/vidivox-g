package editor;

import utility.StagedMedia;
import javafx.scene.control.Tab;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * A BindableTab represents a panel of the Vidivox Editors panel which allows the user to make changes to
 * the original video. Each BindableTable contains the necessary functionality to generate a secondary media source (StagedMedia)
 * and merge this source with the original video.
 * @author adav194
 *
 */
abstract public class BindableTab extends Tab {
	
	/**
	 * Each Tab of the editor panel represents a process which makes changes to the original Media.
	 * This field represents a snapshot of changes thats are being tested out before they are finalized
	 * onto the original Media.
	 */
	protected StagedMedia stagedMedia;
	
	private MediaView mediaView;
	
	public BindableTab(MediaView mv) {
		super();
		mediaView = mv;
		this.setClosable(false);
	}
	
	public BindableTab(MediaView mv, String title) {
		super(title);
		mediaView = mv;
		this.setClosable(false);
	}
	
	/**
	 * This method specifies the specific type of StagedMedia which this tab shall generate.
	 * @return
	 */
	protected abstract void initStagedMedia();
	
	/**
	 * This method sets any necessary binds within the tab based on properties available to the encasing stage.
	 * This can be helpful if you want values in your tab (such as the size of textbox) to change according
	 * to the values of the encasing window (such as the windows width).
	 * @param toBindTo
	 */
	public abstract void setBind(Stage toBindTo);
	
	/**
	 * This method insures Tabs in the editor panel can access the MediaView (and its associated MediaPlayer and Media objects)
	 * @return
	 */
	public MediaView getMediaView() { 
		return mediaView;
	}
	
	/**
	 * This method takes all of the settings and values within this tab and processes them to produce all (if any) StagedMedia.
	 * For example, the SpeechTab will take the text within its TextArea and create an wav file of that speech (linked to a StagedMedia).
	 */
	public abstract void stageMedia();
	
	/**
	 * This method takes previously StagedMedia and combines it with the original Media source.
	 */
	public abstract void publishStage(StagedMedia media);
}

