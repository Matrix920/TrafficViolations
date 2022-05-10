package com.amw.mws.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.activity.UpdateViolationTypeActivity;
import com.amw.mws.myapplication.member.ViolationType;

import java.util.List;



public class ViolationsTypesRecyclerViewAdapter extends RecyclerView.Adapter<ViolationsTypesRecyclerViewAdapter.ViolationTypeViewHolder> {

    Context context;
    List<ViolationType>violationsTypes;

    public ViolationsTypesRecyclerViewAdapter(Context context, List<ViolationType> violationsTypes) {
        this.context = context;
        this.violationsTypes = violationsTypes;
    }

    public void setViolationsTypes(List<ViolationType> violationsTypes) {
        this.violationsTypes = violationsTypes;
        notifyDataSetChanged();
    }

    public void clear() {
        violationsTypes.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViolationTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_violation_type,parent,false);

        return new ViolationTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViolationTypeViewHolder holder, int position) {
        final ViolationType violationType=violationsTypes.get(position);

        holder.txtViewTax.setText(violationType.tax);
        holder.txtViewViolationType.setText(violationType.violationType);
        holder.txtViewViolationID.setText(violationType.violationID);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo start update violation type activity with violationID
                Intent intent=new Intent(context, UpdateViolationTypeActivity.class);
                intent.putExtra(ViolationType.VIOLATION_ID,violationType.violationID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return violationsTypes.size();
    }

    public class ViolationTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewViolationID,txtViewViolationType,txtViewTax;
        public Button btnEdit;

        public ViolationTypeViewHolder(View v) {
            super(v);
            txtViewViolationID=v.findViewById(R.id.textViewViolationID);
            txtViewViolationType=v.findViewById(R.id.textViewViolationType);
            txtViewTax=v.findViewById(R.id.textViewTax);
            btnEdit=v.findViewById(R.id.buttonEdit);
        }
    }
}
