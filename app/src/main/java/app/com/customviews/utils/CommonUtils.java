package app.com.customviews.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;

import app.com.customviews.R;

public class CommonUtils {

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static void startNewFragment(Activity context, int id, Fragment frag, String tag, boolean backStack) {

        final FragmentTransaction ftr = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        if(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(id) != null) {
            ftr.replace(R.id.container, frag);
            if(backStack) {
                ftr.addToBackStack(null);
            }
        } else {
            ftr.add(R.id.container, frag, tag);
        }
        ftr.commitAllowingStateLoss();
    }

    public static void showToast(Activity context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }
}
