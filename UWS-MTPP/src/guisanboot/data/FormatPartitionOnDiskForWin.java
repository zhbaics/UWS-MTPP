package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import guisanboot.ui.*;

public class FormatPartitionOnDiskForWin extends NetworkRunning {
    private String info = null;
    private StringBuffer strBuf = null;
    private boolean isFirst = true;

    public void parser(String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName()," ========> "+ line ); 
        
        info = line.trim();
        if( isFirst ){
            strBuf.append( info);
            isFirst = false;
        }else{
            strBuf.append( "\n" + info );
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
    
    public FormatPartitionOnDiskForWin( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public FormatPartitionOnDiskForWin( String cmd ){
        super( cmd );
    }
    
    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), " format partition for win cmd: "+ this.getCmdLine() );        
        try{
            info = null;
            isFirst = true;
            strBuf = null;
            strBuf = new StringBuffer();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " format partition for win cmd retcode: "+ this.getRetCode() );            
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " format partition for win cmd errmsg: "+ this.getErrMsg() );                
        }
        return isOk;
    }
    
    public String getContents(){
        return strBuf.toString();
    }
    
      /*
     diskno=1
     PartitionNumber=1;StartingOffset=0;PartitionLength=4086;BootIndicator=1;OldLetter=C;Label=aaa;FileSystem=NTFS;VolName=\\?\Volume{67cd5ded-be96-11dc-b
716-000c29d3c475};MountLetter=Z:\;
     */
    public DiskForWin getMpList(){
        DiskForWin disk = null;
        SystemPartitionForWin part;
        String[] line,line1;
        
        ArrayList<DiskForWin> ret = new ArrayList<DiskForWin>();
        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            if( list[i].equals("") )continue;
SanBootView.log.info(getClass().getName()," =====> "+ list[i] );
            
            if( list[i].startsWith("diskno=") ){
                disk = new DiskForWin();
                ret.add( disk );
                
                line = Pattern.compile(";").split( list[i],-1 ); // diskno line
                try{
                    line1 = Pattern.compile("=").split( line[0], -1 );
                    int diskno = Integer.parseInt( line1[1].trim() );
                    disk.setDiskno( diskno );
                }catch(Exception ex){
SanBootView.log.error(getClass().getName(), "Bad [diskno] output: "+ list[i] );                     
                }
                
            }else if( list[i].startsWith("PartitionNumber") ){
                line = Pattern.compile(";").split( list[i], -1 ); // StartingOffset
                try{
                    if( line.length >=9 ){
                        part = new SystemPartitionForWin();
                        disk.addPartiton( part );
                        
                        line1 = Pattern.compile("=").split( line[4], -1 ); // OldLetter
                        part.part_oldLetter = line1[1].trim();
                        
                        line1 = Pattern.compile("=").split( line[5],-1 ); // Label
                        part.label = line1[1].trim();
                        
                        line1 = Pattern.compile("=").split( line[6], -1 ); // FileSystem
                        part.fsType = line1[1].trim();
                        
                        line1 = Pattern.compile("=").split( line[7],-1 ); // VolName
                        part.part_volname = line1[1].trim();
                        
                        line1 = Pattern.compile("=").split( line[8],-1 ); // MountLetter
                        part.part_mntLetter = line1[1].trim();
                        part.disklabel = part.part_mntLetter;
                    }else{
SanBootView.log.error(getClass().getName(), "Bad [PartitionNumber] output: "+ list[i] );                        
                    }
                }catch(Exception ex){
SanBootView.log.error(getClass().getName(), "Bad [PartitionNumber] output: "+ list[i] );                     
                }
            }
        }
    
        if( ret.size()>0 ){
            return (DiskForWin)ret.get(0);
        }else{
SanBootView.log.error(getClass().getName(), "Not found any [diskno] string." );             
            return null;
        }
    }
}
