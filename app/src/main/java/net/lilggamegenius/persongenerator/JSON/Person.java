package net.lilggamegenius.persongenerator.JSON;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"name",
		"surname",
		"gender",
		"region",
		"age",
		"title",
		"phone",
		"birthday",
		"email",
		"password",
		"credit_card",
		"photo"
})
public class Person implements Parcelable {

	public final static Parcelable.Creator<Person> CREATOR = new Creator<Person>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Person createFromParcel(Parcel in) {
			return new Person(in);
		}

		public Person[] newArray(int size) {
			return (new Person[size]);
		}

	};
	@JsonProperty("name")
	public String name;
	@JsonProperty("surname")
	public String surname;
	@JsonProperty("gender")
	public String gender;
	@JsonProperty("region")
	public String region;
	@JsonProperty("age")
	public int age;
	@JsonProperty("phone")
	public String phone;
	@JsonProperty("birthday")
	public Birthday birthday;
	@JsonProperty("email")
	public String email;
	@JsonProperty("photo")
	public String photo;
	@JsonProperty("title")
	private String title;
	@JsonProperty("password")
	private String password;
	@JsonProperty("credit_card")
	private CreditCard creditCard;

	private Person(Parcel in) {
		this.name = ((String) in.readValue((String.class.getClassLoader())));
		this.surname = ((String) in.readValue((String.class.getClassLoader())));
		this.gender = ((String) in.readValue((String.class.getClassLoader())));
		this.region = ((String) in.readValue((String.class.getClassLoader())));
		this.age = ((int) in.readValue((int.class.getClassLoader())));
		this.title = ((String) in.readValue((String.class.getClassLoader())));
		this.phone = ((String) in.readValue((String.class.getClassLoader())));
		this.birthday = ((Birthday) in.readValue((Birthday.class.getClassLoader())));
		this.email = ((String) in.readValue((String.class.getClassLoader())));
		this.password = ((String) in.readValue((String.class.getClassLoader())));
		this.creditCard = ((CreditCard) in.readValue((CreditCard.class.getClassLoader())));
		this.photo = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Person() {
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(name);
		dest.writeValue(surname);
		dest.writeValue(gender);
		dest.writeValue(region);
		dest.writeValue(age);
		dest.writeValue(title);
		dest.writeValue(phone);
		dest.writeValue(birthday);
		dest.writeValue(email);
		dest.writeValue(password);
		dest.writeValue(creditCard);
		dest.writeValue(photo);
	}

	public int describeContents() {
		return 0;
	}

}
