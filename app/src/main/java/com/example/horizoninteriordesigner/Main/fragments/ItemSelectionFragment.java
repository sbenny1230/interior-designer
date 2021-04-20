package com.example.horizoninteriordesigner.Main.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.horizoninteriordesigner.ItemDbApplication;
import com.example.horizoninteriordesigner.Main.adapters.ItemSelectionAdapter;
import com.example.horizoninteriordesigner.R;
import com.example.horizoninteriordesigner.models.Item;

import java.util.ArrayList;


public class ItemSelectionFragment extends Fragment implements ItemSelectionAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private ItemSelectionAdapter adapter;
    private ArrayList<Item> itemArrayList;


    public ItemSelectionFragment() {
        // Required empty public constructor
    }

    public ItemSelectionFragment(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ItemDbApplication itemDbApplication = (ItemDbApplication) getActivity().getApplication();
        itemArrayList = itemDbApplication.getItemDB().getItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_selection, container, false);

        recyclerView = view.findViewById(R.id.rv_items);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new ItemSelectionAdapter(getActivity(), this, itemArrayList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), itemArrayList.get(position).getName() + " has been selected", Toast.LENGTH_SHORT).show();
    }

    /**   @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }**/
}