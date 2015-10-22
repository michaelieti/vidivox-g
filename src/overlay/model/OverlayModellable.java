package overlay.model;

import javafx.collections.ObservableList;
import overlay.Commentary;
import overlay.control.OverlayType;

public interface OverlayModellable {

	/**
	 * Gets the list of commentaries. Can change return type later to ObservableList<? extends StagedMedia>.
	 * @return currently: a list of commentaries. in the future: list of all medias.
	 */
	public ObservableList<Commentary> getOverlayList();
	
	/**
	 * Gets the current OverlayType being filtered by the Filter box in the overlay.
	 * @return
	 */
	public OverlayType getFilterType();
	
}
