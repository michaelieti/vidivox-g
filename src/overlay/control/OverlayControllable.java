package overlay.control;

public interface OverlayControllable {
	
	public void filterList(OverlayType olType);
	
	public void editSelected();
	
	public void deleteSelected();
	
	public void commitOverlay();
}
