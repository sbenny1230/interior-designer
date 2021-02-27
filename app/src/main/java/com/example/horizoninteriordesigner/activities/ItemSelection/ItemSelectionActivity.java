package com.example.horizoninteriordesigner.activities.ItemSelection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.horizoninteriordesigner.*;
import com.example.horizoninteriordesigner.activities.ArCamera.ArCameraActivity;
import com.example.horizoninteriordesigner.activities.ItemSelection.adapters.ItemSelectionAdapter;
import com.example.horizoninteriordesigner.models.Item;
import com.example.horizoninteriordesigner.models.ItemDB;

import java.util.ArrayList;

import static com.example.horizoninteriordesigner.activities.ArCamera.ArCameraActivity.ITEM_KEY;


public class ItemSelectionActivity extends AppCompatActivity {
    GridView itemsGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_selection);
        itemsGridView = findViewById(R.id.grid_view_items);


        // Initialises a version of the default items "database"
        ItemDbApplication itemDbApplication = (ItemDbApplication)this.getApplication();
        ArrayList<Item> itemArrayList = itemDbApplication.getItemDB().getItems();

        ItemSelectionAdapter itemSelectionAdapter = new ItemSelectionAdapter(this, itemArrayList);
        itemsGridView.setAdapter(itemSelectionAdapter);

        // If an item has been selected, pass back its Item id
        itemsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemSelectionActivity.this, ArCameraActivity.class);
                Item selectedItem = itemSelectionAdapter.getItem(position);

                intent.putExtra(ITEM_KEY, selectedItem.getId());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}