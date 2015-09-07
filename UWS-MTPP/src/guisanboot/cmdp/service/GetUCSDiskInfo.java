/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.cmdp.service;

import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class GetUCSDiskInfo extends NetworkRunning {

    public static final String LATEST = "latestpoolid";
    public static final String OLDEST = "oldestpoolid";
    public static final String LOG = "logpoolid";
    public static final String NUM = "lognum";
    public static final String MAX_SIZE = "logmaxsize";
    public static final String MAX_TIME = "logmaxtime";
    
    private int latestpoolid ;
    private int oldestpoolid ;
    private int logpoolid ;
    private int lognum;
    private int logmaxtime;
    private int logmaxsize;
    
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        String tmp1[];
        SanBootView.log.debug(getClass().getName(),"========> "+ s1 );
        if( s1.startsWith(this.LATEST) ){
            String tmp[] = s1.split(" ");
            for( int i = 0 ; i < tmp.length ; i++) {
                if( tmp[i].startsWith(this.LATEST) ){
                    this.latestpoolid = Integer.parseInt(tmp[i].split("=")[1]);
                } else if( tmp[i].startsWith(this.OLDEST) ){
                    this.oldestpoolid = Integer.parseInt(tmp[i].split("=")[1]);
                } else if( tmp[i].startsWith(this.LOG) ){
                    this.logpoolid = Integer.parseInt(tmp[i].split("=")[1]);
                } else if( tmp[i].startsWith(this.NUM) ){
                    this.lognum = Integer.parseInt(tmp[i].split("=")[1]);
                } else if( tmp[i].startsWith(this.MAX_SIZE) ){
                    this.logmaxsize = Integer.parseInt(tmp[i].split("=")[1]);
                } else if( tmp[i].startsWith(this.MAX_TIME) ){
                    this.logmaxtime = Integer.parseInt(tmp[i].split("=")[1]);
                }
            }
        }
        
    }
    
    public GetUCSDiskInfo( String cmd ,Socket _socket) throws IOException{
        super(cmd , _socket);
    }
    
    public GetUCSDiskInfo( String cmd ){
        super(cmd);
    }

    public int getLatestpoolid() {
        return latestpoolid;
    }

    public int getLogmaxsize() {
        return logmaxsize;
    }

    public int getLogmaxtime() {
        return logmaxtime;
    }

    public int getLognum() {
        return lognum;
    }

    public int getLogpoolid() {
        return logpoolid;
    }

    public int getOldestpoolid() {
        return oldestpoolid;
    }

}
