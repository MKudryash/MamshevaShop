package com.example.mamshevashop;



import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button   btnOrder,btnBaseOpen;
    EditText etName, etPrice;
    TextView TxtSumma;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    float Summa = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnBaseOpen = (Button) findViewById(R.id.btnBaseActivity);
        btnBaseOpen.setOnClickListener(this);


        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);

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

                Button addBtn = new Button(this);
                addBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        View outputDBRow = (View) view.getParent();
                        ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                        outputDB.removeView(outputDBRow);
                        outputDB.invalidate();
                        Summa += Float.parseFloat(OutPutAut.getText().toString());
                        TxtSumma.setText(Summa+" руб.");
                        UpdateTable();
                    }
                });
                params.weight = 1.0f;
                addBtn.setLayoutParams(params);
                addBtn.setText("Добавить в корзину");
                addBtn.setId(cursor.getInt(idIndex));
                dbOuyPutRow.addView(addBtn);


                dbOutPut.addView(dbOuyPutRow);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnBaseActivity:
                Intent intent = new Intent(MainActivity.this, BaseAdd.class);
                startActivity(intent);
                break;
            case R.id.btnOrder:
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Сумма заказа: " +Summa+" руб.", Toast.LENGTH_SHORT);
                toast.show();
                Summa=0;
                TxtSumma.setText("0 руб");
                break;
            default:
                break;
        }
    }
}

