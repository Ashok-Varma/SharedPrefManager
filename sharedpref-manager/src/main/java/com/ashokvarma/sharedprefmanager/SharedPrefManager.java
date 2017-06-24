package com.ashokvarma.sharedprefmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @since 21 Jun 2017
 */
public class SharedPrefManager {

    public static void launchSharedPrefManager(Context context, ArrayList<String> privateSharedPrefNames, ArrayList<String> worldReadSharedPrefNames, ArrayList<String> worldWriteSharedPrefNames) {
        boolean worldModeSupported = Build.VERSION.SDK_INT < Build.VERSION_CODES.N;

        int worldModeSharedPrefCount = (worldReadSharedPrefNames == null ? 0 : worldReadSharedPrefNames.size()) + (worldWriteSharedPrefNames == null ? 0 : worldWriteSharedPrefNames.size());
        int worldModeSupportedSharedPrefCount = worldModeSupported ? worldModeSharedPrefCount : 0;
        int totalSupportedSharedPrefCount = (privateSharedPrefNames == null ? 0 : privateSharedPrefNames.size()) + worldModeSupportedSharedPrefCount;

        if (totalSupportedSharedPrefCount == 0) {
            Toast.makeText(context, "No Supported Shared Pref Names so skipping activity call", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(context, SharedPrefManagerActivity.class);
        intent.putStringArrayListExtra(SharedPrefManagerActivity.PRIVATE_SHARED_PREF_NAMES, privateSharedPrefNames);

        if (worldModeSupported) {
            intent.putStringArrayListExtra(SharedPrefManagerActivity.WORLD_READ_SHARED_PREF_NAMES, worldReadSharedPrefNames);
            intent.putStringArrayListExtra(SharedPrefManagerActivity.WORLD_WRITE_SHARED_PREF_NAMES, worldWriteSharedPrefNames);
        } else {
            if (worldModeSharedPrefCount != 0)
                Toast.makeText(context, "MODE_WORLD_READABLE, MODE_WORLD_WRITEABLE are not supported above Android N (Nougat)", Toast.LENGTH_LONG).show();
        }
        context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
