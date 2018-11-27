package net.lilggamegenius.persongenerator.JSON;

public class Request {
	public String region = Region.UnitedStates.toString();
	public boolean extendedInfo = false;
	public String gender = Gender.Random.toString();
	public int amount = -1;
	public int minlen = -1;
	public int maxlen = -1;

	public Request() {
	}

	public Request(String region, boolean extendedInfo, String gender) {
		this.extendedInfo = extendedInfo;
		this.region = region;
		this.gender = gender;
	}
}

