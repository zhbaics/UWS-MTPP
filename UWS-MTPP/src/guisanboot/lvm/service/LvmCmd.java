package guisanboot.lvm.service;
import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;

public class LvmCmd extends NetworkRunning{
    
    @Override
    public void parser(String line) {
        SanBootView.log.info(this.getClass().getName(), "(lvm)===> "+ line);
    }

    public boolean cmd(){
        SanBootView.log.info( getClass().getName(), "lvm cmd: "+ getCmdLine() );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        boolean isOk = this.isOKForExcuteThisCmd();
        
        // retcode在isOKForExcuteThisCmd()里面会重新赋值
        SanBootView.log.info( getClass().getName(), " lvm cmd retcode: "+ getRetCode() );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " lvm cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

}
