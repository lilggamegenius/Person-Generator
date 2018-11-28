package net.lilggamegenius.persongenerator.JSON;

import android.widget.ImageView;
import android.widget.TextView;

import net.lilggamegenius.persongenerator.API.ApiUrl;
import net.lilggamegenius.persongenerator.MainActivity;

public class Request {
	public String region = Region.UnitedStates.toString();
	public boolean extendedInfo = false;
	public String gender = Gender.Random.toString();
	public int amount = -1;
	public int minlen = -1;
	public int maxlen = -1;

	public transient TextView nameText;
	//public transient TextView surnameText;
	public transient TextView genderText;
	public transient TextView ageText;
	public transient TextView regionText;
	public transient ImageView profilePicImage;
	public transient TextView phoneText;
	public transient TextView birthdayText;
	public transient TextView emailText;
	public transient ApiUrl apiUrl;
	public transient MainActivity activity;

	public Request() {
	}

	public Request(String region, boolean extendedInfo, String gender, MainActivity activity, ApiUrl apiUrl, TextView nameText, TextView genderText, TextView regionText, TextView ageText, ImageView profilePicImage, TextView phoneText, TextView birthdayText, TextView emailText) {
		this.extendedInfo = extendedInfo;
		this.region = region;
		this.gender = gender;

		this.apiUrl = apiUrl;
		this.activity = activity;

		this.nameText = nameText;
		this.genderText = genderText;
		this.regionText = regionText;
		this.ageText = ageText;
		this.profilePicImage = profilePicImage;
		this.phoneText = phoneText;
		this.birthdayText = birthdayText;
		this.emailText = emailText;
	}
}

