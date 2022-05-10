package com.amw.mws.myapplication.utils;

import com.amw.mws.myapplication.member.LoadedInformation;
import com.amw.mws.myapplication.member.ViolationLog;

import java.util.List;

/**
 * Created by Matrix on 12/11/2018.
 */

public class UtilViolationLog {

    public static ViolationLog getViolationLog(String id){

        List<ViolationLog> violationLogList= LoadedInformation.getInstance().getViolationsLog();
        for(ViolationLog violationLog:violationLogList){
            if(violationLog.log_ID.equals(id)){
                return violationLog;
            }
        }
        return null;
    }


}
