package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import app.com.customviews.R;
import app.com.customviews.dialogs.CommonOutputDialog;
import app.com.customviews.interfaces.ResultListener;

public class TreemapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treemap);

        Handler handler = new Handler();
        handler.postDelayed(this::putTogether, 2000);

    }

    public void putTogether() {
        Map<String, Set<String>> dictionary = new TreeMap<>();
        Set<String> a = new TreeSet<>(Arrays.asList("Actual", "Arrival", "Argument", "Aristocrat", "Arrogated"));
        Set<String> b = new TreeSet<>(Arrays.asList("Bump", "Bravo", "Basic", "Blooper", "Battling"));

        dictionary.put("B", b);
        dictionary.put("A", a);

        System.out.println(dictionary);
        CommonOutputDialog.newInstance(dictionary.toString(), ((object, isSuccess) -> {
            if(isSuccess) {
                boolean isTrue = (boolean) object;
                if(isTrue) {
                    Toast.makeText(this, "Get Result Successful", Toast.LENGTH_SHORT).show();
                }
            }
        })).show(getSupportFragmentManager(), "CommonOutputDialog");
    }
}
