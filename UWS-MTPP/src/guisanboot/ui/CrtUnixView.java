/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.entity.CloneDiskWrapper;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import mylib.tool.Util;

/**
 *
 * @author Administrator
 */
public class CrtUnixView extends Thread{
    UnixIbootable diag; // UnixIbootable is an interface.
    ProgressDialog pdiag;
    Object[] snaps;
    HashMap map;
    SanBootView view;
    String ip,bootMac,targetSrvName;
    BootHost host;
    boolean allFinish = true;
    Vector partList;
    String start_ip;
    boolean isNetBootOnLocalHost;
    SetDhcpPane setDhcpPane;
    String original_boot_MAC;
    String tftp_ip="";
    boolean isUsingSwap = true;

    String errMsg;
    StringBuffer logBuf = new StringBuffer();

    StringBuffer selVer_buf;  // save selected version info for sdhm
    
    public CrtUnixView( 
        UnixIbootable diag,
        ProgressDialog pdiag,
        Object[] snaps,
        HashMap map,
        SanBootView view,
        String bootMac,
        BootHost host,
        String targetSrvName,
        Vector partList,
        String start_ip,
        boolean isNetBootOnLocalHost,
        SetDhcpPane setDhcpPane,
        String original_boot_MAC,
        boolean isUsingSwap
    ){
        this.diag = diag;
        this.pdiag = pdiag;
        this.snaps = snaps;
        this.map = map;
        this.view = view;
        this.bootMac = bootMac;
        this.host = host;
        this.ip = host.getIP();
        this.targetSrvName = targetSrvName;
        this.partList = partList;
        this.start_ip = start_ip;
        this.isNetBootOnLocalHost = isNetBootOnLocalHost;
        this.setDhcpPane = setDhcpPane;
        this.original_boot_MAC = original_boot_MAC;
        if( setDhcpPane.isAutoSetup() ){
            tftp_ip = view.initor.getTxIP(host.getIP());
        }else{
            DhcpClientInfo dhcpInfo = setDhcpPane.getDhcpSetInfo();
            tftp_ip = dhcpInfo.nextServer_3rd;
        }
        
        // selected single netcard is same as original host's netcard to boot.
        String mac1 = DhcpClientInfo.getSimpleMac( this.bootMac ).toLowerCase();
        String mac2 = DhcpClientInfo.getSimpleMac( this.original_boot_MAC ).toLowerCase();
System.out.println("mac1: "+ mac1 +" mac2: "+mac2 );        
        if( mac1.equals( mac2 ) ){
            this.isNetBootOnLocalHost = true;
        }
        this.isUsingSwap = isUsingSwap;
    }

    Runnable close = new Runnable(){
        public void run(){
            pdiag.dispose();
        }
    };

    public String replaceLine( String msg ){
        boolean isFirst = true;
        String tmp;

        StringBuffer buf = new StringBuffer();

        String[] list = Pattern.compile("\n").split( msg );
        for( int i=0; i<list.length; i++ ){
            tmp = list[i].trim();
            if( !tmp.equals("") ){
                if( isFirst ){
                    buf.append( tmp );
                    isFirst = false;
                }else{
                    buf.append( " " + tmp );
                }
            }
        }

        return buf.toString();
    }

    boolean isWrFirst = true;
    public void writeLogBuf( String errMsg ){
        if( isWrFirst ){
            logBuf.append( replaceLine( errMsg ) );
            isWrFirst = false;
        }else{
            logBuf.append( ResourceCenter.NEWLINE_STRING + replaceLine( errMsg ) );
        }
    }

