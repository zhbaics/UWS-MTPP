package guisanboot.ams.service;

import guisanboot.ams.entity.InitAmsProgressRecord;
import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

public class GetInitAmsProgress extends NetworkRunning{
    private String disk_letter="";
    private InitAmsProgressRecord curRec  = null;
    private StringBuffer lineBuf;

    /** Creates a new instance of GetInitProgress */
    public GetInitAmsProgress( String cmd ) {
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
            SanBootView.log.debug(this.getClass().getName(), "(GetInitProgress)===> "+ s1);

            if( s1.startsWith( InitAmsProgressRecord.INIT_STATE ) ){
                curRec.setState( value );
            }else if( s1.startsWith( InitAmsProgressRecord.INIT_TIME )){
                curRec.setElapsedTime( value );
            }else if( s1.startsWith( InitAmsProgressRecord.INIT_SPEED)){
                curRec.setSpeed( value );
            }else if(s1.startsWith( InitAmsProgressRecord.INIT_LAUGH )){
                curRec.setPercent( value );
            }
        }else{
            if( s1.startsWith( InitAmsProgressRecord.STAT )){
                curRec = new InitAmsProgressRecord();
                curRec.setDisk( this.disk_letter );
            }
        }

    }

    public boolean updateInitProgress( String clntIP,int port,String volName,String letter ){
        this.disk_letter = letter;

        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_IBOOTCMD )+ " " + clntIP + " " + port + " ams_stat.sh " + letter);

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

    public boolean updateInitProgressByMount( String clntIP,int port, String mount ){
        //this.disk_letter = letter;

        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_IBOOTCMD )+ " " + clntIP + " " + port + " ams_stat_query.sh -m " + mount);

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
    
    public InitAmsProgressRecord getInitRecord(){
        return this.curRec;
    }

    public boolean isNotConnectToHost(){
        return lineBuf.toString().contains("connect to server failed,");
    }
    
    public boolean isSync(){
        return this.curRec.getState().toUpperCase().equals( InitAmsProgressRecord.STATE_sync.toUpperCase() );
    }

    public boolean isASync(){
        return this.curRec.getState().toUpperCase().equals( InitAmsProgressRecord.STATE_async.toUpperCase() );
    }

    public boolean isFalse(){
        return this.curRec.getState().toUpperCase().equals( InitAmsProgressRecord.STATE_false.toUpperCase() );
    }

    public boolean isIniting(){
        return this.curRec.getState().toUpperCase().equals( InitAmsProgressRecord.STATE_init.toUpperCase() );
    }

    public boolean isAsync2Sync(){
        return this.curRec.getState().toUpperCase().equals( InitAmsProgressRecord.STATE_async2sync.toUpperCase() );
    }
    
    public boolean isUnknow(){
        return this.curRec.getState().toUpperCase().equals( InitAmsProgressRecord.STATE_unknown.toUpperCase() );
    }

    public boolean isTimeout(){
        return ( this.getRetCode() == ResourceCenter.ERRCODE_TIMEOUT );
    }
}
