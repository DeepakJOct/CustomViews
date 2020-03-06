package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import app.com.customviews.R;
import app.com.customviews.classes.ObservableCharacter;
import app.com.customviews.fragments.ObserverFragment;

public class ObserverActivity extends AppCompatActivity {

    private Button btnChange;
    private ObservableCharacter value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);

        value = new ObservableCharacter();
        value.setValue("01234");

        ObserverFragment fragment = new ObserverFragment();
        Bundle args = new Bundle();
        args.putSerializable(ObserverFragment.PARAM, value);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment, "ObserverFragment").commit();

        btnChange = findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rand = Math.random();
                value.setValue(String.valueOf(rand));
            }
        });

    }
}
