package com.example.android_hw1.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android_hw1.Interfaces.Callback_List;
import com.example.android_hw1.DataManager;
import com.example.android_hw1.Model.Record;
import com.example.android_hw1.R;

public class ListFragment extends Fragment {

    private ListView list_LV;
    private Callback_List callback_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initListView();

        list_LV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                double lat = DataManager.getInstance().getTopRecords().get(position).getLatitude();
                double lon = DataManager.getInstance().getTopRecords().get(position).getLongitude();
                String namePlayer = DataManager.getInstance().getTopRecords().get(position).getName();
                callback_list.setMapLocation(lat,lon,namePlayer);
            }
        });

        return view;
    }

    private void initListView() {
        if(DataManager.getInstance().getTopRecords() != null){
            ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getActivity(), R.layout.item_list, DataManager.getInstance().getTopRecords());
            list_LV.setAdapter(adapter);
        }
    }


    private void findViews(View view){
        this.list_LV = view.findViewById(R.id.list_LV);
    }

    public void setCallback_list(Callback_List callback_list){
        this.callback_list = callback_list;
    }
}