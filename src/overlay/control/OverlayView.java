package overlay.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import overlay.Commentary;


/**
 * The class describing the entire view of the Overlay Panel.
 * Without further modification or application of event handlers,
 * this class is basically a click dummy.
 * @author michael
 *
 */
public class OverlayView extends Stage {
	
	TableView<Commentary> tableView = new TableView<>();
	TableColumn<Commentary, String> typeCol, timeCol, nameCol;
	Button editButton, deleteButton, commitButton;
	
	@SuppressWarnings("unchecked")
	public OverlayView(){
		super();
		
		this.setTitle("Overlay");
		VBox mainPanel = new VBox();
		
		/* Filter Panel creation */
		HBox filterPanel = new HBox();
		Text filterLabel = new Text("Filter by: ");
		filterPanel.getChildren().add(filterLabel);
		
		/* Overlay Table creation */
		typeCol = new TableColumn<Commentary, String>("Type");
		timeCol = new TableColumn<Commentary, String>("Time");
		nameCol = new TableColumn<Commentary, String>("Name/Text");
		tableView.getColumns().addAll(typeCol, timeCol, nameCol);
		tableView.setPlaceholder(new Label("No commits added"));
		
		
		/* creation of overlay editor panel*/
		HBox editBox = new HBox();
		/* EDIT BUTTON*/
		editButton = new Button("Edit selected");
		/* DELETE BUTTON*/
		deleteButton = new Button("Delete selected");
		editBox.getChildren().addAll(editButton, deleteButton);
		
		mainPanel.getChildren().addAll(filterPanel,tableView, editBox, commitButton);
		
		Scene sc = new Scene(mainPanel, 250, 600);
		this.setScene(sc);
		
		final Stage s = this;
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				s.hide();
			}
		});
		
		this.setOnShowing(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				s.setX(Screen.getPrimary().getVisualBounds().getMaxX() - 250);
				s.setY(Screen.getPrimary().getVisualBounds().getMaxY() / 2 - 200);
				
			}
			
		});
		
	}

	@SuppressWarnings("unchecked")
	public void reloadTable(){
		tableView.getColumns().clear();
		tableView.getColumns().addAll(typeCol, timeCol, nameCol);
	}
	
	public TableViewSelectionModel<Commentary> getSelection(){
		TableViewSelectionModel<Commentary> selectionModel = tableView.getSelectionModel();
		return selectionModel;
	}

	
}
