package overlay.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import overlay.Commentary;
import overlay.control.OverlayType;

/**
 * Holds the data used to create the Overlay Panel, including the overlay table
 * and the current table filter.
 * @author michael
 *
 */
public class OverlayModel implements OverlayModellable {

	final private ObservableList<Commentary> commentaryList = 
			FXCollections.observableArrayList();
	private OverlayType currentFilter = OverlayType.NONE;
	
	public OverlayModel(){
		super();
//		commentaryList.add(new Commentary(Duration.seconds(10.0), "Test string", OverlayType.TTS));
	}
	
	@Override
	public ObservableList<Commentary> getOverlayList() {
		return commentaryList;
	}

	@Override
	public OverlayType getFilterType() {
		return currentFilter;
	}

}
