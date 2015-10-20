package player;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainStage extends Stage {

	public enum SkinColor {
		BLUE("/skins/BlueSkin.css"), GREEN("/skins/GreenSkin.css"), ORANGE(
				"/skins/OrangeSkin.css"), PURPLE("/skins/PurpleSkin.css");

		private String url;

		private SkinColor(String url) {
			this.url = url;
		}

		public String toURL() {
			String x = getClass().getResource(url).toExternalForm();
			return x;
		}
	}

	private SkinColor currentSkinColor = SkinColor.BLUE;
	private MediaPanel vidiMedia;
	private VidivoxVideoControls vidiVidCtrl;
	private VidivoxFileControls vidiFileCtrl;
	private Main main;

	public MainStage(Main appLauncher) {
		super();
		this.setTitle(Main.DEFAULT_TITLE);
		main = appLauncher;
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(0, 0, 15, 0));
		grid.setGridLinesVisible(Main.GRID_IS_VISIBLE);

		vidiMedia = new MediaPanel();
		

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
		s.getStylesheets()
				.add(getClass().getResource("/skins/BlueSkin.css")
						.toExternalForm());
		this.setScene(s);
		/*
		 * Setting the close action of this window to close the application
		 */
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				/*
				 * The purpose of this code is to clean up the temporary folder,
				 * ready for the next launch.
				 */
				File p = new File(System.getProperty("user.dir") + "/.temp/");
				if (p.exists() && p.isDirectory()) {
					for (File f : p.listFiles()) {
						f.delete();
					}
				}
				p.delete();
				Platform.exit();
			}
		});
		final Stage current = this;
		//TODO: comment this, wtf is this even
		this.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean oldValue, Boolean newValue) {
				if (!newValue) {

					main.getEditor().setIconified(false);

					main.getOverlay().setIconified(false);
					current.setIconified(false);
				} else {
					main.getEditor().setIconified(true);
					main.getOverlay().setIconified(true);
				}
			}
		});
		setCurrentSkinColor(SkinColor.BLUE);
		/*
		StringBinding currentMedia = new When(vidiMedia.getMediaView()
				.mediaPlayerProperty().isNull()).then(Main.DEFAULT_TITLE)
				.otherwise(
						Main.DEFAULT_TITLE
								+ " - "
								+ vidiMedia.getMediaView().getMediaPlayer()
										.getMedia().getSource());
		this.titleProperty().bind(currentMedia); */
	}

	public MediaPanel getMediaPane() {
		return vidiMedia;
	}

	public VidivoxVideoControls getVideoControls() {
		return vidiVidCtrl;
	}

	public Main getLauncher() {
		return main;
	}

	public void changeSkin(SkinColor sc) {
		setCurrentSkinColor(sc);
		Scene scene = this.getScene();
		scene.getStylesheets().clear(); // remove all skins
		scene.getStylesheets().add(sc.toURL()); // reinstate a skin

		Scene editScene = main.getEditor().getScene();
		editScene.getStylesheets().clear(); // remove all skins
		editScene.getStylesheets().add(sc.toURL()); // reinstate a skin

	}

	public SkinColor getCurrentSkinColor() {
		return currentSkinColor;
	}

	public void setCurrentSkinColor(SkinColor currentSkinColor) {
		this.currentSkinColor = currentSkinColor;
	}

}
