package com.amw.mws.myapplication.utils;

import com.amw.mws.myapplication.member.LoadedInformation;
import com.amw.mws.myapplication.member.ViolationType;

import java.util.List;

/**
 * Created by Matrix on 12/11/2018.
 */

public class UtilViolationType {

    public static ViolationType getViolationType(String id){
        List<ViolationType> violationList= LoadedInformation.getInstance().getViolationsTypes();
        for(ViolationType violationType:violationList){
            if(violationType.violationID.equals(id)){
                return violationType;
            }
        }
        return null;
    }
}
