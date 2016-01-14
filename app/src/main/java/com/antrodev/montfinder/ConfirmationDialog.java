package com.antrodev.montfinder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;

/**
 * Created by antoinedanois on 14/01/2016.
 */
class ConfirmationDialog extends Activity {


    public Dialog onCreateDialog() {
        final Activity parent = getParent();
        return new AlertDialog.Builder(this)
                .setMessage(R.string.request_permission)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(parent,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                    }
                })

                .create();
    }
}
