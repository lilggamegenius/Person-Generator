package net.lilggamegenius.persongenerator.JSON;

import android.support.annotation.NonNull;

public enum Region {
	Random("Random"),
	Albania("Albania"),
	Argentina("Argentina"),
	Armenia("Armenia"),
	Australia("Australia"),
	Austria("Austria"),
	Azerbaijan("Azerbaijan"),
	Bangladesh("Bangladesh"),
	Belgium("Belgium"),
	BosniaAndHerzegovina("Bosnia and Herzegovina"),
	Brazil("Brazil"),
	Bulgaria("Bulgaria"),
	Canada("Canada"),
	China("China"),
	Colombia("Colombia"),
	CostaRica("Costa Rica"),
	Denmark("Denmark"),
	Egypt("Egypt"),
	England("England"),
	Estonia("Estonia"),
	Finland("Finland"),
	France("France"),
	Georgia("Georgia"),
	Germany("Germany"),
	Greece("Greece"),
	Hungary("Hungary"),
	India("India"),
	Iran("Iran"),
	Israel("Israel"),
	Italy("Italy"),
	Japan("Japan"),
	Korea("Korea"),
	Mexico("Mexico"),
	Morocco("Morocco"),
	Nepal("Nepal"),
	Netherlands("Netherlands"),
	NewZealand("New Zealand"),
	Nigeria("Nigeria"),
	Norway("Norway"),
	Pakistan("Pakistan"),
	Poland("Poland"),
	Portugal("Portugal"),
	Romania("Romania"),
	Russia("Russia"),
	SaudiArabia("Saudi Arabia"),
	Slovakia("Slovakia"),
	Slovenia("Slovenia"),
	Spain("Spain"),
	Sweden("Sweden"),
	Switzerland("Switzerland"),
	Turkey("Turkey"),
	Ukraine("Ukraine"),
	UnitedStates("United States"),
	Vietnam("Vietnam");

	private final String region;

	Region(String region) {
		this.region = region;
	}

	@NonNull
	@Override
	public String toString() {
		return region;
	}
}
