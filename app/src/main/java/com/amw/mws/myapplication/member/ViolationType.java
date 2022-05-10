package com.amw.mws.myapplication.member;



public class ViolationType {
    public static final String VIOLATION_ID="Violation_Id";
    public static final String VIOLATION_TYPE="Violation_Type";
    public static final String TAX="Tax";
    public static final String VIOLATIONS_TYPES="violations";

    public String violationID;
    public String violationType;
    public String tax;

    public ViolationType(String violationID, String violationType, String tax) {
        this.violationID = violationID;
        this.violationType = violationType;
        this.tax = tax;
    }
}
