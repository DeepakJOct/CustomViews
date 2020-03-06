package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import app.com.customviews.R;
import app.com.customviews.utils.Constants;

@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity {

    @ViewById(R.id.tv_custom_view)
    TextView tvCustomView;
    @ViewById(R.id.tv_room_db)
    TextView tvRoomDb;
    @ViewById(R.id.textView5)
    TextView tvCheckSum;
    @ViewById(R.id.tv_gallery)
    TextView tvGallery;
    @ViewById(R.id.tv_toolbar)
    TextView tvToolbar;


    @Click(R.id.tv_custom_view)
    protected void onCustomViewClick() {
        Intent customViewIntent = new Intent(this, MainActivity.class);
        startActivity(customViewIntent);
    }
    @Click(R.id.tv_room_db)
    protected void onRoomDbClick() {
        Intent roomActivityIntent = new Intent(this, RoomActivity.class);
        startActivity(roomActivityIntent);
    }

    @Click(R.id.textView5)
    protected void openChecksumActivity() {
        Intent checksumIntent = new Intent(this, ChecksumActivity.class);
        startActivity(checksumIntent);
    }

    @Click(R.id.textView6)
    protected void openTreeMapActivity() {
        Intent treemapIntent = new Intent(this, TreemapActivity.class);
        startActivity(treemapIntent);
    }

    @Click(R.id.tv_gallery)
    protected void openGallery() {
        Intent galleryIntent = new Intent(this, GalleryActivity.class);
        startActivity(galleryIntent);
    }

    @Click(R.id.tv_toolbar)
    protected void openToolbarActivity() {
        Intent toolbarIntent = new Intent(this, ToolbarActivity.class);
        startActivity(toolbarIntent);
    }

    @Click(R.id.tv_drawer)
    protected void openDrawerActivity() {
        Intent drawerIntent = new Intent(this, ObserverActivity.class);
        startActivity(drawerIntent);
    }

    @Click(R.id.tv_retrofit)
    protected void openRetrofitActivity() {
        Intent Retrofitintent = new Intent(this, RetrofitResponseActivity.class);
        startActivity(Retrofitintent);
    }

    @Click(R.id.tv_permissions)
    protected void openPermissions() {
        Intent permissionsIntent = new Intent(this, CommonActivity.class);
        permissionsIntent.putExtra(Constants.FRAGMENT_CODE, 1);
        startActivity(permissionsIntent);
    }

    @Click(R.id.tv_job_scheduler)
    protected void openJobScheduler() {
        Intent jobSchedulerIntent = new Intent(this, CommonActivity.class);
        jobSchedulerIntent.putExtra(Constants.FRAGMENT_CODE, 2);
        startActivity(jobSchedulerIntent);
    }

    @Click(R.id.tv_sensors)
    protected void openSensors() {
        Intent sensorIntent = new Intent(this, CommonActivity.class);
        sensorIntent.putExtra(Constants.FRAGMENT_CODE, 3);
        startActivity(sensorIntent);
    }

    @Click(R.id.rl_harmony)
    protected void openHarmony() {
        Intent musicIntent = new Intent(this, MusicPlayerActivity.class);
        musicIntent.putExtra(Constants.FRAGMENT_CODE, 3);
        startActivity(musicIntent);
    }

    @Click(R.id.tv_broadcast_test)
    protected void openBroadCastReceiver() {
        Intent broadCastReceiverIntent = new Intent(this, CommonActivity.class);
        broadCastReceiverIntent.putExtra(Constants.FRAGMENT_CODE, 4);
        startActivity(broadCastReceiverIntent);
    }

    @Click(R.id.tv_transitions)
    protected void openTransitionsActivity() {
        Intent transitionsIntent = new Intent(this, TransitionsActivity.class);
        startActivity(transitionsIntent);
    }




    @AfterViews
    protected void init() {
        Toast.makeText(HomeActivity.this, "Activity Created", Toast.LENGTH_LONG);
    }
}
