package com.example.notificationscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {

    public static final String TAG = "NotificationJobService";
    public static final String PRIMARY_CHANNEL_ID = "0";
    private NotificationManager mNotifyManager;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Créer le canal de notification

        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyManager != null) {
            createNotificationChannel();
        }
        // Configurer l'intent du contenu de la notification pour lancer l'application en cas de clic.
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Construction de la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Service")
                .setContentText("Votre Job est en cours d'exécution!")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_job_running)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        mNotifyManager.notify(0, builder.build());

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Release resources here
        mNotifyManager = null;
        return false; // Don't reschedule the job
    }

    private void createNotificationChannel() {
        // Créer le canal de notification (à ajouter si non existant)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "notify";
            String description = "Schedule notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Enregistrer le canal avec le gestionnaire de notifications
            mNotifyManager.createNotificationChannel(channel);
        }
    }

}
