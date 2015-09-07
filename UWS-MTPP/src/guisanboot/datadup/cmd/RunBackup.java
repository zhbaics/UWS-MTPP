package guisanboot.datadup.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import java.net.*;
import java.io.*;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */

public class RunBackup extends NetworkRunning{
    public void parser(String line){};

    public RunBackup( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
  
    public boolean isOk(){
        return getRetCode() == AbstractNetworkRunning.OK;
    }
}
