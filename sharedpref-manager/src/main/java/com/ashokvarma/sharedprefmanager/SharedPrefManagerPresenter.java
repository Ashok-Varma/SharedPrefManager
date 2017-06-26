package com.ashokvarma.sharedprefmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see SharedPrefManagerView
 * @since 22 Jun 2017
 */
class SharedPrefManagerPresenter {

    private LinkedHashMap<String, SharedPreferences> mLinkedNameSharedPrefMap = new LinkedHashMap<>();
    private String mSelectedSharedPref;
    private boolean mIsAscendingSort = true;

    private static final ArrayList<String> sharedPrefSupportedTypes = new ArrayList<>();

    static {
        sharedPrefSupportedTypes.add("String");
        sharedPrefSupportedTypes.add("Boolean");
        sharedPrefSupportedTypes.add("Integer");
        sharedPrefSupportedTypes.add("Long");
        sharedPrefSupportedTypes.add("Float");
        sharedPrefSupportedTypes.add("Set");
    }

    private SharedPrefManagerView mSharedPrefManagerView;


    SharedPrefManagerPresenter(Bundle appBundle, Context context) {
        ArrayList<String> privateSharedPrefs = appBundle.getStringArrayList(SharedPrefManagerActivity.PRIVATE_SHARED_PREF_NAMES);
        ArrayList<String> worldReadSharedPrefs = appBundle.getStringArrayList(SharedPrefManagerActivity.WORLD_READ_SHARED_PREF_NAMES);
        ArrayList<String> worldWriteSharedPrefs = appBundle.getStringArrayList(SharedPrefManagerActivity.WORLD_WRITE_SHARED_PREF_NAMES);

        if (privateSharedPrefs != null) {
            for (String currentSharedPRef : privateSharedPrefs) {
                mLinkedNameSharedPrefMap.put(currentSharedPRef, context.getSharedPreferences(currentSharedPRef, Context.MODE_PRIVATE));
            }
        }

        if (worldReadSharedPrefs != null) {
            for (String currentSharedPRef : worldReadSharedPrefs) {
                mLinkedNameSharedPrefMap.put(currentSharedPRef, context.getSharedPreferences(currentSharedPRef, Context.MODE_PRIVATE));
            }
        }
        if (worldWriteSharedPrefs != null) {
            for (String currentSharedPRef : worldWriteSharedPrefs) {
                mLinkedNameSharedPrefMap.put(currentSharedPRef, context.getSharedPreferences(currentSharedPRef, Context.MODE_PRIVATE));
            }
        }
    }

    public SharedPrefManagerView getView() {
        return mSharedPrefManagerView;
    }

    void bindView(SharedPrefManagerView sharedPrefManagerView) {
        mSharedPrefManagerView = sharedPrefManagerView;
        initView();
    }

    void unBindView(SharedPrefManagerView sharedPrefManagerView) {
        mSharedPrefManagerView = null;
    }

    private void initView() {
        getView().setSubTitle(getView().getApplicationName());

        getView().setUpSpinner(mLinkedNameSharedPrefMap.keySet());
    }

    void loadDataForSharedPref(String sharedPrefName) {
        mSelectedSharedPref = sharedPrefName;
        ArrayList<SharedPrefItemModel> sharedPrefItemModels = new ArrayList<>();
        Map<String, ?> prefs = mLinkedNameSharedPrefMap.get(mSelectedSharedPref).getAll();
        for (String key : prefs.keySet()) {
            // value will never be null we enter null value the pref will be deleted
            sharedPrefItemModels.add(new SharedPrefItemModel(key, prefs.get(key), ContextCompat.getColor(getView().getViewContext(), R.color.sharedpref_manager_colorAccent)));
        }

        refreshSortIcon();
        if (sharedPrefItemModels.isEmpty()) {
            getView().showNoPrefItemsView();
        } else {
            Collections.sort(sharedPrefItemModels, new Comparator<SharedPrefItemModel>() {
                @Override
                public int compare(SharedPrefItemModel sharedPrefItemModel1, SharedPrefItemModel sharedPrefItemModel2) {
                    return (mIsAscendingSort ? 1 : -1) * sharedPrefItemModel1.compareTo(sharedPrefItemModel2);
                }
            });
            getView().displaySharedPrefItems(sharedPrefItemModels);
        }

    }

    void onClearCurrentSharedPrefClicked() {
        getView().showDeleteConfirmationDialog();
    }

    @SuppressLint("ApplySharedPref")
    void clearCurrentSharedPref() {
        mLinkedNameSharedPrefMap.get(mSelectedSharedPref).edit().clear().commit();
        loadDataForSharedPref(mSelectedSharedPref);
    }

