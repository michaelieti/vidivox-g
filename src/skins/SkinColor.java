package skins;

public enum SkinColor {
	BLUE("/skins/BlueSkin.css"), GREEN("/skins/GreenSkin.css"), ORANGE(
			"/skins/OrangeSkin.css"), PURPLE("/skins/PurpleSkin.css");

	private String url;

	private SkinColor(String url) {
		this.url = url;
	}

	public String toURL() {
		String x = getClass().getResource(url).toExternalForm();
		return x;
	}
}

