package com.example.android_hw1.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android_hw1.DataManager;
import com.example.android_hw1.Model.Record;
import com.example.android_hw1.R;

public class ListFragment extends Fragment {

    private ListView list_LV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initListView();
        return view;
    }

    private void initListView() {
        if(DataManager.getInstance().getTopRecords() != null){
            ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getActivity(), android.R.layout.simple_expandable_list_item_1, DataManager.getInstance().getTopRecords());
            list_LV.setAdapter(adapter);
        }
    }


    private void findViews(View view){
        this.list_LV = view.findViewById(R.id.list_LV);
    }
}