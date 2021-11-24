package com.example.mamshevashop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Avtoriz extends AppCompatActivity implements View.OnClickListener {
    TextView username, passworfFiled;
    Button loginbtn, signbtn;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtoriz);

        loginbtn = (Button) findViewById(R.id.btnLogIn);
        loginbtn.setOnClickListener((View.OnClickListener) this);

        signbtn = (Button) findViewById(R.id.btnSignIn);
        signbtn.setOnClickListener(this);

        username = (EditText) findViewById(R.id.UserName);
        passworfFiled = (EditText) findViewById(R.id.editTextNumberPassword);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAMES, "admin");
        contentValues.put(DBHelper.KEY_PASSWORD, "admin");
        database.insert(DBHelper.TABLE_USERS, null, contentValues);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogIn:
                Cursor logcursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
                Boolean logged = false;
                if (logcursor.moveToFirst()) {
                    int usernameIndex = logcursor.getColumnIndex(DBHelper.KEY_NAMES);
                    int PassIndex = logcursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do {
                        if (username.getText().toString().equals(logcursor.getString(usernameIndex)) && passworfFiled.getText().toString().equals(logcursor.getString(PassIndex))) {
                            com.example.mamshevashop.User.Userrr = true;
                            startActivity(new Intent(this, BaseAdd.class));
                            logged = true;
                            break;
                        } else if (username.getText().toString().equals(logcursor.getString(usernameIndex)) && passworfFiled.getText().toString().equals(logcursor.getString(PassIndex))) {
                            com.example.mamshevashop.User.Userrr = false;
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            logged = true;
                            break;
                        }
                    } while (logcursor.moveToNext());

                }
                logcursor.close();
                if (logged == false)
                    Toast.makeText(this, "Пользователя не существует", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnSignIn:
                Cursor signIn = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                boolean finded = false;
                if (signIn.moveToFirst()) {
                    int usernameIndex = signIn.getColumnIndex(DBHelper.KEY_NAMES);
                    do {
                        if (username.getText().toString().equals(signIn.getString(usernameIndex))) {
                            Toast.makeText(this, "Пользователь с таким логином уже сущетсвует", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    } while (signIn.moveToNext());
                }

                if (!finded) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_NAMES, username.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, passworfFiled.getText().toString());
                    database.insert(DBHelper.TABLE_USERS, null, contentValues);
                    Toast.makeText(this, "Вы успешно зарегистрировались", Toast.LENGTH_LONG).show();
                }
                signIn.close();
                break;

        }
    }
}