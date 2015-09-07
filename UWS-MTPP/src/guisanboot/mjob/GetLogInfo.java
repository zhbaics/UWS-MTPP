/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.mjob;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.stream.*;
import java.util.ArrayList;


public class GetLogInfo {

    public static ArrayList _getLogInfo(String logFileName) {
        ArrayList myList = new ArrayList();

        String inData = "";
        String beginTime = "";
        String beginJobId = "";

        File logFile = new File(logFileName);
        long fileSize = logFile.length();
        long count = 0;


        FileImageInputStream logFileStream = null;
        try {
            logFileStream = new FileImageInputStream(logFile);


            while(count < fileSize) {
                inData=logFileStream.readLine();
                System.out.println("inData:------" + inData);
                if(inData == null) {
                    break;
                }
                count += inData.length();
                if(inData.contains("begin") ) {
                    //作业开始行
                    String[] begigData = analytical(inData);
                    beginTime = begigData[0];
                    beginJobId = begigData[1];
                } else if(inData.contains("rootid")) {
                    //作业结束行
                    String[] endData = analytical(inData);
                    if(!beginJobId.equals(endData[1])) {
                        continue;
                    }
                    Mjob log = new Mjob();
                    log.beginTime = beginTime;
                    log.endTime = endData[0];
                    for(int i = 1; i < endData.length; i++) {
                        String[] temp = endData[i].split("=");
                        if(temp[0].equals("id")) {
                            log.mjob = Integer.parseInt(temp[1]);
                        } else if(temp[0].equals("rootid")) {
                            log.rootid = Integer.parseInt(temp[1]);
                        } else if(temp[0].equals("destrootid")) {
                            log.destrootid = Integer.parseInt(temp[1]);
                        } else if(temp[0].equals("size")) {
                            log.size = Long.decode(temp[1]);
                        } else if(temp[0].equals("transfersize")) {
                            log.transfersize = Long.decode(temp[1]);
                        } else if(temp[0].equals("runtime")) {
                            log.runtime = tools(temp[1]);
//                            log.runtime = Integer.parseInt(temp[1]);
                        }
                    }
                    myList.add(log);
                }

            }

        } catch (FileNotFoundException e1) {
            Logger.getLogger(GetLogInfo.class.getName()).log(Level.SEVERE, null, e1);

        } catch (IOException e2) {
            Logger.getLogger(GetLogInfo.class.getName()).log(Level.SEVERE, null, e2);
        } catch (NumberFormatException e3) {
            Logger.getLogger(GetLogInfo.class.getName()).log(Level.SEVERE, null, e3);
        } finally {
            try {
                logFileStream.close();
            } catch (IOException ex) {
                Logger.getLogger(GetLogInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return myList;
    }

    private static String[] analytical(String inStr) {
        String[] result = inStr.split(" ");
        String temp = result[0];
        int index =temp.lastIndexOf("]");
        temp = temp.substring(1, index);
        result[0] = temp;

        return result;
    }

    static String tools(String input) {
        int input1 = Integer.parseInt(input);
        //小时  
        int hh = input1/3600;
        //分钟  
        int mm = (input1-hh*3600)/60;
        //秒数  
        int ss = (input1-hh*3600-mm*60);
        String time = hh+"h"+mm+"min"+ss+"s";
        return time;  
    }  

}
