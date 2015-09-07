/*
 * GetInitProgress.java
 *
 * Created on June 23, 2010, 09:58 AM
 */

package guisanboot.cmdp.service;

import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.data.BootHost;
import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.tool.Tool;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetInitProgress extends NetworkRunning{
    private String disk_letter="";
    private InitProgressRecord curRec  = null;
    private StringBuffer lineBuf;

    /** Creates a new instance of GetInitProgress */
    public GetInitProgress( String cmd ) {
        super( cmd );
    }

    /*
        count=1
        ---index 0----
        state=init
        usetime=0.9s
        speed=9.3MB/s
        copyed=8.0M
        ret=0
        leftime=0.0s
        percent=94.7%
    */
    public void parser( String line ){
        String s1 = line.trim();

        this.lineBuf.append( s1 );
        
        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
// System.out.println("@@@@@: "+value);
SanBootView.log.debug(this.getClass().getName(), "(GetInitProgress)===> "+ s1);

            if( s1.startsWith( InitProgressRecord.INIT_STATE ) ){
                curRec.setState( value );
            }else if( s1.startsWith( InitProgressRecord.INIT_usetime )){
                curRec.setElapsedTime( value );
            }else if( s1.startsWith( InitProgressRecord.INIT_speed)){
                curRec.setSpeed( value );
            }else if( s1.startsWith( InitProgressRecord.INIT_copyed )){
                curRec.setFinished( value );
            }else if(s1.startsWith( InitProgressRecord.INIT_leftime )){
                curRec.setRemainTime( value );
            }else if(s1.startsWith( InitProgressRecord.INIT_percent )){
                curRec.setPercent( value );
            }
        }else{
            if( s1.startsWith( BootHost.CMDPRECFLAG )){
                curRec = new InitProgressRecord();
                curRec.setDisk( this.disk_letter );
            }
        }
    }

    public boolean updateBuildProgress( String clntIP,int port,String uuid,String letter ){
        this.disk_letter = letter;

        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_QUERY_INIT_STAT1 ) +
            clntIP+","+port+","+uuid
        );
SanBootView.log.info( getClass().getName(), " get init-progress cmd: "+ getCmdLine() );
        curRec = null;
        this.lineBuf = new StringBuffer();

        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

SanBootView.log.info( getClass().getName(), " get init-progress retcode: "+ getRetCode() );
        boolean isOk = this.isOk();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get init-progress errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public boolean updateInitProgress( String clntIP,int port,String volName,String letter ){
        this.disk_letter = letter;

        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_QUERY_INIT_STAT ) +
            clntIP+","+port+","+volName
        );
SanBootView.log.info( getClass().getName(), " get init-progress cmd: "+ getCmdLine() );
        curRec = null;
        this.lineBuf = new StringBuffer();

        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

SanBootView.log.info( getClass().getName(), " get init-progress retcode: "+ getRetCode() );
        boolean isOk = this.isOk();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get init-progress errmsg: "+ getErrMsg() );
        }
        return isOk;
    }
    
    private boolean isOk(){
        return ( (this.curRec != null) && ( !this.isCommunicationError( this.retCode ) ) );
    }
    
    public InitProgressRecord getInitRecord(){
        return this.curRec;
    }

    public boolean isNotConnectToHost(){
        return lineBuf.toString().contains("connect to server failed,");
    }
    
    public boolean isSync(){
        return this.curRec.getState().toUpperCase().equals( InitProgressRecord.STATE_sync.toUpperCase() );
    }

    public boolean isASync(){
        return this.curRec.getState().toUpperCase().equals( InitProgressRecord.STATE_async.toUpperCase() );
    }

    public boolean isIniting(){
        return this.curRec.getState().toUpperCase().equals( InitProgressRecord.STATE_init.toUpperCase() );
    }

    public boolean isAsync2Sync(){
        return this.curRec.getState().toUpperCase().equals( InitProgressRecord.STATE_async2sync.toUpperCase() );
    }
    
    public boolean isUnknow(){
        return this.curRec.getState().toUpperCase().equals( InitProgressRecord.STATE_unknown.toUpperCase() );
    }

    public boolean isTimeout(){
        return ( this.getRetCode() == ResourceCenter.ERRCODE_TIMEOUT );
    }
}
