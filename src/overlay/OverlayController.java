package overlay;

import java.util.ArrayList;

/**
 * Co-ordinates message sending between the OverlayWindow and the Editor Panel,
 * where the messages are mainly OverlayWindow updating.
 * @author michael
 *
 */
public class OverlayController {
	
	private static OverlayController singletonObject;
	private ArrayList<Commentary> commentaryList;
	
	public static OverlayController getOLController(){
		if (singletonObject == null){
			singletonObject = new OverlayController();
		}
		return singletonObject;
	}
	
	private OverlayController(){
		singletonObject = this;
		commentaryList = new ArrayList<Commentary>();
		//initialize the controller
	}
	
	public void addCommentary(Commentary newCommentary){
		commentaryList.add(newCommentary);
		updateTable();
	}
	public void deleteCommentary(){
		//TODO: implement a deletion
		updateTable();
	}
	
	public void updateTable(){
		//TODO: looks at the list and reupdates the table
	}
	
	public void commitOverlay(){
		//TODO: creates the mp3/wav/whatever
		// from the list of commentaries
		//and sticks it onto the current video
	}
	
	
}
