package com.Surajtechstudio.smartguidebengali;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Mynotificationservice extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }
    public void showNotification(String title, String mesg)
    {
       /* RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.notification_view);
        remoteViews.setTextViewText(R.id.text_id,title);*/
        URL url = null;
        Bitmap bmp = null;
        try {
            url = new URL("https://firebasestorage.googleapis.com/v0/b/smartguides-2a434.appspot.com/o/LOGO%20JPG%20ORIGINAL.jpg?alt=media&token=885c2026-2a12-4399-b860-1966df781e8a");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
             bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bmp);
        s.setSummaryText("Summary text appears on expanding the notification");

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this, "MyNotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.header_icon)
                .setAutoCancel(true)
                .setContentText(mesg)
                .setStyle(s)

                //.setCustomBigContentView(remoteViews)
                .setDefaults(Notification.DEFAULT_SOUND)
               .setColor(Color.BLUE);
        NotificationManagerCompat manager=NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());
    }
}
