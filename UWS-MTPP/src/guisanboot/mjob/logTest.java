/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.mjob;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class logTest {

    public static void main(String[] args) {
//        int count = args.length;                // 参数个数
        String logFileName = "C:\\work\\mjob.log";                //参数[0]：Log文件名
        ArrayList myList = null;

//        if (count > 0) {
            try {
//                logFileName = args[0];

                if( logFileName.length() == 0){
                    Logger.getLogger(GetLogInfo.class.getName()).log(Level.SEVERE, "args.length =0", args);
                } else {
                    GetLogInfo logInfo = new GetLogInfo();
                    myList = logInfo._getLogInfo(logFileName);
                    for(int i = 0; i < myList.size(); i++){
                        Mjob log_Data = (Mjob)myList.get(i);
//                        System.out.println("beginTime=" + log_Data.beginTime + "\n");
//                        System.out.println("endTime=" + log_Data.endTime + "\n");
//                        System.out.println("jobId=" + log_Data.mjob + "\n");
//                        System.out.println("rootid=" + log_Data.rootid + "\n");
//                        System.out.println("destrootid=" + log_Data.destrootid + "\n");
//                        System.out.println("size=" + Long.toString(log_Data.size) + "\n");
//                        System.out.println("transfersize=" + Long.toString(log_Data.transfersize) + "\n");
//                        System.out.println("runtime=" + log_Data.runtime + "\n");
                        System.out.println("----------");
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(GetLogInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
//        } else {
//            Logger.getLogger(GetLogInfo.class.getName()).log(Level.SEVERE, "args.length =0", args);
//        }
    }
}
