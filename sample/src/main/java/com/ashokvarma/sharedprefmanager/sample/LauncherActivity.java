package com.ashokvarma.sharedprefmanager.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.sharedprefmanager.SharedPrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class LauncherActivity extends AppCompatActivity {

    private static final String SP_PRI_APP = "App SharedPref";
    private static final String SP_PRI_USER_JSON = "User Json SharedPref";
    private static final String SP_PRI_LONG_DATA = "Long Data SharedPref";
    private static final String SP_PRI_10_000_ENTRY = "10,000 entry SharedPref";
    private static final String SP_PRI_NO_DATA = "No Data SharedPref";

    private static final String SP_WORLD_READ_USER = "User SharedPref Read";
    private static final String SP_WORLD_READ_DATABASE = "Database SharedPref Read";

    private static final String SP_WORLD_WRITE_USER = "User SharedPref Write";

    @Override
    @SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        findViewById(R.id.load_test_data_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floodExtremes(getSharedPreferences(SP_PRI_APP, MODE_PRIVATE));
                floodJsons(getSharedPreferences(SP_PRI_USER_JSON, MODE_PRIVATE));
                floodLongNames(getSharedPreferences(SP_PRI_LONG_DATA, MODE_PRIVATE));
                floodLargeDataSet(getSharedPreferences(SP_PRI_10_000_ENTRY, MODE_PRIVATE));

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    floodJsons(getSharedPreferences(SP_WORLD_READ_USER, MODE_WORLD_READABLE));
                    floodExtremes(getSharedPreferences(SP_WORLD_READ_DATABASE, MODE_WORLD_READABLE));

                    floodExtremes(getSharedPreferences(SP_WORLD_WRITE_USER, MODE_WORLD_WRITEABLE));
                }
            }
        });

        findViewById(R.id.clear_test_data_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData(getSharedPreferences(SP_PRI_APP, MODE_PRIVATE));
                clearData(getSharedPreferences(SP_PRI_USER_JSON, MODE_PRIVATE));
                clearData(getSharedPreferences(SP_PRI_LONG_DATA, MODE_PRIVATE));
                clearData(getSharedPreferences(SP_PRI_10_000_ENTRY, MODE_PRIVATE));

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    clearData(getSharedPreferences(SP_WORLD_READ_USER, MODE_WORLD_READABLE));
                    clearData(getSharedPreferences(SP_WORLD_READ_DATABASE, MODE_WORLD_READABLE));

                    clearData(getSharedPreferences(SP_WORLD_WRITE_USER, MODE_WORLD_WRITEABLE));
                }
            }
        });

        findViewById(R.id.launch_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager
                        .launchSharedPrefManager(
                                LauncherActivity.this
                                , new ArrayList<>(Arrays.asList(new String[]{SP_PRI_APP, SP_PRI_USER_JSON, SP_PRI_LONG_DATA, SP_PRI_10_000_ENTRY, SP_PRI_NO_DATA}))
                                , new ArrayList<>(Arrays.asList(new String[]{SP_WORLD_READ_USER, SP_WORLD_READ_DATABASE}))
                                , new ArrayList<>(Arrays.asList(new String[]{SP_WORLD_WRITE_USER}))
                        );
            }
        });

        findViewById(R.id.launch_manager_wo_world).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager
                        .launchSharedPrefManager(
                                LauncherActivity.this
                                , new ArrayList<>(Arrays.asList(new String[]{SP_PRI_APP, SP_PRI_USER_JSON, SP_PRI_LONG_DATA, SP_PRI_10_000_ENTRY, SP_PRI_NO_DATA}))
                                , null
                                , null
                        );
            }
        });
    }

    @SuppressLint("ApplySharedPref")
    public void clearData(SharedPreferences sharedPreferences) {
        sharedPreferences.edit().clear().commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.launcher_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_github:
                String url = "https://github.com/Ashok-Varma/SharedPrefManager";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void floodExtremes(SharedPreferences sharedPreferences) {
        sharedPreferences
                .edit()
                .putLong("long_max", Long.MAX_VALUE)
                .putLong("long_min", Long.MIN_VALUE)
                .putLong("long", 100L)
                .putInt("int_max", Integer.MAX_VALUE)
                .putInt("int_min", Integer.MIN_VALUE)
                .putInt("int", 100)
                .putFloat("float_max", Float.MAX_VALUE)
                .putFloat("float_min", Float.MIN_VALUE)
                .putFloat("float", 100)
                .putBoolean("boolean_false", false)
                .putBoolean("boolean_true", true)
                .putString("string_null", null)// won't be saved in pref. if we pass null it delete the key from pref
                .putString("string_empty", "")
                .putString("string", "String")
                .putStringSet("string_set_null", null)// won't be saved in pref. if we pass null it delete the key from pref
                .putStringSet("string_set", new HashSet<>(Arrays.asList(new String[]{"set1", "set2", "set3", "set1"})))
                .apply();
    }

    public void floodLargeDataSet(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 10_000; i++) {
            editor
                    .putLong("long_max" + i, Long.MAX_VALUE)
                    .putLong("long_min" + i, Long.MIN_VALUE)
                    .putLong("long" + i, 100L)
                    .putInt("int_max" + i, Integer.MAX_VALUE)
                    .putInt("int_min" + i, Integer.MIN_VALUE)
                    .putInt("int" + i, 100)
                    .putFloat("float_max" + i, Float.MAX_VALUE)
                    .putFloat("float_min" + i, Float.MIN_VALUE)
                    .putFloat("float" + i, 100)
                    .putBoolean("boolean_false" + i, false)
                    .putBoolean("boolean_true" + i, true)
                    .putString("string_empty" + i, "")
                    .putString("string" + i, "String")
                    .putStringSet("string_set" + i, new HashSet<>(Arrays.asList(new String[]{"set1", "set2", "set3", "set1"})));
        }
        editor.apply();
    }

    public void floodLongNames(SharedPreferences sharedPreferences) {
        sharedPreferences
                .edit()
                .putLong("test big name ", Long.MAX_VALUE)
                .putInt("int_max", Integer.MAX_VALUE)
                .putFloat("float_max", Float.MAX_VALUE)
                .putBoolean("boolean_false", false)
                .putString("string", "String")
                .putStringSet("string_set", new HashSet<>(Arrays.asList(new String[]{"set1", "set2", "set3", "set1"})))
                .apply();
    }

    public void floodJsons(SharedPreferences sharedPreferences) {
        sharedPreferences
                .edit()
                .putString("user", "{\"id\":1,\"first_name\":\"FN 1\",\"last_name\":\"LN 1\",\"phone_number\":\"9876543211\",\"email\":\"email1@gmail.com\",\"friends\":[{\"id\":2,\"first_name\":\"FN 2\",\"last_name\":\"LN 2\",\"phone_number\":\"9876543212\",\"email\":\"email2@gmail.com\",\"favorite\":false},{\"id\":3,\"first_name\":\"FN 3\",\"last_name\":\"LN 3\",\"phone_number\":\"9876543213\",\"email\":\"email3@gmail.com\",\"favorite\":false}],\"favorite\":false}")
                .putString("user_friends", "[{\"id\":1,\"first_name\":\"FN 1\",\"last_name\":\"LN 1\",\"phone_number\":\"9876543211\",\"email\":\"email1@gmail.com\",\"favorite\":false},{\"id\":2,\"first_name\":\"FN 2\",\"last_name\":\"LN 2\",\"phone_number\":\"9876543212\",\"email\":\"email2@gmail.com\",\"favorite\":false},{\"id\":3,\"first_name\":\"FN 3\",\"last_name\":\"LN 3\",\"phone_number\":\"9876543213\",\"email\":\"email3@gmail.com\",\"favorite\":false},{\"id\":4,\"first_name\":\"FN 4\",\"last_name\":\"LN 4\",\"phone_number\":\"9876543214\",\"email\":\"email4@gmail.com\",\"favorite\":false},{\"id\":5,\"first_name\":\"FN 5\",\"last_name\":\"LN 5\",\"phone_number\":\"9876543215\",\"email\":\"email5@gmail.com\",\"favorite\":false},{\"id\":6,\"first_name\":\"FN 6\",\"last_name\":\"LN 6\",\"phone_number\":\"9876543216\",\"email\":\"email6@gmail.com\",\"favorite\":false},{\"id\":7,\"first_name\":\"FN 7\",\"last_name\":\"LN 7\",\"phone_number\":\"9876543217\",\"email\":\"email7@gmail.com\",\"favorite\":false},{\"id\":8,\"first_name\":\"FN 8\",\"last_name\":\"LN 8\",\"phone_number\":\"9876543218\",\"email\":\"email8@gmail.com\",\"favorite\":false},{\"id\":9,\"first_name\":\"FN 9\",\"last_name\":\"LN 9\",\"phone_number\":\"9876543219\",\"email\":\"email9@gmail.com\",\"favorite\":false}]")
                .putString("user_empty", "")
                .apply();
    }
}
