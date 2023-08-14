package com.example.lab11contacts;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    ListView contacts;
    ArrayList<String> names= new ArrayList<String>();
    ArrayList<String> numbers= new ArrayList<String>();
    ArrayAdapter<String> adapter ;
    File namefile;
    File numberfile;
    String toadd="add";
    String toview="view";

    //202 is returning from creating a new contact
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contacts = findViewById(R.id.contactslist);
        namefile= new File(this.getFilesDir(),"names.txt");
        numberfile= new File(this.getFilesDir(), "numbers.txt");
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, names);
        contacts.setAdapter(adapter);
        adapter.notifyDataSetChanged();//hata y2elo lal adapter eno 8ayaret bel data
        try {
            readFile(); //does the file exist or not and read from it(i read the file on opining)
        } catch (IOException e) {
            e.printStackTrace();
        }
        contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent vieww= new Intent(MainActivity.this,addcontact.class);
                vieww.putExtra("names",names);
                vieww.putExtra("numbers",numbers);
                vieww.putExtra("position",position);
                vieww.putExtra("origin",toview);
                startActivityForResult(vieww, 204);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){//menu inflater
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){//item select from the menu
        switch(item.getItemId()){
            case R.id.add:
                Intent addcontact= new Intent(MainActivity.this,addcontact.class);
                addcontact.putExtra("names",names);
                addcontact.putExtra("numbers",numbers);
                addcontact.putExtra("origin",toadd);
                startActivityForResult(addcontact, 202);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void readFile() throws IOException {
        if(!namefile.exists()){
            namefile.createNewFile();
        }
        if(!numberfile.exists()){
            numberfile.createNewFile();
        }
        //reading the name file
        Scanner readfile= new Scanner(openFileInput("names.txt"));
        while(readfile.hasNext()){
            String line= readfile.nextLine();
            names.add(line);
        }
        // closing name file
        readfile.close();
        // reading the number file
        readfile= new Scanner(openFileInput("numbers.txt"));
        while(readfile.hasNext()){
            String line= readfile.nextLine();
            numbers.add(line);
        }
        // closing the number file
        readfile.close();
    }

    public void onActivityResult(int requestcode, int resultcode, Intent intent){
        super.onActivityResult(requestcode, resultcode,intent);

        if(requestcode==202){
            if(resultcode==RESULT_OK){
                names= (ArrayList<String>) intent.getSerializableExtra("names");
                numbers= (ArrayList<String>) intent.getSerializableExtra("numbers");
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, names);
                contacts.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                try {
                    write();// writing in the files
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestcode==204){
            if(resultcode==RESULT_OK) {
                names = (ArrayList<String>) intent.getSerializableExtra("names");
                numbers = (ArrayList<String>) intent.getSerializableExtra("numbers");
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, names);
                contacts.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void write() throws FileNotFoundException {
        // to write in name file
        try(PrintStream output= new PrintStream(openFileOutput("names.txt",MODE_PRIVATE))){
            for(String name: names){
                output.println(name);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        // to write in file number
        try(PrintStream output= new PrintStream(openFileOutput("numbers.txt",MODE_PRIVATE))){
            for(String number: numbers){
                output.println(number);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}