package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import guisanboot.res.ResourceCenter;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import guisanboot.ui.*;

public class GetDiskPartitionTableForWinOnClnt extends NetworkRunning {
    private String info = null;
    private StringBuffer strBuf = null;
    private boolean isFirst = true;

    public void parser( String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        
        info = line.trim();
        if( isFirst ){
            strBuf.append( info);
            isFirst = false;
        }else{
            strBuf.append( "\n" + info );
        }
    }

    public void parserForCMDP(String line)  {
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public GetDiskPartitionTableForWinOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetDiskPartitionTableForWinOnClnt( String cmd ){
        super( cmd,ResourceCenter.DEFAULT_CHARACTER_SET );
    }
    
    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), " get old(new) disk partition table for win cmd: " + this.getCmdLine() );         
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
SanBootView.log.info( getClass().getName(), " get old(new) disk partition table for win retcode: " + this.getRetCode() );        
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get old(new) disk partition table for win errmsg: " + this.getErrMsg() );                    
        }
        return isOk;
    }
    
/*    
diskno=0;disk_size=4293596160;PartitionCount=4;signature=3279668091
StartingOffset=32256;PartitionLength=4285338624;HiddenSectors=63;PartitionNumber=1;PartitionType=7;BootIndicator=1;RecognizedPartition=1;RewritePartition=0;letter=C;label=新加卷;filesystem=NTFS;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
diskno=1;disk_size=8587192320;PartitionCount=12;signature=1240543748
StartingOffset=32256;PartitionLength=3150249984;HiddenSectors=63;PartitionNumber=1;PartitionType=7;BootIndicator=0;RecognizedPartition=1;RewritePartition=0;letter=E;label=aaa;filesystem=NTFS;
StartingOffset=3150282240;PartitionLength=4194892800;HiddenSectors=6152895;PartitionNumber=0;PartitionType=15;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=7345175040;PartitionLength=526417920;HiddenSectors=14346045;PartitionNumber=2;PartitionType=7;BootIndicator=0;RecognizedPartition=1;RewritePartition=0;letter=H;label=新加卷;filesystem=NTFS;
StartingOffset=7871592960;PartitionLength=715599360;HiddenSectors=15374205;PartitionNumber=3;PartitionType=7;BootIndicator=0;RecognizedPartition=1;RewritePartition=0;letter=I;label=新加卷;filesystem=NTFS;
StartingOffset=3150314496;PartitionLength=3150249984;HiddenSectors=63;PartitionNumber=4;PartitionType=7;BootIndicator=0;RecognizedPartition=1;RewritePartition=0;letter=F;label=新加卷;filesystem=NTFS;
StartingOffset=6300564480;PartitionLength=1044610560;HiddenSectors=6152895;PartitionNumber=0;PartitionType=5;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=6300596736;PartitionLength=1044578304;HiddenSectors=63;PartitionNumber=5;PartitionType=6;BootIndicator=0;RecognizedPartition=1;RewritePartition=0;letter=G;label=新加卷;filesystem=FAT;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;PartitionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
 */   
    
    public String getContents(){
        return strBuf.toString();
    }
    
    public ArrayList getAllDiskPartInfo(){
        DiskForWin disk = null;
        SystemPartitionForWin part;
        String[] line,line1;
        
        ArrayList<DiskForWin> ret1 = new ArrayList<DiskForWin>();
        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            if( list[i].equals("") )continue;
SanBootView.log.debug(getClass().getName(),"=======>"+list[i] );              
            
            if( list[i].startsWith("diskno=") ){
                disk = new DiskForWin();
                ret1.add( disk );
                
                line = Pattern.compile(";").split( list[i],-1 ); // diskno line
                try{
                    line1 = Pattern.compile("=").split( line[0], -1 );
                    int diskno = Integer.parseInt( line1[1].trim() );
                    disk.setDiskno( diskno );
                    line1 = Pattern.compile("=").split( line[1],-1);
                    long size = Long.parseLong( line1[1].trim() );
                    disk.setDiskSize( size );
                }catch(Exception ex){
                }
                
            }else if( list[i].startsWith("StartingOffset") ){
                line = Pattern.compile(";").split( list[i], -1 ); // StartingOffset
                
                try{
                    if( line.length >=4 ){
                        
                        part = new SystemPartitionForWin();
                        line1 = Pattern.compile("=").split( line[3], -1 ); // partitioNumber
                        
                        int pnumber = Integer.parseInt( line1[1].trim() );
                        if( pnumber !=0 ){
                            disk.addPartiton( part );
                        }
                        
                        line1 = Pattern.compile("=").split( line[1],-1 ); // partitionlength
                        long len = Long.parseLong( line1[1].trim() );
                        part.size = len +"";
                        
                        if( line.length >=9 ){
                            line1 = Pattern.compile("=").split( line[8], -1 ); // letter
                            part.disklabel = line1[1].trim();
                        }
                        
                        if( line.length >=10 ){
                            line1 = Pattern.compile("=").split( line[9],-1 ); // label
                            part.label = line1[1].trim();
                        }
                        
                        if( line.length >=11 ){
                            line1 = Pattern.compile("=").split( line[10],-1 ); // fs type
                            part.fsType = line1[1].trim();
                        }
                    }
                }catch(Exception ex){
                }
            }
        }
        
        return ret1;
    }
}
