package com.amw.mws.myapplication.member;



public class ViolationLog {
    public static final String VLOG_ID="Log_ID";
    public static final String DATE="Date";
    public static final String IS_PAID="Is_Paid";
    public static final String VIOLATIONS_LOG="violations";
    public static final String LOCATION="Location";
    public static final String FROM_DATE="FromDate";
    public static final String TO_DATE="ToDate";

    public String plugedNumber;
    public String violationType;
    public String driver;
    public String location;
    public String date;
    public String isPaid;
    public String tax;
    public String log_ID;
    public String violationID;

    public ViolationLog(String violationID,String tax,String plugedNumber, String violationType, String location, String date, int isPaid, String log_ID,String driver) {
        this.plugedNumber = plugedNumber;
        this.violationType = violationType;
        this.location = location;
        this.date = date;
        if(isPaid==0){
            this.isPaid="No";
        }else{
            this.isPaid="Yes";
        }
        this.driver=driver;
        this.log_ID = log_ID;
        this.tax=tax;
        this.violationID=violationID;
    }

    public ViolationLog(String tax,String plugedNumber, String violationType, String location, String date, int isPaid, String log_ID,String driver) {
        this.plugedNumber = plugedNumber;
        this.violationType = violationType;
        this.location = location;
        this.date = date;
        if(isPaid==0){
            this.isPaid="No";
        }else{
            this.isPaid="Yes";
        }
        this.driver=driver;
        this.log_ID = log_ID;
        this.tax=tax;
    }

    public String getisPaid(){
        if(isPaid.equals("No")){
            return "0";
        }else{
            return "1";
        }
    }
    public ViolationLog(String violationType, String location, String date, String tax, String log_ID) {
        this.violationType = violationType;
        this.location = location;
        this.date = date;
        this.tax = tax;
        this.log_ID = log_ID;
    }
}
