package guisanboot.cmdp.service;

import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;

public class ModVolInfo extends NetworkRunning{
    private String lineBuf;

    /** Creates a new instance of GetInitProgress */
    public ModVolInfo( String cmd ) {
        super( cmd );
    }

    public void parser( String line ){
        lineBuf = line.trim();
    }

    public String getVolInfo(){
        return lineBuf;
    }

    public boolean execCMDPcmd( String cmd_desc,String cmd ){
        this.setCmdLine( cmd );
        SanBootView.log.info( getClass().getName(),cmd_desc + cmd );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        return this.isEqZero();
    }
}
