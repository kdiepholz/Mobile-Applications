package com.example.knowyourgovernment_diepholz;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView position;

    public TextView fullName;

    public TextView partyRepresenting;

    public ViewHolder(View view){
        super(view);

        position = view.findViewById(R.id.position);
        fullName = view.findViewById(R.id.name);
        partyRepresenting = view.findViewById(R.id.party);
    }
}
