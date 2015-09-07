/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorJob;
import guisanboot.data.SourceAgent;
import guisanboot.data.UnixFileObj;
import guisanboot.tool.tftp.TFtpConsole;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * BasicStartorStopMjThread.java
 *
 * Created on 2010-1-26, 15:07:49
 */
public class BasicStartorStopMjThread extends BasicGetSomethingThread{
    protected MirrorJob mj;
    protected int mj_type;
    protected int action;   // 0 : start 1: stop
    protected Object host;  // BootHost or SourceAgent
    protected int src_rootid;
    protected String vol_name;
    protected String vol_mp;
    protected String dest_uws_ip;
    protected int dest_uws_port;
    protected int dest_rootid;
    protected int dest_poolid;
    protected String dest_pool_passwd;
    protected String linuxOrgBootMac = "";
    protected boolean quickStart;
    protected int ptype;
    protected boolean isOkForSyncManageInfo=true;
    protected boolean isTimeOut;

    // 对于普通镜像任务来说，只有远程镜像，所以isLocalType缺省为false;
    protected boolean isLocalType = false;
    protected SourceAgent srcAgnt;

    protected boolean giveTipToUser = true;

    public BasicStartorStopMjThread(
        SanBootView view,
        MirrorJob mj,
        int action,
        Object host,
        int src_rootid,
        String vol_name,
        String vol_mp,
        String linuxOrgBootMac,
        boolean quickStart,
        boolean giveTipToUser,
        int ptype
    ){
        super( view );

        this.mj = mj;
        this.action = action;
        this.host = host;
        this.src_rootid = src_rootid;
        this.vol_name = vol_name;
        this.vol_mp = vol_mp;
        this.mj_type = mj.getMj_job_type();
        this.dest_uws_ip = mj.getMj_dest_ip();
        this.dest_uws_port = mj.getMj_dest_port();
        this.dest_rootid = mj.getMj_dest_root_id();
        this.dest_poolid = mj.getMj_dest_pool();
        this.dest_pool_passwd = mj.getMj_dest_pool_passwd();
        this.linuxOrgBootMac = linuxOrgBootMac;
        this.quickStart = quickStart;
        this.giveTipToUser = giveTipToUser;
        this.ptype = ptype;
    }
    
    public boolean realRun(){
        return true;
    }
    
    protected boolean crtSrcAgent( String parm1,String parm2,String parm3,String uuid,int src_uws_id,int src_agnt_port,int src_agnt_port1,int src_agnt_boot_mode,int src_agnt_protect_type  ){
        srcAgnt = new SourceAgent( -1, parm1, parm2, parm3, uuid, src_uws_id,src_agnt_port,src_agnt_port1,src_agnt_boot_mode,src_agnt_protect_type );
        boolean aIsOk = view.initor.mdb.addSrcAgnt( this.isLocalType?"":dest_uws_ip,dest_uws_port,dest_poolid,dest_pool_passwd, srcAgnt );
        if( !aIsOk ){
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }else{
            srcAgnt.setSrc_agnt_id( view.initor.mdb.getNewId() );
        }
        return aIsOk;
    }

    protected boolean crtMDI( int dest_root_id,int srcAgntID,int src_rootid,String vol_name,String vol_mp ){
        MirrorDiskInfo mdi = new MirrorDiskInfo( dest_root_id, srcAgntID, src_rootid, vol_name, vol_mp, 0,this.mj_type,ptype );
        boolean aIsOk = view.initor.mdb.addMDI( this.isLocalType?"":dest_uws_ip,dest_uws_port, dest_poolid,dest_pool_passwd,mdi );
        if( !aIsOk ){
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }
        return aIsOk;
    }

    protected boolean modMDI( int dest_rootid,int srcAgntID ){
        boolean aIsOk = view.initor.mdb.modMDI( this.isLocalType?"":dest_uws_ip,dest_uws_port,dest_poolid,dest_pool_passwd,dest_rootid, srcAgntID );
        if( !aIsOk ){
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }
        return aIsOk;
    }

