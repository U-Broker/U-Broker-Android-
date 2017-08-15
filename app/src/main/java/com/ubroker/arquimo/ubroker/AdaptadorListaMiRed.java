package com.ubroker.arquimo.ubroker;

/**
 * Created by andresrodriguez on 03/08/17.
 */

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import modelos.Referencias;

public class AdaptadorListaMiRed extends BaseExpandableListAdapter {
    //TEST
    private Context context;
    private List<Referencias> expandableListDetail;
    private static final String TAG = "MI RED ADAPTADOR | ";

    public AdaptadorListaMiRed(Context context, List<Referencias> expandableListDetail) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) { return  this.expandableListDetail.get(listPosition).getReferenciados().get(expandedListPosition).getNombre() + " " + this.expandableListDetail.get(listPosition).getReferenciados().get(expandedListPosition).getApellido();}

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_lista_mi_red, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.tvNombreReferencia_MiRed);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(listPosition).getReferenciados().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListDetail.get(listPosition).getNombre() + " "+ this.expandableListDetail.get(listPosition).getApellido();
    }

    public String getMessage(int posicion){
        return this.expandableListDetail.get(posicion).getMensaje();
    }
    @Override
    public int getGroupCount() {
        return this.expandableListDetail.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String listTitle = (String) getGroup(listPosition);
        String listMessage =  getMessage(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fila_lista_mi_red, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.tvNombreSeccion_MiRed);
        TextView listMessageView = (TextView) convertView.findViewById(R.id.tvInfoSeccion_MiRed);
        listTitleTextView.setText(listTitle);
        listMessageView.setText(listMessage);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
