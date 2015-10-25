package utility.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

import utility.media.MediaFile;

public class SchemeFile {

	private File scmPath;
	private StringBuilder inputBuilder = new StringBuilder();
	private VoiceActor actor = VoiceActor.Robot;
	private int initialPitch = 120;
	private int finalPitch = 105;
	private double rateOfSpeech = 1.0;
	public enum VoiceActor {
		Jono("voice_akl_nz_jdt_diphone"), Robot("voice_kal_diphone"), Gordon(
				"voice_rab_diphone");

		private String voiceName;

		private VoiceActor(String voice) {
			voiceName = voice;
		}

		public String getVoice() {
			return "(" + voiceName + ")\n";
		}
	}

	public static void main(String[] args) throws IOException {
		SchemeFile ff = new SchemeFile(
				new File(System.getProperty("user.home")
						+ "/SoftEng206/ffmpeg/derp.scm"));
		ff.writeToDisk();
	}

	public SchemeFile() {
		scmPath = new File(System.getProperty("user.dir") + "/.temp/"
				+ Math.abs(this.hashCode()) + ".scm");
		while (scmPath.exists()) {
			scmPath = new File(System.getProperty("user.dir") + "/.temp/"
					+ Math.abs(scmPath.hashCode()) + ".scm");
		}
	}

	public SchemeFile(File path) {
		this.scmPath = path;
	}

	public File getPath() {
		return scmPath;
	}
	
	public String getAbsolutePath() {
		return "\"" + scmPath.getAbsolutePath() + "\"";
	}
	
	public VoiceActor getActor() {
		return actor;
	}

	public void setActor(VoiceActor actor) {
		this.actor = actor;
	}

	public int getInitialPitch() {
		return initialPitch;
	}

	public void setInitialPitch(int initialPitch) {
		this.initialPitch = initialPitch;
	}

	public int getFinalPitch() {
		return finalPitch;
	}

	public void setFinalPitch(int finalPitch) {
		this.finalPitch = finalPitch;
	}

	public double getRateOfSpeech() {
		return rateOfSpeech;
	}

	public void setRateOfSpeech(double rateOfSpeech) {
		this.rateOfSpeech = rateOfSpeech;
	}

	public void writeToDisk() throws IOException {
		Formatter appendWithFormat = new Formatter(inputBuilder);
		inputBuilder
				.append("(require '/netmount/usr/local/festival/initcustom)\n");
		inputBuilder.append(actor.getVoice());
		appendWithFormat.format("(Parameter.set 'Duration_Stretch %.1f)\n", rateOfSpeech);
		appendWithFormat.format("(set! duffint_params '((start %d) (end %d)))\n",initialPitch, finalPitch);
		appendWithFormat.format("(Parameter.set 'Int_Method 'DuffInt)\n");
		appendWithFormat.format("(Parameter.set 'Int_Target_Method Int_Targets_Default)\n");
		appendWithFormat.close();
		FileWriter scmFile = new FileWriter(scmPath);
		scmFile.write(inputBuilder.toString());
		scmFile.close();
//		System.out.println(inputBuilder.toString());
	}

}
