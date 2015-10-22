package overlay.control;

import overlay.Commentary;

public interface OverlayControllable {
	
	public void filterList(OverlayType olType);
	
	public void editSelectedCommentary();
	
	public void addCommentary(Commentary commentary);
	
	public void editCommentary(Commentary commentary);
	
	public void deleteSelectedCommentary();
	
	public void deleteCommentary(Commentary commentary);
	
	public void commitOverlay();
}
