package net.lilggamegenius.persongenerator.API;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import net.lilggamegenius.persongenerator.JSON.Gender;
import net.lilggamegenius.persongenerator.JSON.Person;
import net.lilggamegenius.persongenerator.JSON.Region;
import net.lilggamegenius.persongenerator.JSON.Request;
import net.lilggamegenius.persongenerator.R;

import java.io.IOException;
import java.text.DateFormat;

public class UiNameUtil {
	public static void callRequest(Request request, Activity activity, TextView name, TextView surname, TextView gender, TextView region, TextView age, ImageView profilePic, TextView phone, TextView birthday, TextView email) {
		ApiUrl apiUrl = makeApiUrl();
		String regionStr = request.region;
		if (!regionStr.equalsIgnoreCase(Region.Random.toString())) {
			apiUrl.setParameter("region", regionStr.toLowerCase());
		}
		String genderStr = request.gender;
		if (!genderStr.equalsIgnoreCase(Gender.Random.toString())) {
			apiUrl.setParameter("gender", genderStr.toLowerCase());
		}
		if (request.extendedInfo) {
			apiUrl.setParameter("ext", null); // Include parameter without value
		}
		callRequest(apiUrl, activity, name, surname, gender, region, age, profilePic, phone, birthday, email);
	}

	private static void callRequest(ApiUrl apiUrl, Activity activity, TextView name, TextView surname, TextView gender, TextView region, TextView age, ImageView profilePic, TextView phone, TextView birthday, TextView email) {
		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(activity);
		String url = apiUrl.toString();

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
				response -> {
					try {
						ObjectMapper mapper = new ObjectMapper();
						String value = response;
						///*
						// todo for debugging
						Object json = mapper.readValue(value, Object.class);
						value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
						Log.v("UiNameUtil", url);
						Log.v("UiNameUtil", value);
						//*/
						Person person = mapper.readValue(response, Person.class);

						name.setText(person.name);
						surname.setText(person.surname);
						gender.setText(activity.getString(R.string.gender_format, person.gender));
						region.setText(activity.getString(R.string.region_format, person.region));

						if (apiUrl.map.containsKey("ext")) {
							Picasso
									.with(activity)
									.load(person.photo)
									//.resize(movieImage.getWidth(), movieImage.getHeight())
									//.centerCrop()
									.into(profilePic);
							profilePic.setVisibility(View.VISIBLE);
							age.setText(activity.getString(R.string.age_format, person.age));
							String phoneNumber = person.phone;
							/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
								phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "USA");
							} else {
								phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber); //Deprecated method

							}*/
							phone.setText(activity.getString(R.string.phone_format, phoneNumber));
							java.util.Date time = new java.util.Date(person.birthday.raw * 1000);

							String birthdayStr = DateFormat.getDateInstance(DateFormat.SHORT).format(time);
							birthday.setText(activity.getString(R.string.birthday_format, birthdayStr));
							email.setText(activity.getString(R.string.email_format, person.email));
						} else {
							String empty = "";
							profilePic.setVisibility(View.GONE);
							phone.setText(empty);
							birthday.setText(empty);
							email.setText(empty);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}, error -> {
			String empty = "";
			name.setText(empty);
			surname.setText(empty);
			gender.setText(empty);
			region.setText(empty);

			phone.setText(empty);
			birthday.setText(empty);
			email.setText(empty);
		});

		// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}

	private static ApiUrl makeApiUrl() {
		return new ApiUrl("https://uinames.com/", "api/");
	}
}
