package utility;

/**
 * A list of supported media formats and their types. The Corrupt MediaFormat
 * indicates that a file appears to be valid, but is corrupted in some fashion.
 * The Unsupported MediaFormat indicates that a file appears to be unsupported,
 * via an invalid extension or otherwise.
 * 
 * @author adav194
 * 
 */
public enum MediaFormat {

	MP3(MediaType.Audio), WAV(MediaType.Audio), MP4(MediaType.Video), FLV(
			MediaType.Video), Corrupt(MediaType.Invalid), Unsupported(
			MediaType.Invalid);

	private MediaType type;

	private MediaFormat(MediaType type) {
		this.type = type;

	}
}
