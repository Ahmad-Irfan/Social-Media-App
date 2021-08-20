package innovativedeveloper.com.socialapp.services;

import android.util.Log;

/*import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;*/
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

/*    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("InstanceIDService", "Token updated: " + refreshedToken);
    }*/

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = s;
        Log.d("InstanceIDService", "Token updated: " + refreshedToken);
        Log.e("NEW_TOKEN",s);
    }

}
