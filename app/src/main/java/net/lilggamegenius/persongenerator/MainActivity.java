package net.lilggamegenius.persongenerator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.lilggamegenius.persongenerator.API.UiNameUtil;
import net.lilggamegenius.persongenerator.JSON.Region;
import net.lilggamegenius.persongenerator.JSON.Request;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	//public static final String PREFERENCES = "settings";
	private static final String EXT = "extended_info";
	private static final String GENDER = "gender";
	private static final String REGION = "region";
	private static final String TAG = "PersonGenerator";

	private Spinner spinner;
	private SwitchCompat extSwitch;
	private SharedPreferences sharedPreferences;
	private String[] genderValues;

	private TextView name;
	private TextView surname;
	private TextView gender;
	private TextView age;
	private TextView region;
	private ImageView profilePic;
	private TextView phone;
	private TextView birthday;
	private TextView email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view -> getNewPerson());

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		// todo throw into another thread

		genderValues = getResources().getStringArray(R.array.pref_gender_values);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		MenuItem item = navigationView.getMenu().findItem(R.id.spinner);
		spinner = (Spinner) item.getActionView();

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.pref_gender_values, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(GENDER, genderValues[position]);
				editor.apply();
				getNewPerson();
				//Log.i(TAG, String.format("Position: %d Saved value: %s", position, sharedPreferences.getString(GENDER, genderValues[0])));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		item = navigationView.getMenu().findItem(R.id.nav_ext);
		LinearLayout linearLayout = (LinearLayout) item.getActionView();
		extSwitch = linearLayout.findViewById(R.id.drawer_switch);
		extSwitch.setOnClickListener(v -> {
			//SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			boolean ext = sharedPreferences.getBoolean(EXT, false);
			editor.putBoolean(EXT, !ext);
			editor.apply();
			getNewPerson();
			//Log.i(TAG, String.format("value: %b", !ext));
		});

		name = findViewById(R.id.name);
		surname = findViewById(R.id.surname);
		gender = findViewById(R.id.gender);
		age = findViewById(R.id.age);
		region = findViewById(R.id.region);
		profilePic = findViewById(R.id.profile_picture);
		phone = findViewById(R.id.phone);
		birthday = findViewById(R.id.birthday);
		email = findViewById(R.id.email);
	}

	private void getNewPerson() {
		String regionStr, genderStr;
		boolean ext;
		ext = sharedPreferences.getBoolean(EXT, false);
		regionStr = sharedPreferences.getString(REGION, Region.Random.toString());
		genderStr = sharedPreferences.getString(GENDER, genderValues[0]);
		UiNameUtil.callRequest(new Request(regionStr, ext, genderStr), this, name, surname, gender, region, age, profilePic, phone, birthday, email);
	}

	private int getIndexFromArray(String search, String... array) {
		for (int i = 0; i < array.length; i++) {
			String val = array[i];
			if (search.equals(val)) {
				return i;
			}
		}
		Log.w(TAG, "getIndexFromArray():Could not find index for given array");
		return -1;
	}

	@Override
	public void onResume() {
		//NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		//MenuItem item = navigationView.getMenu().findItem(R.id.spinner);
		//Spinner spinner = (Spinner) item.getActionView();

		int genderFilter = getIndexFromArray(sharedPreferences.getString(GENDER, genderValues[0]), genderValues);
		spinner.setSelection(genderFilter, true);

		//item = navigationView.getMenu().findItem(R.id.nav_ext);
		//LinearLayout linearLayout = (LinearLayout) item.getActionView();
		//SwitchCompat extSwitch = linearLayout.findViewById(R.id.drawer_switch);
		boolean ext = sharedPreferences.getBoolean(EXT, false);
		extSwitch.setChecked(ext);
		//extSwitch.toggle();
		getNewPerson();
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
			getNewPerson();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//spinner.setOnItemSelectedListener(this);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		boolean close = false;

		if (id == R.id.nav_settings) {
			close = true;
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}

		if (close) {
			DrawerLayout drawer = findViewById(R.id.drawer_layout);
			drawer.closeDrawer(GravityCompat.START);
			getNewPerson();
		}
		return true;
	}
}
