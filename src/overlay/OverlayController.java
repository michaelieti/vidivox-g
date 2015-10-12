package overlay;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Co-ordinates message sending between the OverlayWindow and the Editor Panel,
 * where the messages are mainly OverlayWindow updating.
 * @author michael
 *
 */
public class OverlayController {
	
	private static OverlayController singletonObject;
	final private ObservableList<Commentary> commentaryList = FXCollections.observableArrayList();
	
	public static OverlayController getOLController(){
		if (singletonObject == null){
			singletonObject = new OverlayController();
		}
		return singletonObject;
	}
	
	private OverlayController(){
		singletonObject = this;
		//initialize the controller
	}
	
	public void addCommentary(Commentary newCommentary){
		commentaryList.add(newCommentary);
		updateTable();
	}
	public void deleteCommentary(Commentary commentary){
		commentaryList.remove(commentary);
		updateTable();
	}
	
	public void updateTable(){
		//TODO: looks at the list and reupdates the table
		
	}
	
	public void setCommentaryTable(TableView<Commentary> table){
		table.setItems(commentaryList);
	}
	
	public ObservableList<Commentary> getCommentaryList(){
		return commentaryList;
	}
	
	public void commitOverlay(){
		//TODO: creates the mp3/wav/whatever
		// from the list of commentaries
		//and sticks it onto the current video
		
		/* create a duration marker
		 * create the commentary file
		 * from the file, read off times = create an initial blank audio for buffer
		 * create new audio and attach to end
		 * get the total duration and continue to attach blank - new audio - blank - new audio
		 * */
	}
	
	public File createCommentaryFile(){
		//TODO: creates a formatted commentary file
		//that can be read easily from the command line or bash
		return null;
	}
	
	
	
	
}
