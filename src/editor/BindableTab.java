package editor;

import javafx.scene.control.Tab;
import javafx.stage.Stage;

/**
 * Tabs which extend this class are able to bind their context nodes with a
 * setBind() function.
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
	
	public abstract void setBind(Stage toBindTo);
	
}

