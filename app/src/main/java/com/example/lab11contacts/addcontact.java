package com.example.lab11contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class addcontact extends AppCompatActivity  {
    // key listner is used how bas metl listner hata bas bade a3mel she yetsarf howe mtl clickable or no
    private KeyListener listenername;
    private KeyListener listenernumber;
    EditText name;
    EditText number;
    Button save;
    ArrayList<String> names= new ArrayList<>();
    ArrayList<String> numbers= new ArrayList<>();
    String origin;
    String originalname;
    String originalnumber;
    int position;// howe lindex le ana kbst 3le
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        listenername = name.getKeyListener(); // Save the default KeyListener!!!(text view for name is editable)
        listenernumber = number.getKeyListener();
        save = findViewById(R.id.contact);
        Intent in = getIntent();// getting intent of contacts
        names = (ArrayList<String>) in.getSerializableExtra("names");
        numbers = (ArrayList<String>) in.getSerializableExtra("numbers");
        origin = in.getStringExtra("origin");// bade a3ref ana 3a shu kbst
        position = in.getIntExtra("position", 0);// 3am e5od lindex lases

        if (origin.equals("view")) // ya3ne ana kabaset 3al view la a3mel view
        {
            name.setText(names.get(position));
            number.setText(numbers.get(position));
            name.setKeyListener(null);// not editable
            number.setKeyListener(null);
            save.setVisibility(View.INVISIBLE);
            originalname = name.getText().toString();
            originalnumber = number.getText().toString();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name != null && number != null) {
                    if (!names.contains(originalname)) {
                        names.add(name.getText().toString());
                        numbers.add(number.getText().toString());
                        name.setKeyListener(null);
                        number.setKeyListener(null);
                        save.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(addcontact.this, MainActivity.class);
                        intent.putExtra("names", names);
                        intent.putExtra("numbers", numbers);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        int po = names.indexOf(originalname);
                        names.set(po, name.getText().toString());
                        numbers.set(po, number.getText().toString());
                        name.setKeyListener(null);
                        number.setKeyListener(null);
                        save.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(addcontact.this, MainActivity.class);
                        i.putExtra("names", names);
                        i.putExtra("numbers", numbers);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.contextualmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.edit:
                edit();
                return true;
            case R.id.delete:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void edit(){
        name.setKeyListener(listenername);
        number.setKeyListener(listenernumber);
        save.setVisibility(View.VISIBLE);
    }

    public void delete(){
        names.remove(position);
        numbers.remove(position);
        showdeletedialog();
    }

    public void showdeletedialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(addcontact.this);
        builder.setMessage("This contact have been deleted");
        builder.setNeutralButton("Contacts", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(addcontact.this, MainActivity.class);
                i.putExtra("names", names);
                i.putExtra("numbers", numbers);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        dialog= builder.create();
        dialog.show();
    }
}