package guisanboot.ams.service;

import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;

public class AmsBuildMirrorOnClnt extends NetworkRunning{
    
    @Override
    public void parser(String line) {
        SanBootView.log.info(this.getClass().getName(), "(AmsBuildMirrorOnClnt)===> "+ line);
    }

    public boolean buildMirror(){
        SanBootView.log.info( getClass().getName(), " build ams mirroring cmd: "+ getCmdLine() );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        boolean isOk = this.isOKForExcuteThisCmd();
        
        // retcode在isOKForExcuteThisCmd()里面会重新赋值
        SanBootView.log.info( getClass().getName(), " build ams mirroring cmd retcode: "+ getRetCode() );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " build ams mirroring cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

}
