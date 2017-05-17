package bcr6.uow.comp548.assignment2.models;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bcr6 on 3/2/17.
 * Database table mapping to class
 */

@SuppressWarnings("unused")
@DatabaseTable(tableName = "friend")
public class Friend {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private String firstName;
    @DatabaseField(canBeNull = false)
    private String lastName;
    @DatabaseField(canBeNull = false)
    private String mobileNumber;
    @DatabaseField(canBeNull = false)
    private String emailAddress;
    @DatabaseField(canBeNull = false)
    private String address;
    @DatabaseField(canBeNull = false)
    private String imagePath;

    public Friend() {

    }

    public Friend(String firstName, String lastName, String mobileNumber, String emailAddress, String address, String imagePath) {
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.mobileNumber = mobileNumber != null ? mobileNumber : "";
        this.emailAddress = emailAddress != null ? emailAddress : "";
        this.address = address != null ? address : "";
        this.imagePath = imagePath != null ? imagePath : "";
    }


    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

	public boolean isEmpty() {
        return mobileNumber.isEmpty() && emailAddress.isEmpty() && address.isEmpty();

    }

    /**
     * Returns the first or last name of this friend. Favours the first name, and if
     * that is empty, returns the last name
     * @return Returns the first name if it's not empty, else it return the last name
     */
    public String getOneName() {
        return getFirstName().isEmpty() ? getLastName() : getFirstName();
    }
}