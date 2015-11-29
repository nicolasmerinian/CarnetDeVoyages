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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.esiea.kokumo.carnetdevoyages.db.UsersDBHelper;
import fr.esiea.kokumo.carnetdevoyages.db.UsersContentProvider;
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
        //this.usersContentProvider = new UsersContentProvider();
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
        Log.i("main.onclick", "enter");
        getAll();
        Log.i("main.onclick", "after all");
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
    private void login() {
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
    private boolean isAuthentificated() {
        Log.i("MainActivity isAuth", "Enter authentification");
        // Gets the values from the fields Login and Password.
        ContentValues values = this.getValues();

        // The columns to be returned
        // final String[] columns = { UsersDBHelper.KEY_LOGIN };//, UsersDBHelper.KEY_PASSWORD };

        // The WHERE clause to filter the results
        // final String whereClause = UsersDBHelper.KEY_LOGIN + "=?";

        // final String[] whereArgs = new String[1];
        // whereArgs[0] = "Nico";//values.getAsString(UsersDBHelper.KEY_LOGIN);

        // Execute the query
        // Log.i("MainActivity isAuth", "before fail!" + whereClause + ", " + whereArgs[0]);
        // Cursor cursor = getContentResolver().query(UsersContentProvider.CONTENT_URI, columns, whereClause,
        //         whereArgs, null);
        // if (cursor == null || cursor.getCount() != 1) {
        //     Log.i("MainAct. isAuth. ifcurs", "Cursor null or empty (not possible from now by the way...)");
        //     return false;
        // }
        // else {
        //     Log.i("MainAc isAuth else curs", "There are some corresponding results, so the query to log succeeded! Authentificated!");
        //     return true;
        // }

        ArrayList<String[]> users = getAll();
        for (String[] row : users) {
            if (row[1].equals(values.getAsString(UsersDBHelper.KEY_LOGIN))
                    && row[2].equals(values.getAsString(UsersDBHelper.KEY_PASSWORD))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new user in the database.
     */
    private void signIn() {
        Log.i("main.signIn", "enter");

        // Gets the values from the fields
        ContentValues values = this.getValues();

        // Gets the next available id.
        Integer newId = this.getNewId();

        // Adds a new id
        values.put(UsersDBHelper.KEY_ID, newId);

        Log.i("main.signIn", "values got:" + values.toString() + ". Before : " + getRowNumber());

        // Try to insert the new data
        getContentResolver().insert(UsersContentProvider.CONTENT_URI, values);

        Log.i("main.signIn", "values inserted end. Now : " + getRowNumber());

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
        Log.i("main.getValues", "Values: " + newLogin + ", " + newPassword);

        // Create a wrapper
        ContentValues testValues = new ContentValues();

        // Fill the wrapper with login and password values
        testValues.put(UsersDBHelper.KEY_LOGIN, newLogin);
        testValues.put(UsersDBHelper.KEY_PASSWORD, newPassword);

        return testValues;
    }

    /**
     * Returns the next available id.
     *
     * @return
     */
    private Integer getNewId() {
        Log.i("main.getNewId", "enter");
        // The columns to be returned
        // final String[] columns = { " MAX(" + UsersDBHelper.KEY_ID + ") " };
        final String[] columns = { UsersDBHelper.KEY_ID };
        // Execute the query
        Cursor cursor = getContentResolver().query(UsersContentProvider.CONTENT_URI, columns, null,
                null, null);

        Log.i("main.getNewId", "count : " + cursor.getCount() + ", ");
        if (cursor.getCount() == 1) {
            Integer id = null;
            do {
                id = cursor.getColumnIndex(UsersDBHelper.KEY_ID);
                Log.i("main.getNewId dowhile", "current id : " + id);
            } while(cursor.moveToNext());
            id += 1;
            Log.i("main.getNewId dowhile", "new id : " + id);
            return id;
        }
        return null;
    }

    private int getRowNumber() {
        Log.i("main.getRowNumber", "enter");
        Cursor cursor = getContentResolver().query(UsersContentProvider.CONTENT_URI, null, null,
                null, null);
        return cursor.getCount();
    }

    private ArrayList<String[]> getAll() {
        Log.i("main.getRowNumber", "getAll");
        ArrayList<String[]> users = new ArrayList<String[]>();
        Cursor cursor = getContentResolver().query(UsersContentProvider.CONTENT_URI, null, null, null, null);
        String result = "All : ";

        if (!cursor.moveToFirst()) {
            Toast.makeText(this, result + " no content yet!", Toast.LENGTH_LONG).show();
        }
        else {
            int i = 0;
            do {
                String[] row = new String[3];
                String id = cursor.getString(cursor.getColumnIndex(UsersDBHelper.KEY_ID));
                String login = cursor.getString(cursor.getColumnIndex(UsersDBHelper.KEY_LOGIN));
                String password = cursor.getString(cursor.getColumnIndex(UsersDBHelper.KEY_PASSWORD));
                row[0] = id;
                row[1] = login;
                row[2] = password;
                users.add(row);
                result = result + "\n" + i + " : " + id + ", " +  login + ", " + password;
                i += 1;
            } while (cursor.moveToNext());
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
        return users;
    }
}
