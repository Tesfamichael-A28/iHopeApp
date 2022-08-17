package com.example.MovieStoreList;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Name = (EditText) findViewById(R.id.ename);
        Password = (EditText) findViewById(R.id.epassword);
        Info = (TextView) findViewById(R.id.einfo);
        Login = (Button) findViewById(R.id.ebtn);
        Info.setText("No of attempts remaining: 5 ");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate(String username, String userPassword) {
        if ((username.equals("")) && (userPassword.equals(""))) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else {
            counter--;
            Info.setText("No of attempts remaining:" + String.valueOf(counter));
            if (counter == 0) {
                Login.setEnabled(false);
            }
        }

    }
}
