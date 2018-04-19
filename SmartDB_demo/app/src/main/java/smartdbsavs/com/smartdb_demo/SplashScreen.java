package smartdbsavs.com.smartdb_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.media.MediaPlayer;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smartdbsavs.com.smartdb_demo.holders.AddMemberDTO;
import smartdbsavs.com.smartdb_demo.holders.LogsDTO;
import smartdbsavs.com.smartdb_demo.holders.RegistrationDetailDTO;
import smartdbsavs.com.smartdb_demo.utils.PrefHelper;

/**
 * Created by Lenovo on 27-12-2017.
 */

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    TextView txtFadeIn1, txtFadeIn2;
    Animation animation1, animation2;
    MediaPlayer music;
    Context mContext;
    String titleMessage, label, dateTime, imageUrl;
    static Bundle mBundle;
   // public static String status="0";
    LogsDTO logsDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mContext = getApplicationContext();

        txtFadeIn1 = (TextView) findViewById(R.id.text1);
        txtFadeIn2 = (TextView) findViewById(R.id.text2);


        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_1);
        txtFadeIn1.setVisibility(TextView.VISIBLE);
        txtFadeIn2.setVisibility(TextView.VISIBLE);
        txtFadeIn1.startAnimation(animation1);
        txtFadeIn2.startAnimation(animation2);



        if (getIntent().getExtras() != null) {
            mBundle=getIntent().getExtras();
            setNotificationData(getIntent().getExtras());
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("SmartNOTIFY", "Key: " + key + " Value: " + value);
                //Toast.makeText(MainActivity.this, key+value, Toast.LENGTH_SHORT).show();
            }
        }

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    music = MediaPlayer.create(SplashScreen.this, R.raw.doorbell);
                    music.start();
                    sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.putExtra("Notification Details",mBundle);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();

    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        music.release();
        finish();
    }

    private void setNotificationData(Bundle extras) {
     /*   if (extras == null) {
            System.out.println("Bundle is empty");
            return;
        }*/
        if (extras != null && extras.getString("action")!=null && extras.getString("action").equalsIgnoreCase("notification")) {
        /*    StringBuilder text = new StringBuilder("");
            text.append("Message Details:");
            text.append("\n");
            text.append("\n");
            //MyFirebaseMessagingService fms=new MyFirebaseMessagingService();

            if (extras.containsKey("title") && extras.containsKey("message")) {
                text.append("Image: ");
                text.append(extras.get("title"));
                text.append("Date&Time: ");
                text.append(extras.get("message"));
            }
        */
            PrefHelper.getInstance(mContext).setNotifStatusKey((String) extras.get("title"));
            PrefHelper.getInstance(mContext).setNotifStatusMsg((String) extras.get("message"));
            PrefHelper.getInstance(mContext).setNotifStatusUrl((String) extras.get("url"));


            titleMessage = (String) extras.get("title");
            label = (String) extras.get("label");
            dateTime = (String) extras.get("message");
            imageUrl = (String) extras.get("url");
            Log.i("Notification", "do entry in DB");

            logsDTO = new LogsDTO();
            if(dateTime!=null) logsDTO.setDateTime(dateTime);
            if(label!=null) logsDTO.setLabel(label);
            if(titleMessage!=null) logsDTO.setTitleMessage(titleMessage);
            if(imageUrl!=null) logsDTO.setImageUrl(imageUrl);
           /* if(status!=null) logsDTO.setStatus(status);*/


            Log.d("Print bundle", getIntent().getExtras().toString());

        }
    }
}