package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> itemsInString = new ArrayList<String>();

    ArrayList<Item> items;

    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setItems();

        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.float_menu, menu);
        menu.setHeaderTitle("Actions");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (selectedPosition != -1) {
            if (item.getItemId() == R.id.opt1) {
                Toast.makeText(this, items.get(selectedPosition).getChineseD(), Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (item.getItemId() == R.id.opt2) {
                removeItem(selectedPosition);
                return true;
            }
            else {
                return false;
            }
        }
        else return false;
    }

    protected void onRestart() {
        super.onRestart();
        String data = utils.readFromFile(this, "config.txt");
        if (!data.isEmpty()) {
            String[] temp = data.split("&");

            List<String> tempList = Arrays.asList(temp);
            Log.d("loadData", String.valueOf(temp.length > itemsInString.size()));

            Log.d("loadData", String.valueOf(temp.length +" "+ itemsInString.size()));

            if (temp.length > itemsInString.size()) {
                setItem(temp[itemsInString.size()], itemsInString.size());

                itemsInString = new ArrayList<>(tempList);
            }
        }
    }

    public void toAddRow(View view) {
        startActivity(new Intent(this, addRow.class)); //start new Activity
    }

    public void getInfo(View view) {
        String data = utils.readFromFile(this, "config.txt");
        itemsInString = new ArrayList<>(Arrays.asList(data.split("&")));
        //Log.d("loaddata", itemsInString.get(4));
    }

    public void setItems() {
        String data = utils.readFromFile(this, "config.txt");

        if (!data.isEmpty()) {
            itemsInString = new ArrayList<>(Arrays.asList(data.split("&")));
            for (int i = 0; i < itemsInString.size(); i++) {
                String[] temp = new String[2];
                temp = itemsInString.get(i).split(">");
                try {items.add(new Item(temp[0].replace("\n", ""), temp[1], temp[2]));}
                catch(Exception e) {items.add(new Item(temp[0], "empty", temp[2]));}
            }
        }
    }

    private void setItem(String str, int position) {
        String[] temp = str.split(">");
        items.add(new Item(temp[0].replace("\n", ""), temp[1], temp[2]));
        Log.d("loadData", String.valueOf("position" + position));
        adapter.notifyItemInserted(position);
    }

    private void init() {

        items = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        adapter = new adapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener((new adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedPosition = position;
                adapter.notifyItemChanged(position);
            }
        }));
    }

    public void removeItem(int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);

        itemsInString.remove(position);
        try {
            Log.d("loadData", itemsInString.get(itemsInString.size()-1));
        }
        catch(Exception e) {}

        String temp = arraylistToString(itemsInString);

        Log.d("loadData", temp);

        try {
            FileOutputStream fOut = openFileOutput("config.txt",  Context.MODE_PRIVATE);
            if (utils.writeToFile(temp, fOut)) {
                Toast.makeText(getBaseContext(), "Item deleted", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e) {}
    }

    private String arraylistToString(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();

        for (String s : arrayList) {
            sb.append(s);
            sb.append("&");
        }

        return sb.toString();
    }
}