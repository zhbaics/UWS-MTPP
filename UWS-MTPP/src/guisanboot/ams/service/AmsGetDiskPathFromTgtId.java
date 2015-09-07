package guisanboot.ams.service;

import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class AmsGetDiskPathFromTgtId extends NetworkRunning{
    private String diskpath;

    public String getDiskpath() {
        return diskpath;
    }

    public void setDiskpath(String diskpath) {
        this.diskpath = diskpath;
    }
    
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        this.setDiskpath(s1);
        
        SanBootView.log.info(this.getClass().getName(), "(getDiskpathFromtgtId)===> "+ line);
    }

    public boolean buildMirror(){
        SanBootView.log.info( getClass().getName(), " getDiskpathFromtgtId: "+ getCmdLine() );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        boolean isOk = this.isOKForExcuteThisCmd();
        
        // retcode在isOKForExcuteThisCmd()里面会重新赋值
        SanBootView.log.info( getClass().getName(), " getDiskpathFromtgtId cmd retcode: "+ getRetCode() );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " getDiskpathFromtgtId cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

}
