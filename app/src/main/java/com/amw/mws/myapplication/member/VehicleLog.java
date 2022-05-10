package com.amw.mws.myapplication.member;



public class VehicleLog {
    public static final String VEHICLES="vehicles";
    public static final String PLUGED_NUMBER="Pluged_Number";
    public static final String DRIVER="Driver";
    public static final String TYPE="Type";
    public static final String CATEGORY="Category";
    public static final String REGISERATION_DATE="Registeration_Date";
    public static final String PRODUCTION_DATE="Production_Date";
    public static final String IS_CROSS_OUT="Is_Cross_Out";
    public static final String IS_ADMIN="isAdmin";

    public String plugedNumber;
    public String driver;
    public String category;
    public String type;
    public String registerationDate;
    public String productionDate;
    public String isCrossOut;

    public VehicleLog(String plugedNumber, String driver, String category, String type, String registerationDate, String productionDate, int isCrossOut) {
        this.plugedNumber = plugedNumber;
        this.driver = driver;
        this.category = category;
        this.type = type;
        this.registerationDate = registerationDate;
        this.productionDate = productionDate;
        if(isCrossOut==0){
            this.isCrossOut="No";
        }else{
            this.isCrossOut="Yes";
        }
    }
}
