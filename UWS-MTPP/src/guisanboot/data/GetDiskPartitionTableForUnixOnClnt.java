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

public class GetDiskPartitionTableForUnixOnClnt extends NetworkRunning {
    private String info = null;
    private StringBuffer strBuf = null;
    private boolean isFirst = true;
    
    public void parser(String line)  {
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
    
    public GetDiskPartitionTableForUnixOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetDiskPartitionTableForUnixOnClnt( String cmd ){
        super( cmd );
    }
    
    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), " get old(new) disk partition table for unix cmd: " + this.getCmdLine() );         
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
SanBootView.log.info( getClass().getName(), " get old(new) disk partition table for unix retcode: " + this.getRetCode() );         
        
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get old(new) disk partition table for unix errmsg: " + this.getErrMsg() );                     
        }
        return isOk;
    }
    
/*    
[sda]  TotalSize(B)=400088457216
 # devpath     b         size            mp             partType    FsType         Label   [flag]
/dev/sda1      y         386644356K      none           82          unknown        none     boot
/dev/sda2      n         3984120K        none           82          unknown        none     hp-service
[sdb]  TotalSize(B)=18361030656
 /dev/sdb1     y         15470563K       none           81          unknown        none     none
 /dev/sdb2     n         987997K         none           84          unknown        none     none
[PV]
  /dev/sdb3
  /dev/sdb7
  /dev/sdb8
  /dev/sdb9
  /dev/sdb2
  /dev/sdb5
  /dev/sda3
  /dev/sda7
  /dev/sda8
  /dev/sda9
  /dev/sda2
  /dev/sda5
  /dev/sda6
  /dev/sdb6
[VG]
 VolGroup00   /dev/sda2
 VolGroup00   /dev/sda5
 VolGroup00yyg   /dev/sdb2
 VolGroup00yyg   /dev/sdb5
 VolGroup02   /dev/sda3
 VolGroup02   /dev/sda7
 VolGroup02   /dev/sda8
 VolGroup02   /dev/sda9
 VolGroup02yyg   /dev/sdb3
 VolGroup02yyg   /dev/sdb7
 VolGroup02yyg   /dev/sdb8
 VolGroup02yyg   /dev/sdb9
[LV]
/dev/VolGroup00/LV00       500K   unknown  none
/dev/VolGroup00/LV01       500.00K   unknown  none
/dev/VolGroup00yyg/LV01       500   unknown  none
/dev/VolGroup02/LV00       1210   unknown  none
/dev/VolGroup02yyg/LV00       1180   ext2  /mnt
 */   
    
    public String getContents(){
        return strBuf.toString();
    }
    
    public ArrayList<SystemPartitionForUnix> getIAHidenPartition(){
        SystemPartitionForUnix part;
        String[] line;
        String str;

        ArrayList<SystemPartitionForUnix> ret = new ArrayList<SystemPartitionForUnix>();

        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            str = list[i].trim();

            if( str.equals("") || str.startsWith("#") )continue;
SanBootView.log.debug(getClass().getName(),"################ >" + str );

            if( str.startsWith("[") ){
            }else if( str.startsWith("/") ){
                line = Pattern.compile("\\s+").split( str, -1 ); // "/dev/sda1 " line
                try{
                    if( line.length >=7 ){
                        part = new SystemPartitionForUnix();
                        part.dev_path = line[0].trim();
                        part.isBootable = line[1].trim().toUpperCase().equals("Y");
                        //long len = Long.parseLong( line[2].trim() );
                        //part.size = len +"";
                        part.size = line[2].trim();
                        part.mp = line[3].trim();
                        part.partType = line[4].trim();
                        part.fsType = line[5].trim();
                        part.label = line[6].trim();
                        if( line[7]!=null && !line[7].equals("") ){
                            part.flag = line[7].trim();
                        }
                        if( part.flag!=null && !part.flag.equals("") && !part.flag.equals("none") ){
                            ret.add( part );
                        }
                    }
                }catch(Exception ex){
                }
            }
        }

        return ret;
    }

    // 只要disk的信息，其他的pv,lv和vg的信息全部不要
    public ArrayList getAllDiskPartInfo(){
        DiskForUnix disk = null;
        SystemPartitionForUnix part;
        String[] line,line1;
        String str;
        
        ArrayList<DiskForUnix> ret = new ArrayList<DiskForUnix>();
        
        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            str = list[i].trim();
            
            if( str.equals("") || str.startsWith("#") )continue;
SanBootView.log.debug(getClass().getName(),"################ >" + str );     
            
            if( str.startsWith("[") ){
                if( str.startsWith("[PV]") || str.startsWith("[VG]") || str.startsWith("[LV]") ) continue;
                
                line = Pattern.compile("\\s+").split( str,-1 ); // "[sda] TotalSize(B)=400088457216" line
                try{
                    String tmp = line[0].trim();
                    int indx1 = tmp.indexOf("[");
                    int indx2 = tmp.indexOf("]");
                    String diskDevName = tmp.substring( indx1+1, indx2 );
                    line1 = Pattern.compile("=").split( line[1],-1);
                    long size = Long.parseLong( line1[1].trim() );
                    
                    disk = new DiskForUnix( diskDevName, size );
                    ret.add( disk );
                
                }catch(Exception ex){
                    disk = null;
                }
            }else if( str.startsWith("/") ){
                line = Pattern.compile("\\s+").split( str, -1 ); // "/dev/sda1 " line 
                try{
                    if( line.length >=7 ){
                        part = new SystemPartitionForUnix();
                        if( disk != null ){
                            disk.addPartiton( part ); 
                        }
                        part.dev_path = line[0].trim();
                        part.isBootable = line[1].trim().toUpperCase().equals("Y");
                        
                        //long len = Long.parseLong( line[2].trim() );
                        //part.size = len +"";
                        part.size = line[2].trim();
                        
                        part.mp = line[3].trim();
                        part.partType = line[4].trim();
                        part.fsType = line[5].trim();
                        part.label = line[6].trim();

                        if( line[7]!=null && !line[7].equals("") ){
                            part.flag = line[7].trim();
                        }
                    }
                }catch(Exception ex){
                }
            }
        }
        
        return ret;
    }

    public Vector getAllDiskPartInfo2(){
        DiskForUnix disk = null;
        SystemPartitionForUnix part;
        String[] line,line1;
        String str;

        Vector<DiskForUnix> ret = new Vector<DiskForUnix>();

        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            str = list[i].trim();

            if( str.equals("") || str.startsWith("#") )continue;
SanBootView.log.debug(getClass().getName(),"################ >" + str );

            if( str.startsWith("[") ){
                if( str.startsWith("[PV]") || str.startsWith("[VG]") || str.startsWith("[LV]") ) continue;

                line = Pattern.compile("\\s+").split( str,-1 ); // "[sda] TotalSize(B)=400088457216" line
                try{
                    String tmp = line[0].trim();
                    int indx1 = tmp.indexOf("[");
                    int indx2 = tmp.indexOf("]");
                    String diskDevName = tmp.substring( indx1+1, indx2 );
                    line1 = Pattern.compile("=").split( line[1],-1);
                    long size = Long.parseLong( line1[1].trim() );

                    disk = new DiskForUnix( diskDevName, size );
                    ret.add( disk );

                }catch(Exception ex){
                    disk = null;
                }
            }else if( str.startsWith("/") ){
                line = Pattern.compile("\\s+").split( str, -1 ); // "/dev/sda1 " line
                try{
                    if( line.length >=7 ){
                        part = new SystemPartitionForUnix();
                        if( disk != null ){
                            disk.addPartiton( part );
                        }
                        part.dev_path = line[0].trim();
                        part.isBootable = line[1].trim().toUpperCase().equals("Y");

                        //long len = Long.parseLong( line[2].trim() );
                        //part.size = len +"";
                        part.size = line[2].trim();

                        part.mp = line[3].trim();
                        part.partType = line[4].trim();
                        part.fsType = line[5].trim();
                        part.label = line[6].trim();

                        if( line[7]!=null && !line[7].equals("") ){
                            part.flag = line[7].trim();
                        }
                    }
                }catch(Exception ex){
                }
            }
        }

        return ret;
    }

}
