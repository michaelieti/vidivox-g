package overlay;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

public class OverlayPanel extends Stage {

	//remember to change the generic type to something extending StagedMedia
	//following StagedMedia's clearing up
	private TableView<Commentary> tableView = new TableView<>();
	private TableColumn<Commentary, String> typeCol, timeCol, nameCol;
	
	public OverlayPanel(){
		super();
		
		OverlayController olc = OverlayController.getOLController();
		
		this.setTitle("Overlay");
		VBox mainPanel = new VBox();
		
		HBox filterPanel = new HBox();
		Text filterLabel = new Text("Filter by: ");
		//add in Filter text and filter box
		filterPanel.getChildren().add(filterLabel);
		
		//add in Commentary table
		typeCol = new TableColumn("Type");
		timeCol = new TableColumn("Time");
		nameCol = new TableColumn("Name/Text");
		bindColumns();
		tableView.getColumns().addAll(typeCol, timeCol, nameCol);
		
		olc.addCommentary(new Commentary(Duration.valueOf("1h"), "this is a test"));
		
		
		//add in Edit button and Delete button in HBox
		//add in Commit Overlay button
		
		
		mainPanel.getChildren().addAll(filterPanel,tableView);
		
		Scene sc = new Scene(mainPanel, 250, 600);
		this.setScene(sc);
		/*
		 * Setting the close operation for this window to simply hide it. This
		 * will allow the window to be reopened at a future stage
		 */
		final Stage s = this;
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				s.hide();
			}
		});
		
	}
	
	private void bindColumns(){
		ObservableList<Commentary> tableList = 
				OverlayController.getOLController().getCommentaryList();
		/* bind in all columns */
		typeCol.setCellValueFactory(new Callback(){
			@Override
			public SimpleStringProperty call(Object param) {
				//TODO: supposedly should return the type of the object.
				return new SimpleStringProperty("TTS");
			}
		});
		timeCol.setCellValueFactory(new PropertyValueFactory<Commentary, String>("timeString"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Commentary, String>("text"));
		
		/* set the data for the table */
		tableView.setItems(tableList);
	}
	
}
