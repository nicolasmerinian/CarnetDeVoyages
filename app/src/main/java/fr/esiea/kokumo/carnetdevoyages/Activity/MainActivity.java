package fr.esiea.kokumo.carnetdevoyages.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import db.UsersContentProvider;
import db.UsersDBHelper;
import fr.esiea.kokumo.carnetdevoyages.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText loginTextField;
    private EditText passwordInTextField;
    private Button loginButton;
    private Button signinButton;
    private UsersContentProvider usersContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Link view's items to Java Objects
        this.loginTextField = (EditText) findViewById(R.id.logintext);
        this.passwordInTextField = (EditText) findViewById(R.id.passwordtext);
        this.loginButton = (Button) findViewById(R.id.loginButton);
        this.loginButton.setOnClickListener(this);
        this.signinButton = (Button) findViewById(R.id.signinButton);
        this.signinButton.setOnClickListener(this);
        this.usersContentProvider = new UsersContentProvider();
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.loginButton :
                Log.i("main.onclick", "loginButton");
                login();
                break;
            case R.id.signinButton :
                Log.i("main.onclick", "signinButton");
                signIn();
                login();
                break;
            default :
                Log.i("main.onclick", "default oups");
        }
    }

    /**
     * Displays the next activity (maps) if the user sucessfully logs in.
     */
    public void login() {
        Log.i("main.login", "enter");
        if (isAuthentificated()) {
            Log.i("MainActivity.onClick.if", "login success");
            Intent mapsActivity = new Intent(this, MapsActivity.class);
            Log.i("MainActivity.onClick", "lancement de l'activity maps");
            startActivity(mapsActivity);
        }
        else {
            Log.i("MainActivityonClickelse", "login failed");
        }
    }

    /**
     * Tries to log in, checks in the database whether the user already exists or not.
     * Returns true if the user exists, false otherwise.
     *
     * @return
     */
    public boolean isAuthentificated() {
        Log.i("MainActivity isAuth", "Enter authentification");
        // Gets the values from the fields Login and Password.
        ContentValues values = this.getValues();
        // The columns to be returned
        final String[] columns = { UsersDBHelper.KEY_LOGIN, UsersDBHelper.KEY_PASSWORD };
        // The WHERE clause to filter the results
        final String whereClause =
                UsersDBHelper.KEY_LOGIN + " = " + values.getAsString(UsersDBHelper.KEY_LOGIN)
                + " AND "
                + UsersDBHelper.KEY_PASSWORD + " = " + values.getAsString(UsersDBHelper.KEY_PASSWORD);
        // Execute the query
        Cursor cursor = this.usersContentProvider.query(UsersContentProvider.CONTENT_URI, columns, whereClause,
                null, null);
        if (cursor == null || cursor.getCount() != 1) {
            Log.i("MainAct. isAuth. ifcurs", "Cursor null or empty");
            return false;
        }
        else {
            Log.i("MainAc isAuth else curs", "query to log succeeded! Authentificated!");
            return true;
        }
    }

    /**
     * Creates a new user in the database.
     */
    public void signIn() {
        Log.i("main.signIn", "enter");
        ContentValues values = this.getValues();
        Log.i("main.signIn", "values got");
        this.usersContentProvider.insert(UsersContentProvider.CONTENT_URI, values);
        Log.i("main.signIn", "values inserted end");
    }

    /**
     * Gets the values from the fields Login and Password.
     *
     * @return
     */
    private ContentValues getValues() {
        Log.i("main.getValues", "enter");
        // Get the values
        String newLogin = this.loginTextField.getText().toString();
        String newPassword = this.passwordInTextField.getText().toString();

        // Create a wrapper
        ContentValues testValues = new ContentValues();

        // Fill the wrapper with the login and password values
        testValues.put(UsersDBHelper.KEY_LOGIN, newLogin);
        testValues.put(UsersDBHelper.KEY_PASSWORD, newPassword);

        return testValues;
    }
}
