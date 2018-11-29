package net.lilggamegenius.persongenerator;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.lilggamegenius.persongenerator.API.ApiUrl;
import net.lilggamegenius.persongenerator.API.UiNameUtil;
import net.lilggamegenius.persongenerator.JSON.Gender;
import net.lilggamegenius.persongenerator.JSON.Person;
import net.lilggamegenius.persongenerator.JSON.Region;

import java.io.IOException;

import static net.lilggamegenius.persongenerator.MainActivity.GENDER;
import static net.lilggamegenius.persongenerator.MainActivity.REGION;

/**
 * Implementation of App Widget functionality.
 */
public class PersonGeneratorWidgetActivity extends AppWidgetProvider {
	private static final String UPDATE_PERSON = "updatePerson";

	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

		//String widgetText = "";
		// Construct the RemoteViews object
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.person_generator_widget_activity);
		//views.setTextViewText(R.id.widget_name, widgetText);

		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		for (int appWidgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.person_generator_widget_activity);
			views.setTextViewText(R.id.widget_name, "Refreshing...");

			//Intent intent = new Intent(context, PersonGeneratorWidgetActivity.class);
			//intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			//intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			//PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			views.setOnClickPendingIntent(R.id.widget_refresh,
					getPendingSelfIntent(context, appWidgetId));

			appWidgetManager.updateAppWidget(appWidgetId, views);

			new GeneratePersonWidgetAsyncTask().execute(new WidgetRequest(null, null, context, null, appWidgetId));
		}
	}

	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (UPDATE_PERSON.equals(intent.getAction())) {
			new GeneratePersonWidgetAsyncTask().execute(new WidgetRequest(null, null, context, null, intent.getIntExtra("appWidgetId", -1)));
		}
	}

	@Override
	public void onEnabled(Context context) {
		//RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.person_generator_widget_activity);
		//views.setOnClickPendingIntent(R.id.widget_refresh,
		//		getPendingSelfIntent(context));
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}

	protected PendingIntent getPendingSelfIntent(Context context, int appWidgetId) {
		Intent intent = new Intent(context, getClass());
		intent.setAction(PersonGeneratorWidgetActivity.UPDATE_PERSON);
		intent.putExtra("appWidgetId", appWidgetId);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private static class GeneratePersonWidgetAsyncTask extends AsyncTask<WidgetRequest, Void, Void> {

		@Override
		protected Void doInBackground(WidgetRequest... requests) {
			WidgetRequest request = requests[0]; // Only handle a single request
			Context context = request.context;
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			request.region = sharedPreferences.getString(REGION, Region.Random.toString());
			String[] genderValues = context.getResources().getStringArray(R.array.pref_gender_values);
			request.gender = sharedPreferences.getString(GENDER, genderValues[0]);

			request.apiUrl = UiNameUtil.makeApiUrl();
			String regionStr = request.region;
			if (!regionStr.equalsIgnoreCase(Region.Random.toString())) {
				request.apiUrl.setParameter("region", regionStr);
			}
			String genderStr = request.gender;
			if (!genderStr.equalsIgnoreCase(Gender.Random.toString())) {
				request.apiUrl.setParameter("gender", genderStr.toLowerCase());
			}
			RequestQueue queue = Volley.newRequestQueue(context);
			String url = request.apiUrl.toString();

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.person_generator_widget_activity);

			// Request a string response from the provided URL.
			StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
					response -> {
						try {
							ObjectMapper mapper = new ObjectMapper();
							Person person = mapper.readValue(response, Person.class);
							views.setTextViewText(R.id.widget_name, context.getString(R.string.name_format, person.name, person.surname));
							views.setTextViewText(R.id.widget_gender, context.getString(R.string.gender_format, person.gender));
							views.setTextViewText(R.id.widget_region, context.getString(R.string.region_format, person.region));
							// Refreshes all widgets but every other method doesn't update text at all
							appWidgetManager.updateAppWidget(new ComponentName(context, PersonGeneratorWidgetActivity.class), views);
							//appWidgetManager.updateAppWidget(request.appWidgetId, views);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}, error -> {
				String empty = "";
				views.setTextViewText(R.id.widget_name, empty);
				views.setTextViewText(R.id.widget_gender, empty);
				views.setTextViewText(R.id.widget_region, empty);
				appWidgetManager.updateAppWidget(new ComponentName(context, PersonGeneratorWidgetActivity.class), views);
				Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
				Crashlytics.logException(error);
			});

			// Add the request to the RequestQueue.
			queue.add(stringRequest);
			return null;
		}
	}

	private class WidgetRequest {

		public String region = Region.UnitedStates.toString();
		public String gender = Gender.Random.toString();
		public transient ApiUrl apiUrl;
		public transient Context context;
		public transient int appWidgetId;

		public WidgetRequest() {
		}

		public WidgetRequest(String region, String gender, Context context, ApiUrl apiUrl, int appWidgetId) {
			this.region = region;
			this.gender = gender;

			this.apiUrl = apiUrl;
			this.context = context;
		}
	}
}

