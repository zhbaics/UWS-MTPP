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


public class GetLVMInfoFromPXELinux extends NetworkRunning {
    private StringBuffer buf;
    private boolean isFirst = true;
    private ArrayList<LVInfo> infoList;
    private ArrayList<VGItem> vgList;
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;        
SanBootView.log.debug(getClass().getName(),"========> "+ line );
        if( isFirst ){
            buf.append( line );
            isFirst = false;
        }else{
            buf.append("\n"+line);
        }
    }
    
    public GetLVMInfoFromPXELinux( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetLVMInfoFromPXELinux( String cmd ){
        super( cmd );
    }

    public boolean realDo(){        
SanBootView.log.info( getClass().getName(), " get lvm info from pxelinux cmd: "+ getCmdLine() );         
        try{
            buf = new StringBuffer();
            isFirst = true;
            infoList = new ArrayList<LVInfo>();
            vgList = new ArrayList<VGItem>();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get lvm info from pxelinux retcode: "+ getRetCode() );    
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get lvm info from pxelinux errmsg: "+ getErrMsg() );              
        }
        return isOk;
    }
    
    public ArrayList parse(){
        String contents = buf.toString();
        String[] lines = Pattern.compile("\n").split( contents,-1 );
        int i=0;
        while( i<lines.length ){
            if( lines[i].equals("") ) continue;
            if( isKey( lines[i] ) && isLV_MP_FSTYPE( lines[i] ) ){
                i = parseLV_MP_FSTYPE( i,lines );
            }else if( isKey( lines[i] ) && isVG( lines[i] ) ){
                i = parseVG( i,lines );
            }else{
                i++;
            }
        }
        
        // set lvm type
        int size = infoList.size();
        for( i=0; i<size; i++ ){
            LVInfo info = infoList.get(i);
            String lvmType = getLVMType( info.vg );
            if( lvmType == null ) {
                return null;
            }
            info.lvmType = lvmType;
        }
        
        return infoList;
    }
    
    private String getLVMType( String vgName ){
System.out.println("find lvmtype for vg: " + vgName );         
        int size = vgList.size();
        for( int i=0; i<size; i++ ){
            VGItem vg = vgList.get(i);
System.out.println(" selected vg: " + vg.name );              
            if( vg.name.equals( vgName ) ){
                return vg.lvmtype;
            }
        }
SanBootView.log.error( getClass().getName(), "Can't find lvmtype for vg: " + vgName ); 
        return null;
    }
    
    public String prtInfo(){
        StringBuffer aBuf = new StringBuffer();
        
        int size = infoList.size();
        for( int i=0; i<size; i++ ){
            LVInfo info = infoList.get(i);
            aBuf.append( info.toString() );
            aBuf.append("\n");
        }
        return aBuf.toString();
    }
    
    private boolean isKey( String line ){
        return line.charAt(0) == '[' && line.charAt( line.length() - 1 ) == ']';
    }
    
    private boolean isLV_MP_FSTYPE( String line ){
        return ( line.toUpperCase().indexOf("LV-MOUNTPOINT-FSTYPE") >= 0 );
    }
    
    private boolean isVG( String line ){
        return ( line.toUpperCase().indexOf("VG") >=0 );
    }
    
//[lv-mountpoint-fstype]
//vg_27_32782/vol101.boot /boot ext3
//vg_27_32783/vol101.swap swap    
    private int parseLV_MP_FSTYPE( int index,String[] lines ){
        String str,vg_lv;
        
        index++;
        while( index<lines.length ){
            str = lines[index];
            if( this.isKey( str) ) break;
            
            String[] tgts = Pattern.compile("\\s+").split( str, -1 );

            if( tgts.length > 0 ){
                try{
                    LVInfo info = new LVInfo();
                    if( tgts.length >=3 ){
                        vg_lv= tgts[0];
                        info.mp = tgts[1];
                        info.fstype = tgts[2];
                    }else if( tgts.length == 2 ){
                        vg_lv = tgts[0];
                        info.mp = tgts[1];
                        info.fstype = tgts[1];
                    }else{
                        vg_lv = tgts[0];
                    }

                    int indx = vg_lv.indexOf( "/" );
                    info.vg = vg_lv.substring( 0, indx );
                    info.lv = vg_lv.substring( indx+1 );
                    indx = info.vg.lastIndexOf("_");
                    info.tid = info.vg.substring( indx+1 );

                    infoList.add( info );
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            index++;
        }
        
        return index;
    }
    
/*
[vg]
name=vg_33_32818
pvs=32818
lvs=vol121.swap
lvmtype=NONE
*/   
    private int parseVG( int index,String[] lines ){
        String str;
        
        index++;
        while( index<lines.length ){
            str = lines[index];
            if( this.isKey( str) ) break;
            
            try{
                VGItem vg = new VGItem();
                
                int indx = str.indexOf("=");  // name
                vg.name = str.substring( indx+1 );
                index++;  // pvs
                index++;  // lvs
                index++;  // lvmtype
                str = lines[index];
                indx = str.indexOf("=");
                vg.lvmtype = str.substring( indx+1 );
                vgList.add( vg );
                index++;   //partition
                index++;   //vgpartition
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            index++;
        }
        
        return index;
    }
}

class VGItem {
    public String name;
    public String lvmtype;
}