package overlay.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import overlay.Commentary;


/**
 * Handles logical type operations for the overlay functionality.
 * Does not attempt to affect the view of the overlay panel.
 * @author michael
 *
 */
public class OverlayController implements OverlayControllable {


	private static OverlayController singletonObject;
	final private ObservableList<Commentary> commentaryList = 
			FXCollections.observableArrayList();
	
	@Override
	public void filterList(OverlayType olType) {
		// TODO Auto-generated method stub
		// sets filter off or onto a selected overlay type
	}

	@Override
	public void editSelected() {
		//TODO: gets the selection model, 
		// obtains the selected comment, 
		// and passes it to the editor controller class.
	}

	@Override
	public void deleteSelected() {
		// TODO: gets the selection model,
		// obtains the selected comment
		// deletes it from the list + reupdates the table.
	}

	@Override
	public void commitOverlay() {
		// TODO: creates a new video, overlaid with the commentary.

	}

}
