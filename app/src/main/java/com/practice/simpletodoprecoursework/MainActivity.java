package com.practice.simpletodoprecoursework;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.apache.commons.io.FileUtils;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;
    Button btnAdd;
    EditText eItem;
    RecyclerView rView;
    ItemAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("David's Todo App");
        btnAdd = findViewById(R.id.btnAdd);
        eItem = findViewById(R.id.editText);
        rView = findViewById(R.id.rvItems);


        loadItems();
        ItemAdapter.OnClickListener onClickListener = new ItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // create new edit activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass data
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);

                //display edit activity
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };

        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClick(int position) {
                    //delete
                items.remove(position);
                    //notify of deletion
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT);
                saveItems();
            }
        };
        itemsAdapter = new ItemAdapter(items,onLongClickListener, onClickListener);
        rView.setAdapter(itemsAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = eItem.getText().toString();
                //add item to model
                items.add(todoItem);
                //notify adapter
                itemsAdapter.notifyItemInserted(items.size() - 1);
                eItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //retrieve updated text val
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //extract position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update model
            items.set(position, itemText);
            //notify adapter
            itemsAdapter.notifyItemChanged(position);
            //persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_SHORT);
        } else {
            Log.w("MainActivity", "Unknown call to onActivity");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    // reading function
    private  void loadItems(){
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<String>();
        }

    }
    //writing file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
        }
    }
}