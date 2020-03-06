package app.com.customviews.classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import app.com.customviews.R;
import app.com.customviews.activities.HomeActivity;
import app.com.customviews.activities.MainActivity;

public class NotificationJobService extends JobService {

    private Context context;

    private NotificationManager mNotifyManager;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    public static NotificationJobService newInstance(Context context) {
        NotificationJobService njs = new NotificationJobService();
        njs.context = context;
        return njs;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //create the notification channel
        createNotificationChannel();

        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,
                new Intent(getApplicationContext(), HomeActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Scheduler")
                .setContentText("Your job ran to completion")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.notif)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        mNotifyManager.notify(0, builder.build());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    public void createNotificationChannel() {
        //Define notification manager object
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //notification channels are only available in OREO and higher.
        //so add a check on SDK Version.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name;
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "job_service_notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Notifications from Job Service");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
