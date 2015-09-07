package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;

public class ListPartitionForWinOnDisk extends NetworkRunning {
    private String partInfo = null;
    private StringBuffer strBuf = null;
    private boolean isFirst = true;

    public void parser( String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    /*
    diskno=1;disk_size=4094 MegaBytes
    PartitionNumber=1;StartingOffset=0;PartitionLength=4086;BootIndicator=1;OldLetter=C;Label=新加卷;FileSystem=NTFS;
*/   
    public void parserForMTPP(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );        
        
        partInfo = line.trim();
        if( isFirst ){
            strBuf.append( partInfo );
            isFirst = false;
        }else{
            strBuf.append( "\n"+partInfo );
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public ListPartitionForWinOnDisk( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public ListPartitionForWinOnDisk( String cmd ){
        super( cmd );
    }

    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), " list disk's partition for win  cmd: "+ getCmdLine() );         
        try{
            partInfo = null;
            isFirst = true;
            strBuf = null;
            strBuf = new StringBuffer();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " list disk's partition for win  retcode: "+ getRetCode() );  
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " list disk's partition for win  errmsg: "+ getErrMsg() );              
        }
        return isOk;
    }
    
    public String getListContents(){
        return strBuf.toString();
    }
}
