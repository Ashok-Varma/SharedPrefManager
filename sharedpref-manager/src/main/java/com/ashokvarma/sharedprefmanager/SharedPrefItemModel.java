package com.ashokvarma.sharedprefmanager;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @since 22 Jun 2017
 */
class SharedPrefItemModel implements Comparable<SharedPrefItemModel> {
    @NonNull
    private final String mKey;
    @NonNull
    private final Object mValue;

    @NonNull
    private final SpannableString mDisplayText;

    SharedPrefItemModel(@NonNull String key, @NonNull Object value, @ColorInt int keyColor) {
        this.mKey = key;
        this.mValue = value;

        String valueString = mValue.toString();

        if (mValue instanceof String) {
            // check if there is a better way  to do this
            try {
                valueString = new JSONObject(valueString).toString(4);
            } catch (JSONException ex) {
                try {
                    valueString = new JSONArray(valueString).toString(4);
                } catch (JSONException ex1) {

                }
            }
        }

        SpannableString textToDisplay = new SpannableString(key + " \n\n " + valueString);

        textToDisplay.setSpan(new ForegroundColorSpan(keyColor), 0, mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textToDisplay.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textToDisplay.setSpan(new RelativeSizeSpan(1.5f), 0, mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mDisplayText = textToDisplay;
    }

    @NonNull
    String getKey() {
        return mKey;
    }

    @NonNull
    Object getValue() {
        return mValue;
    }

    @NonNull
    SpannableString getDisplayText() {
        return mDisplayText;
    }

    @Override
    public int compareTo(@NonNull SharedPrefItemModel model) {
        return mDisplayText.toString().compareTo(model.mDisplayText.toString());
    }
}