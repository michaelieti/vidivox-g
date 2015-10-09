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
	
	public enum SkinColor {
		BLUE("/skins/BlueSkin.css"), GREEN("/skins/GreenSkin.css"), 
		ORANGE("/skins/OrangeSkin.css"), PURPLE("/skins/PurpleSkin.css");
		
		private String url;

		private SkinColor(String url) {
			this.url = url;
		}
		
		public String toURL() {
			String x = getClass().getResource(url).toExternalForm();
			return x;
		}
	}
	private VidivoxMedia vidiMedia;
	private VidivoxVideoControls vidiVidCtrl;
	private VidivoxFileControls vidiFileCtrl;
	private Launcher launcher;

	public MainStage(Launcher vl) {
		super();
		this.setTitle(Launcher.DEFAULT_TITLE);
		launcher = vl;
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(0, 0, 15, 0));
		grid.setGridLinesVisible(Launcher.GRID_IS_VISIBLE);

		vidiMedia = new VidivoxMedia();

		// FILE CONTROL BAR: ADDED TO TOP
		vidiFileCtrl = new VidivoxFileControls(this, vidiMedia);
		grid.add(vidiFileCtrl, 0, 0);

		// MEDIA VIEW NODE ADDED: CENTER
		grid.add(vidiMedia, 0, 1);

		// CONTROL PANEL ADDED: BOTTOM
		vidiVidCtrl = new VidivoxVideoControls(vidiMedia.getMediaView());
		grid.add(vidiVidCtrl, 0, 2);

		Scene s = new Scene(grid);

		// grid complete, set scene
		// MainStage contains the styling information for all the components of
		// MainStage that do not change
		s.getStylesheets().add(getClass().getResource("/skins/BlueSkin.css").toExternalForm());
		this.setScene(s);
		/*
		 * Setting the close action of this window to close the application
		 */
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
		});
	}

	public VidivoxMedia getMediaPane() {
		return vidiMedia;
	}
	
	public VidivoxVideoControls getVideoControls() {
		return vidiVidCtrl;
	}

	public Launcher getLauncher() {
		return launcher;
	}
	
	public void changeSkin(SkinColor sc){
		Scene scene = this.getScene();
		scene.getStylesheets().clear();			//remove all skins
		scene.getStylesheets().add(sc.toURL());	//reinstate a skin
	}

}
