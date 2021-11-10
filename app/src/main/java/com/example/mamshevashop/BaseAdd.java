package com.example.mamshevashop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class BaseAdd extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnClear,btnMainActivity;
    EditText etName, etPrice;
    TextView TxtSumma;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    float Summa = 0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_base_add);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

            btnMainActivity = (Button) findViewById(R.id.btnMainActivity);
            btnMainActivity.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);


        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.TextPrice);

        TxtSumma = (TextView) findViewById(R.id.Summa);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();

    }
    public  void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int AutIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutPut = findViewById(R.id.dbOutPut);
            dbOutPut.removeAllViews();
            do {
                TableRow dbOuyPutRow = new TableRow( this);
                dbOuyPutRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);

                TextView OutPutID = new TextView(this);
                params.weight = 1.0f;
                OutPutID.setLayoutParams(params);
                OutPutID.setText(cursor.getString(idIndex));
                dbOuyPutRow.addView(OutPutID);

                TextView OutPutName = new TextView(this);
                params.weight = 3.0f;
                OutPutName.setLayoutParams(params);
                OutPutName.setText(cursor.getString(nameIndex));
                dbOuyPutRow.addView(OutPutName);

                TextView OutPutAut = new TextView(this);
                params.weight = 3.0f;
                OutPutAut.setLayoutParams(params);
                OutPutAut.setText(cursor.getString(AutIndex));
                dbOuyPutRow.addView(OutPutAut);


                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить товар");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOuyPutRow.addView(deleteBtn);

                dbOutPut.addView(dbOuyPutRow);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAdd:
                String name = etName.getText().toString();
                String author = etPrice.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_PRICE, author);
                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                etName.setText(null);
                etPrice.setText(null);
                UpdateTable();
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                TableLayout dbOutPut = findViewById(R.id.dbOutPut);
                dbOutPut.removeAllViews();
                etName.setText(null);
                etPrice.setText(null);
                UpdateTable();
                break;
            case R.id.btnMainActivity:
                Intent intent = new Intent(BaseAdd.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();
                database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID+ " = ?", new String[]{String.valueOf(v.getId())});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                    int AutIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex)>realID){
                            contentValues.put(DBHelper.KEY_ID, realID);
                            contentValues.put(DBHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(AutIndex));
                            database.replace(DBHelper.TABLE_CONTACTS, null, contentValues);
                        }
                        realID++;
                    }while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()&& v.getId()!=realID){
                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}
