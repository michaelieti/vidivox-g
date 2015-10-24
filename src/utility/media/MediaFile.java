package utility.media;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import editor.MediaConverter;

/**
 * A MediaFile object represents a media source located on disk. Through file
 * type checking, a valid media object can be formed.
 * 
 * @author adav194
 * 
 */
public class MediaFile {

	private File path;
	private MediaFormat format = MediaFormat.Unknown;
	private Double length = 0.0;

	public MediaFile(File location) {
		path = location;
		if (!location.exists() || !location.isFile()) {
			return;
		} else {
			String typeFromProbe = probeForType();
			format = MediaFormat.processTypeFromString(typeFromProbe);
		}
	}

	/**
	 * @see utility.media.MediaFormat MediaFormat
	 * @return The type of the Media source.
	 */
	public MediaFormat getType() {
		return format;
	}

	public File getPath() {
		return path;
	}

	public Double getLength() {
		return length;
	}

	/**
	 * Deletes the Media source which the MediaFile is pointing to.
	 */
	public void delete() {
		path.delete();
		format = MediaFormat.Unknown;
	}

	/**
	 * This function can be used to create an empty media container of a
	 * selected type (See {@link utility.media.MediaFormat}).
	 * 
	 * @param desiredFormat
	 *            The desired format of the media container.
	 * @param desiredLocation
	 *            The desired location of the media container.
	 * @return An UnknownMedia object with attached container (if successfully
	 *         created).
	 */
	public static MediaFile createMediaContainer(MediaFormat desiredFormat,
			File desiredLocation) {
		File correctLocation = addExtension(desiredLocation, desiredFormat);
		String expansion = "ffmpeg -y -filter_complex \"aevalsrc=0::duration=0.1\" "
				+ correctLocation.getAbsolutePath();
		String[] cmd = { "bash", "-c", expansion };
		ProcessBuilder build = new ProcessBuilder(cmd);
		Process p;
		try {
			p = build.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return new MediaFile(correctLocation);
	}

	/*
	 * A helper function which probes an UnknownMedia for its true file type.
	 */
	private String probeForType() {
		String expansion = "ffprobe " + path.getAbsolutePath();
		String[] cmd = { "bash", "-c", expansion };
		ProcessBuilder build = new ProcessBuilder(cmd);
		build.redirectErrorStream(true);
		Process p;
		try {
			p = build.start();
			BufferedReader processOutput = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			String currentLineOfOutput;
			String[] splitOfCurrentLine;
			while ((currentLineOfOutput = processOutput.readLine().trim()) != null) {

				if (currentLineOfOutput.startsWith("Duration:")) {
					// Example changes are as shown.
					// Duration: 00:00:01.01, bitrate: 256 kb/s
					splitOfCurrentLine = currentLineOfOutput.split(",");
					// [Duration: 00:00:01.01] [bitrate: 256 kb/s]

					splitOfCurrentLine = splitOfCurrentLine[0].split(" ");
					// [Duration:] [00:00:01.01]
					length = MediaConverter
							.timeToSeconds(splitOfCurrentLine[1]);

				}
				if (currentLineOfOutput.startsWith("Input #0,")) {
					splitOfCurrentLine = currentLineOfOutput.split(",");
					if (splitOfCurrentLine.length >= 2) {
						return splitOfCurrentLine[1].trim().toUpperCase();
					}
					return "";
				}
			}
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * A helper function which takes a file path and a media format, and
	 * attempts to apply the correct extension syntax.
	 */
	private static File addExtension(File toAddExtension,
			MediaFormat validFormatExtension) {
		String absolutePath = toAddExtension.getAbsolutePath();
		if (!absolutePath.endsWith(validFormatExtension.getExtension())) {
			absolutePath = absolutePath.concat("."
					+ validFormatExtension.getExtension());
			return new File(absolutePath);
		} else {
			return toAddExtension;
		}
	}

}