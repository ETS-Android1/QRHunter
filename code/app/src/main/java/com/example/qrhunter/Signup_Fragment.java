package com.example.qrhunter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class Signup_Fragment extends DialogFragment {
    private Context context = (Context) getContext();
    private TextInputLayout txtInputUsername, txtInputPass, txtInputConfirmPass, txtInputEmail, txtInputPhone;
    private OnFragmentInteractionListener listener;

    public Signup_Fragment() {
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(String username, String pass, String passConf, String email, String phone);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_signup, null);

        String title = "Sign Up";
        // Code for handling input for user info
        txtInputUsername = view.findViewById(R.id.txtInputUsername);
        txtInputPass = view.findViewById(R.id.txtInputPassword);
        txtInputConfirmPass = view.findViewById(R.id.txtInputPasswordConfirm);
        txtInputEmail = view.findViewById(R.id.txtInputEmail);
        txtInputPhone = view.findViewById(R.id.txtInputPhone);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String name = edtName.getText().toString();
//                        String date = edtDate.getText().toString();
//                        int numRolls = Integer.parseInt(edtNumRolls.getText().toString());
//                        int numSides = Integer.parseInt(edtNumSides.getText().toString());
                        String username = txtInputUsername.getEditText().getText().toString();
                        String pass = txtInputPass.getEditText().getText().toString();
                        String passConf = txtInputConfirmPass.getEditText().getText().toString();
                        String email = txtInputEmail.getEditText().getText().toString();
                        String phone = txtInputPhone.getEditText().getText().toString();

                        listener.onOkPressed(username, pass, passConf, email, phone);
                    }
                }).create();
    }

}