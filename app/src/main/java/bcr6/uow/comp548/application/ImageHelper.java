package bcr6.uow.comp548.application;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Brendan on 17/02/17.
 *
 */

public class ImageHelper {

    public static String createImageFile(Activity a) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

	    String imageName = "JPEG_" + timeStamp;
	    File storageDirectory = new File(a.getFilesDir(), "images");
	    if (!storageDirectory.mkdirs())
		    Log.e("Image", "Image directory either already exists... or it was unable to be created (uh-oh)");

		File image = null;
        try {
	        image = File.createTempFile(imageName, ".jpg", storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Image", e.getMessage());
        }
        if(image != null)
			return image.getAbsolutePath();
	    else
	    	return null;
    }
}
