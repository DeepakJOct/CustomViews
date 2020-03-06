package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.com.customviews.R;
import app.com.customviews.views.LovelyView;

public class MainActivity extends AppCompatActivity {

    private LovelyView myView;
    private Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = findViewById(R.id.lv_custom_view);
        btnChange = findViewById(R.id.btn_change_color);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed();
                showNotification();
            }
        });

    }

    private void btnPressed() {
        myView.setCircleCol(Color.GREEN);
        myView.setLabelCol(Color.MAGENTA);
        myView.setCircleText("LovelyViewCreate");
    }

    private void showNotification() {
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID);
        notiBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        notiBuilder.setContentTitle("Notifications Title");
        notiBuilder.setContentText("Custom Views by Deepak");
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notiBuilder.setContentIntent(pendingIntent);
        createNotificationChannel();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notiBuilder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "channel_01";
        String description = "Test Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
