package overlay.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import overlay.Commentary;
import overlay.model.OverlayModellable;


/**
 * Handles logical type operations for the overlay functionality.
 * Does not attempt to affect the view of the overlay panel.
 * @author michael
 *
 */
public class OverlayController implements OverlayControllable {

	private OverlayModellable model;
	private OverlayView view;
	private static OverlayController singletonObject;
	
	/* constructors */
	
	public OverlayController(){
		super();
		singletonObject = this;
	}
	
	public OverlayController(OverlayModellable model){
		super();
		this.model = model;
		singletonObject = this;
	}
	
	/* singleton getter */

	public static OverlayController getOLController(){
		if (singletonObject == null){
			singletonObject = new OverlayController();
		}
		return singletonObject;
	}
	
	/* initializer */
	
	public void setView(OverlayView view){
		this.view = view;
	}
	public void setModel(OverlayModellable model){
		this.model = model;
	}
	/**
	 * Sets up the control bindings for the current overlay view.
	 */
	public void initialize(){
		//stick in all your bindings here!
		//TODO: bind filter drop box to overlay model
		bindTableModel();		//binding: tableview and commentary list
		setEditButton(view.editButton);		//eventhandler: edit button
		setDeleteButton(view.deleteButton);	//eventhandler:for delete button
		//TODO: register eventhandler for overlay button
	}
	
	/* interface methods */
	
	@Override
	public void filterList(OverlayType olType) {
		//TODO: sets filter off or onto a selected overlay type
	}

	@Override
	public void editSelectedCommentary() {
		SelectionModel<Commentary> selectionModel = view.getSelection();
		Commentary commentToEdit = selectionModel.getSelectedItem();
		//TODO: passes it to the editor controller class.
	}

	@Override
	public void editCommentary(Commentary commentary) {
		//TODO: pass the commentary to the editor controller class
	}
	@Override
	public void deleteSelectedCommentary() {
		SelectionModel<Commentary> selModel = view.getSelection();
		//delete the comment from the list
		if (selModel.getSelectedItem() != null){
			model.getOverlayList().remove(selModel.getSelectedItem());
		}
	}

	@Override
	public void commitOverlay() {
		// TODO: creates a new video, overlaid with the commentary.

	}


	@Override
	public void deleteCommentary(Commentary commentary) {
		model.getOverlayList().remove(commentary);
	}

	/* private initializer methods */
	
	private void bindTableModel(){
		ObservableList<Commentary> list = model.getOverlayList();
		view.typeCol.setCellValueFactory(new PropertyValueFactory<Commentary, String>("type"));
		view.timeCol.setCellValueFactory(new PropertyValueFactory<Commentary, String>("timeString"));
		view.nameCol.setCellValueFactory(new PropertyValueFactory<Commentary, String>("text"));
		
		view.tableView.setItems(list);
		view.reloadTable();
		
	}
	
	private void setEditButton(Button btn){
		btn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				editSelectedCommentary();
			}
		});
	}
	
	private void setDeleteButton(Button btn){
		btn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				deleteSelectedCommentary();
			}
		});
	}
	
	
}
