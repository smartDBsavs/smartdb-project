package smartdbsavs.com.smartdb_demo.service;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by hi on 19-02-2018.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    public String TOKEN=null;
//    private  Context mContext;
//    public FirebaseIDService(Context context) {
//        mContext=context;
//    }

    private static final String TAG = "FirebaseIDService";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
//        FirebaseApp.initializeApp(mContext);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        TOKEN=refreshedToken;
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);

    }
    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
