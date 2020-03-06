package app.com.customviews.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import androidx.core.app.ActivityCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Function {

    public static final String KEY_ALBUM = "album_name";
    public static final String KEY_PATH = "path";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_TIME = "date";
    public static final String KEY_COUNT = "date";

    //permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    public static HashMap<String, String> mappingBox(String album, String path, String timeStamp, String time, String count) {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_ALBUM, album);
        map.put(KEY_PATH, path);
        map.put(KEY_TIMESTAMP, timeStamp);
        map.put(KEY_TIME, time);
        map.put(KEY_COUNT, count);
        return map;
    }

    public static String getCount(Context c, String album_name) {
        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_MODIFIED};
        String selection;
        Cursor cursorExternal = c.getContentResolver().query(uriExternal, projection, "bucket_display_name = \"" + album_name + "\"", null, null);
        Cursor cursorInternal = c.getContentResolver().query(uriInternal, projection, "bucket_display_name = \"" + album_name + "\"", null, null);
        Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});
        return cursor.getCount() + " Photos";
    }

    public static String convertToTime(String timestamp) {
        long dateTime = Long.parseLong(timestamp);
        Date date = new Date(dateTime);
        DateFormat formater = new SimpleDateFormat("dd/MM HH:mm");
        return formater.format(date);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float px = dp * (displayMetrics.densityDpi / 160f);
        return px;
    }
}
