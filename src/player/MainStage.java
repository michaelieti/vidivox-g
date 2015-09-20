package player;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
		grid.setPadding(new Insets(25,25,15,25));
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
		s.getStylesheets().add(getClass().getResource("/player/sheet.css").toExternalForm());
		this.setScene(s);
	}
	
	public VidivoxMedia getMediaPane(){
		return vidiMedia;
	}
}

