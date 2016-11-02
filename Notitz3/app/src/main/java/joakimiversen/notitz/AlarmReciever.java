package joakimiversen.notitz;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.app.TaskStackBuilder;

import static joakimiversen.notitz.MainActivity.INTENT_EXTRA_ID;
import static joakimiversen.notitz.MainActivity.INTENT_EXTRA_TITLE;
import static joakimiversen.notitz.MainActivity.INTENT_EXTRA_TEXT;
import static joakimiversen.notitz.MainActivity.NOTITZ;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // unique id for the app
        int nId = intent.getIntExtra(INTENT_EXTRA_ID, -1) + 41092;
        String nTitle = intent.getStringExtra(INTENT_EXTRA_TITLE);
        String nText = intent.getStringExtra(INTENT_EXTRA_TEXT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(nTitle)
                        .setContentText(nText)
                        .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTITZ, nId, mBuilder.build());
    }
}
