package com.example.knowyourgovernment_diepholz;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PoliticalAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<PoliticalOfficial> politicalOfficials;
    private MainActivity mainActivity;

    public PoliticalAdapter(List<PoliticalOfficial> politicalOfficialList, MainActivity mainActivity1){
        this.politicalOfficials = politicalOfficialList;
        this.mainActivity = mainActivity1;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int vType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.politicalofficial_view, viewGroup,false);

        view.setOnClickListener(mainActivity);
        view.setOnLongClickListener(mainActivity);

        return new ViewHolder(view);
    }

    @Override

    public void onBindViewHolder(ViewHolder viewHolder, int yeet){
        PoliticalOfficial politicalOfficial = politicalOfficials.get(yeet);

        viewHolder.position.setText(politicalOfficial.getPosition());

        viewHolder.fullName.setText(politicalOfficial.getName());

        viewHolder.partyRepresenting.setText(politicalOfficial.getParty());
    }

    @Override
    public int getItemCount(){
        return politicalOfficials.size();
    }
}
