package player;

import java.io.File;

import main.control.MainControllable;
import skins.SkinColor;
import main.model.MainModelable;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class MainStage extends Stage implements MainControllable, MainModelable {

	private SkinColor currentSkinColor = skins.SkinColor.BLUE;
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
		s.getStylesheets().add(
				getClass().getResource("/skins/BlueSkin.css").toExternalForm());
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
		// TODO: comment this, wtf is this even
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
		 * StringBinding currentMedia = new When(vidiMedia.getMediaView()
		 * .mediaPlayerProperty().isNull()).then(Main.DEFAULT_TITLE) .otherwise(
		 * Main.DEFAULT_TITLE + " - " +
		 * vidiMedia.getMediaView().getMediaPlayer() .getMedia().getSource());
		 * this.titleProperty().bind(currentMedia);
		 */
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

	@Override
	public MediaView getMediaView() {
		return vidiMedia.getMediaView();
	}

	@Override
	public MediaPlayer getMediaPlayer() {
		return getMediaView().getMediaPlayer();
	}

	@Override
	public Media getMedia() {
		return getMediaPlayer().getMedia();
	}

	@Override
	public Duration getDuration() {
		return getMedia().getDuration();
	}

	@Override
	public ReadOnlyObjectProperty<Duration> getCurrentTimeProperty() {
		return getMediaPlayer().currentTimeProperty();
	}

	@Override
	public DoubleProperty getVolumeProperty() {
		return getMediaPlayer().volumeProperty();
	}

	@Override
	public void setMedia(Media media) {
		VidivoxPlayer.getVPlayer().setMedia(media);
	}

	@Override
	public void setTime(Duration time) {
		getMediaPlayer().seek(time);
	}

	@Override
	public void setVolume(double vol) {
		getMediaPlayer().setVolume(vol);

	}

	@Override
	public void setMute(boolean mute) {
		getMediaPlayer().setMute(mute);

	}

	@Override
	public BooleanProperty hasMedia() {
		BooleanBinding validMediaBinding = new When(getMediaPlayer()
				.statusProperty().isEqualTo(MediaPlayer.Status.UNKNOWN)).then(
				false).otherwise(true);
		BooleanBinding mediaBinding = new When(getMediaView()
				.mediaPlayerProperty().isNull()).then(false).otherwise(
				validMediaBinding);
		BooleanProperty mediaProperty = new SimpleBooleanProperty(false);
		mediaProperty.bind(mediaBinding);
		return mediaProperty;
	}

	@Override
	public void play() {
		getMediaPlayer().play();
	}

	@Override
	public void ffwd() {

	}

	@Override
	public void rwd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		getMediaPlayer().stop();

	}

	@Override
	public void setSkinColor(SkinColor color) {
		// TODO Auto-generated method stub
		
	}


}