    protected boolean queryMDI( int dest_root_id ){
        boolean aIsOk = view.initor.mdb.queryMdiOnDestUWSrv( this.isLocalType?"":dest_uws_ip,dest_uws_port, dest_root_id );
        if( !aIsOk ){
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }
        return aIsOk;
    }

    // get file which format is like "rd.<kernel_version>.<lvm type>.gz"
    protected ArrayList getRdxxxFileList( ArrayList fileList ){
        String name;
        String[] line;

        ArrayList ret = new ArrayList();

        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get(i);
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                line = Pattern.compile("\\.").split( name );
                if( line.length > 3 ){
                    String end = line[line.length-2];
System.out.println(" end: "+ end );
                    if( line[0].equals("rd") ){
                        if( end.equals("NONE") || end.equals("LVM1") || end.equals("LVM2") ){
                            ret.add( one );
                        }
                    }
                }
            }
        }

        return ret;
    }

    // get file which format is like "pxe.<kernel_version>.<lvm type>"
    public ArrayList getPXEFileList( ArrayList fileList ){
        String name;
        String[] line;

        ArrayList ret = new ArrayList();

        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get(i);
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                line = Pattern.compile("\\.").split( name );
                if( line.length > 3 ){
                    String end = line[line.length-1];
System.out.println(" end: "+ end );
                    if( line[0].equals("pxe") ){
                        if( end.equals("NONE") || end.equals("LVM1") || end.equals("LVM2") ){
                            ret.add( one );
                        }
                    }
                }
            }
        }

        return ret;
    }

    // get file which format is like "vmlinuz-<kernel_version>.<lvm type>"
    public ArrayList getVmlinuzFileList( ArrayList fileList ){
        String name;
        String[] line;

        ArrayList ret = new ArrayList();

        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get(i);
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                line = Pattern.compile("\\.").split( name );
                if( line.length > 3 ){
                    String end = line[line.length-1];
System.out.println(" end: "+ end );
                    if( line[0].startsWith("vmlinuz-") ){
                        if( end.equals("NONE") || end.equals("LVM1") || end.equals("LVM2") ){
                            ret.add( one );
                        }
                    }
                }
            }
        }

        return ret;
    }

    // get file which format is like "iBootInfo.<kernel_version>.<lvm type>"
    public ArrayList getiBootInfoFileList( ArrayList fileList ){
        String name;
        String[] line;

        ArrayList ret = new ArrayList();

        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get(i);
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                line = Pattern.compile("\\.").split( name );
                if( line.length > 3 ){
                    String end = line[line.length-1];
System.out.println(" end: "+ end );
                    if( line[0].equals("iBootInfo") ){
                        if( end.equals("NONE") || end.equals("LVM1") || end.equals("LVM2") ){
                            ret.add( one );
                        }
                    }
                }
            }
        }

        return ret;
    }

    // get file which format is like "bootlog"
    public ArrayList getBootLogFileList( ArrayList fileList ){
        String name;

        ArrayList ret = new ArrayList();

        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get(i);
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                if( one.getName().equals("bootlog") ){
                    ret.add( one );
                }
            }
        }
        return ret;
    }

    // get file which format is like "yyyxen"
    public ArrayList getXenFileList( ArrayList fileList ){
        String name;

        ArrayList ret = new ArrayList();

        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get(i);
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                if( one.getName().startsWith("xen.") ){
                    ret.add( one );
                }
            }
        }
        return ret;
    }
    
    // get file which format is like "elilo-xxx"
    public ArrayList getEliloFileList( ArrayList fileList ){
        String name;
        
        ArrayList ret = new ArrayList();
        
        int size = fileList.size();
        for( int i=0; i<size; i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get( i );
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                if( one.getName().startsWith("elilo-") ){
                    ret.add( one );
                }
            }
        }
        return ret;
    }

    private String getTransferFileName(){
        java.util.GregorianCalendar c = new java.util.GregorianCalendar();
        StringBuffer prefix = new StringBuffer();
        prefix.append("-");
        int year = c.get( Calendar.YEAR );
        prefix.append(""+year);
        int month = c.get( Calendar.MONTH )+1;
        if( month < 10 ){
            prefix.append("0"+month);
        }else{
            prefix.append(""+month);
        }
        int day_of_month = c.get( Calendar.DAY_OF_MONTH );
        if( day_of_month < 10 ){
            prefix.append("0"+day_of_month);
        }else{
            prefix.append(""+day_of_month);
        }
        int hour_of_day = c.get( Calendar.HOUR_OF_DAY );
        if( hour_of_day < 10 ){
            prefix.append("0"+hour_of_day);
        }else{
            prefix.append(""+hour_of_day);
        }
        int minute = c.get( Calendar.MINUTE );
        if( minute < 10 ){
            prefix.append("0"+minute);
        }else{
            prefix.append(""+minute);
        }
        int sec = c.get(Calendar.SECOND );
        if( sec < 10 ){
            prefix.append("0"+sec);
        }else{
            prefix.append(""+sec);
        }
        return prefix.toString();
    }
    
    public boolean transferNetBootfileForLinux(
        String whatFile,
        String src_uws_tftproot,
        String dest_uws_tftproot,
        String orgSimpleMac,
        int src_agnt_id
    ){
        boolean aIsOk = view.initor.mdb.listDir( src_uws_tftproot+"/"+orgSimpleMac+"/" );
        if( !aIsOk ){
            errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
SanBootView.log.error( getClass().getName(),"failed to list tftproot/<original_mac> on src_swu server.");
            return false;
        }else{
            ArrayList fList = view.initor.mdb.getFileList();
            if( fList.size() == 0 ){
                errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
SanBootView.log.error( getClass().getName(),"there is no files in tftproot/<original_mac> on src_swu server.");
                return false;
            }else{
                ArrayList fList2;
                if( whatFile.equals("Elilo") ){
                    fList2 = getEliloFileList( fList );
                }else if( whatFile.equals("Xen") ){
                    fList2 = this.getXenFileList( fList );
                }else if( whatFile.equals("BootLog") ){
                    fList2 = this.getBootLogFileList( fList );
                }else if( whatFile.equals("IBootInfo") ){
                    fList2 = this.getiBootInfoFileList( fList );
                }else if( whatFile.equals("PXEXXX")){
                    fList2 = this.getPXEFileList( fList );
                }else if( whatFile.equals("VmlinuzXXX") ){
                    fList2 = this.getVmlinuzFileList( fList );
                }else{ // whatFile.equals("RdXXX")
                    fList2 = this.getRdxxxFileList( fList );
                }

                if( fList2.size() == 0  ){
                    if( whatFile.equals("Elilo") || whatFile.equals("Xen") ){
                        // 这个机器的平台不是IA的，所以不需要传输elilo file and xen file
SanBootView.log.warning( getClass().getName(),"there is not a " + whatFile + " file in tftproot/<original_mac> dir on src_swu server. Maybe it's non-IA kernel.");
                        return true;
                    }else{
SanBootView.log.error( getClass().getName(),"there is not a " + whatFile + " file in tftproot/<original_mac> dir.");
                        return false;
                    }
                }else{
                    // 将prefix加到每个传输的文件名后面，使得每次传输的文件名得以区分
                    String prefix = this.getTransferFileName();
                    String realFileName = ((UnixFileObj)fList2.get(0)).getName();
                    String rdpath = orgSimpleMac + "/" + realFileName;
                    TFtpConsole tftp = new TFtpConsole( view.initor.serverIp,realFileName+prefix,rdpath );
SanBootView.log.info(getClass().getName()," tftp get(from src swu server to gui) ----> local file: "+ tftp.getLocalFileName() + " remote file: "+ tftp.getRemoteFileName() );
                    // get xxxx from src_uws tftp root to gui
                    if( tftp.open() && tftp.recive() ){
SanBootView.log.info( getClass().getName(),"rec bootlog from src-end tftp is ok.");
                        // put xxxx to dest_uws tftp root from gui
                        tftp = new TFtpConsole( dest_uws_ip,realFileName+prefix,"remir/"+src_agnt_id+"/"+ realFileName );
SanBootView.log.info(getClass().getName()," tftp put(from gui to dest swu server) ----> local file: "+ tftp.getLocalFileName() + " remote file: "+ tftp.getRemoteFileName() );
                        String new_conf_path = dest_uws_tftproot+"/remir/";
                        view.initor.mdb.listDirOnRemoteHost( dest_poolid, dest_pool_passwd, dest_uws_ip, dest_uws_port,new_conf_path );
                        if( view.initor.mdb.isExistFile( ""+src_agnt_id ) ){
                            if( !view.initor.mdb.touchAFile( dest_poolid, dest_pool_passwd, dest_uws_ip, dest_uws_port, new_conf_path+src_agnt_id+"/"+ realFileName ) ){
                                errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                return false;
                            }
                        }else{
                            if(  view.initor.mdb.mkdirOnRemoteHost( dest_poolid, dest_pool_passwd, dest_uws_ip, dest_uws_port,new_conf_path+src_agnt_id ) ){
                                if( !view.initor.mdb.touchAFile( dest_poolid, dest_pool_passwd, dest_uws_ip, dest_uws_port, new_conf_path+src_agnt_id+"/"+realFileName ) ){
                                    errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                    return false;
                                }
                            }else{
                                errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                return false;
                            }
                        }

                        if( tftp.open() && tftp.put() ){
                            // delete file on gui-localhost
                            tftp.deleteLocalFile();
                            return true;
                        }else{
                            // delete file on gui-localhost
                            tftp.deleteLocalFile();
                            errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                            return false;
                        }
                    }else{
                        errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                        return false;
                    }
                }
            }
        }
    }

    protected boolean transferManageInfo( String src_uws_tftproot,String dest_uws_tftproot, String orgSimpleMac, int src_agnt_id,boolean excluded ){
        boolean isOkForRdxxxfile = this.transferNetBootfileForLinux( "RdXXX",src_uws_tftproot, dest_uws_tftproot, orgSimpleMac, src_agnt_id );
        if( !isOkForRdxxxfile ) return false;

        boolean isOkForVmlinuxxxxFile = this.transferNetBootfileForLinux( "VmlinuzXXX",src_uws_tftproot, dest_uws_tftproot, orgSimpleMac, src_agnt_id );
        if( !isOkForVmlinuxxxxFile ) return false;

        boolean isOkForPXE = this.transferNetBootfileForLinux( "PXEXXX",src_uws_tftproot, dest_uws_tftproot, orgSimpleMac, src_agnt_id );
        if( !isOkForPXE )return false;

        boolean isOkForIBootInfo = this.transferNetBootfileForLinux( "IBootInfo",src_uws_tftproot, dest_uws_tftproot, orgSimpleMac, src_agnt_id );
        if( !isOkForIBootInfo ) return false;

        boolean isOkForBootLog = this.transferNetBootfileForLinux( "BootLog",src_uws_tftproot, dest_uws_tftproot, orgSimpleMac, src_agnt_id );
        if( !isOkForBootLog ) return false;

        boolean isOkForXen = this.transferNetBootfileForLinux( "Xen",src_uws_tftproot, dest_uws_tftproot, orgSimpleMac, src_agnt_id );
        if( !isOkForXen ) return false;

        if( !excluded ){
            boolean isOkForElilo = this.transferNetBootfileForLinux( "Elilo",src_uws_tftproot, dest_uws_tftproot, orgSimpleMac, src_agnt_id );
            if( !isOkForElilo ) return false;
        }
        return true;
    }
    
    public boolean isTransferManagedInfoOk(){
        return this.isOkForSyncManageInfo;
    }
}
