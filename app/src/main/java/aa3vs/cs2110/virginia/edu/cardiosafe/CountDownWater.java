package aa3vs.cs2110.virginia.edu.cardiosafe;
//Import statements

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//Main Class
public class CountDownWater extends ActionBarActivity {

    //Private textViews, Buttons, and miscellaneous variables
    private TextView count;
    private TextView countmin;
    private Button stopButton;
    private Button resumeButton;
    private Button resetButton;
    private Button mainButton;
    private int runningtime;
    private CountDownTimer timmy;
    private boolean timmytrue;
    private String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timerwater);



        //Sets up textviews and buttons
        count = (TextView) findViewById(R.id.watertime);
        countmin = (TextView) findViewById(R.id.watermin);
        stopButton = (Button) findViewById(R.id.waterstopButton);
        resumeButton = (Button) findViewById(R.id.waterresumeButton);
        resetButton = (Button) findViewById(R.id.waterresetButton);
        mainButton = (Button) findViewById(R.id.mainButton);

        //Parses data from previous screen
        Bundle extras = getIntent().getExtras();
        String time = extras.getString("time-length");
        number = extras.getString("phone-number");
        final int t = Integer.parseInt(time);
        runningtime = Integer.parseInt(time);
        timmytrue = false;

        //Main timer. Will send txt if you don't come back in time.
        final CountDownTimer tim = new CountDownTimer(60000 * runningtime, 1000) {
            public void onTick(long millisUntilFinished) {
                countmin.setText("Minutes Left: " + (int) (millisUntilFinished / 60000));
                if (((millisUntilFinished / 1000) - ((int) (millisUntilFinished / 60000)) * 60) < 10) {
                    count.setText("Seconds Left:0" + ((millisUntilFinished / 1000) - ((int) (millisUntilFinished / 60000)) * 60));
                } else {
                    count.setText("Seconds Left: " + ((millisUntilFinished / 1000) - ((int) (millisUntilFinished / 60000)) * 60));
                }
                runningtime = (int) millisUntilFinished / 1000;

            }

            @Override
            public void onFinish() {
                sendSMSMessage();

            }

        };


        tim.start();
        //Takes you to home screen
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountDownWater.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //Stops timer
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = count.getText().toString();
                String s = countmin.getText().toString();
                runningtime = Integer.parseInt(p.substring(p.indexOf(":") + 2, p.length()));
                runningtime += 60 * Integer.parseInt(s.substring(s.indexOf(":") + 2, s.length()));
                tim.cancel();
                if (timmytrue) {
                    timmy.cancel();
                }

            }
        });
        //Resets timer
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tim.cancel();
                if (timmytrue) {
                    timmy.cancel();
                }
                tim.start();
            }
        });
        //Resumes timer
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timmy = new CountDownTimer(runningtime * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int x = (int)(millisUntilFinished/60000);
                        int y = (int)(millisUntilFinished/1000);
                        while(y >=60){
                            y-=60;
                        }
                        countmin.setText("Minutes Left: " + x);
                        if (y < 10) {
                            count.setText("Seconds Left:0" + ((millisUntilFinished / 1000) - ((int) (millisUntilFinished / 60000)) * 60));
                        } else {
                            count.setText("Seconds Left: " + y);
                        }
                        runningtime = (int) millisUntilFinished / 1000;

                    }


                    public void onFinish() {
                        sendSMSMessage();


                    }

                };
                timmytrue = true;
                timmy.start();
            }
        });
    }

    //Sends txt to contact. Run if timer finishes.
    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        Bundle extras = getIntent().getExtras();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, "Hey, I haven't come back from exercising in the water yet! Please check if I'm OK!!", null, null);
            Toast.makeText(getApplicationContext(), "A text was sent to your emergency contact", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


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


}