    @Override public void run(){
        int i,tid,size,snapid,viewid,disk_type=-1;
        boolean isOk,checkSnap;
        BindOfSnapAndView bindsv;
        BindofTgtAndSnap bindts;
        Snapshot selSnap,tmpsnap;
        View selView,newView;
        VolumeMap selVolMap;
        String key,viewName,crttime,tftpRootPath="";
        TargetWrapper selTgt;
        CloneDisk selCloneDisk;
        DestAgent newDa = null;
        
        String orgBootMacForUnix = DhcpClientInfo.getMacStr( this.original_boot_MAC );
        String orgSimpleMac = NetCard.getSimpleMac1( orgBootMacForUnix ).toLowerCase();
        String newBootMacForUnix = DhcpClientInfo.getMacStr( this.bootMac );
        String newSimpleMac = NetCard.getSimpleMac1( newBootMacForUnix ).toLowerCase();
        String org_pxe_mac = UnixNetCard.getPXEMac( orgBootMacForUnix ).toLowerCase();
        String new_pxe_mac = UnixNetCard.getPXEMac( newBootMacForUnix ).toLowerCase();
        String iscsiVar = ResourceCenter.ISCSI_PREFIX + targetSrvName;
        String mac = UnixNetCard.getSimpleMac( bootMac ).toLowerCase();
        
        size = snaps.length;
        for( i=0; i<size; i++ ){
            bindts = (BindofTgtAndSnap)snaps[i];
            key = bindts.tgt.toString();
System.out.println("CrtUnixView--> "+ key );
            
            viewName = ResourceCenter.NET_START_VIEW + key;
            bindsv = (BindOfSnapAndView)map.get( key );
            
            if( bindts.snap instanceof SnapWrapper ){
System.out.println("CrtUnixView-->: is a snapwrapper.");
                selSnap = ((SnapWrapper)bindts.snap).snap; 
                
                // 检查snap number是否合适
                checkSnap = diag.checkSnapNumber( bindts.tgt.toString(), selSnap.getSnap_root_id(),selSnap.getSnap_local_snapid() );
                if( !checkSnap ){
                    allFinish = false;
                    break;
                }
                
                if( bindts.isSel ){  // 该卷被选择了  
                    if( bindsv != null ){ // hashmap中已经有了
                        if( bindsv.snap instanceof Snapshot ){
                            tmpsnap = (Snapshot)bindsv.snap;
                            if( tmpsnap.getSnap_root_id() == selSnap.getSnap_root_id() &&
                                tmpsnap.getSnap_local_snapid() == selSnap.getSnap_local_snapid() ){
                                // 之前已经为该快照生成view了
                            }else{
                                 // 之前为该盘生成的view不对应现在所选的快照，删除它，然后再创建一个
                                //tid = bindsv.view.getTargetID();
                                //delLunMap( tid );
                                view.initor.mdb.delView( bindsv.view ); // 不管结果
                                map.remove( key );
                                
                                isOk = view.initor.mdb.addView( viewName, selSnap.getSnap_root_id(), selSnap.getSnap_local_snapid() );
                                if( !isOk ){
                                    errMsg = SanBootView.res.getString("FailoverWizardDialog.error.crtView")+" : [ SnapID: "+selSnap.getCreateTimeStr()+" ]";
                                    JOptionPane.showMessageDialog(pdiag,
                                        errMsg
                                    );
                                    this.writeLogBuf( errMsg );
                                    allFinish = false;
                                }else{
                                    newView = view.initor.mdb.getCrtView();
                                    
                                    //isOk = addLunMap( newView.getTargetID() );
                                    isOk = true;
                                    if( !isOk ){
                                        JOptionPane.showMessageDialog(pdiag,
                                            SanBootView.res.getString("FailoverWizardDialog.error.addLunmap")
                                        );
                                        allFinish = false;
                                        view.initor.mdb.delView( newView );
                                    }else{
                                        bindsv = new BindOfSnapAndView();
                                        bindsv.snap = selSnap;
                                        bindsv.view = newView;
                                        map.put( key, bindsv );
                                    }
                                }
                            }
                        }else{
                            map.remove( key );
                            
                            isOk = view.initor.mdb.addView( viewName, selSnap.getSnap_root_id(), selSnap.getSnap_local_snapid() );
                            if( !isOk ){
                                errMsg = SanBootView.res.getString("FailoverWizardDialog.error.crtView")+" : [ SnapID: "+selSnap.getCreateTimeStr()+" ]";
                                JOptionPane.showMessageDialog(pdiag,
                                    errMsg
                                );
                                this.writeLogBuf( errMsg );
                                allFinish = false;
                            }else{
                                newView = view.initor.mdb.getCrtView();
                                //isOk = addLunMap( newView.getTargetID() );
                                isOk = true;
                                if( !isOk ){
                                    JOptionPane.showMessageDialog(pdiag,
                                        SanBootView.res.getString("FailoverWizardDialog.error.addLunmap")
                                    );
                                    allFinish = false;
                                    view.initor.mdb.delView( newView );
                                }else{
                                    bindsv = new BindOfSnapAndView();
                                    bindsv.snap = selSnap;
                                    bindsv.view = newView;
                                    map.put( key, bindsv );
                                }
                            }
                        }    
                    }else{
                        // 之前没有为该盘生成view,创建一个
                        isOk = view.initor.mdb.addView( viewName, selSnap.getSnap_root_id(), selSnap.getSnap_local_snapid() );
                        if( !isOk ){
                            errMsg = SanBootView.res.getString("FailoverWizardDialog.error.crtView")+" : [ SnapID: "+selSnap.getCreateTimeStr()+" ]";
                            JOptionPane.showMessageDialog(pdiag,
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
                            allFinish = false;
                        }else{
                            newView = view.initor.mdb.getCrtView();
                            //isOk = addLunMap( newView.getTargetID() );
                            isOk = true;
                            if( !isOk ){
                                JOptionPane.showMessageDialog(pdiag,
                                    SanBootView.res.getString("FailoverWizardDialog.error.addLunmap")
                                );
                                allFinish = false;
                                view.initor.mdb.delView( newView );
                            }else{
                                bindsv = new BindOfSnapAndView();
                                bindsv.snap = selSnap;
                                bindsv.view = newView;
                                map.put( key, bindsv );
                            }
                        }
                    }
                }else{ //  没有选择恢复该盘，将之前为它创建的view删除（如果有的话）
                    if( bindsv != null ){
                        // ɾ�����view
                        if( bindsv.snap instanceof Snapshot ){
                            tid = bindsv.view.getTargetID();
                            //if( delLunMap( tid ) && view.initor.mdb.delView( bindsv.view ) ){
                            if(  view.initor.mdb.delView( bindsv.view ) ){
                                map.remove( key );
                            }
                        }
                    }
                }
            }else if( bindts.snap instanceof ViewWrapper ){
System.out.println("CrtUnixView-->: is a viewwrapper.");
                selView = ( (ViewWrapper)bindts.snap ).view;
                
                // 检查snap number是否合适
                checkSnap = diag.checkSnapNumber( bindts.tgt.toString(), -1,-1 );
                if( !checkSnap ){
                    allFinish = false;
                    break;
                }
                
                if( bindts.isSel ){    
                    if( bindsv != null ){ // hashmap中已经有了
                        if( bindsv.snap instanceof Snapshot ){
                            // 删除之前创建的这个view
                            //tid = bindsv.view.getTargetID();
                            //delLunMap( tid );
                            view.initor.mdb.delView( bindsv.view ); // 不管结果   
                        }
                        
                        map.remove( key );
                                
                        //if( addLunMap( selView.getTargetID() ) ){
                        if( true ){
                            bindsv = new BindOfSnapAndView();
                            bindsv.snap = selView;
                            bindsv.view = null;
                            map.put( key, bindsv );
                        }else{
                            JOptionPane.showMessageDialog(pdiag,
                                SanBootView.res.getString("FailoverWizardDialog.error.addLunmap")
                            );
                            allFinish = false;
                        }
                    }else{
                        //if( addLunMap( selView.getTargetID() ) ){
                        if( true ){
                            bindsv = new BindOfSnapAndView();
                            bindsv.snap = selView;
                            bindsv.view = null;
                            map.put( key, bindsv );
                        }else{
                            JOptionPane.showMessageDialog(pdiag,
                                SanBootView.res.getString("FailoverWizardDialog.error.addLunmap")
                            );
                            allFinish = false;
                        }
                    }
                }else{ //  没有选择恢复该盘，将之前为它创建的view删除（如果有的话）
                    if( bindsv != null ){
                        // 删除这个view
                        if( bindsv.snap instanceof Snapshot ){
                            tid = bindsv.view.getTargetID();
                            //if( delLunMap( tid ) && view.initor.mdb.delView( bindsv.view ) ){
                            if( view.initor.mdb.delView( bindsv.view ) ){
                                map.remove( key );
                            }
                        }
                    }
                }
            }else if( bindts.snap instanceof CloneDiskWrapper ){
System.out.println("CrtUnixView-->: is a clonediskwrapper.");
                selCloneDisk = ((CloneDiskWrapper)bindts.snap).cloneDisk;

                if( bindts.isSel ){
                    if( bindsv != null ){ // hashmap中已经有了
                        if( bindsv.snap instanceof Snapshot ){
                            // 删除之前创建的这个view
                            //tid = bindsv.view.getTargetID();
                            //delLunMap( tid );
                            view.initor.mdb.delView( bindsv.view ); // 不管结果
                        }

                        map.remove( key );
                        bindsv = new BindOfSnapAndView();
                        bindsv.snap = selCloneDisk;
                        bindsv.view = null;
                        map.put( key, bindsv );
                    }else{
                        bindsv = new BindOfSnapAndView();
                        bindsv.snap = selCloneDisk;
                        bindsv.view = null;
                        map.put( key, bindsv );
                    }
                }else{ //  没有选择恢复该盘，将之前为它创建的view删除（如果有的话）
                    if( bindsv != null ){
                        // 删除这个view
                        if( bindsv.snap instanceof Snapshot ){
                            tid = bindsv.view.getTargetID();
                            //if( delLunMap(tid) && view.initor.mdb.delView( bindsv.view ) ){
                            if( view.initor.mdb.delView( bindsv.view ) ){
                                map.remove( key );
                            }
                        }
                    }
                }
            }else{ // VolumeMapWrapper
System.out.println("CrtUnixView-->: is a volumemapwrapper.");
                selVolMap = ((VolumeMapWrapper)bindts.snap).volMap;
                
                // 检查snap number是否合适
                checkSnap = diag.checkSnapNumber( bindts.tgt.toString(), -1,-1 );
                if( !checkSnap ){
                    allFinish = false;
                    break;
                }
                
                if( bindts.isSel ){    
                    if( bindsv != null ){ // hashmap中已经有了
                        if( bindsv.snap instanceof Snapshot ){
                            // 删除之前创建的这个view
                            //tid = bindsv.view.getTargetID();
                            //delLunMap( tid );
                            view.initor.mdb.delView( bindsv.view ); // 不管结果 
                        }
                        
                        map.remove( key );
                        bindsv = new BindOfSnapAndView();
                        bindsv.snap = selVolMap;
                        bindsv.view = null;
                        map.put( key, bindsv );
                    }else{
                        bindsv = new BindOfSnapAndView();
                        bindsv.snap = selVolMap;
                        bindsv.view = null;
                        map.put( key, bindsv );
                    }
                }else{ //  没有选择恢复该盘，将之前为它创建的view删除（如果有的话）
                    if( bindsv != null ){
                        // 删除这个view
                        if( bindsv.snap instanceof Snapshot ){
                            tid = bindsv.view.getTargetID();
                            //if( delLunMap(tid) && view.initor.mdb.delView( bindsv.view ) ){
                            if( view.initor.mdb.delView( bindsv.view ) ){
                                map.remove( key );
                            }
                        }
                    }
                }
            }
        }
        
        //  没有选择恢复该盘，将之前为它创建的view删除（如果有的话）
        if( allFinish && isNetBootOnLocalHost ){
            boolean isFirst = true;
            selVer_buf = new StringBuffer();

            size = snaps.length;
            for( i=0; i<size; i++ ){
                bindts = (BindofTgtAndSnap)snaps[i];
                selTgt = bindts.tgt;
                key = bindts.tgt.toString();
                disk_type = -1;

                if( bindts.snap instanceof SnapWrapper ){
                    bindsv = (BindOfSnapAndView)map.get( key );
                    tid = bindsv.view.getTargetID();
                    crttime = bindsv.view.getSnap_create_time();
                }else if( bindts.snap instanceof ViewWrapper ){
                    tid =((ViewWrapper)bindts.snap).view.getTargetID();
                    crttime = ((ViewWrapper)bindts.snap).view.getSnap_create_time();
                }else if( bindts.snap instanceof CloneDiskWrapper ){
                    tid = ((CloneDiskWrapper)bindts.snap).cloneDisk.getTarget_id();
                    crttime = ((CloneDiskWrapper)bindts.snap).cloneDisk.getCrt_time();
                    disk_type = VolumeMap.DISK_TYPE_CLONE_DISK;
                }else{ // VolumeWrapper
                    tid = selTgt.tgt.getVolTargetID();
                    crttime = "";
                    disk_type = VolumeMap.DISK_TYPE_VOLUME;
                }
SanBootView.log.info( getClass().getName()," =========:  " + key+" " + disk_type );

                if( bindts.isSel ){
                    isOk = view.initor.mdb.modOneVolumeMap1( selTgt.tgt.getVolName(),tid,crttime,disk_type );
                    if( isOk ){
                        // 修改 cache中的这个 volMap的 Vol_view_targetid值
                        selTgt.tgt.setVol_view_targetid( tid );
                        selTgt.tgt.setLastGoodBootInfo( crttime );
                        selTgt.tgt.setLast_goog_boot_disk_type( disk_type );
                    }else{
                        errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.modVolMap")+" "+
                                 selTgt.tgt.getVolName() +" "+
                                 SanBootView.res.getString("common.failed") ;
                        JOptionPane.showMessageDialog(pdiag, 
                            errMsg
                        );
                        this.writeLogBuf( errMsg );
                        allFinish = false;
                    }
                }else{
                    tid = -1;
                    isOk = view.initor.mdb.modOneVolumeMap1( selTgt.tgt.getVolName(), -1,"",disk_type );
                    if( isOk ){
                        // 修改 cache中的这个 volMap的 Vol_view_targetid值
                        selTgt.tgt.setVol_view_targetid( -1 );
                        selTgt.tgt.setLastGoodBootInfo( "" );
                        selTgt.tgt.setLast_goog_boot_disk_type( disk_type );
                    }else{
                        errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.modVolMap")+" "+
                                 selTgt.tgt.getVolName() +" "+
                                 SanBootView.res.getString("common.failed") ;
                        JOptionPane.showMessageDialog(pdiag, 
                            errMsg
                        );
                        this.writeLogBuf( errMsg );
                        allFinish = false;
                    }
                }

                // 记录selected version for sdhm
                VolumeMap lv = view.initor.mdb.getLVOnClntAndVG( selTgt.tgt.getVolClntID(),selTgt.tgt.getVolDiskLabel() );
                if( lv != null ){
                    if( isFirst ){
                        this.selVer_buf.append( lv.getVolDiskLabel() + "-?" + tid );
                        isFirst = false;
                    }else{
                        this.selVer_buf.append( "-?" + lv.getVolDiskLabel() + "-?" + tid );
                    }
                }else{
                    if( isFirst ){
                        this.selVer_buf.append( selTgt.tgt.getVolName() + "-?" + tid );
                        isFirst = false;
                    }else{
                        this.selVer_buf.append( "-?" + selTgt.tgt.getVolName() + "-?" + tid );
                    }
                }
            }
        }
        
        // 在原机上进行网络启动的操作( 只修改tftproot/<original_mac>/iBootInfo-xxx )
        if( allFinish && isNetBootOnLocalHost ){
            // 修改fstab,将来实现。（这要求目前只能恢复全部文件系统，不能只恢复部分文件系统） 2008/2/27
            
            // 修改boot Info(生成与曹锡韬关于lvm方面的接口) 
            tftpRootPath = view.initor.mdb.getTftpRootPath();
            if( tftpRootPath.equals("") ){
                errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.setBootInfo") +
                        SanBootView.res.getString("common.failed");
                JOptionPane.showMessageDialog(pdiag, 
                    errMsg
                );
                this.writeLogBuf( errMsg );
                allFinish = false;
            }else{
                isOk = view.initor.mdb.listDir( tftpRootPath+"/"+mac+"/" );
                if( !isOk ){
                    errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.setBootInfo") +
                            SanBootView.res.getString("common.failed");
                    JOptionPane.showMessageDialog(pdiag, 
                        errMsg
                    );
                    this.writeLogBuf( errMsg );
                    allFinish = false;
                }else{
                    ArrayList fList = view.initor.mdb.getFileList();
                    if( fList.size() == 0 ){
                        errMsg =  SanBootView.res.getString("InitNWinHostWizardDialog.log.setBootInfo") +
                                SanBootView.res.getString("common.failed");
                        JOptionPane.showMessageDialog(pdiag, 
                           errMsg
                        );
                        this.writeLogBuf( errMsg );
                        allFinish = false;
                    }else{
                        fList = getFileList( fList );
                        if( fList.size() == 0 ){
                            errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.setBootInfo") +
                                    SanBootView.res.getString("common.failed");
                            JOptionPane.showMessageDialog(pdiag, 
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
                            allFinish = false;
                        }else{
                            String conf = ((UnixFileObj)fList.get(0)).getAbsPath();
                            isOk = saveInfo( conf, getIBootConfContents(iscsiVar, host.getID()) );
                            if( !isOk ){
                                errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.setBootInfo") +
                                        SanBootView.res.getString("common.failed");
                                JOptionPane.showMessageDialog(pdiag, 
                                    errMsg
                                );
                                this.writeLogBuf( errMsg );
                                allFinish = false;
                            }
                        }
                    }
                }
            }
        }

        // 在其他机器上进行网络启动操作( 修改CaoXiTao的iboot部分 )
        if( allFinish && !isNetBootOnLocalHost ){
            tftpRootPath = view.initor.mdb.getTftpRootPath();
            if( tftpRootPath.equals("") ){
                errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.getTftpPath") ;

                JOptionPane.showMessageDialog(pdiag, 
                    errMsg
                );
                this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName()," tftp path is null. ");
                allFinish = false;
            }
            
            if( allFinish ){
                String org_path = tftpRootPath + orgSimpleMac;                
                String new_path = tftpRootPath + newSimpleMac;
                isOk = view.initor.mdb.listDir( tftpRootPath + newSimpleMac );
                if( isOk ){
                    // destination dir exist, deleting it or something behide will be wrong.
                    isOk = view.initor.mdb.delFile( new_path );
                    if( !isOk ){
SanBootView.log.error( getClass().getName(),"deleting destination dir is failed. ");
                    }else{
                        isOk = view.initor.mdb.copyFiles( org_path, new_path );
                    }
                }else{
                    isOk = view.initor.mdb.copyFiles( org_path, new_path );
                }
                
                if( !isOk ){
                    errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                    JOptionPane.showMessageDialog(pdiag,
                        errMsg
                    );
                    this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"duplication of original pxe config file to new location is failed. ");
                    allFinish = false;
                }
            }

             ArrayList orgBootFileList = null;
            if( allFinish ){                
                String org_pxe_path = tftpRootPath + "/pxelinux.cfg/" + org_pxe_mac;
                if( allFinish ){
                    isOk = view.initor.mdb.getPXEInfoFromTFtp( org_pxe_path );
                    if( !isOk ){
                        errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.getPxeInfo") ;
                        JOptionPane.showMessageDialog(pdiag, 
                            errMsg
                        );
                        this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"getting original pxe config file is failed. ");
                        allFinish = false;
                    }else{
                        String _newPxeInfo = view.initor.mdb.replacePXEInfo( orgSimpleMac,newSimpleMac );
                        String newPxeInfo = view.initor.mdb.replaceTftpInfo( _newPxeInfo,tftp_ip );
                        isOk = saveInfo( tftpRootPath + "/pxelinux.cfg/" + new_pxe_mac, newPxeInfo );
                        if( !isOk ){
                            errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                            JOptionPane.showMessageDialog(pdiag, 
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"creating new pxe config file in tftproot/pxelinux.cfg/<new_mac> directory is failed. ");                        
                            allFinish = false;
                        }
                        
                        if( allFinish ){
                            isOk = view.initor.mdb.listDir( tftpRootPath+"/"+orgSimpleMac+"/" );
                            if( !isOk ){
                                errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                JOptionPane.showMessageDialog(pdiag, 
                                    errMsg
                                );
                                this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"listing tftproot/<original_mac> is failed. ");                                
                                allFinish = false;
                            }else{
                                orgBootFileList = view.initor.mdb.getFileList();
                                if( orgBootFileList.size() == 0 ){
                                    errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo") ;
                                    JOptionPane.showMessageDialog(pdiag, 
                                        errMsg
                                    );
                                    this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"listing tftproot/<original_mac> is failed. ");                                
                                    allFinish = false;
                                }else{
                                    // modify pxe.<kernel>.<lvmtype> file
                                    ArrayList fList1 = getFileList1( orgBootFileList );
                                    if( fList1.size() == 0 ){
                                        errMsg =  SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                        JOptionPane.showMessageDialog(pdiag, 
                                           errMsg
                                        );
                                        this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"there is not a pxe-xxx file in tftproot/<original_mac> dir.");                                     
                                        allFinish = false;
                                    }else{
                                        String conf = tftpRootPath+"/"+newSimpleMac+"/"+((UnixFileObj)fList1.get(0)).getName();
                                        isOk = saveInfo( conf, newPxeInfo );
                                        if( !isOk ){
                                            errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                            JOptionPane.showMessageDialog(pdiag, 
                                                errMsg
                                            );
                                            this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"creating new pxe config file in tftproot/<new_mac> dir is failed");                                        
                                            allFinish = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if( allFinish){
                // 为支持IA创建ln
                if( this.host.isIA() ){
                    boolean useOdyDhcp = setDhcpPane.isAutoSetup();
                    DhcpClientInfo  dhcpInfo1 = setDhcpPane.getDhcpSetInfo();
                    String hexIp;
                    if( useOdyDhcp ){
                        hexIp = Util.inetAddress2HexString( dhcpInfo1.ip ).toUpperCase();
                    }else{
                        hexIp = Util.inetAddress2HexString( dhcpInfo1.ip_3rd ).toLowerCase();
                    }
System.out.println(" hexIp: "+ hexIp );

                    isOk = view.initor.mdb.delFile( tftpRootPath + "/" + hexIp + ".conf" );
                    if( !isOk ){
                        errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                        JOptionPane.showMessageDialog(pdiag,
                            errMsg
                        );
                        this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"duplication of original elilo config file to new location is failed. ");
                        allFinish = false;
                    }else{
                        ArrayList flist = this.getEliloFileList( orgBootFileList );
                        if( flist.size() == 0 ){
                            errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                            JOptionPane.showMessageDialog(pdiag,
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"there is not a elilo-xxx file in tftproot/<original_mac> dir.");
                            allFinish = false;
                        }else{
                            String eliloName = ((UnixFileObj)flist.get(0)).getName();
                            isOk = view.initor.mdb.ln( tftpRootPath + "/" + hexIp + ".conf", mac+"/" + eliloName );
                            if( !isOk ){
                                errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                JOptionPane.showMessageDialog(pdiag,
                                    errMsg
                                );
                                this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"failed to create link to tftproot/<new_mac> dir.");
                                allFinish = false;
                            }
                        }
                    }
                }
            }
        }
        
        int da_id = -1;
        if( allFinish && !isNetBootOnLocalHost ){
            // 记录最新的恢复版本( 对destagent而言是修改snapusage )
            SnapUsage su = null;
            int rootid = -1;
            String simpleMAC = DhcpClientInfo.getSimpleMac( this.bootMac );
SanBootView.log.debug( getClass().getName()," selected boot mac: "+ bootMac );            
            DestAgent da = getSelectedMC( bootMac ); 
            
            size = snaps.length; // booted version
            for( i=0; i<size; i++ ){
                bindts = (BindofTgtAndSnap)snaps[i];
                selTgt = bindts.tgt;
                key = bindts.tgt.toString();
                
                if( bindts.snap instanceof SnapWrapper ){
                    bindsv = (BindOfSnapAndView)map.get( key );
                    tid = bindsv.view.getTargetID();
                    crttime = bindsv.view.getSnap_create_time();
                    snapid = ((SnapWrapper)bindts.snap).snap.getSnap_local_snapid();
                    viewid = bindsv.view.getSnap_local_snapid();
                }else if( bindts.snap instanceof ViewWrapper ){
                    tid =((ViewWrapper)bindts.snap).view.getTargetID();
                    crttime = ((ViewWrapper)bindts.snap).view.getSnap_create_time();
                    snapid = -1;
                    viewid = ((ViewWrapper)bindts.snap).view.getSnap_local_snapid();
                }else if( bindts.snap instanceof CloneDiskWrapper ){
                    tid = ((CloneDiskWrapper)bindts.snap).cloneDisk.getTarget_id();
                    crttime = ((CloneDiskWrapper)bindts.snap).cloneDisk.getCrt_time();
                    snapid = -1;
                    viewid = -1;
                }else{
                    tid = selTgt.tgt.getVolTargetID();
                    crttime = "";
                    snapid = -1;
                    viewid = -1;
                }
                
                // 用户指定了网卡地址或要进行网络启动的主机和初始化的原机不一样
                if( da == null ){
                    if( newDa != null ){
                        da_id = newDa.getDst_agent_id();
                    }else{
                        newDa = new DestAgent(
                                    -1,
                                    start_ip,
                                    host.getPort(),
                                    host.getOS(),
                                    simpleMAC,
                                    "",
                                    0
                                );
                        if( view.initor.mdb.addNBH( newDa ) ){
                            newDa.setDst_agent_id( view.initor.mdb.getNewId() );
                            view.initor.mdb.addNBHIntoCache( newDa );
                            da_id = newDa.getDst_agent_id();              
                        }else{
                            errMsg = SanBootView.res.getString("FailoverWizardDialog.log.addNBH") +" "+
                                    SanBootView.res.getString("common.failed");
                            JOptionPane.showMessageDialog(pdiag, 
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
                            allFinish = false;
                        }
                    }
                }else{
                    da_id = da.getDst_agent_id();
                    if( !da.getIP().equals( start_ip ) ){
                        if( view.initor.mdb.modNBH( da_id,start_ip ) ){
                            da.setDst_agent_ip( start_ip );
                        }else{
                            errMsg = SanBootView.res.getString("FailoverWizardDialog.log.modNBH") +" "+
                                    SanBootView.res.getString("common.failed");
                            JOptionPane.showMessageDialog(pdiag, 
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
                            allFinish = false;
                        }
                    }
                }
                
SanBootView.log.info( getClass().getName()," (add snap-usage)destAgent id: " + da_id );                        
                if( da_id != -1 ){
                    rootid = selTgt.tgt.getVol_rootid();
                    su = view.initor.mdb.getSnapUsageOnSomething( da_id, rootid, key );
                    if( !bindts.isSel ){
                        tid = -1;
                        crttime ="";
                    }

                    if( su == null ){
                        su = new SnapUsage(
                                    -1,
                                    da_id, 
                                    selTgt.tgt.getVol_rootid(),
                                    snapid,
                                    viewid,
                                    key,
                                    tid,
                                    crttime
                                );
                        if( view.initor.mdb.addMSU( su ) ) {
                            su.setUsage_id( view.initor.mdb.getNewId() );
                            view.initor.mdb.addMSUIntoCache( su );
                        }else{
                            errMsg = SanBootView.res.getString("FailoverWizardDialog.log.addSU")+" "+
                                        SanBootView.res.getString("common.failed");
                            JOptionPane.showMessageDialog(pdiag, 
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
                            allFinish = false;
                        }
                    }else{
                        if( view.initor.mdb.modMSU( su.getUsage_id(),snapid,viewid,tid,crttime ) ){
                            su.setSnap_local_id( snapid );
                            su.setSnap_view_local_id( viewid );
                            su.setSnapTid( tid );
                            su.setCrtTime( crttime );
                        }else{
                            errMsg =  SanBootView.res.getString("FailoverWizardDialog.log.modSU")+" "+
                                        SanBootView.res.getString("common.failed");
                            JOptionPane.showMessageDialog(pdiag, 
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
                            allFinish = false;
                        }
                    }
                }
            }
        }
        
        // setup dhcp record for other netboot host
        if( allFinish ){
            boolean useOdyDhcp = setDhcpPane.isAutoSetup();
            DhcpClientInfo  dhcpInfo = setDhcpPane.getDhcpSetInfo();
            if( useOdyDhcp ){
                // 先删除这个dhcp conf
                isOk = view.initor.dhcpdb.dhcpOperation(
                    ResourceCenter.BIN_DIR + "dhcp_set.sh delcli -p " + newSimpleMac
                );
                if( isOk ){
                    try{
                        Thread.sleep( 5000 ); // 睡5秒钟，否则连续执行delcli和addcli会造成dhcpd起不来
                    }catch(Exception ex){}
                    
                    boolean hasdns = ( !dhcpInfo.dns.equals("") );
                    boolean hasgw = ( !dhcpInfo.defgw.equals("") );
                    String ibootSrv = dhcpInfo.nextServer;
                    String args = "";
                    if( hasdns ){
                        args+=" -d " +dhcpInfo.dns;
                    }
                    if( hasgw ){
                        args+=" -g "+dhcpInfo.defgw;
                    }

                    if( host.getMachine().toUpperCase().equals( ResourceCenter.PLATFORM_IA64 ) ){
                        args += " -i "+dhcpInfo.ip + " -p " + newSimpleMac +" -s "+dhcpInfo.subnet + " -x "+ibootSrv + " -machine " + host.getMachine() + " -ostype linux";
                    }else{
                        args += " -i "+dhcpInfo.ip + " -p " + newSimpleMac +" -s "+dhcpInfo.subnet + " -x "+ibootSrv + " -ostype linux";
                    }

                    isOk = view.initor.dhcpdb.dhcpOperation(
                        ResourceCenter.BIN_DIR + "dhcp_set.sh addcli " + args
                    ); 
                    if( isOk ){
                        isOk = view.initor.dhcpdb.getClientFromDhcp();
                        if( isOk ){
                            if( view.initor.dhcpdb.getSelClntOnMac( newBootMacForUnix ) == null ){
SanBootView.log.error( getClass().getName(),"Error: not found record in dhcp config about MAC: " + newBootMacForUnix );
                                errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                                          SanBootView.res.getString("common.failed");
                                JOptionPane.showMessageDialog(pdiag,
                                    errMsg
                                );
                                this.writeLogBuf( errMsg );
                                allFinish = false;
                            }else{
                                if( isNetBootOnLocalHost ){
                                    if( !host.isUseOdyDhcp() ){
                                        host.setUseOdyDhcp();
                                        view.initor.mdb.modOneBootHost2( host.getID(),host.getStopAllBaseServFlag() );
                                    }
                                }else{ // is a destagent
                                    view.initor.mdb.delFile( ResourceCenter.CLT_IP_CONF + "/"+ResourceCenter.PREFIX_DST_AGNT + da_id + ResourceCenter.CONF_3RD_DHCP );
                                }
                            }
                        }else{
                            errMsg =  SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                                      SanBootView.res.getString("common.failed");
                            JOptionPane.showMessageDialog(pdiag,
                               errMsg
                            );
                            this.writeLogBuf( errMsg );
                            allFinish = false;
                        }
                    }else{
                        errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                                  SanBootView.res.getString("common.failed");
                        JOptionPane.showMessageDialog(pdiag,
                            errMsg
                        );
                        this.writeLogBuf( errMsg );
                        allFinish = false;
                    }
                }else{
                    errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                              SanBootView.res.getString("common.failed");
                    JOptionPane.showMessageDialog(pdiag,
                        errMsg
                    );
                    this.writeLogBuf( errMsg );
                    allFinish = false;
                }
            }else{
                // save the 3rd dhcp setup info. 
                String conf="";
                if( isNetBootOnLocalHost ){
                    conf = ResourceCenter.CLT_IP_CONF + "/" + host.getID() + ResourceCenter.CONF_3RD_DHCP;
                }else{
                    conf = ResourceCenter.CLT_IP_CONF + "/"+ResourceCenter.PREFIX_DST_AGNT + da_id + ResourceCenter.CONF_3RD_DHCP;
                }
                saveInfo( conf , "ip:"+dhcpInfo.ip_3rd +"  nextsrv:"+ dhcpInfo.nextServer_3rd );
                
                if( isNetBootOnLocalHost ){
                    if( host.isUseOdyDhcp() ){
                        host.clearUseOdyDhcp();
                        view.initor.mdb.modOneBootHost2( host.getID(),host.getStopAllBaseServFlag() );
                    }
                }
            }
        }
        
        if( allFinish && !isNetBootOnLocalHost ){
            // 修改fstab,将来实现。（这要求目前只能恢复全部文件系统，不能只恢复部分文件系统） 2008/2/27

            // 修改boot Info(生成与曹锡韬关于lvm方面的接口)
            if( tftpRootPath.equals("") ){
                tftpRootPath = view.initor.mdb.getTftpRootPath();
                if( tftpRootPath.equals("") ){
                    errMsg = SanBootView.res.getString("InitNWinHostWizardDialog.log.setBootInfo") +
                            SanBootView.res.getString("common.failed");
                    JOptionPane.showMessageDialog(pdiag, 
                        errMsg
                    );
                    this.writeLogBuf( errMsg );
                    allFinish = false;
                }
            }
            
            if( allFinish ){
                isOk = view.initor.mdb.listDir( tftpRootPath+"/"+newSimpleMac+"/" );
                if( !isOk ){
                    errMsg =  SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                    JOptionPane.showMessageDialog(pdiag, 
                       errMsg
                    );
                    this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"listing tftproot/<original_mac> is failed. ");                                
                    allFinish = false;
                }else{
                    ArrayList fList = view.initor.mdb.getFileList();
                    if( fList.size() == 0 ){
                        errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo") ;
                        JOptionPane.showMessageDialog(pdiag, 
                            errMsg
                        );
                        this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"listing tftproot/<original_mac> is failed. ");                                
                        allFinish = false;
                    }else{
                        // modify iBootInfo.<kernel>.<lvmtype> file
                        ArrayList fList2 = getFileList( fList );
                        if( fList2.size() == 0 ){
                            errMsg = SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                            JOptionPane.showMessageDialog(pdiag, 
                                errMsg
                            );
                            this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"there is not a iBootInfo-xxx file in tftproot/<original_mac> dir.");                                             
                            allFinish = false;
                        }else{
                            String conf = tftpRootPath+"/"+newSimpleMac+"/"+((UnixFileObj)fList2.get(0)).getName();
                            isOk = saveInfo( conf, getIBootConfContentsForDestAgent( iscsiVar, host.getID(),da_id ) );
                            if( !isOk ){
                                errMsg =  SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo");
                                JOptionPane.showMessageDialog(pdiag, 
                                   errMsg
                                );
                                this.writeLogBuf( errMsg );
SanBootView.log.error( getClass().getName(),"creating new iBootInfo config file in tftproot/<new_mac> dir is failed");                                           
                                allFinish = false;
                            }
                        }
                    }
                }
            }
        }
        
        try{
            SwingUtilities.invokeAndWait( close );
        }catch( Exception ex ){
            ex.printStackTrace();
        }
    }

    private boolean addLunMap( int tid ){
        return view.initor.mdb.addLunMap( tid, start_ip, "255.255.255.255", "rw", "", "", "", "" );
    }

    private boolean delLunMap( int tid ){
        return view.initor.mdb.delLunMap( tid, start_ip,"255.255.255.255", "rw" );
    }
    
    public ArrayList getFileList( ArrayList fileList ){
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
                if( line.length > 2 ){
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

    public ArrayList getEliloFileList( ArrayList fileList ){
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
                if( one.getName().startsWith("elilo-") ){
                    ret.add( one );
                }
            }
        }
        return ret;
    }

    public ArrayList getFileList1( ArrayList fileList ){
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
                if( line.length > 2 ){
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
    
    private String getIBootConfContents( String iscsiVar, int clntID ){
        int i,j,size,size1,tid;
        String tmp,fsType;
        boolean isFirst;
        VolumeMap tgt,vg,lv;
        Vector tgtList,vgList,lvList;
        StringBuffer buf1;
        
        Vector volMapList = view.initor.mdb.getVolMapOnClntID( clntID );
        
        StringBuffer buf  = new StringBuffer();
        buf.append("[header]");
        buf.append("\ntgt-prefix="+iscsiVar );
        buf.append("\nTIP=" + tftp_ip );
        buf.append("\nSTRICT=1");
        
        tgtList = view.initor.mdb.getTgtListOnClntIDAndVg( volMapList,null, clntID );
        size = tgtList.size();
        isFirst = true;
        tmp="";
        buf.append("\n\n[targetlist]");

        for( i=0; i<size; i++ ){
            tgt = (VolumeMap)tgtList.elementAt(i);
//System.out.println(" tgt name: "+tgt.getVolName() +" view id: "+tgt.getVol_view_targetid() );
            
            // vol_view_targetid为-1,表示这个target不被用来网络启动���
            if( tgt.getVol_view_targetid() == -1 ) continue;
            
            if( tgt.getVol_view_targetid() != 0 ){
                tid = tgt.getVol_view_targetid();
            }else{
                tid = tgt.getVolTargetID();
            }
                   
            // add lunmap on iboot_dhcp_ip for targets(Don't care result) 
            view.initor.mdb.addLunMap( tid, start_ip, "255.255.255.255", "rw", "", "", "", "" );
            
            if( isFirst ){
                tmp = tid + "";
                isFirst = false;
            }else{
                tmp += ";" + tid; 
            }
        }
        buf.append("\ntargetlist="+tmp+"\n");
        
        vgList = view.initor.mdb.getVgListOnClntID( volMapList, clntID );
        size = vgList.size();
        for( i=0; i<size; i++ ){
            vg = (VolumeMap)vgList.elementAt(i);
            
            tgtList = view.initor.mdb.getTgtListOnClntIDAndVg( volMapList, vg.getVolName(), clntID );
            size1 = tgtList.size();
            tmp="";
            isFirst = true;
            for( j=0; j<size1; j++ ){
                tgt = (VolumeMap)tgtList.elementAt( j );
                // vol_view_targetid为-1,表示这个target不被用来网络启动����
                if( tgt.getVol_view_targetid() == -1 ) continue;
                
                if( tgt.getVol_view_targetid() != 0 ){
                    tid = tgt.getVol_view_targetid();
                }else{
                    tid = tgt.getVolTargetID();
                }
                
                if( isFirst ){
                    tmp = tid + "";
                    isFirst = false;
                }else{
                    tmp += ";" + tid;
                }
            }
            
            if( !tmp.equals("") ){
                buf.append("\n\n[vg]");
                buf.append("\nname="+vg.getVolName());
                buf.append("\npvs="+tmp);
                
                lvList = view.initor.mdb.getLVListOnClntID( volMapList, vg.getVolName() );
                size1 = lvList.size();
                tmp="";
                isFirst = true;
                for( j=0; j<size1; j++ ){
                    lv = (VolumeMap)lvList.elementAt(j);
                    if( isFirst ){
                        tmp=lv.getVolName();
                        isFirst = false;
                    }else{
                        tmp+=";"+lv.getVolName();
                    }
                }
                buf.append("\nlvs="+tmp);
                buf.append("\nlvmtype="+vg.getVolDiskLabel());
                buf.append("\npartition=1");
                buf.append("\nvgpartition=1");
            }
        }
        
        buf1 = new StringBuffer();
        buf1.append("\n\n[lv-mountpoint-fstype]");
        lvList = view.initor.mdb.getRealLVListOnClntID( clntID );
        size = lvList.size();
        for( i=0; i<size; i++ ){
            lv = (VolumeMap)lvList.elementAt(i);
            tgt = view.initor.mdb.getTgtOnVGname( lv.getVolDesc(),lv.getVolClntID() );
            // vol_view_targetid为-1, 表示这个target不被用来网络启动����
            if( tgt.getVol_view_targetid() == -1 ) continue;
            
            buf.append("\n\n[lv]");
            buf.append("\nname="+lv.getVolName());
            buf.append("\nowner="+lv.getVolDesc());
            fsType = getFSType( lv.getVolDiskLabel() );
            buf.append("\nfstype="+fsType);
            buf.append("\nsize=");
            buf.append("\nuuid=");
            buf.append("\ntag=");
            if( !isUsingSwap ){
                if( lv.getVolDiskLabel().equals(ResourceCenter.SWAP_MP ) ) continue;
            }
            buf1.append( "\n"+lv.getVolDesc()+"/"+lv.getVolName()+" "+lv.getVolDiskLabel() +" "+fsType );
        }
        
        buf.append( buf1.toString() );
        buf.append("\n");
        
        return buf.toString();
    }
     
    private String getIBootConfContentsForDestAgent( String iscsiVar, int clntID,int da_id ){
        int i,j,size,size1,tid;
        String tmp,fsType;
        boolean isFirst;
        VolumeMap tgt,vg,lv;
        Vector tgtList,vgList,lvList;
        StringBuffer buf1;
        ArrayList<SnapUsage> suList;
        
        Vector volMapList = view.initor.mdb.getVolMapOnClntID( clntID );
        
        StringBuffer buf  = new StringBuffer();
        buf.append("[header]");
        buf.append("\ntgt-prefix=" + iscsiVar );
        buf.append("\nTIP=" + tftp_ip );
        buf.append("\nSTRICT=1");
        
        tgtList = view.initor.mdb.getTgtListOnClntIDAndVg( volMapList,null, clntID );
        size = tgtList.size();
        isFirst = true;
        tmp="";
        buf.append("\n\n[targetlist]");
        
        for( i=0; i<size; i++ ){
            tgt = (VolumeMap)tgtList.elementAt(i);
//System.out.println(" tgt name: "+tgt.getVolName() +" view id: "+tgt.getVol_view_targetid() );
            suList = view.initor.mdb.getSnapUsageOnSomething( da_id, tgt.getVol_rootid() );
            if( suList.size() > 0 ){
                tid = suList.get(0).getSnapTid();
            }else{
                if( tgt.getVol_view_targetid() == 0 ){ // is a swap
                    tid = tgt.getVolTargetID();
                }else{
                    tid = -1;
                }
            }
            
            if( tid == -1 ) continue;
            
            // add lunmap on iboot_dhcp_ip for targets(Don't care result) 
            view.initor.mdb.addLunMap( tid, start_ip, "255.255.255.255", "rw", "", "", "", "" );
            
            if( isFirst ){
                tmp = tid + "";
                isFirst = false;
            }else{
                tmp += ";" + tid; 
            }
        }
        buf.append("\ntargetlist="+tmp+"\n");
        
        vgList = view.initor.mdb.getVgListOnClntID( volMapList, clntID );
        size = vgList.size();
        for( i=0; i<size; i++ ){
            vg = (VolumeMap)vgList.elementAt(i);
            
            tgtList = view.initor.mdb.getTgtListOnClntIDAndVg( volMapList, vg.getVolName(), clntID );
            size1 = tgtList.size();
            tmp="";
            isFirst = true;
            for( j=0; j<size1; j++ ){
                tgt = (VolumeMap)tgtList.elementAt( j );
                suList = view.initor.mdb.getSnapUsageOnSomething( da_id, tgt.getVol_rootid() );
                if( suList.size() > 0 ){
                    tid = suList.get(0).getSnapTid();
                }else{
                    if( tgt.getVol_view_targetid() == 0 ){ // is a swap
                        tid = tgt.getVolTargetID();
                    }else{
                        tid = -1;
                    }
                }
                
                if( tid == -1 ) continue;
                
                if( isFirst ){
                    tmp = tid + "";
                    isFirst = false;
                }else{
                    tmp += ";" + tid;
                }
            }
            
            if( !tmp.equals("") ){
                buf.append("\n\n[vg]");
                buf.append("\nname="+vg.getVolName());
                buf.append("\npvs="+tmp);
                
                lvList = view.initor.mdb.getLVListOnClntID( volMapList, vg.getVolName() );
                size1 = lvList.size();
                tmp="";
                isFirst = true;
                for( j=0; j<size1; j++ ){
                    lv = (VolumeMap)lvList.elementAt(j);
                    if( isFirst ){
                        tmp=lv.getVolName();
                        isFirst = false;
                    }else{
                        tmp+=";"+lv.getVolName();
                    }
                }
                buf.append("\nlvs="+tmp);
                buf.append("\nlvmtype="+vg.getVolDiskLabel());
                buf.append("\npartition=1");
                buf.append("\nvgpartition=1");
            }
        }
        
        buf1 = new StringBuffer();
        buf1.append("\n\n[lv-mountpoint-fstype]");
        lvList = view.initor.mdb.getRealLVListOnClntID( clntID );
        size = lvList.size();
        for( i=0; i<size; i++ ){
            lv = (VolumeMap)lvList.elementAt(i);
            tgt = view.initor.mdb.getTgtOnVGname( lv.getVolDesc(),lv.getVolClntID() );
            suList = view.initor.mdb.getSnapUsageOnSomething( da_id, tgt.getVol_rootid() );
            if( suList.size() > 0 ){
                tid = suList.get(0).getSnapTid();
            }else{
                if( tgt.getVol_view_targetid() == 0 ){ // is a swap
                    tid = tgt.getVolTargetID();
                }else{
                    tid = -1;
                }
            }
            
            if( tid == -1 ) continue;
            
            buf.append("\n\n[lv]");
            buf.append("\nname="+lv.getVolName());
            buf.append("\nowner="+lv.getVolDesc());
            fsType = getFSType( lv.getVolDiskLabel() );
            buf.append("\nfstype="+fsType);
            buf.append("\nsize=");
            buf.append("\nuuid=");
            buf.append("\ntag=");
            if( !isUsingSwap ){
                if( lv.getVolDiskLabel().equals(ResourceCenter.SWAP_MP ) ) continue;
            }
            buf1.append( "\n"+lv.getVolDesc()+"/"+lv.getVolName()+" "+lv.getVolDiskLabel() +" "+fsType );
        }
        
        buf.append( buf1.toString() );
        buf.append("\n");
        
        return buf.toString();
    }
    
    private String getFSType( String mp ){
        SystemPartitionForUnix part;
        
        int size = partList.size();
        for( int i=0; i<size; i++ ){
            part = (SystemPartitionForUnix)partList.elementAt(i);
            if( part.mp.equals(mp) ){
                return part.fsType;
            }
        }
        
        return "";
    }
    
    private boolean saveInfo( String confile,String contents ){        
        File tmpFile = view.initor.mdb.createTmpFile( ResourceCenter.PREFIX,ResourceCenter.SUFFIX_IPINFO );
        if( tmpFile == null ){           
            JOptionPane.showMessageDialog(pdiag,SanBootView.res.getString("common.errmsg.crtTmpFileLocallyFailed") );
            return false; 
        }
        
        // 发送profile的内容
        if( !view.initor.mdb.sendFileToServer( tmpFile.getName(),contents ) ){
            JOptionPane.showMessageDialog(pdiag,SanBootView.res.getString("common.errmsg.sendFileFailed") );        
            tmpFile.delete();
            return false;
        }   
           
        tmpFile.delete();
            
        // ��tmpFile move to profile dir
        boolean isOk = view.initor.mdb.moveFile(
            ResourceCenter.TMP_DIR + tmpFile.getName(), confile      
        );
        
        return isOk;   
    }
    
    private DestAgent getSelectedMC( String mac ){
        ArrayList list = new ArrayList(1);
        NetCard nc = new NetCard();
        nc.mac = mac;
        list.add( nc );
        return getSelectedMC( list );
    }
    
    private DestAgent getSelectedMC( ArrayList netCardList ){
        int i,j,size,size1;
        Object nc;
        NetCard wnc;
        UnixNetCard unc;
        String mac;
        DestAgent da;
        
        ArrayList netBootedHostList = view.getNetbootedHostOnHost( host.getTreeNode() );
        size = netCardList.size();
        for( i=0; i<size; i++ ){
            nc = netCardList.get(i);
            if( nc instanceof NetCard ){
                wnc = (NetCard)nc;
                mac = DhcpClientInfo.getSimpleMac( wnc.mac );
            }else{
                unc = (UnixNetCard)nc;
                mac = DhcpClientInfo.getSimpleMac( unc.mac );
            }
            
            size1 = netBootedHostList.size();
SanBootView.log.debug( getClass().getName(),"netbooted host size: "+ size1 +" mac: "+ mac );            
            for( j=0; j<size1; j++ ){
                da = (DestAgent)netBootedHostList.get(j);
                if( da.getDst_agent_mac().equals( mac ) ){
SanBootView.log.debug( getClass().getName(),"11111111111111 "+ da.getDst_agent_ip() );                      
                    return da;
                }
            }
        }
        
        return null;
    }

    public String getSelVersionInfo(){
        if( this.selVer_buf != null ){
            return selVer_buf.toString();
        }else{
            return "";
        }
    }

    public String getErrLog(){
        return this.logBuf.toString();
    }
}
