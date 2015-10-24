package overlay.control;
/**
 * Types for overlays include mp3, tts, mp4, avi, wav, no_type and none.
 * no_type is meant to be used for overlays that do not have a type.
 * none is meant to be used for things that are not overlays.
 * @author michael
 *
 */
public enum OverlayType {
	
	MP3(".mp3"), TTS(".tts"), WAV(".wav"), MP4(".mp4"), AVI(".avi"), NO_TYPE(""), NONE("");
	
	private String type;
	
	private OverlayType(String type){
		this.type = type;
	}
	/**
	 * Returns the extension for the type of overlay.
	 * @return
	 */
	public String getType(){
		return type;
	}
	
}
