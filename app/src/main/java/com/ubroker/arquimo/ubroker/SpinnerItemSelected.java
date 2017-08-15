package com.ubroker.arquimo.ubroker;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by andresrodriguez on 28/06/17.
 */

public class SpinnerItemSelected implements AdapterView.OnItemSelectedListener {
//TEST
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString() + "HOLA",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
