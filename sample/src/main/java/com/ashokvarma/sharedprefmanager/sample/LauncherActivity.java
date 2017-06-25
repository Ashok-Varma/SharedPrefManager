package com.ashokvarma.sharedprefmanager.sample;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ashokvarma.sharedprefmanager.SharedPrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class LauncherActivity extends AppCompatActivity {

    private static final String SP_PRI_APP = "App SharedPref";
    private static final String SP_PRI_USER = "User SharedPref";
    private static final String SP_PRI_DATABASE = "Database SharedPref";
    private static final String SP_PRI_TUTS = "TUTORIAL SharedPref";
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
                floodDummyData(getSharedPreferences(SP_PRI_APP, MODE_PRIVATE));
                floodDummyData(getSharedPreferences(SP_PRI_USER, MODE_PRIVATE));
                floodDummyData(getSharedPreferences(SP_PRI_DATABASE, MODE_PRIVATE));
                floodDummyData(getSharedPreferences(SP_PRI_TUTS, MODE_PRIVATE));

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    floodDummyData(getSharedPreferences(SP_WORLD_READ_USER, MODE_WORLD_READABLE));
                    floodDummyData(getSharedPreferences(SP_WORLD_READ_DATABASE, MODE_WORLD_READABLE));

                    floodDummyData(getSharedPreferences(SP_WORLD_WRITE_USER, MODE_WORLD_WRITEABLE));
                }
            }
        });

        findViewById(R.id.clear_test_data_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData(getSharedPreferences(SP_PRI_APP, MODE_PRIVATE));
                clearData(getSharedPreferences(SP_PRI_USER, MODE_PRIVATE));
                clearData(getSharedPreferences(SP_PRI_DATABASE, MODE_PRIVATE));
                clearData(getSharedPreferences(SP_PRI_TUTS, MODE_PRIVATE));

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
                                , new ArrayList<>(Arrays.asList(new String[]{SP_PRI_APP, SP_PRI_USER, SP_PRI_DATABASE, SP_PRI_TUTS, SP_PRI_NO_DATA}))
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
                                , new ArrayList<>(Arrays.asList(new String[]{SP_PRI_APP, SP_PRI_USER, SP_PRI_DATABASE, SP_PRI_TUTS, SP_PRI_NO_DATA}))
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

    @SuppressLint("ApplySharedPref")
    public void floodDummyData(SharedPreferences sharedPreferences) {
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
                .putString("string_null", null)
                .putString("string", "String")
                .putStringSet("string_set_null", null)
                .putStringSet("string_set", new HashSet<>(Arrays.asList(new String[]{"set1", "set2", "set3", "set1"})))
                .commit();
    }
}
