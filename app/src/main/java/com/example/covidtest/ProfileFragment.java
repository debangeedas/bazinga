package com.example.covidtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText name, email, address, gender, dob;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        address = view.findViewById(R.id.profile_address);
        gender = view.findViewById(R.id.profile_gender);
        dob = view.findViewById(R.id.profile_dob);
        final Button edit = view.findViewById(R.id.editDetails);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name.setText(sharedPreferences.getString("name", ""));
        email.setText(sharedPreferences.getString("email", ""));
        address.setText(sharedPreferences.getString("address", ""));
        dob.setText(sharedPreferences.getString("dob", ""));
        gender.setText(sharedPreferences.getString("gender", ""));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText() == "Save Changes") {
                    updateSharedPreferences();

                    name.setEnabled(false);
                    email.setEnabled(false);
                    dob.setEnabled(false);
                    gender.setEnabled(false);
                    address.setEnabled(false);

                    edit.setText("Edit Details");
                } else {
                    name.setEnabled(true);
                    email.setEnabled(true);
                    dob.setEnabled(true);
                    address.setEnabled(true);
                    gender.setEnabled(true);

                    edit.setText("Save Changes");
                }

            }
        });

        return view;
    }

    private void updateSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();

        editor.putString("name", name.getText().toString());
        editor.putString("email", email.getText().toString());
        editor.putString("address", address.getText().toString());
        editor.putString("dob", dob.getText().toString());
        editor.putString("gender", gender.getText().toString());

        editor.apply();
    }
}
