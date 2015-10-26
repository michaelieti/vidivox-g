package player;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
	private MediaPanel mediaPanel;
	private VidivoxVideoControls controlPanel;
	private VidivoxFileControls filePanel;
	private Main main;
	final private MainStage mainStage;

	public MainStage(Main appLauncher) {
		super();
		this.setTitle(Main.DEFAULT_TITLE);
		main = appLauncher;
		mainStage = this;
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(0, 0, 15, 0));

		mediaPanel = new MediaPanel();
		

		// FILE CONTROL BAR: ADDED TO TOP
		filePanel = new VidivoxFileControls(this, mediaPanel);
		borderPane.setTop(filePanel);

		// MEDIA VIEW NODE ADDED: CENTER
		borderPane.setCenter(mediaPanel);

		// CONTROL PANEL ADDED: BOTTOM
		controlPanel = new VidivoxVideoControls(mediaPanel.getMediaView());
		borderPane.setBottom(controlPanel);

		//sets up the VidivoxPlayer class to start taking inputs
		VidivoxPlayer.getVPlayer().
			setControlPanel(controlPanel).
			setFilePanel(filePanel).
			setMediaPanel(mediaPanel);
		
		heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				VidivoxPlayer.getVPlayer().getMediaView()
				.setFitHeight(newValue.doubleValue() - 180);
			}
		});
		widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				VidivoxPlayer.getVPlayer().getMediaView().
				setFitWidth(newValue.doubleValue());
			}
		});
		
		Scene s = new Scene(borderPane);

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
		this.setOnCloseRequest(new ClosureHandler());

		this.iconifiedProperty().addListener(new IconificationListener());
		setCurrentSkinColor(SkinColor.BLUE);

	}

	public MediaPanel getMediaPane() {
		return mediaPanel;
	}

	public VidivoxVideoControls getVideoControls() {
		return controlPanel;
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

	
	private class IconificationListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> ov,
				Boolean oldValue, Boolean newValue) {
			if (!newValue) {

				main.getEditor().setIconified(false);

				main.getOverlay().setIconified(false);
				mainStage.setIconified(false);
			} else {
				main.getEditor().setIconified(true);
				main.getOverlay().setIconified(true);
			}
		}
	}
	
	private class ClosureHandler implements EventHandler<WindowEvent>{
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
	}
}