    void toggleSort() {
        mIsAscendingSort = !mIsAscendingSort;
        loadDataForSharedPref(mSelectedSharedPref);
    }

    void refreshSortIcon(){
        if (mIsAscendingSort) {
            getView().changeSortIcon(R.drawable.ic_sort_ascending_white_24dp);
        } else {
            getView().changeSortIcon(R.drawable.ic_sort_descending_white_24dp);
        }
    }

    void refreshCurrentSharedPref() {
        loadDataForSharedPref(mSelectedSharedPref);
    }

    void onSharedPrefItemClicked(SharedPrefItemModel sharedPrefItemModel) {
        getView().showAddEditPrefItemDialog(true, sharedPrefItemModel, mSelectedSharedPref, sharedPrefSupportedTypes, getValueType(sharedPrefItemModel));
    }

    void onAddPrefItemClicked() {
        getView().showAddEditPrefItemDialog(false, null, mSelectedSharedPref, sharedPrefSupportedTypes, 0);
    }

    void deleteSharedPrefItem(String sharedPrefItemModelKey, String selectedSharedPref) {
        mLinkedNameSharedPrefMap.get(selectedSharedPref).edit().remove(sharedPrefItemModelKey).apply();
        refreshCurrentSharedPref();
    }

    @StringRes
    int addOrUpdateSharedPrefItem(String newValue, String sharedPrefKey, String selectedSharedPref, int selectedSupportedTypePosition) {

        if (TextUtils.isEmpty(sharedPrefKey)) {
            return R.string.sharedpref_manager_invalid_key_exception;
        }

        if (selectedSupportedTypePosition == 0) {
            mLinkedNameSharedPrefMap.get(selectedSharedPref).edit().putString(sharedPrefKey, newValue).apply();
        } else if (selectedSupportedTypePosition == 1) {
            mLinkedNameSharedPrefMap.get(selectedSharedPref).edit().putBoolean(sharedPrefKey, !("0".equalsIgnoreCase(newValue) || "false".equalsIgnoreCase(newValue))).apply();
        } else if (selectedSupportedTypePosition == 2) {
            try {
                mLinkedNameSharedPrefMap.get(selectedSharedPref).edit().putInt(sharedPrefKey, Integer.parseInt(newValue)).apply();
            } catch (Exception e) {
                return R.string.sharedpref_manager_int_parse_exception;
            }
        } else if (selectedSupportedTypePosition == 3) {
            try {
                mLinkedNameSharedPrefMap.get(selectedSharedPref).edit().putLong(sharedPrefKey, Long.parseLong(newValue)).apply();
            } catch (Exception e) {
                return R.string.sharedpref_manager_long_parse_exception;
            }
        } else if (selectedSupportedTypePosition == 4) {
            try {
                mLinkedNameSharedPrefMap.get(selectedSharedPref).edit().putFloat(sharedPrefKey, Float.parseFloat(newValue)).apply();
            } catch (Exception e) {
                return R.string.sharedpref_manager_float_parse_exception;
            }
        } else if (selectedSupportedTypePosition == 5) {
            try {
                mLinkedNameSharedPrefMap.get(selectedSharedPref).edit().putStringSet(sharedPrefKey, getSetFromString(newValue)).apply();
            } catch (Exception e) {
                return R.string.sharedpref_manager_set_parse_exception;
            }
        } else {
            return R.string.sharedpref_manager_unknown_exception;
        }

        return -1;
    }

    private Set<String> getSetFromString(String string) throws ClassFormatError {

        if (string.startsWith("[")) {
            string = string.substring(1);
        }
        if (string.endsWith("]")) {
            string = string.substring(0, string.length() - 2);
        }

        if (string.isEmpty() || "null".equalsIgnoreCase(string)) {
            return null;
        }

        List<String> items = Arrays.asList(string.split("\\s*,\\s*"));
        return new HashSet<>(items);
    }

    private int getValueType(SharedPrefItemModel sharedPrefItemModel) {
        Object sharedPrefItemModelValue = sharedPrefItemModel.getValue();
        if (sharedPrefItemModelValue instanceof String) {
            return 0;
        } else if (sharedPrefItemModelValue instanceof Boolean) {
            return 1;
        } else if (sharedPrefItemModelValue instanceof Integer) {
            return 2;
        } else if (sharedPrefItemModelValue instanceof Long) {
            return 3;
        } else if (sharedPrefItemModelValue instanceof Float) {
            return 4;
        } else if (sharedPrefItemModelValue instanceof Set) {
            return 5;
        }
        return 0;
    }
}
