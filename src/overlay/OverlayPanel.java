package overlay;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class OverlayPanel extends Stage {
	
	public OverlayPanel(){
		super();
		this.setTitle("Overlay");
		VBox mainPanel = new VBox();
		
		HBox filterPanel = new HBox();
		Text filterLabel = new Text("Filter by: ");
		//add in Filter text and filter box
		filterPanel.getChildren().add(filterLabel);
		
		//add in Commentary table
		//add in Edit button and Delete button in HBox
		//add in Commit Overlay button
		
		
		mainPanel.getChildren().add(filterPanel);
		
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
	
}
