package net.lilggamegenius.persongenerator.JSON;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"dmy",
		"mdy",
		"raw"
})
public class Birthday implements Parcelable {

	public final static Parcelable.Creator<Birthday> CREATOR = new Creator<Birthday>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Birthday createFromParcel(Parcel in) {
			return new Birthday(in);
		}

		public Birthday[] newArray(int size) {
			return (new Birthday[size]);
		}

	};
	@JsonProperty("raw")
	public long raw;
	@JsonProperty("dmy")
	private String dmy;
	@JsonProperty("mdy")
	private String mdy;

	private Birthday(Parcel in) {
		this.dmy = ((String) in.readValue((String.class.getClassLoader())));
		this.mdy = ((String) in.readValue((String.class.getClassLoader())));
		this.raw = ((long) in.readValue((long.class.getClassLoader())));
	}

	public Birthday() {
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(dmy);
		dest.writeValue(mdy);
		dest.writeValue(raw);
	}

	public int describeContents() {
		return 0;
	}

}
