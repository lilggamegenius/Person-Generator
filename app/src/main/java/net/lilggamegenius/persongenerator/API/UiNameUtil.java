package net.lilggamegenius.persongenerator.API;

import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import net.lilggamegenius.persongenerator.JSON.Gender;
import net.lilggamegenius.persongenerator.JSON.Person;
import net.lilggamegenius.persongenerator.JSON.Region;
import net.lilggamegenius.persongenerator.JSON.Request;
import net.lilggamegenius.persongenerator.MainActivity;
import net.lilggamegenius.persongenerator.R;

import java.io.IOException;
import java.text.DateFormat;

public class UiNameUtil extends AsyncTask<Request, Void, Void> {

	protected Void doInBackground(Request... requests) {
		for (Request request : requests) {
			if (request.apiUrl == null) {
				request.apiUrl = makeApiUrl();
				String regionStr = request.region;
				if (!regionStr.equalsIgnoreCase(Region.Random.toString())) {
					request.apiUrl.setParameter("region", regionStr);
				}
				String genderStr = request.gender;
				if (!genderStr.equalsIgnoreCase(Gender.Random.toString())) {
					request.apiUrl.setParameter("gender", genderStr.toLowerCase());
				}
				if (request.extendedInfo) {
					request.apiUrl.setParameter("ext", null); // Include parameter without value
				}
			}
			MainActivity activity = request.activity;
			// Instantiate the RequestQueue.
			RequestQueue queue = Volley.newRequestQueue(activity);
			String url = request.apiUrl.toString();


			// Request a string response from the provided URL.
			StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
					response -> {
						try {
							ObjectMapper mapper = new ObjectMapper();
						/*
						// todo for debugging
						String value = response;
						Object json = mapper.readValue(value, Object.class);
						value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
						Log.v("UiNameUtil", url);
						Log.v("UiNameUtil", value);
						//*/
							Person person = mapper.readValue(response, Person.class);
							DatabaseReference db = activity.mDatabase;
							db.child("lastPerson").push().setValue(person);
							//name.setText(person.name);
							//name.setText(activity.getString(R.string.name_format, person.name, person.surname));
							SpannableString content = new SpannableString(activity.getString(R.string.name_format, person.name, person.surname));
							content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
							request.nameText.setText(content);
							request.genderText.setText(activity.getString(R.string.gender_format, person.gender));
							request.regionText.setText(activity.getString(R.string.region_format, person.region));

							if (request.apiUrl.map.containsKey("ext")) {
								Picasso
										.with(activity)
										.load(person.photo)
										//.resize(movieImage.getWidth(), movieImage.getHeight())
										//.centerCrop()
										.into(request.profilePicImage);
								request.profilePicImage.setVisibility(View.VISIBLE);
								request.ageText.setVisibility(View.VISIBLE);
								request.ageText.setText(activity.getString(R.string.age_format, person.age));
								String phoneNumber = person.phone;
								request.phoneText.setText(activity.getString(R.string.phone_format, phoneNumber));
								java.util.Date time = new java.util.Date(person.birthday.raw * 1000);

								String birthdayStr = DateFormat.getDateInstance(DateFormat.SHORT).format(time);
								request.birthdayText.setText(activity.getString(R.string.birthday_format, birthdayStr));
								request.emailText.setText(activity.getString(R.string.email_format, person.email));
							} else {
								String empty = "";
								request.ageText.setText(empty);
								request.ageText.setVisibility(View.GONE);
								request.profilePicImage.setVisibility(View.GONE);
								request.phoneText.setText(empty);
								request.birthdayText.setText(empty);
								request.emailText.setText(empty);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}, error -> {
				String empty = "";
				request.nameText.setText(empty);
				request.genderText.setText(empty);
				request.regionText.setText(empty);
				request.phoneText.setText(empty);
				request.birthdayText.setText(empty);
				request.emailText.setText(empty);
				Toast.makeText(activity, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				Crashlytics.logException(error);
			});

			// Add the request to the RequestQueue.
			queue.add(stringRequest);
		}
		return null;
	}

	public static ApiUrl makeApiUrl() {
		return new ApiUrl("https://uinames.com/", "api/");
	}
}
