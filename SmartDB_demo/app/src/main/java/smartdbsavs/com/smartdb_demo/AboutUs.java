package smartdbsavs.com.smartdb_demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by hi on 15-04-2018.
 */

public class AboutUs extends AppCompatActivity {

    Element adsElement1, adsElement, adsElement2, adsElement3, adsElement4;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        simulateDayNight(/* DAY */ 0);

        adsElement =new Element();
        adsElement.setTitle("Abhishek Jain, abhi63269@gmail.com");
        adsElement.setIconDrawable(R.drawable.ic_person_black_24dp);

        adsElement1 =new Element();
        adsElement1.setTitle("Surendra Lalwani, lalwanisurendra@gmail.com");
        adsElement1.setIconDrawable(R.drawable.ic_person_black_24dp);

        adsElement2 =new Element();
        adsElement2.setTitle("Suyash Jain, suyashj96@gmail.com");
        adsElement2.setIconDrawable(R.drawable.ic_person_black_24dp);

        adsElement3 =new Element();
        adsElement3.setTitle("Varun Karandikar, varjk30@gmail.com");
        adsElement3.setIconDrawable(R.drawable.ic_person_black_24dp);

        adsElement4 = new Element();
        adsElement4.setTitle("Call Us");
        adsElement4.setIconDrawable(R.drawable.ic_phone_black_24dp);

        adsElement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code in here!
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9993127213"));
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });



        View helpPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.bell_ic_launcher)
                .addItem(new Element().setTitle("Version 1.0"))
                .setDescription("SmartDB introduces the idea to provide secure access to home. It will be achieved through smart doorbell. " +
                        "Our system connects WiFi enabled android devices with firebase server using Raspberry pi and enables user to answer the door when the doorbell is pressed. " +
                        "It learns to identify new user by using face recognition as a unique identity to authenticate the individual.")
                .addGroup("Team SmartDB :")
                .addItem(adsElement)
                .addItem(adsElement1)
                .addItem(adsElement2)
                .addItem(adsElement3)
                .addItem(getCopyRightsElement())
                .create();

        setContentView(helpPage);
    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUs.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

}
/*
.setDescription("SmartDB introduce the idea to provide secure access to home. It will be achieved through smart doorbell. Our system connects WiFi enabled android devices with firebase server using Raspberry pi and enables user to answer the door when the doorbell is pressed. It learns to identify new user by using face recognition as a unique identity to authenticate the individual.")
*/
