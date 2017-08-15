package com.ubroker.arquimo.ubroker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelos.Referencias;

/**
 * Created by andresrodriguez on 13/06/17.
 */

public class MiRed extends AppCompatActivity {


    //TEST
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> listaNombres = new ArrayList<String>();
    List<String> listaMensajes = new ArrayList<String>();
    List<Referencias>expandableListDetail;
    private static  final String TAG = "MI RED | ";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_red);

        iniciarToolBar();






        expandableListView = (ExpandableListView) findViewById(R.id.elMiRed);
        expandableListDetail = DatosListaMiRed.getData();














        expandableListAdapter = new AdaptadorListaMiRed(this,  expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
              /*  Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();
*/
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show(); */
                return false;
            }
        });


    }

    public void iniciarToolBar(){
        Toolbar miBarra;
        miBarra = (Toolbar) findViewById(R.id.tbBarra_MiRed);
        miBarra.setTitle( getResources().getString(R.string.app_name) + " - Mi Red" );
        setSupportActionBar(miBarra);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null

        miBarra.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAtras(v);
            }
        });
    }

    public void irAtras(View vista){
        this.finish();
    }
}
