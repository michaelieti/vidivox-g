package player;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainStage extends Stage {
	
	private VidivoxMedia vidiMedia;
	private VidivoxVideoControls vidiVidCtrl;
	private VidivoxFileControls vidiFileCtrl;

	public MainStage(VidivoxLauncher vl) {
		super();
		this.setTitle(VidivoxLauncher.DEFAULT_TITLE);
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);	
		grid.setHgap(10);
		grid.setPadding(new Insets(0,0,15,0));
		grid.setGridLinesVisible(VidivoxLauncher.GRID_IS_VISIBLE);
		
		vidiMedia = new VidivoxMedia();
				
		//FILE CONTROL BAR: ADDED TO TOP
		vidiFileCtrl = new VidivoxFileControls(this, vidiMedia.getMediaView());
		grid.add(vidiFileCtrl,  0, 0);

		//MEDIA VIEW NODE ADDED: CENTER
		grid.add(vidiMedia,0,1);
		
		
		//CONTROL PANEL ADDED: BOTTOM
		vidiVidCtrl = new VidivoxVideoControls(vidiMedia.getMediaView());
		grid.add(vidiVidCtrl, 0,2);
		
		Scene s = new Scene(grid);
		
		//grid complete, set scene
		//MainStage contains the styling information for all the components of MainStage that do not change
		s.getStylesheets().add(getClass().getResource("/skins/MainStage.css").toExternalForm());
		this.setScene(s);
		/*
		 * Setting the close action of this window to close the application
		 */
		this.setOnCloseRequest(new EventHandler<WindowEvent>(){
		    @Override
		    public void handle(WindowEvent event) {
		        Platform.exit();
		    }
		});
	}
	
	public VidivoxMedia getMediaPane(){
		return vidiMedia;
	}
	
}

