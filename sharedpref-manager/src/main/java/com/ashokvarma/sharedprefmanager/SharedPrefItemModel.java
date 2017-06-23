package com.ashokvarma.sharedprefmanager;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see
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

        SpannableString textToDisplay = new SpannableString(key + " \n\n " + mValue);

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