/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

import guisanboot.data.BindOfSnapAndView;
import guisanboot.data.BindofMdiAndSnap;
import guisanboot.data.DestAgent;
import guisanboot.data.DhcpClientInfo;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorDiskInfoWrapper;
import guisanboot.data.NetCard;
import guisanboot.data.SnapUsage;
import guisanboot.data.SnapWrapper;
import guisanboot.data.Snapshot;
import guisanboot.data.SourceAgent;
import guisanboot.data.UnixFileObj;
import guisanboot.data.UnixNetCard;
import guisanboot.data.View;
import guisanboot.data.ViewWrapper;
import guisanboot.res.ResourceCenter;
import guisanboot.remotemirror.iBootInfo.*;
import guisanboot.tool.Tool;
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
public class CrtUnixViewForSrcAgent extends Thread{
    UnixIbootable diag; // UnixIbootable is an interface.
    ProgressDialog pdiag;
    Object[] snaps;
    HashMap map;
    SanBootView view;
    String ip,bootMac,targetSrvName;
    SourceAgent host;
    public boolean allFinish = true;
    Vector partList;
    String start_ip;
    boolean isNetBootOnLocalHost;
    SetDhcpPane setDhcpPane;
    String original_boot_MAC;
    String tftp_ip="";
    boolean isUsingSwap = true;
    
    public CrtUnixViewForSrcAgent( 
            UnixIbootable diag,
            ProgressDialog pdiag,
            Object[] snaps,
            HashMap map,
            SanBootView view,
            String bootMac,
            SourceAgent host,
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
        this.ip = host.getSrc_agnt_ip();
        this.targetSrvName = targetSrvName;
        this.partList = partList;
        this.start_ip = start_ip;
        this.isNetBootOnLocalHost = isNetBootOnLocalHost;
        this.setDhcpPane = setDhcpPane;
        this.original_boot_MAC = original_boot_MAC;
        if( setDhcpPane.isAutoSetup() ){
            tftp_ip = view.initor.getTxIP(host.getSrc_agnt_ip());
        }else{
            DhcpClientInfo dhcpInfo = setDhcpPane.getDhcpSetInfo();
            tftp_ip = dhcpInfo.nextServer_3rd;
        }
        
        /*
        // selected single netcard is same as original host's netcard to boot.
        String mac1 = DhcpClientInfo.getSimpleMac( this.bootMac ).toLowerCase();
        String mac2 = DhcpClientInfo.getSimpleMac( this.original_boot_MAC ).toLowerCase();
System.out.println("mac1: "+ mac1 +" mac2: "+mac2 );        
        if( mac1.equals( mac2 ) ){
            this.isNetBootOnLocalHost = true;
        }
         */
        
        this.isUsingSwap = isUsingSwap;
    }

    Runnable close = new Runnable(){
        public void run(){
            pdiag.dispose();
        }
    };

