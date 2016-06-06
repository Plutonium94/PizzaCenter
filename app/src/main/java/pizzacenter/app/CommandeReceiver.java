package pizzacenter.app;

import android.content.*;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by user on 05/06/16.
 */
public class CommandeReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;
    public static final String ACCEPTED_PREFIX = "MyPizzaCommande";

    public CommandeReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d("CommandeReceiver: " , "onReceive");
        assert(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for(SmsMessage message : messages) {
            Log.d("PizzaCenter: ","got a message");
            String corps = message.getMessageBody();
            Log.d("CommandeReceiver : ",corps);
            if(corps.startsWith(ACCEPTED_PREFIX)) {
                Log.d("originator: ", message.getOriginatingAddress());
                mainActivity.sms(corps, message.getOriginatingAddress());
            }
        }
    }


}
