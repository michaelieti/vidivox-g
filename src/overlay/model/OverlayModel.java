package overlay.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import overlay.Commentary;
import overlay.control.OverlayType;

public class OverlayModel implements OverlayModellable {

	final private ObservableList<Commentary> commentaryList = 
			FXCollections.observableArrayList();
	private OverlayType currentFilter = OverlayType.NONE;
	
	@Override
	public ObservableList<Commentary> getOverlayList() {
		return commentaryList;
	}

	@Override
	public OverlayType getFilterType() {
		// TODO Auto-generated method stub
		return null;
	}

}
