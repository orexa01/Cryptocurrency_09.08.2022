package com.cryptocurrency_java.cryptocurrency_java;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToolBar extends Fragment implements View.OnClickListener{

    ImageView next, menu;

    interface ToolBarButtonClick{
        void ToolBarClick(int idButtonClick);
    }

    private ToolBarButtonClick toolBarButtonClick;
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            toolBarButtonClick = (ToolBarButtonClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс toolBarButtonClick");
        }
    }

    public ToolBar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_bar, container, false);
        Init(view);
        return view;
    }

    private void Init(View view){
        next = view.findViewById(R.id.next);
        menu = view.findViewById(R.id.menu);

        next.setOnClickListener(this);
        menu.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next:
//                Toast.makeText(getActivity(), "next",Toast.LENGTH_LONG).show();
                toolBarButtonClick.ToolBarClick(0);
                break;
            case R.id.menu:
//                Toast.makeText(getActivity(), "menu",Toast.LENGTH_LONG).show();
                toolBarButtonClick.ToolBarClick(1);
                break;

        }
    }




}