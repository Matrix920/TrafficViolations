package com.amw.mws.myapplication.member;

import java.util.List;


public class LoadedInformation {

    private static LoadedInformation loadedInformation;

     List<ViolationType>violationsTypes;

     List<ViolationLog>violationsLog;

     List<VehicleLog>vehicles;


    private LoadedInformation(){

    }


    public static LoadedInformation getInstance(){
        if(loadedInformation==null){
            loadedInformation=new LoadedInformation();
        }

        return loadedInformation;
    }

    public List<ViolationType> getViolationsTypes() {
        return violationsTypes;
    }

    public List<ViolationLog> getViolationsLog() {
        return violationsLog;
    }

    public List<VehicleLog> getVehicles() {
        return vehicles;
    }

    public void setViolationsTypes(List<ViolationType> violationsTypes) {
        this.violationsTypes = violationsTypes;
    }

    public void setViolationsLog(List<ViolationLog> violationsLog) {
        this.violationsLog = violationsLog;
    }

    public void setVehicles(List<VehicleLog> vehicles) {
        this.vehicles = vehicles;
    }
}
