package com.amw.mws.myapplication.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.fragment.DriverFragment;
import com.amw.mws.myapplication.member.ViolationLog;

import java.util.List;



public class DriverViolationsRecyclerViewAdapter  extends RecyclerView.Adapter<DriverViolationsRecyclerViewAdapter.DriverViolationViewHolder>{

    List<ViolationLog> violationsLog;
    Context context;
    DriverFragment fragment;

    public DriverViolationsRecyclerViewAdapter(DriverFragment fragment,List<ViolationLog> violationsLog, Context context) {
        this.violationsLog = violationsLog;
        this.context = context;
        this.fragment=fragment;
    }

    @Override
    public DriverViolationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_violation_driver,parent,false);

        return new DriverViolationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DriverViolationViewHolder holder, int position) {
        final ViolationLog violationLog=violationsLog.get(position);

        holder.txtViewLogID.setText(violationLog.log_ID);
        holder.txtViewViolationType.setText(violationLog.violationType);
        holder.txtViewTax.setText(violationLog.tax);
        holder.txtViewLocation.setText(violationLog.location);
        holder.txtViewDate.setText(violationLog.date);

        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragment.pay(violationLog.log_ID);
            }
        });


    }

    public void setViolationsLog(List<ViolationLog> violationsLog) {
        this.violationsLog = violationsLog;
        notifyDataSetChanged();
    }

    public void clear() {
        violationsLog.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return violationsLog.size();
    }

    public class DriverViolationViewHolder extends RecyclerView.ViewHolder{

        TextView txtViewViolationType,txtViewTax,txtViewLocation,txtViewDate,txtViewLogID;
        Button btnPay;

        public DriverViolationViewHolder(View v) {
            super(v);

            txtViewDate=v.findViewById(R.id.textViewDate);
            txtViewViolationType=v.findViewById(R.id.textViewViolationType);
            txtViewTax=v.findViewById(R.id.textViewTax);
            txtViewLocation=v.findViewById(R.id.textViewLocation);
            txtViewLogID=v.findViewById(R.id.textViewLogID);

            btnPay=v.findViewById(R.id.btnPay);
        }
    }
}
