package aa3vs.cs2110.virginia.edu.cardiosafe;
//Import Statements

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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

// Main Class
public class CountDown extends ActionBarActivity {
    // Shake fields
    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    //Text Views
    private TextView count;
    private TextView countmin;
    private TextView emer;
    //Buttons
    private Button stopButton;
    private Button resumeButton;
    private Button resetButton;
    private Button mainButton;
    //Miscellaneous variables
    private int runningtime;
    private CountDownTimer timmy;
    private boolean timmytrue;
    private String number;

    // OnCreate, sets up page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        //Sets up textfields and buttons
        count = (TextView) findViewById(R.id.time);
        countmin = (TextView) findViewById(R.id.landmin);
        emer = (TextView) findViewById(R.id.emergency);
        stopButton = (Button) findViewById(R.id.stopButton);
        resumeButton = (Button) findViewById(R.id.resumeButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        mainButton = (Button) findViewById(R.id.mainButton);

        //Parses info from previous screen
        Bundle extras = getIntent().getExtras();
        String time = extras.getString("time-length");
        number = extras.getString("phone-number");
        final int t = Integer.parseInt(time);
        runningtime = Integer.parseInt(time);
        timmytrue = false;

        //Main Timer, Starts immediately, sends text if not stopped by time limit.
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


            public void onFinish() {
                sendSMSMessage();

            }

        };

        //Emergency Timer. If phone doesn't shake for 2 minutes, sends alert text.
        final CountDownTimer emerg = new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (((millisUntilFinished / 1000) - ((int) (millisUntilFinished / 60000)) * 60) < 10) {
                    emer.setText("Emergency Timer:" + (int) (millisUntilFinished / 60000) + ":0" + ((millisUntilFinished / 1000) - ((int) (millisUntilFinished / 60000)) * 60));
                } else {
                    emer.setText("Emergency Timer: " + (int) (millisUntilFinished / 60000) + ":" + ((millisUntilFinished / 1000) - ((int) (millisUntilFinished / 60000)) * 60));
                }
            }

            public void onFinish() {
                sendAlertSMSMessage();
            }
        };


        //Start timer
        tim.start();
        emerg.start();

        //Home Button (takes to MainActivity)
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountDown.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Stop Button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = count.getText().toString();
                runningtime = Integer.parseInt(p.substring(p.indexOf(":") + 2, p.length()));
                String s = countmin.getText().toString();
                runningtime += 60 * Integer.parseInt(s.substring(s.indexOf(":") + 2, s.length()));
                tim.cancel();
                if (timmytrue) {
                    timmy.cancel();
                }

            }
        });

        //Reset Button (to original time entered)
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

        // Resumes if you have previously stopped
        // Needed to create a new timer to do this (called timmy), acts the same as tim.
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

        //Sensor code. When phone shakes, emergency timer resets.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                emerg.cancel();
                emerg.start();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    // If timer is set and finishes, sends a text saying you haven't come back yet
    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        Bundle extras = getIntent().getExtras();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, "Hey, I haven't come back from my run yet! Please check if I'm OK!!", null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // If emergency timer finishes, sends alert text
    protected void sendAlertSMSMessage() {
        Log.i("Send SMS", "");
        Bundle extras = getIntent().getExtras();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, "Hey! I've been running, but my phone hasn't shaken for a while. I may have fell!", null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
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