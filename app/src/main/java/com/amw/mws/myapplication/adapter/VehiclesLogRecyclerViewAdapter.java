package com.amw.mws.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.fragment.VehiclesFragment;
import com.amw.mws.myapplication.member.VehicleLog;

import java.util.List;



public class VehiclesLogRecyclerViewAdapter extends RecyclerView.Adapter<VehiclesLogRecyclerViewAdapter.VehicleViewHolder> {

    LayoutInflater inflater;
    Context context;
    List<VehicleLog>vehiclesLog;
    VehiclesFragment fragment;

    public VehiclesLogRecyclerViewAdapter(VehiclesFragment fragment,Context context, List<VehicleLog>vehiclesLog) {
        this.context = context;
        this.vehiclesLog=vehiclesLog;
        this.fragment=fragment;
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_log,parent,false);
        return new VehicleViewHolder(view);
    }
    public void clear() {
        vehiclesLog.clear();
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        final VehicleLog vehicleLog=vehiclesLog.get(position);
        holder.txtViewPlugedNumber.setText(vehicleLog.plugedNumber);
        holder.txtViewDriver.setText(vehicleLog.driver);
        holder.txtViewCategory.setText(vehicleLog.category);
        holder.txtViewType.setText(vehicleLog.type);
        holder.txtViewProductionDate.setText(vehicleLog.productionDate);
        holder.txtViewRegisterationDate.setText(vehicleLog.registerationDate);
        holder.txtViewIsCrossOut.setText(vehicleLog.isCrossOut);

        holder.btnCrossOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo cross out vehicle
                fragment.crossOutVehicle(vehicleLog.plugedNumber);
            }
        });
    }

    public void setVehiclesLog(List<VehicleLog> vehiclesLog) {
        this.vehiclesLog = vehiclesLog;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return vehiclesLog.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder{

        public TextView txtViewPlugedNumber,txtViewDriver,txtViewCategory,txtViewType,txtViewRegisterationDate,txtViewProductionDate,txtViewIsCrossOut;
        public Button btnCrossOut;

        public VehicleViewHolder(View v) {
            super(v);
            txtViewPlugedNumber=v.findViewById(R.id.textViewPlugedNumber);
            txtViewDriver=v.findViewById(R.id.textViewDriver);
            txtViewCategory=v.findViewById(R.id.textViewCategory);
            txtViewType=v.findViewById(R.id.textViewType);
            txtViewRegisterationDate=v.findViewById(R.id.textViewRegisterationDate);
            txtViewProductionDate=v.findViewById(R.id.textViewProductionDate);
            txtViewIsCrossOut=v.findViewById(R.id.textViewIsCrossOut);
            btnCrossOut=v.findViewById(R.id.buttonCrossOut);
        }
    }
}
