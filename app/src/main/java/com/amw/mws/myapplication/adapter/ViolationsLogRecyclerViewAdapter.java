package com.amw.mws.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.activity.UpdateViolationLogActivity;
import com.amw.mws.myapplication.fragment.ViolationsLogFragment;
import com.amw.mws.myapplication.member.ViolationLog;

import java.util.List;



public class ViolationsLogRecyclerViewAdapter extends RecyclerView.Adapter<ViolationsLogRecyclerViewAdapter.ViolationLogViewHolder> {

    List<ViolationLog>violationsLog;
    Context context;
    ViolationsLogFragment violationsLogFragment;

    public ViolationsLogRecyclerViewAdapter(List<ViolationLog> violationsLog, Context context) {
        this.violationsLog = violationsLog;
        this.context = context;
    }

    public ViolationsLogRecyclerViewAdapter(ViolationsLogFragment violationsLogFragment, List<ViolationLog> violationsLog, Context context) {
        this.violationsLog = violationsLog;
        this.context = context;
        this.violationsLogFragment=violationsLogFragment;
    }

    @Override
    public ViolationLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_violation_log,parent,false);

        return new ViolationLogViewHolder(view);
    }

    public void setViolationsLog(List<ViolationLog> violationsLog) {
        this.violationsLog = violationsLog;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViolationLogViewHolder holder, int position) {
        final ViolationLog violationLog=violationsLog.get(position);

        holder.txtViewPlugedNumber.setText(violationLog.plugedNumber);
        holder.txtViewDriver.setText(violationLog.driver);
        holder.txtViewDate.setText(violationLog.date);
        holder.txtViewLocation.setText(violationLog.location);
        holder.txtViewIsPaid.setText(violationLog.isPaid);
        holder.txtViewViolation.setText(violationLog.violationType);
        holder.txtViewLogID.setText(violationLog.log_ID);
        holder.txtViewTax.setText(violationLog.tax);

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo start update violation log activity with LogID
                Intent intent=new Intent(context,UpdateViolationLogActivity.class);
                intent.putExtra(ViolationLog.VLOG_ID,violationLog.log_ID);
                context.startActivity(intent);
            }
        });

       /* holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                violationsLogFragment.deleteViolationLog(violationLog.log_ID);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return violationsLog.size();
    }

    public void clear() {
        violationsLog.clear();
        notifyDataSetChanged();
    }

    public class ViolationLogViewHolder extends RecyclerView.ViewHolder{

        public TextView txtViewTax,txtViewPlugedNumber,txtViewViolation,txtViewLocation,txtViewDate,txtViewIsPaid,txtViewLogID,txtViewDriver;
        public Button btnUpdate,btnDelete;

        public ViolationLogViewHolder(View v) {
            super(v);
            txtViewPlugedNumber=v.findViewById(R.id.textViewPlugedNumber);
            txtViewViolation=v.findViewById(R.id.textViewViolationType);
            txtViewLocation=v.findViewById(R.id.textViewLocation);
            txtViewDate=v.findViewById(R.id.textViewDate);
            txtViewIsPaid=v.findViewById(R.id.textViewIsPaid);
            txtViewLogID=v.findViewById(R.id.textViewLogID);
            txtViewDriver=v.findViewById(R.id.textViewDriver);
            txtViewTax=v.findViewById(R.id.textViewTax);

          //  btnDelete=v.findViewById(R.id.buttonDelete);
            btnUpdate=v.findViewById(R.id.buttonEdit);
        }
    }
}
