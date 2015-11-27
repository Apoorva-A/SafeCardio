package aa3vs.cs2110.virginia.edu.cardiosafe;
//Imports

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

//Main Screen Activity
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons and their listeners
        final Button runButton = (Button) findViewById(R.id.runButton);
        final Button waterButton = (Button) findViewById(R.id.waterButton);

        runButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        openLandOptions(runButton);
                    }
                }
        );
        waterButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        openWaterOptions(waterButton);
                    }
                }
        );
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

    //Will open up either run or water page

    public void openLandOptions(View runButton) {
        Intent intent = new Intent(this, Running.class);

        startActivity(intent);
    }

    public void openWaterOptions(View waterButton) {
        Intent intent = new Intent(this, Water.class);

        startActivity(intent);
    }
}