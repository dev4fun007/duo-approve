package bytes.sync.duoapprove;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.PrimitiveIterator;

public class DuoNotificationListener extends NotificationListenerService {

    private static final String TAG = DuoNotificationListener.class.getName();
    private static final String DUO_MOBILE_PACKAGE = "com.duosecurity.duomobile";

    public DuoNotificationListener() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(DUO_MOBILE_PACKAGE.equals(sbn.getPackageName())) {
            if(sbn.getNotification().actions.length > 0) {
                try {
                    sbn.getNotification().actions[0].actionIntent.send();
                    Log.d(TAG, "sent action intent for - " + sbn.getNotification().actions[0].title);
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            saveNotificationTimestamp();
        }
    }

    private void saveNotificationTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        String format = simpleDateFormat.format(new Date());
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("TIMESTAMP", format).apply();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "notification listener connected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}