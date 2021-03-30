package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addRow extends AppCompatActivity {

    private List<String> names = new ArrayList<>();

    EditText eText1;
    EditText eText2;
    EditText eText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_row);
        getSupportActionBar().hide();
        eText1 = (EditText) findViewById(R.id.edit_message1);
        eText2 = (EditText) findViewById(R.id.edit_message2);
        eText3 = (EditText) findViewById(R.id.edit_message3);
        setEditText(eText1);
        setEditText(eText2);
        setEditText(eText3);
    }

    protected void onResume() {
        super.onResume();
        String data = utils.readFromFile(this, "config.txt");
        String[] temp = data.split("&");
        for (int i = 0; i < temp.length; i++) {
            names.add(temp[i].split(">")[0]);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setEditText(EditText edittext) {
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void addRow(View view) { // defined in layout xml file
        // show typed text to text view when goto button clicked
        String str1 = String.valueOf(eText1.getText());
        String str2 = String.valueOf(eText2.getText());
        String str3 = String.valueOf(eText3.getText());

        Log.d("LoadData", String.valueOf(checkContainSpecial(str1)));
        if (str1.isEmpty() || str2.isEmpty() || str3.isEmpty()) {
            getError(1);
            return;
        }
        else if (checkContainSpecial(str1) || checkContainSpecial(str2) || checkContainSpecial(str3)) {
            getError(3);
            return;
        }

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(str1)) {
                getError(2);
                return;
            }
        }

        Log.d("a", str1 + "," + str2 + ";");

        try {
            FileOutputStream fOut = openFileOutput("config.txt",  Context.MODE_APPEND);
            if (utils.writeToFile(str1 + ">" + str2 + ">" + str3 + "&", fOut)) {
                Toast.makeText(getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        catch(Exception e) {}

    }

    private void getError(int type) {
        String message;

        switch(type) {
            case 1:
                message = "field cannot be empty.";
                break;
            case 2:
                message = "Name already exist.";
                break;
            case 3:
                message = "Unauthorized input";
                break;
            default:
                message = "no message.";
                break;
        };
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkContainSpecial (String str) {
        String specialStr = ">&";
        for (int i=0; i < str.length() ; i++) {
            char ch = str.charAt(i);
            if(specialStr.contains(Character.toString(ch))) {
                return true;
            }
        }
        return false;
    }
}