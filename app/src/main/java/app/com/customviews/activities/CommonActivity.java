package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import app.com.customviews.fragments.BroadcastReceiverFragment;
import app.com.customviews.fragments.JobSchedulerFragment;
import app.com.customviews.fragments.PermissionFragment;
import app.com.customviews.R;
import app.com.customviews.fragments.SensorFragment;
import app.com.customviews.utils.CommonUtils;
import app.com.customviews.utils.Constants;

public class CommonActivity extends AppCompatActivity
        implements PermissionFragment.OnFragmentInteractionListener,
        JobSchedulerFragment.OnFragmentInteractionListener,
        SensorFragment.OnFragmentInteractionListener, BroadcastReceiverFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        int requestedFragment = getIntent().getIntExtra(Constants.FRAGMENT_CODE, 0);
        goToFragment(requestedFragment);
    }

    private void goToFragment(int requestedFragment) {
        switch (requestedFragment) {
            case 1:
                CommonUtils.startNewFragment(this,
                        R.id.container,
                        PermissionFragment.newInstance(),
                        "PermissionFragment", false);
            case 2:
                CommonUtils.startNewFragment(this,
                        R.id.container,
                        JobSchedulerFragment.newInstance(this),
                        "JobSchedulerFragment", false);
            case 3:
                CommonUtils.startNewFragment(this,
                        R.id.container,
                        SensorFragment.newInstance(),
                        "SensorFragment", false);
            case 4:
                CommonUtils.startNewFragment(this,
                        R.id.container,
                        BroadcastReceiverFragment.newInstance(),
                        "BroadcastReceiverFragment", false);
        }
    }


    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onJobSchedulerFragmentInteraction() {

    }

    @Override
    public void onSensorFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
