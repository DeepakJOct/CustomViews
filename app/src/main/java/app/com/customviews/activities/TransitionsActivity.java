package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import app.com.customviews.R;
import app.com.customviews.fragments.DrawableTransitionFragment;
import app.com.customviews.utils.CommonUtils;

public class TransitionsActivity extends AppCompatActivity implements DrawableTransitionFragment.OnFragmentInteractionListener {

//    AnimationDrawable rocketAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitions);

        /*ImageView rocketImage = findViewById(R.id.rocket_image);
        rocketImage.setBackgroundResource(R.drawable.rocket_anim);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        rocketImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rocketAnimation.start();
            }
        });*/

        CommonUtils.startNewFragment(TransitionsActivity.this,
                R.id.container,
                new DrawableTransitionFragment(),
                "DrawableTransitionFragment",
                false);

    }


    @Override
    public void onDrawableAnimationFragInteraction(Uri uri) {

    }
}
