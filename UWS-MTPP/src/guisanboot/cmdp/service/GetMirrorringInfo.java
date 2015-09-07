/*
 * GetMirrorringInfo.java
 *
 * Created on 2010/7/14,�PM�13:00
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.service;

import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class GetMirrorringInfo extends NetworkRunning {
    public final static int STATE_SYNC  = 2;
    public final static int STATE_ASYNC = 3;
    public final static int STATE_INIT  = 1;
    public final static int STATE_A2S   = 4;

    public final static String Info_sync = "sync";
    public final static String Info_desttarget = "desttarget";

    private String sync_state = "";
    private String dest_tid ="";

    /** Creates a new instance of GetMirrorringInfo */
    public GetMirrorringInfo( String cmd, Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetMirrorringInfo( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line )) return;

        if( this.isEqZero() ){
            String[] lines = Pattern.compile( "\\*" ).split( line ,-1 );
            // get info success: *sync=2 *chunksize=8388608 
            // *diskno=0,uuid=7f5339ba-86a8-46b8-b634-a666b257fd35, start=32256,length=8595385344; 
            // *worktype=0 
            // *desttarget=iqn.2005-05.cn.com.odysys.iscsi.uws9901:32787
            try{
                if( lines[1].startsWith( Info_sync ) ){
                    int indx = lines[1].trim().indexOf("=");
                    this.sync_state = lines[1].trim().substring( indx+1 );
                }

                if( lines[5].startsWith( Info_desttarget ) ){
                    int indx1 = lines[5].trim().indexOf(":");
                    this.dest_tid = lines[5].trim().substring( indx1+1 );
                }
            }catch(Exception ex){
                this.sync_state ="";
                this.dest_tid ="";
            }
        }else{
            this.errMsg += line +"\n";
        }
    }

    public boolean realDo( String ip,int port,String drive ){
        this.setCmdLine( ResourceCenter.getCmdpS2A_CmdPath_FOR_INFO_CMD( ip,port ) + " -srcdrive " + drive );
SanBootView.log.info( getClass().getName(), " get volume's mirror info cmd: "+ getCmdLine() );
        try{
            this.sync_state = "";
            this.dest_tid = "";

            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get volume's mirror info cmd retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get volume's mirror info cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public String getTid(){
        return this.dest_tid;
    }

    public boolean isNotConnectToHost(){
        return this.errMsg.contains("connect to server failed,");
    }

    public boolean isSync(){
        return this.sync_state.equals( STATE_SYNC+"" );
    }

    public boolean isASync(){
        return this.sync_state.equals( STATE_ASYNC+"");
    }

    public boolean isA2S(){
        return this.sync_state.equals( STATE_A2S+"" );
    }

    public boolean isIniting(){
        return this.sync_state.equals( STATE_INIT+"" );
    }

    public boolean isUnknown(){
        return this.sync_state.equals("");
    }
    
    public String getStateString(){
        if( this.isSync() ){
            return InitProgressRecord.STATE_sync;
        }else if( this.isASync() ){
            return InitProgressRecord.STATE_async;
        }else if( this.isIniting() ){
            return InitProgressRecord.STATE_init;
        }else if( this.isA2S() ){
            return InitProgressRecord.STATE_async2sync;
        }else if( this.isUnknown() ){
            return InitProgressRecord.STATE_unknown;
        }else if( this.isNotConnectToHost() ){
            return InitProgressRecord.STATE_notConnect;
        }else{
            return InitProgressRecord.STATE_unknown;
        }
    }
}
