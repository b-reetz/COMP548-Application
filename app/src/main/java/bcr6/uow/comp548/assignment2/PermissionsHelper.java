package bcr6.uow.comp548.assignment2;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brendan on 5/17/17.
 *
 * Used as a global class to check user's permissions on relevant permissions
 */

public class PermissionsHelper {

    static public final int ALLOW_FINE_GPS_CODE = 1;
    static public final int ALLOW_COARSE_GPS_CODE = 2;
    static public final int ALLOW_WRITE_EXTERNAL_STORAGE_CODE = 3;
    static public final int ALLOW_CAMERA_CODE = 4;


    static private PermissionsHelper instance;
    static private Context context;

    static public void init() {
        if (instance==null)
            instance = new PermissionsHelper();
    }

    private PermissionsHelper(){}

    static public PermissionsHelper getInstance(Context ctx) {
        init();
        context = ctx;
        return instance;
    }



    /**
     * Returns whether the user has access to this particular permission
     * @param context The context requesting this permission
     * @param permission The particular permission to look in to
     * @return True if the activity has the permission, false if not
     */
    public static boolean checkPermissions(Context context, String permission) {
        try {
            //If the manifest doesn't contain the permission we're requesting, throw a new exception
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            List<String> requestedPermissions = new ArrayList<>(Arrays.asList(packageInfo.requestedPermissions));
            if (!requestedPermissions.contains(permission)) {
                Toast.makeText(context, "Error requesting permissions. Unable to access this feature", Toast.LENGTH_LONG).show();
                Log.e("Permissions", "This permission does not exist in the manifest");
                throw new UnsupportedOperationException("This permission does not exist in the manifest");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new UnsupportedOperationException("The activity's package name was not found");
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static List<String> hasPermissions(Context context, String... permissions) {

	    List<String> toReturn = new ArrayList<>();
	    //Checks to see if the permissions we're requesting are in the manifest
	    try {
		    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
		    List<String> requestedPermissions = new ArrayList<>(Arrays.asList(packageInfo.requestedPermissions));

		    for (String s : permissions) {

			    //If the permission is listed in the manifest
			    if (requestedPermissions.contains(s)) {
				    //if the permission is not granted
				    if (ContextCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_DENIED)
				    	toReturn.add(s);
			    } else {
				    //else throw an error
				    Toast.makeText(context, "Error requesting permissions. Unable to access this feature", Toast.LENGTH_LONG).show();
				    throw new PackageManager.NameNotFoundException("The permission " + s + " does not exist in the manifest");
			    }
		    }

	    } catch (PackageManager.NameNotFoundException e) {
		    e.printStackTrace();
		    Log.e("Permissions", e.getMessage());
	    }
	    return toReturn;
    }

    /**
     * @param activity The target activity
     * @param requestCode The request code. This is defined by the class calling this method
     * @param permissions The list of permissions to request from the user
     */
    public static void getPermissions(Activity activity, int requestCode, String... permissions)  {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void getPermissions(Activity activity, int requestCode, List<String> permissions) {
	    if (permissions.isEmpty()) {
		    Log.e("Permissions", "No permissions to request for");
		    return;
	    }
//	    String[] arrayPermissions = new String[permissions.size()];
	    ActivityCompat.requestPermissions(activity, permissions.toArray(new String[]{}), requestCode);
    }

}
