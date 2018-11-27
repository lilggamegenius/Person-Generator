package net.lilggamegenius.persongenerator.JSON;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"expiration",
		"number",
		"pin",
		"security"
})
public class CreditCard implements Parcelable {

	public final static Parcelable.Creator<CreditCard> CREATOR = new Creator<CreditCard>() {


		@SuppressWarnings({
				"unchecked"
		})
		public CreditCard createFromParcel(Parcel in) {
			return new CreditCard(in);
		}

		public CreditCard[] newArray(int size) {
			return (new CreditCard[size]);
		}

	};
	@JsonProperty("expiration")
	private String expiration;
	@JsonProperty("number")
	private String number;
	@JsonProperty("pin")
	private int pin;
	@JsonProperty("security")
	private int security;

	private CreditCard(Parcel in) {
		this.expiration = ((String) in.readValue((String.class.getClassLoader())));
		this.number = ((String) in.readValue((String.class.getClassLoader())));
		this.pin = ((int) in.readValue((int.class.getClassLoader())));
		this.security = ((int) in.readValue((int.class.getClassLoader())));
	}

	public CreditCard() {
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(expiration);
		dest.writeValue(number);
		dest.writeValue(pin);
		dest.writeValue(security);
	}

	public int describeContents() {
		return 0;
	}

}
