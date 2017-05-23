package bcr6.uow.comp548.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import bcr6.uow.comp548.application.MainActivity;
import bcr6.uow.comp548.application.R;
import bcr6.uow.comp548.application.database.DatabaseHelper;
import bcr6.uow.comp548.application.database.ORMBaseActivity;
import bcr6.uow.comp548.application.models.Friend;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Address;

public class ImportContacts extends ORMBaseActivity<DatabaseHelper> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vcfcontacts_activity);

		try {
			//Begins a Database Transaction
			TransactionManager.callInTransaction(getConnectionSource(),
					new Callable<Void>() {
						public Void call() throws Exception {
							RuntimeExceptionDao<Friend, Integer> dao = getHelper().getFriendDataDao();
							//Gets every VCard entry out of this VCard file
							List<VCard> list = Ezvcard.parse(getContentResolver().openInputStream(getIntent().getData())).all();
							int count = 0;
							for (VCard card : list) {
								String firstName;
								String lastName;
								String mobile;
								String emailAddress;
								String address = "";

								//Tries to extract the data out of the VCard. If it fails, it makes the value empty
								try { firstName = card.getStructuredName().getGiven(); } catch (Exception e) { firstName = ""; }
								try { lastName = card.getStructuredName().getFamily(); } catch (Exception e) { lastName = ""; }
								try { mobile = card.getTelephoneNumbers().get(0).getText(); } catch (Exception e) { mobile = ""; }
								try { emailAddress = card.getEmails().get(0).getValue(); } catch (Exception e) { emailAddress = ""; }

								Address addressObject = null;

								if (!card.getAddresses().isEmpty())
									addressObject = card.getAddresses().get(0);

								if (addressObject != null) {
									if (addressObject.getStreetAddress() != null) address = addressObject.getStreetAddress();
									if (addressObject.getLocality() != null) address += ", " + addressObject.getLocality();
									if (addressObject.getRegion() != null) address += ", " + addressObject.getRegion();
									if (addressObject.getPostalCode() != null) address += ", " + addressObject.getPostalCode();
									if (addressObject.getCountry() != null) address += ", " + addressObject.getCountry();
								}


								//Adds to the database
								dao.create(new Friend(firstName, lastName, mobile, emailAddress, address, null, null));
								count++;
							}
							//If we get here, we finish the transaction and continue on
							Toast.makeText(getApplicationContext(), "Successfully imported " + count + " contacts", Toast.LENGTH_LONG).show();
							return null;
						}
					});
			//Redirects to main activity and destroys this one
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();

		//If the database transaction fails, let the user know and destroy the activity
		} catch (SQLException e) {
			Toast.makeText(getApplicationContext(), "Failed to import contacts", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			finish();
		}
	}
}
