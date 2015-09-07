/*
 * GetAuditLog.java
 *
 * Created on Aug 10, 2009, 12:00 AM
 */
package guisanboot.mjob.cmd;

import guisanboot.audit.cmd.*;
import guisanboot.audit.data.BackupUser;
import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.mjob.GetLogInfo;
import javax.swing.*;
import javax.swing.table.*;
import java.io.IOException;

import mylib.UI.*;
import guisanboot.audit.data.*;
import guisanboot.ui.*;
import guisanboot.res.*;
import guisanboot.data.*;
import guisanboot.mjob.Mjob;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetMjobLog extends NetworkRunning {

    private Mjob curMjobLog;
    private DefaultTableModel model;
    private SanBootView view;
    private int count;
    private String logFileName = " ";
    private ArrayList myList;
    String beginTime = "";
    String beginJobId = "";
    String inData = "";

    /** Creates a new instance of GetTaskLog */
    public GetMjobLog(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetMjobLog(String cmd) {
        super(cmd);
    }

    public GetMjobLog() {
    }

    public void parser(String line) {

        String s1 = line.trim();
//        System.out.println("#####(GetMjobLog): " + s1);

        long count = 0;
        myList = new ArrayList();

//        Mjob log = new Mjob();
//        ArrayList myList = log.getMyList();

        while (count < s1.length()) {


            if (s1.contains("begin")) {
//                //作业开始行
                String[] begigData = analytical(s1);
                beginTime = begigData[0];//开始时间
                beginJobId = begigData[1];
            } else if (s1.contains("rootid")) {


//                //作业结束行
                String[] endData = analytical(s1);
//                if (!beginJobId.equals(endData[1])) {
//                    continue;
//                }
                Mjob log = new Mjob();
//                curMjobLog.beginTime = beginTime;//1
                log.setBeginTime(beginTime);

                log.setEndTime(endData[0]);//2
                for (int i = 1; i < endData.length; i++) {
                    String[] temp = endData[i].split("=");
                    if (temp[0].equals("id")) {
                        log.setMjob(Integer.parseInt(temp[1]));//3
                    } else if (temp[0].equals("rootid")) {
                        log.setRootid(Integer.parseInt(temp[1]));//4
                    } else if (temp[0].equals("destrootid")) {
                        log.setDestrootid(Integer.parseInt(temp[1]));//5
                    } else if (temp[0].equals("size")) {
                        log.setSize(Long.decode(temp[1]));//6
                    } else if (temp[0].equals("transfersize")) {
                        log.setTransfersize(Long.decode(temp[1]));//7
                    } else if (temp[0].equals("runtime")) {
                        log.setRuntime(tools(temp[1]));//8
//                            log.runtime = Integer.parseInt(temp[1]);
                        }
                }
                myList.add(log);

//                curMjobLog.setMyList(curMjobLog);
            }
            count += s1.length();
        }

//        for (int i = 0; i < myList.size(); i++) {
//            Mjob log_Data = (Mjob) myList.get(i);
//            System.out.println("beginTime=" + log_Data.getBeginTime() + "\n");
//            System.out.println("endTime=" + log_Data.getEndTime() + "\n");
//            System.out.println("jobId=" + log_Data.getMjob() + "\n");
//            System.out.println("rootid=" + log_Data.getRootid() + "\n");
//            System.out.println("destrootid=" + log_Data.getDestrootid() + "\n");
//            System.out.println("size=" + Long.toString(log_Data.getSize()) + "\n");
//            System.out.println("transfersize=" + Long.toString(log_Data.getTransfersize()) + "\n");
//            System.out.println("runtime=" + log_Data.getRuntime() + "\n");
//            System.out.println("----------");
//        }

        for (int i = 0; i < myList.size(); i++) {
            Mjob dataObj = (Mjob) myList.get(i);
            Object[] one = new Object[8];
            one[0] = dataObj.getBeginTime();
            one[1] = dataObj.getEndTime();
            one[2] = dataObj.getMjob();
            one[3] = dataObj.getRootid();
            one[4] = dataObj.getDestrootid();
            one[5] = dataObj.getSize();
            one[6] = dataObj.getTransfersize();
            one[7] = dataObj.getRuntime();
            model.addRow(one);
        }

    }

    public void setTableModel(DefaultTableModel _model) {
        model = _model;
    }
    Runnable insertModel = new Runnable() {

        public void run() {
            if (model == null) {
                return;
            }


        }
    };

    private static String[] analytical(String inStr) {
        String[] result = inStr.split(" ");
        String temp = result[0];
        int index = temp.lastIndexOf("]");
        temp = temp.substring(1, index);
        result[0] = temp;

        return result;
    }

    static String tools(String input) {
        int input1 = Integer.parseInt(input);
        //小时
        int hh = input1 / 3600;
        //分钟
        int mm = (input1 - hh * 3600) / 60;
        //秒数
        int ss = (input1 - hh * 3600 - mm * 60);
        String time = hh + "h" + mm + "min" + ss + "s";
        return time;
    }

    public boolean updateMjobLog(int begin, int num) {
        try {
            curMjobLog = null;
            count = 0;
            setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_MJOB));
//                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_MJOB) +
            // begin + " " + num);

            SanBootView.log.info(getClass().getName(), " get mjob log cmd: " + getCmdLine());
            run();

        } catch (Exception ex) {
            setExceptionErrMsg(ex);
            setExceptionRetCode(
                    ex);
        }

        SanBootView.log.info(getClass().getName(), " get mjob log cmd retcode: " + getRetCode());
        boolean isOk = (getRetCode() == AbstractNetworkRunning.OK);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " get mjob log cmd errmsg: " + getErrMsg());
        }

        return isOk;
    }

    public int getCount() {
        return count;
    }

    public ArrayList getMyList() {
        return myList;
    }
}
