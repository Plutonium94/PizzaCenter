package pizzacenter.app;

import android.database.Cursor;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.provider.Telephony;
import android.content.*;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.*;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.LinkedList;
import java.util.List;

import pizzacenter.app.R;


public class MainActivity extends Activity {

    private LinkedList<String> commandesList = new LinkedList<String>();
    private CommandeReceiver receiver = null;
    private BaseAdapter ba = null;
    private String originator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PizzaCenter: ","in constructor" );
        setContentView(R.layout.activity_main);

        receiver = new CommandeReceiver(this);
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(receiver, filter);

        ListView commandesGrid = (ListView)findViewById(R.id.commandesGrid);
        final Context that = this;


        ba = new BaseAdapter() {
            @Override
            public int getCount() {
                return commandesList.size();
            }

            @Override
            public Object getItem(int i) {
                return commandesList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView tv = new TextView(that);
                tv.setText(commandesList.get(i));
                return tv;
            }
        };
        commandesGrid.setAdapter(ba);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException iae) {

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sms(String corps, String originator) {
        commandesList.add(corps);
        this.originator = originator;
        ListView commandesGrid = (ListView)findViewById(R.id.commandesGrid);
        ba.notifyDataSetChanged();
        Log.d("MainActivity: ", "notified");
        Intent i = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        i.setType(Phone.CONTENT_TYPE);
        startActivityForResult(i, 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri contactUri = data.getData();
        Cursor cursor = getContentResolver().query(contactUri,new String[]{Phone.CONTACT_ID}, "number like "+ originator, null, null);
        if(getApplicationContext() != null) {
            if (!cursor.moveToFirst() || cursor.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "Commande par "+cursor.getString(cursor.getColumnIndex(Phone.CONTACT_ID)), Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(getApplicationContext(), "Commande par nouveau client", Toast.LENGTH_SHORT);
            }
        }
    }

}
