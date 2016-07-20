package aa3vs.cs2110.virginia.edu.cardiosafe;
//imports

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


//Same as Running Class
public class Water extends ActionBarActivity {

    private Button saveButton;
    private EditText timeRun;
    private EditText phoneNumber;
    private Button waterHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waterscreen);

        saveButton = (Button) findViewById(R.id.watersavephoneNo);
        timeRun = (EditText) findViewById(R.id.watertime);
        phoneNumber = (EditText) findViewById(R.id.waterphoneNo);
        waterHomeButton = (Button) findViewById(R.id.waterHomeButton);


        waterHomeButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        goHome(waterHomeButton);
                    }
                }
        );



        saveButton.setOnClickListener(saveButtonListener);


    }


    public View.OnClickListener saveButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (timeRun.getText().length() > 0 && phoneNumber.getText().length() > 0) {
                String howlong = timeRun.getText().toString();
                String contact = phoneNumber.getText().toString();
                Intent intent = new Intent(Water.this, CountDownWater.class);
                intent.putExtra("time-length", howlong);
                intent.putExtra("phone-number", contact);
                startActivity(intent);

            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goHome(View waterHomeButton) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }


}
