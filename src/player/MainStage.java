package player;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainStage extends Stage {
	
	String URL = null;
	private VidivoxMedia vidiMedia;
	private VidivoxVideoControls vidiVidCtrl;
	private VidivoxFileControls vidiFileCtrl;
	MediaPlayer mp;
	Media currentMedia;
	
	public MainStage(VidivoxLauncher vl) {
		super();
		this.setTitle(VidivoxLauncher.DEFAULT_TITLE);
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);	
		grid.setHgap(10);
		grid.setPadding(new Insets(25,25,15,25));
		grid.setGridLinesVisible(VidivoxLauncher.GRID_IS_VISIBLE);

		//FILE CONTROL BAR: ADDED TO TOP
		vidiFileCtrl = new VidivoxFileControls(vl);
		grid.add(vidiFileCtrl,  0, 0);

		
		//MEDIA VIEW NODE ADDED: CENTER
		vidiMedia = new VidivoxMedia();
		grid.add(vidiMedia,0,1);
		
		//CONTROL PANEL ADDED: BOTTOM
		vidiVidCtrl = new VidivoxVideoControls(vl);
		grid.add(vidiVidCtrl, 0,2);
		
		//grid complete, set scene
		this.setScene(new Scene(grid));
	}
	
	protected VidivoxMedia getMediaPane(){
		return vidiMedia;
	}
}

