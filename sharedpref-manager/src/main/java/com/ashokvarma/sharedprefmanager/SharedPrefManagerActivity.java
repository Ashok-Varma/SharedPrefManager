package com.ashokvarma.sharedprefmanager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class SharedPrefManagerActivity extends AppCompatActivity implements SharedPrefManagerView, AdapterView.OnItemSelectedListener, SharedPrefAdapter.Listener, View.OnClickListener {

    static final String PRIVATE_SHARED_PREF_NAMES = "private_shared_pref_names";
    static final String WORLD_READ_SHARED_PREF_NAMES = "world_read_shared_pref_names";
    static final String WORLD_WRITE_SHARED_PREF_NAMES = "world_write_shared_pref_names";

    private AppCompatSpinner mSharedPrefSelectionSpinner;
    private FloatingActionButton mAddFab;
    private RecyclerView mSharedPrefRecyclerView;
    private TextView mErrorText;
    private Toolbar mToolbar;

    private MenuItem mSortItem;

    private SharedPrefAdapter mSharedPrefAdapter;
    private SharedPrefManagerPresenter mSharedPrefManagerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_pref_manager);

        mSharedPrefManagerPresenter = new SharedPrefManagerPresenter(getIntent().getExtras(), this);

        mSharedPrefSelectionSpinner = findViewById(R.id.sharedpref_manager_spinner);
        mAddFab = findViewById(R.id.sharedpref_manager_add_fab);
        mSharedPrefRecyclerView = findViewById(R.id.sharedpref_manager_recycler_view);
        mErrorText = findViewById(R.id.sharedpref_manager_error_layout);

        mToolbar = findViewById(R.id.sharedpref_manager_toolbar);
        setSupportActionBar(mToolbar);

        mSharedPrefAdapter = new SharedPrefAdapter(this, null);
        mSharedPrefAdapter.setListener(this);
        mSharedPrefRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSharedPrefRecyclerView.setAdapter(mSharedPrefAdapter);
        mSharedPrefSelectionSpinner.setOnItemSelectedListener(this);
        mAddFab.setOnClickListener(this);

        mSharedPrefManagerPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        mSharedPrefManagerPresenter.unBindView(this);
        super.onDestroy();
    }

    @NonNull
    @Override
    public AppCompatActivity getViewContext() {
        return this;
    }

    @Override
    public void setUpSpinner(Set<String> sharedPrefNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sharedPrefNames.toArray(new String[sharedPrefNames.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSharedPrefSelectionSpinner.setAdapter(adapter);
    }

    @Override
    public String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }

    @Override
    public void setSubTitle(String subTitle) {
        mToolbar.setSubtitle(subTitle);
    }

    @Override
    public void displaySharedPrefItems(ArrayList<SharedPrefItemModel> sharedPrefItemModelList) {
        mSharedPrefRecyclerView.setVisibility(View.VISIBLE);
        mErrorText.setVisibility(View.GONE);
        mSharedPrefAdapter.setSharedPrefItemModelList(sharedPrefItemModelList);
    }

    @Override
    public void showNoPrefItemsView() {
        mSharedPrefRecyclerView.setVisibility(View.GONE);
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(R.string.sharedpref_manager_no_data);
    }

    @Override
    public void showAddEditPrefItemDialog(final boolean isEdit, @Nullable final SharedPrefItemModel sharedPrefItemModel, final String selectedSharedPref, ArrayList<String> sharedPrefSupportedTypes, int preSelectTypePosition) {
        if (isEdit && sharedPrefItemModel == null) {
            return;
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.sharedpref_manager_edit_new_dialog, null);

        final Spinner spinner = dialogView.findViewById(R.id.sharedpref_manager_dialog_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sharedPrefSupportedTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(preSelectTypePosition);

        dialogView.findViewById(R.id.sharedpref_manager_dialog_key_input_layout).setVisibility(isEdit ? View.GONE : View.VISIBLE);

        final TextInputEditText valueString = dialogView.findViewById(R.id.sharedpref_manager_dialog_value_edit_text);
        valueString.setText(sharedPrefItemModel == null ? "" : sharedPrefItemModel.getValue().toString());

        final TextInputEditText keyString = dialogView.findViewById(R.id.sharedpref_manager_dialog_key_edit_text);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog
                .Builder(this)
                .setView(dialogView)
                .setTitle(isEdit ? getString(R.string.sharedpref_manager_edit_key_title, sharedPrefItemModel.getKey(), selectedSharedPref) : getString(R.string.sharedpref_manager_add_item_title, selectedSharedPref))
                .setPositiveButton(R.string.sharedpref_manager_update, null)
                .setNegativeButton(R.string.sharedpref_manager_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

        if (isEdit) {
            alertDialogBuilder.
                    setNeutralButton(R.string.sharedpref_manager_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSharedPrefManagerPresenter.deleteSharedPrefItem(sharedPrefItemModel.getKey(), selectedSharedPref);
                            dialog.dismiss();
                        }
                    });
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        int error = mSharedPrefManagerPresenter
                                .addOrUpdateSharedPrefItem(
                                        getTextFrom(valueString)
                                        , isEdit ? sharedPrefItemModel.getKey() : getTextFrom(keyString)
                                        , selectedSharedPref
                                        , spinner.getSelectedItemPosition()
                                );

                        if (error == -1) {
                            dialog.dismiss();
                            mSharedPrefManagerPresenter.refreshCurrentSharedPref();
                        } else {
                            Toast.makeText(SharedPrefManagerActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    @NonNull
    private String getTextFrom(TextInputEditText textInputEditText) {
        Editable editable = textInputEditText.getText();
        return editable == null ? "" : editable.toString();
    }

    @Override
    public void showDeleteConfirmationDialog(String sharedPrefName) {
        new AlertDialog
                .Builder(this)
                .setTitle(getString(R.string.sharedpref_manager_clear_sharedpref, sharedPrefName))
                .setMessage(getString(R.string.sharedpref_manager_clear_dialog_message, sharedPrefName, sharedPrefName))
                .setPositiveButton(R.string.sharedpref_manager_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSharedPrefManagerPresenter.clearCurrentSharedPref();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.sharedpref_manager_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void changeSortIcon(@DrawableRes int drawableRes) {
        if (mSortItem != null) {
            mSortItem.setIcon(drawableRes);
        }
    }

    @Override
    public void onSharedPrefItemClicked(SharedPrefItemModel sharedPrefItemModel) {
        mSharedPrefManagerPresenter.onSharedPrefItemClicked(sharedPrefItemModel);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSharedPrefManagerPresenter.loadDataForSharedPref(mSharedPrefSelectionSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sharedpref_manager_menu, menu);
        mSortItem = menu.findItem(R.id.action_sharedpref_sort);
        if (mSharedPrefManagerPresenter != null) {
            mSharedPrefManagerPresenter.refreshSortIcon();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_sharedpref_sort) {
            mSharedPrefManagerPresenter.toggleSort();
            return true;
        } else if (i == R.id.action_sharedpref_refresh) {
            mSharedPrefManagerPresenter.refreshCurrentSharedPref();
            return true;
        } else if (i == R.id.action_sharedpref_clear) {
            mSharedPrefManagerPresenter.onClearCurrentSharedPrefClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sharedpref_manager_add_fab) {
            mSharedPrefManagerPresenter.onAddPrefItemClicked();
        }
    }
}