    @Override public void run(){
        int i,tid,size,snapid,viewid;
        boolean isOk,checkSnap;
        BindOfSnapAndView bindsv;
        BindofMdiAndSnap bindts;
        Snapshot selSnap,tmpsnap;
        View selView,newView;
        String key,viewName,crttime,tftpRootPath="";
        MirrorDiskInfoWrapper selTgt;
        MirrorDiskInfo mdi;
        CloneDisk selCloneDisk;
        DestAgent newDa = null;
        
        String orgBootMacForUnix = DhcpClientInfo.getMacStr( this.original_boot_MAC );
        String orgSimpleMac = NetCard.getSimpleMac1( orgBootMacForUnix ).toLowerCase();
        String newBootMacForUnix = DhcpClientInfo.getMacStr( this.bootMac );
        String newSimpleMac = NetCard.getSimpleMac1( newBootMacForUnix ).toLowerCase();
        String new_pxe_mac = UnixNetCard.getPXEMac( newBootMacForUnix ).toLowerCase();
        
        size = snaps.length;
        for( i=0; i<size; i++ ){
            bindts = (BindofMdiAndSnap)snaps[i];
            key = bindts.mdi.toString();
            viewName = ResourceCenter.NET_START_VIEW + key;
            bindsv = (BindOfSnapAndView)map.get( key );

            if( bindts.snap == null ){
                JOptionPane.showMessageDialog( pdiag, SanBootView.res.getString("FailoverWizardDialog.error.verisnull") +" : "+ key );
                this.allFinish = false;
                break;
            }

            if( bindts.snap instanceof SnapWrapper ){
                selSnap = ((SnapWrapper)bindts.snap).snap; 
                
                // 检查snap number是否合适
                checkSnap = diag.checkSnapNumber( bindts.mdi.toString(), selSnap.getSnap_root_id(),selSnap.getSnap_local_snapid() );
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
                                    JOptionPane.showMessageDialog(pdiag,
                                        SanBootView.res.getString("FailoverWizardDialog.error.crtView")+" : [ SnapID: "+selSnap.getCreateTimeStr()+" ]"
                                    );
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
                                JOptionPane.showMessageDialog(pdiag,
                                    SanBootView.res.getString("FailoverWizardDialog.error.crtView")+" : [ SnapID: "+selSnap.getCreateTimeStr()+" ]"
                                );
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
                            JOptionPane.showMessageDialog(pdiag,
                                SanBootView.res.getString("FailoverWizardDialog.error.crtView")+" : [ SnapID: "+selSnap.getCreateTimeStr()+" ]"
                            );
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
                        // 删除这个view
                        if( bindsv.snap instanceof Snapshot ){
                            tid = bindsv.view.getTargetID();
                            //if( delLunMap( tid ) && view.initor.mdb.delView( bindsv.view ) ){
                            if(  view.initor.mdb.delView( bindsv.view ) ){
                                map.remove( key );
                            }
                        }
                    }
                }
            }else if(  bindts.snap instanceof ViewWrapper ){ // bindts.snap instanceof ViewWrapper 
                selView = ( (ViewWrapper)bindts.snap ).view;
                
                // 检查snap number是否合适
                checkSnap = diag.checkSnapNumber( bindts.mdi.toString(), -1,-1 );
                if( !checkSnap ){
                    allFinish = false;
                    break;
                }
                
                if( bindts.isSel ){    
                    if( bindsv != null ){// hashmap中已经有了
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
                            if( view.initor.mdb.delView( bindsv.view ) ){
                                map.remove( key );
                            }
                        }
                    }
                }
            }else{ //  bindts.snap is MirrorDiskInfoWrapper
                mdi = ((MirrorDiskInfoWrapper)bindts.snap).mdi;
                
                // 检查snap number是否合适
                checkSnap = diag.checkSnapNumber( bindts.mdi.toString(), -1,-1 );
                if( !checkSnap ){
                    allFinish = false;
                    break;
                }
                
                if( bindts.isSel ){    
                    if( bindsv != null ){// hashmap中已经有了
                        if( bindsv.snap instanceof Snapshot ){
                            // 删除之前创建的这个view
                            //tid = bindsv.view.getTargetID();
                            //delLunMap( tid );
                            view.initor.mdb.delView( bindsv.view );// 不管结果
                        }
                        
                        map.remove( key );
                        bindsv = new BindOfSnapAndView();
                        bindsv.snap = mdi;
                        bindsv.view = null;
                        map.put( key, bindsv );
                    }else{
                        bindsv = new BindOfSnapAndView();
                        bindsv.snap = mdi;
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
        
        // 在其他机器上进行网络启动操作( 修改CaoXiTao的iboot部分 )
        String org_path = "";
        String new_path = "";
        if( allFinish && !isNetBootOnLocalHost ){
            tftpRootPath = view.initor.mdb.getTftpRootPath();
            if( tftpRootPath.equals("") ){
                JOptionPane.showMessageDialog(pdiag, 
                    SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.getTftpPath") 
                );
SanBootView.log.error( getClass().getName()," tftp path is null. ");
                allFinish = false;
            }
            
            org_path = tftpRootPath + "/remir/"+host.getSrc_agnt_id();
            new_path = tftpRootPath + newSimpleMac;
            
            if( allFinish ){
                isOk = view.initor.mdb.listDir( new_path );
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
                     JOptionPane.showMessageDialog(pdiag, 
                        SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo") 
                    );
SanBootView.log.error( getClass().getName(),"duplication of original pxe config file to new location is failed. ");
                    allFinish = false;
                }
            }
            
            if( allFinish ){
                isOk = view.initor.mdb.listDir( new_path );
                if( !isOk ){
                    JOptionPane.showMessageDialog(pdiag, 
                        SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                    );
SanBootView.log.error( getClass().getName(),"listing tftproot/<new_mac> is failed. ");                                
                    allFinish = false;
                }else{
                    ArrayList fList = view.initor.mdb.getFileList();
                    if( fList.size() == 0 ){
                        JOptionPane.showMessageDialog(pdiag, 
                            SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo") 
                        );
SanBootView.log.error( getClass().getName(),"listing tftproot/<original_mac> is failed. ");                                
                        allFinish = false;
                    }else{
                        // modify pxe.<kernel>.<lvmtype> file
                        ArrayList fList1 = getFileList1( fList );
                        if( fList1.size() == 0 ){
                            JOptionPane.showMessageDialog(pdiag, 
                                SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                            );
SanBootView.log.error( getClass().getName(),"there is not a pxe-xxx file in tftproot/<new_mac> dir.");                                     
                            allFinish = false;
                        }else{
                            // pxe
                            isOk = view.initor.mdb.getPXEInfoFromTFtp( new_path + "/" + ((UnixFileObj)fList1.get(0)).getName() );
                            if( !isOk ){
                                JOptionPane.showMessageDialog(pdiag, 
                                    SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.getPxeInfo") 
                                );
SanBootView.log.error( getClass().getName(),"getting original pxe config file is failed. ");
                                allFinish = false;
                            }else{
                                String _newPxeInfo = view.initor.mdb.replacePXEInfo( orgSimpleMac,newSimpleMac );
                                String newPxeInfo = view.initor.mdb.replaceTftpInfo( _newPxeInfo,tftp_ip );
                                isOk = saveInfo( tftpRootPath + "/pxelinux.cfg/" + new_pxe_mac, newPxeInfo );
                                if( !isOk ){
                                    JOptionPane.showMessageDialog(pdiag, 
                                        SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                                    );
SanBootView.log.error( getClass().getName(),"creating new pxe config file in tftproot/pxelinux.cfg/<new_mac>  is failed. ");                        
                                    allFinish = false;
                                }

                                if( allFinish ){
                                    isOk = saveInfo( new_path + "/" + ((UnixFileObj)fList1.get(0)).getName() , newPxeInfo );
                                }   
                            }

                            if( allFinish ){
                                if( host.isIA() ){
                                    // create link for IA
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
                                        JOptionPane.showMessageDialog(pdiag,
                                            SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                                        );
    SanBootView.log.error( getClass().getName(),"duplication of original elilo config file to new location is failed. ");
                                        allFinish = false;
                                    }else{
                                        ArrayList flist = this.getEliloFileList( fList );
                                        if( flist.size() == 0 ){
                                            JOptionPane.showMessageDialog(pdiag,
                                                SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                                            );
    SanBootView.log.error( getClass().getName(),"there is not a elilo-xxx file in tftproot/<original_mac> dir.");
                                            allFinish = false;
                                        }else{
                                            String eliloName = ((UnixFileObj)flist.get(0)).getName();
                                            isOk = view.initor.mdb.ln( tftpRootPath + "/" + hexIp + ".conf", newSimpleMac+"/" + eliloName );
                                            if( !isOk ){
                                                JOptionPane.showMessageDialog(pdiag,
                                                    SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                                                );
    SanBootView.log.error( getClass().getName(),"failed to create link to tftproot/<new_mac> dir.");
                                                allFinish = false;
                                            }
                                        }
                                    }
                                }
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
                bindts = (BindofMdiAndSnap)snaps[i];
                selTgt = bindts.mdi;
                key = bindts.mdi.toString();
                
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
                    viewid = -2;  //用于标识克隆盘
                }else{
                    // impossible to happen,but the following availables must be assigned some values.
                    tid = selTgt.mdi.getTargetID();
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
                                    host.getSrc_agnt_port(),
                                    host.getSrc_agnt_os(),
                                    simpleMAC,
                                    "",
                                    0
                                );
                        if( view.initor.mdb.addNBH( newDa ) ){
                            newDa.setDst_agent_id( view.initor.mdb.getNewId() );
                            view.initor.mdb.addNBHIntoCache( newDa );
                            da_id = newDa.getDst_agent_id();              
                        }else{
                            JOptionPane.showMessageDialog(pdiag, 
                                SanBootView.res.getString("FailoverWizardDialog.log.addNBH") +" "+
                                    SanBootView.res.getString("common.failed")
                            );
                            allFinish = false;
                        }
                    }
                }else{
                    da_id = da.getDst_agent_id();
                    if( !da.getIP().equals( start_ip ) ){
                        if( view.initor.mdb.modNBH( da_id,start_ip ) ){
                            da.setDst_agent_ip( start_ip );
                        }else{
                            JOptionPane.showMessageDialog(pdiag, 
                                SanBootView.res.getString("FailoverWizardDialog.log.modNBH") +" "+
                                    SanBootView.res.getString("common.failed")
                            );
                            allFinish = false;
                        }
                    }
                }
                
SanBootView.log.info( getClass().getName()," (add snap-usage)destAgent id: " + da_id );                        
                if( da_id != -1 ){
                    rootid = selTgt.mdi.getSnap_rootid();
                    su = view.initor.mdb.getSnapUsageOnSomething( da_id, rootid, key );
                    if( !bindts.isSel ){
                        tid = -1;
                        crttime ="";
                    }

                    if( su == null ){
                        su = new SnapUsage(
                                    -1,
                                    da_id, 
                                    selTgt.mdi.getSnap_rootid(),
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
                            JOptionPane.showMessageDialog(pdiag, 
                                SanBootView.res.getString("FailoverWizardDialog.log.addSU")+" "+
                                        SanBootView.res.getString("common.failed")
                            );
                            allFinish = false;
                        }
                    }else{
                        if( view.initor.mdb.modMSU( su.getUsage_id(),snapid,viewid,tid,crttime ) ){
                            su.setSnap_local_id( snapid );
                            su.setSnap_view_local_id( viewid );
                            su.setSnapTid( tid );
                            su.setCrtTime( crttime );
                        }else{
                            JOptionPane.showMessageDialog(pdiag, 
                               SanBootView.res.getString("FailoverWizardDialog.log.modSU")+" "+
                                        SanBootView.res.getString("common.failed")
                            );
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
                                JOptionPane.showMessageDialog(pdiag,
                                    SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                                          SanBootView.res.getString("common.failed")
                                );
                                allFinish = false;
                            }else{
                                if( !isNetBootOnLocalHost ){                                 
                                    view.initor.mdb.delFile( ResourceCenter.CLT_IP_CONF + "/"+ResourceCenter.PREFIX_DST_AGNT + da_id + ResourceCenter.CONF_3RD_DHCP );
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(pdiag,
                                SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                                      SanBootView.res.getString("common.failed")
                            );
                            allFinish = false;
                        }
                    }else{
                        JOptionPane.showMessageDialog(pdiag,
                            SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                                  SanBootView.res.getString("common.failed")
                        );
                        allFinish = false;
                    }
                }else{
                    JOptionPane.showMessageDialog(pdiag,
                        SanBootView.res.getString("InitNWinHostWizardDialog.log.saveDhcpConf")+" "+
                              SanBootView.res.getString("common.failed")
                    );
                    allFinish = false;
                }
            }else{
                // save the 3rd dhcp setup info. 
                String conf="";
                if( isNetBootOnLocalHost ){
                    conf = ResourceCenter.CLT_IP_CONF + "/" + host.getSrc_agnt_id() + ResourceCenter.CONF_3RD_DHCP;
                }else{
                    conf = ResourceCenter.CLT_IP_CONF + "/"+ResourceCenter.PREFIX_DST_AGNT + da_id + ResourceCenter.CONF_3RD_DHCP;
                }
                saveInfo( conf , "ip:"+dhcpInfo.ip_3rd +"  nextsrv:"+ dhcpInfo.nextServer_3rd );               
            }
        }
        
        if( allFinish && !isNetBootOnLocalHost ){
            // 修改fstab,将来实现。（这要求目前只能恢复全部文件系统，不能只恢复部分文件系统） 2008/2/27

            // 修改iboot Info(生成与曹锡韬关于lvm方面的接口)
            if( tftpRootPath.equals("") ){
                tftpRootPath = view.initor.mdb.getTftpRootPath();
                if( tftpRootPath.equals("") ){
                    JOptionPane.showMessageDialog(pdiag, 
                      SanBootView.res.getString("InitNWinHostWizardDialog.log.setBootInfo") +
                            SanBootView.res.getString("common.failed")
                    );
                    allFinish = false;
                }
            }
            
            if( allFinish ){
                isOk = view.initor.mdb.listDir( new_path );
                if( !isOk ){
                    JOptionPane.showMessageDialog(pdiag, 
                        SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                    );
SanBootView.log.error( getClass().getName(),"listing tftproot/<new_mac> is failed. ");                                
                    allFinish = false;
                }else{
                    ArrayList fList = view.initor.mdb.getFileList();
                    if( fList.size() == 0 ){
                        JOptionPane.showMessageDialog(pdiag, 
                            SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo") 
                        );
SanBootView.log.error( getClass().getName(),"listing tftproot/<new_mac> is failed. ");        
                        allFinish = false;
                    }else{
                        // modify iBootInfo.<kernel>.<lvmtype> file
                        ArrayList fList2 = getFileList( fList );
                        if( fList2.size() == 0 ){
                            JOptionPane.showMessageDialog(pdiag, 
                                SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                            );
SanBootView.log.error( getClass().getName(),"there is not a iBootInfo-xxx file in tftproot/<new_mac> dir.");                                             
                            allFinish = false;
                        }else{
                            isOk = view.initor.mdb.getIBootInfoFromTFtp( new_path + "/" + ((UnixFileObj)fList2.get(0)).getName() );
                            if( !isOk ){
                                JOptionPane.showMessageDialog(pdiag, 
                                    SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                                );                                          
                                allFinish = false;
                            }else{
                                StringBuffer buf = view.initor.mdb.getIBootInfoContents();
                                IBootInfo ibootObj = new IBootInfo();
                                try{
                                    ibootObj.parserConf( buf );
                                    ibootObj.replacePSNOnHeader( targetSrvName );
                                    ibootObj.replaceTipOnHeader( tftp_ip );
                                    replaceIBootConfContentsForDestAgent( ibootObj,host.getSrc_agnt_id(),da_id );
                                    
                                    isOk = saveInfo( new_path + "/" +((UnixFileObj)fList2.get(0)).getName(), ibootObj.prtMe() );
                                    if( !isOk ){
                                        JOptionPane.showMessageDialog(pdiag, 
                                            SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                                        );
SanBootView.log.error( getClass().getName(),"creating new iBootInfo config file in tftproot/<new_mac> dir is failed");                                           
                                        allFinish = false;
                                    }
                                }catch( Exception ex ){
                                    Tool.prtExceptionMsg( ex, "CrtUnixViewForSrcAgent" );
                                    JOptionPane.showMessageDialog(pdiag, 
                                        SanBootView.res.getString("IbootForLinuxWizardDialog.errMsg.crtPxeInfo")
                                    );
SanBootView.log.error( getClass().getName(),"creating new iBootInfo config file in tftproot/<new_mac> dir is failed");                                           
                                    allFinish = false;
                                }
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
    
    private void replacePvs( IBootInfo ibootInfo,String lv_name,int pv_tid ){
        IBVg vg = ibootInfo.getVgOnLVName( lv_name );
        if( vg != null ){
            vg.setPvs( pv_tid +"" );
        }
    }
    
    private void replaceIBootConfContentsForDestAgent( IBootInfo ibootInfo, int clntID,int da_id ){
        int tid;
        String tmp = "";
        boolean isFirst = true;
        ArrayList<SnapUsage> suList;
        MirrorDiskInfo mdi;
        
        ArrayList mdiList = view.initor.mdb.getMDIFromCacheOnSrcAgntID( clntID );
        int size = mdiList.size();
        for( int i=0; i<size; i++ ){
            mdi = (MirrorDiskInfo)mdiList.get( i );
//System.out.println( " mdi name: " + mdi.getSrc_snap_name() );
            suList = view.initor.mdb.getSnapUsageOnSomething( da_id, mdi.getSnap_rootid() );
            tid = -1;
            if( suList.size() > 0 ){
                tid = suList.get(0).getSnapTid();
            }
            
            if( tid == -1 ) continue;
            
            // add lunmap on iboot_dhcp_ip for targets(Don't care result) 
            addLunMap( tid );
            
            if( isFirst ){
                tmp = tid + "";
                isFirst = false;
            }else{
                tmp += ";" + tid; 
            }
            
            replacePvs( ibootInfo, mdi.getSrc_snap_name(), tid );            
        }
        
        ibootInfo.replaceTgtList( tmp );    
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
        
        ArrayList netBootedHostList = getNetbootedHostOnSrcAgnt( host.getSrc_agnt_id() );
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
    
    public ArrayList getNetbootedHostOnSrcAgnt( int srcAgntID ){
        boolean isok = view.initor.mdb.updateMDI();
        if( !isok ){
            return new ArrayList();
        }else{
            ArrayList list = view.initor.mdb.getMDIFromCacheOnSrcAgntID( srcAgntID );
            int size = list.size();
            ArrayList rootidList = new ArrayList( size );
            for( int i=0; i<size; i++ ){
                MirrorDiskInfo mdi = (MirrorDiskInfo)list.get(i); 
                rootidList.add( new Integer( mdi.getSnap_rootid() ) );
            }
            
            ArrayList ret = view.getNetbootedHostOnHost( rootidList );
            return ret;
        }
    }
}
