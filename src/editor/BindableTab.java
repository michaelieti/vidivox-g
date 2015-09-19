package editor;

import utility.StagedMedia;
import javafx.scene.control.Tab;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * Tabs which extend this class are eligble to be used in the vidivox editors panel.
 * The required abstract methods allow the tabs to interact correctly with supplied media.
 * @author adav194
 *
 */
abstract public class BindableTab extends Tab {
	
	public BindableTab() {
		super();
	}
	
	public BindableTab(String title) {
		super(title);
	}
	
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
	public abstract MediaView getMediaView();
	
	/**
	 * This method insures that the Tab can 'stage' any change it has made to its associated Media object.
	 * Instead of making changes directly to the media file, the relevant sections of the editted Media are
	 * processed separately. This allows the user to preview any changes s/he has made before saving them to the
	 * original media.
	 * @return
	 */
	public abstract boolean stageMedia();
	
	public abstract void previewMedia(StagedMedia media);
	
	
}

