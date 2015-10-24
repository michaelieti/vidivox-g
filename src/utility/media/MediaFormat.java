package utility.media;

/**
 * A list of supported media formats and their types.
 * <P>
 * 
 * The Corrupt MediaFormat indicates that a file appears to be valid, but is
 * corrupted in some fashion.
 * <P>
 * 
 * The Unsupported MediaFormat indicates that a file appears to be unsupported,
 * via an invalid extension or otherwise.
 * <P>
 * 
 * The Unknown MediaFormat indicates that the file provided could not be located
 * or could not be processed for some reason.
 * <P>
 * 
 * @author adav194
 * 
 */
public enum MediaFormat {

	MP3(MediaType.Audio), WAV(MediaType.Audio), MP4(MediaType.Video), FLV(
			MediaType.Video), Unknown(MediaType.Invalid, false), Unsupported(
			MediaType.Invalid, false), Corrupt(MediaType.Invalid, false);

	private MediaType type;
	private boolean valid = true;

	/*
	 * Note: This constructor should only be used for valid media formats.
	 */
	private MediaFormat(MediaType type) {
		this.type = type;
	}

	/*
	 * This constructor should be used to specify the validity of a media
	 * format.
	 */
	private MediaFormat(MediaType type, boolean valid) {
		this.type = type;
		this.valid = valid;
	}

	/**
	 * Return the type of media source for this MediaFormat.
	 * 
	 * @return
	 */
	public MediaType getType() {
		return type;
	}

	/**
	 * Indicates whether the MediaFormat is a supported by Vidivox.
	 * 
	 * @return
	 */
	public boolean isValid() {
		return valid;
	}

	public static MediaFormat processTypeFromString(String type) {
		type = type.toUpperCase();
		for (MediaFormat format : MediaFormat.values()) {
			if (format.getExtension().toUpperCase().equals(type)) {
				return format;
			}
		}
		return MediaFormat.Unsupported;

	}

	public String getExtension() {
		if (this.isValid()) {
			return this.toString().toLowerCase();
		} else {
			return "";
		}
	}
}
