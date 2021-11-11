package com.example.banksalad.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public void basicToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void returnDlg(String alamText) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
        dlg.setMessage(alamText); // 내용

        dlg.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dlg.show();
    }
}
