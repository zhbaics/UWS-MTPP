package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import guisanboot.tool.Tool;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import guisanboot.ui.*;


public class FormatPartitionOnDiskForUnix extends NetworkRunning {
    // 该返回值是linux源盘恢复涉及到的三个命令都可能返回的错误值，当出现
    // 这个错误值时要提示用户重启机器，然后重新做自动格式化
    public final static int Error_99 = 99;
    
    private String info = null;
    private StringBuffer strBuf = null;
    private boolean isFirst = true;
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return; 
SanBootView.log.debug(getClass().getName()," =====>(parse cmd output) "+ line);         
        info = line.trim();
        if( isFirst ){
            strBuf.append( info);
            isFirst = false;
        }else{
            strBuf.append( "\n" + info );
        }
    }
    
    public FormatPartitionOnDiskForUnix( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public FormatPartitionOnDiskForUnix( String cmd ){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), "format new partiton(lv) for unix cmd: "+this.getCmdLine()  );         
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
SanBootView.log.info( getClass().getName(), "format new partition(lv) for unix retcode: "+this.getRetCode()  );         
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "format new partition(lv)  for unix errmsg: "+this.getErrMsg()  );               
        }
        return isOk;
    }
    
    public String getContents(){
        return strBuf.toString();
    }
    
   /*
[sda]
 # devpath     b         size       old-mp   new-mp         partType       FsType         Label
/dev/sda1      y         386644356K      /        /mnt/uws/aa    82          ext3           ody_/
/dev/sda2      n         3984120M        /boot    /mnt/uws/bb    82          ext3           ody_/boot
     */
    public DiskForUnix getMpList(){
        DiskForUnix disk = null;
        SystemPartitionForUnix part;
        String[] line,line1;
        
        ArrayList<DiskForUnix> ret = new ArrayList<DiskForUnix>();
        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            if( list[i].equals("") || list[i].startsWith("#") )continue;
SanBootView.log.info(getClass().getName()," =====>(parse partition mp) "+ list[i] ); 
            
            if( list[i].startsWith("[") ){
                line = Pattern.compile("\\s+").split( list[i],-1 ); // "[sda]" line
                try{
                    String tmp = line[0].trim();
                    int indx1 = tmp.indexOf("[");
                    int indx2 = tmp.indexOf("]");
                    String diskDevName = tmp.substring( indx1+1, indx2 );
                    disk = new DiskForUnix( diskDevName, 0L );
                    ret.add( disk );
                }catch(Exception ex){
                    disk = null;
                }
            }else if( list[i].startsWith("/") ){
                line = Pattern.compile("\\s+").split( list[i], -1 ); // "/dev/sda1 " line 
                try{
                    if( line.length >=8 ){
                        part = new SystemPartitionForUnix();
                        if( disk!=null ){
                            disk.addPartiton( part );
                        }
                        part.dev_path = line[0].trim();
                        part.isBootable = line[1].trim().toUpperCase().equals("Y");
                        
                        //long len = Long.parseLong( line[2].trim() );  //有可能带单位
                        //part.size = len +"";
                        part.size = line[2].trim();
                        
                        part.mp = line[3].trim();   // old mp
                        part.new_mp = line[4].trim(); // new mp
                        part.partType = line[5].trim();
                        part.fsType = line[6].trim();
                        part.label = line[7].trim();  // label
                    }
                }catch(Exception ex){
                }
            }
        }
        
        if( ret.size()>0 ){
            return (DiskForUnix)ret.get(0);
        }else{
            return null;
        }
    }
    
    /*
    # devpath                    capcity     fstype    old-mp       new-mp       label 
    /dev/VolGroup00/LogVol00     7340K        ext3        /          /mnt/root    ody_/
     */
    public DiskForUnix getLVMpList(){
        DiskForUnix disk = null;
        SystemPartitionForUnix part;
        String[] line;
        boolean aIsFirst = true;
        
        ArrayList<DiskForUnix> ret = new ArrayList<DiskForUnix>();
        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            if( list[i].equals("") || list[i].startsWith("#") )continue;
SanBootView.log.info(getClass().getName(),"====>(parse lv and it's mp) "+ list[i] ); 
            
            if( aIsFirst ){
                disk = new DiskForUnix( "***lvm-disk***", 0L );
                ret.add( disk ); 
                aIsFirst = false;
                
                line = Pattern.compile("\\s+").split( list[i], -1 ); // "/dev/VolGroup00/LogVol00 " line 
                try{
                    if( line.length >=6 ){
                        part = new SystemPartitionForUnix();
                        if( disk!=null ){
                            disk.addPartiton( part );
                        }
                        part.dev_path = line[0].trim();
                        //long len = Long.parseLong( line[1].trim() );
                        part.size = line[1].trim();
                        part.fsType = line[2].trim();
                        part.partType = part.fsType;
                        part.mp = line[3].trim();   // old mp
                        part.new_mp = line[4].trim(); // new mp
                        part.label = line[5].trim();  // label
                    }
                }catch(Exception ex){
                    Tool.prtExceptionMsg( ex );
                }
            }else{
                line = Pattern.compile("\\s+").split( list[i], -1 ); // "/dev/VolGroup00/LogVol00 " line 
                try{
                    if( line.length >=6 ){
                        part = new SystemPartitionForUnix();
                        if( disk!=null ){
                            disk.addPartiton( part );
                        }
                        part.dev_path = line[0].trim();
                        //long len = Long.parseLong( line[1].trim() );
                        part.size = line[1].trim();
                        part.fsType = line[2].trim();
                        part.partType = part.fsType;
                        part.mp = line[3].trim();   // old mp
                        part.new_mp = line[4].trim(); // new mp
                        part.label = line[5].trim(); // label
                    }
                }catch(Exception ex){
                    Tool.prtExceptionMsg( ex );
                }
            }
        }
        
        if( ret.size()>0 ){
            return (DiskForUnix)ret.get(0);
        }else{
            return null;
        }
    }
}
