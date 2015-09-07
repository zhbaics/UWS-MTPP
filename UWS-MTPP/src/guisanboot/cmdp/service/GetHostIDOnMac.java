/*
 * GetHostIDOnMac.java
 *
 * Created on 2010/7/30,�PM�1:55
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.service;

import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */

// 从*.conf中匹配指定的mac地址，然后将对应的conf文件的文件名中的host id得到
public class GetHostIDOnMac extends NetworkRunning {
    private ArrayList<Integer> id_list = new ArrayList<Integer>();

    /** Creates a new instance of GetHostIDOnMac */
    public GetHostIDOnMac( String cmd, Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetHostIDOnMac( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

        int index = s1.indexOf(":");
        if( index > 0 ){
            String tmp = s1.substring( 0,index );
            int index1 = tmp.lastIndexOf("/");
            String tmp1 = tmp.substring( index1+1 );
            int index2 = tmp1.indexOf(".");
            String tmp2 = tmp1.substring( 0,index2 );

            try{
                int id = Integer.parseInt( tmp2 );
                this.id_list.add( new Integer( id ) );
            }catch(Exception ex){}
        }
    }

    public boolean realDo( String mac ){
        ArrayList<String> list = DhcpClientInfo.getMacItem( mac );
        StringBuffer buf = new StringBuffer();
        if( list == null ){
            buf.append( mac );
        }else{
            int size = list.size();
            boolean isFirst = true;
            for( int i=0; i<size; i++ ){
                if( isFirst ){
                    buf.append( list.get(i) );
                    isFirst = false;
                }else{
                    buf.append("[:-]");
                    buf.append( list.get(i) );
                }
            }
        }

        this.setCmdLine( ResourceCenter.BIN_DIR +"/getHostIdFromConf " + ResourceCenter.CLT_IP_CONF + " " + buf.toString() );
SanBootView.log.info( getClass().getName(), " get hostID_On_MAC cmd: "+ getCmdLine() );
        try{
            id_list.clear();

            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get hostID_On_MAC retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get hostID_On_MAC errmsg: "+ getErrMsg() );
        }
        return isOk;
    }
    
    public ArrayList<Integer> getHostIDList(){
        return this.id_list;
    }
}
