/*
 * StartorStopMjThread.java
 *
 * Created on 2008/6/13, AM�11:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.service.BasicStartorStopMjThread;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author zjj
 */
public class StartorStopMjThread extends BasicStartorStopMjThread{
    /** Creates a new instance of StartorStopMjThread */
    public StartorStopMjThread(
        SanBootView view,
        MirrorJob mj,
        int action,
        Object host,
        int src_rootid,
        String vol_name,
        String vol_mp,
        String linuxOrgBootMac,
        boolean quickStart,
        int ptype
    ) {
        super( view,mj,action,host,src_rootid,vol_name,vol_mp,linuxOrgBootMac,quickStart,true,ptype );
    }

    /** Creates a new instance of StartorStopMjThread */
    public StartorStopMjThread(
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
    ) {
        super( view,mj,action,host,src_rootid,vol_name,vol_mp,linuxOrgBootMac,quickStart,giveTipToUser,ptype );
    }
     
    @Override public boolean realRun(){
        boolean ok;
        String src_uws_psn,uuid;
        
        if( action == 0 ){ // start mirror job
            // 操作目的端UWS服务器上的数据库
            String tgtSrvName = view.initor.mdb.getHostName();
            if( tgtSrvName.equals("") ){
SanBootView.log.error( getClass().getName()," Can't get source_end target server host name(PSN)." );
                errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                isOk = false;
            }else{
                if( vol_name.equals("") ){
SanBootView.log.debug(getClass().getName(), "we will transfer a free volume.");
                    // 传输的是一个free vol,查找该free vol的名字
                    ok = view.initor.mdb.queryVSnapDB(
                        "select * from " + ResourceCenter.VSnap_DB +" where (" +BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_DISK +" or "+BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_AppMirr+" or "+BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_UCSDISK+") "+" and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+src_rootid+";"
                    );
                    if( ok ){
                        ArrayList ll = view.initor.mdb.getAllQueryResult();
                        if( ll.size() == 0 ){
                            errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                            ok = false;
                        }else{
                            BasicVDisk bdisk = (BasicVDisk)ll.get( 0 );
                            vol_name = bdisk.getSnap_name();
                        }
                    }else{
                        errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                    }
                }else{
                    ok = true;
                }
                
                if( ok ){
                    src_uws_psn = tgtSrvName.toUpperCase();
                    isOk = view.initor.mdb.queryUWSrvOnDestUWSrv( dest_uws_ip, dest_uws_port, src_uws_psn );
                    if( !isOk ){
                        errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                    }else{
                        UWSrvNode src_uws = view.initor.mdb.getUWSrvFromVecOnIP ( view.initor.getTxIP(dest_uws_ip) ); 
                        if( src_uws == null ){
                            src_uws = new UWSrvNode( -1, view.initor.getTxIP(dest_uws_ip), UWSrvNode.MEDIA_SERVER_PORT, src_uws_psn );
                            isOk = view.initor.mdb.addUWSrv( dest_uws_ip, dest_uws_port, dest_poolid,dest_pool_passwd,src_uws );
                            if( !isOk ){
                                errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                            }else{
                                src_uws.setUws_id( view.initor.mdb.getNewId() );
                            }
                        }
                        
                        if( isOk ){
                            if( host != null ){
                                uuid = ( (host instanceof BootHost )? ((BootHost)host).getUUID() : ((SourceAgent)host).getSrc_agnt_desc() );
                                isOk = view.initor.mdb.querySrcAgntOnDestUWSrv( dest_uws_ip,dest_uws_port, uuid );
                                if( !isOk ){
                                    errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                                }else{
SanBootView.log.info( getClass().getName(), "get src-agent according uuid: "+ uuid + " and uws server id: " + src_uws.getUws_id() );
                                    SourceAgent tmpSrcAgnt = view.initor.mdb.isThisSrcAgntOnDestUWSrv( uuid,src_uws.getUws_id() );
                                    if( tmpSrcAgnt == null ){
                                        if( host instanceof BootHost ){
                                            BootHost bh = (BootHost)host;
                                            isOk = crtSrcAgent( bh.getIP(), bh.getOS(), bh.getMachine(),uuid, src_uws.getUws_id(),bh.getPort(),bh.getClntPort1(),bh.getBootMode(),bh.getProtectType() );
                                        }else{
                                            SourceAgent sa = (SourceAgent)host;
                                            isOk = crtSrcAgent( sa.getSrc_agnt_ip(), sa.getSrc_agnt_os(),sa.getMachine(),uuid, src_uws.getUws_id(),sa.getSrc_agnt_port(),sa.getSrc_agnt_port1(),sa.getSrc_agnt_boot_mode(),sa.getProtectType() );
                                        }
                                    }else{
                                        srcAgnt = tmpSrcAgnt;                                         
                                    }
                                    if( isOk ){
                                        isOk = queryMDI( dest_rootid );
                                        if( isOk ){
                                            MirrorDiskInfo mdi = view.initor.mdb.isExistMdiOnDestUWSrv( dest_rootid );
                                            if( mdi == null ){
                                                isOk = crtMDI( dest_rootid, srcAgnt.getSrc_agnt_id(), src_rootid, vol_name,vol_mp );
                                            }else{
                                                if( mdi.getSrc_agnt_id() != srcAgnt.getSrc_agnt_id() ){
                                                    SourceAgent aSa = view.initor.mdb.getSrcAgntFromVectorOnID( mdi.getSrc_agnt_id() );
                                                    if( ( aSa != null ) && aSa.isRollbackedHost() ){
                                                        isOk = false;
                                                        errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail1");
                                                    }else{
                                                        isOk = modMDI( dest_rootid, srcAgnt.getSrc_agnt_id() );
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }else{
                                // 查询代表源端UWS的srcagnt,该对象维系了所有属于源端UWS上的空闲卷
                                isOk = view.initor.mdb.queryUWSSrcAgntOnDestUWSrv( dest_uws_ip,dest_uws_port, src_uws_psn );
                                if( !isOk ){
                                    errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                                }else{
                                    SourceAgent tmpSrcAgnt = view.initor.mdb.isThisUWSSrcAgntOnDestUWSrv1( view.initor.getTxIP(dest_uws_ip) );
                                    if( tmpSrcAgnt == null ){
                                        // 代表源端UWS的srcagnt的uuid为空,src_agent_os字段表示psn
                                        isOk = crtSrcAgent( view.initor.getTxIP(dest_uws_ip), src_uws_psn, "","", src_uws.getUws_id(), -1,-1,0, 1 );
                                    }else{
                                        srcAgnt = tmpSrcAgnt;
                                    }
                                    if( isOk ){
                                        isOk = queryMDI( dest_rootid );
                                        if( isOk ){
                                            MirrorDiskInfo mdi = view.initor.mdb.isExistMdiOnDestUWSrv( dest_rootid );
                                            if( mdi == null ){
                                                isOk = crtMDI( dest_rootid, srcAgnt.getSrc_agnt_id(), src_rootid, vol_name,vol_mp );
                                            }else{
                                                if( mdi.getSrc_agnt_id() != srcAgnt.getSrc_agnt_id() ){
                                                    SourceAgent aSa = view.initor.mdb.getSrcAgntFromVectorOnID( mdi.getSrc_agnt_id() );
                                                    if( ( aSa != null ) && aSa.isRollbackedHost() ){
                                                        isOk = false;
                                                        errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail1");
                                                    }else{
                                                        isOk = modMDI( dest_rootid, srcAgnt.getSrc_agnt_id() );
                                                    }
                                                }
                                             }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    isOk = false;
                }
            }
            
            if( isOk ){
                // 将网络启动使用到的配置信息也传到dest uws上
                if( host != null && !quickStart ){
                    boolean isWin = ( ( host instanceof BootHost )? ((BootHost)host).isWinHost(): ((SourceAgent)host).isWinHost() );     
                    int host_id = ( ( host instanceof BootHost )? ((BootHost)host).getID(): ((SourceAgent)host).getSrc_agnt_id() );                        
                    if( isWin ){
                        if( host instanceof BootHost ){
                            view.initor.mdb.sendWinNetBootInfoToDestUWS( host_id, dest_uws_ip,dest_uws_port, dest_poolid, dest_pool_passwd, srcAgnt.getSrc_agnt_id() );
                        }else{
                            view.initor.mdb.sendWinNetBootInfoToDestUWS1( host_id, dest_uws_ip,dest_uws_port, dest_poolid, dest_pool_passwd, srcAgnt.getSrc_agnt_id() );
                        }
                    }else{
System.out.println(" linux boot mac: " + linuxOrgBootMac );    
                        String orgBootMacForUnix = DhcpClientInfo.getMacStr( this.linuxOrgBootMac );
                        String orgSimpleMac = NetCard.getSimpleMac1( orgBootMacForUnix ).toLowerCase();
                        
                        // 1. transfer netboot configuration from src_end uws to dest_end uws                      
                        String src_uws_tftproot = view.initor.mdb.getTftpRootPath();
                        if( !( host instanceof BootHost ) ){
                            orgSimpleMac = "remir/" + host_id;
                        }
                        
                        String dest_uws_tftproot = view.initor.mdb.getTftpRootPath( 1,dest_poolid,dest_pool_passwd,dest_uws_ip,dest_uws_port );                        
                        if( !dest_uws_tftproot.equals("") && !src_uws_tftproot.equals("") ){
                            this.isOkForSyncManageInfo = transferManageInfo( src_uws_tftproot, dest_uws_tftproot, orgSimpleMac,srcAgnt.getSrc_agnt_id(),false  );
SanBootView.log.info(getClass().getName(), "transfer managed info is " + (isOkForSyncManageInfo?"ok":"failed") +" for mj: " + mj.getMj_job_name() );
                        }else{
                            if( dest_uws_tftproot.equals("") ){
SanBootView.log.error( getClass().getName(),"Can't get tftp root path on [ " + dest_uws_ip +"/" + dest_uws_port +" ]" ); 
                            }else{
SanBootView.log.error( getClass().getName(),"Can't get tftp root path on src_end swu server." );                                 
                            }
                        }
                        
                        // 2. transfer other configuration generated by init-op
                        if( host instanceof BootHost ){
                            view.initor.mdb.sendLinuxNetBootInfoToDestUWS( host_id, dest_uws_ip,dest_uws_port, dest_poolid, dest_pool_passwd, srcAgnt.getSrc_agnt_id() );
                        }else{
                            view.initor.mdb.sendLinuxNetBootInfoToDestUWS1( host_id, dest_uws_ip,dest_uws_port, dest_poolid, dest_pool_passwd, srcAgnt.getSrc_agnt_id() );                            
                        }
                    }
                }
                
                ok = view.initor.mdb.checkMg( mj.getMj_mg_id() );
                if( ok ){
                    isOk = view.initor.mdb.startMj( mj.getMj_id() );
                    if( isOk ){
                        if( this.giveTipToUser ){
                            if( this.isOkForSyncManageInfo ){
                                JOptionPane.showMessageDialog(pdiag,
                                    SanBootView.res.getString("MenuAndBtnCenter.error.startMjOk")
                                );
                            }else{
                                JOptionPane.showMessageDialog(pdiag,
                                    SanBootView.res.getString("MenuAndBtnCenter.error.startMjWarning")
                                );
                            }
                        }
                    }else{
                        errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_START_MJ )+" : "+view.initor.mdb.getErrorMessage();
                    }
                }else{
                    isOk = view.initor.mdb.startMg( mj.getMj_mg_id() );
                    if( isOk ){
                        isOk = view.initor.mdb.startMj( mj.getMj_id() );
                        if( isOk ){
                            if( this.giveTipToUser ){
                                if( this.isOkForSyncManageInfo ){
                                    JOptionPane.showMessageDialog(pdiag,
                                        SanBootView.res.getString("MenuAndBtnCenter.error.startMjOk")
                                    );
                                }else{
                                    JOptionPane.showMessageDialog(pdiag,
                                        SanBootView.res.getString("MenuAndBtnCenter.error.startMjWarning")
                                    );
                                }
                            }
                        }else{
                            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_START_MJ )+" : "+view.initor.mdb.getErrorMessage();
                        }
                    }else{
                        errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_START_MJ ) + " : " + view.initor.mdb.getErrorMessage();
                    }
                }
            }
        }else{ // stop mirror job
            ok = view.initor.mdb.checkMg( mj.getMj_mg_id() );
            if( !ok ){
                isOk = view.initor.mdb.stopMj( mj.getMj_id() );
                if( isOk ){
                    if( this.giveTipToUser ){
                        JOptionPane.showMessageDialog(pdiag,
                            SanBootView.res.getString("MenuAndBtnCenter.error.stopMjOk")
                        );
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_STOP_MJ )+" : "+view.initor.mdb.getErrorMessage();
                }
            }else{
                VolumeMap aVolMap = view.initor.mdb.getVolMapFromVecOnMgID( mj.getMj_mg_id() );
                if( aVolMap == null ){
                    // 如果是mtpp的mj，则将mg也停掉
                    isOk = view.initor.mdb.stopMg( mj.getMj_mg_id() );
                }else{
                    isOk = true;
                }
                if( isOk ){
                    isOk = view.initor.mdb.stopMj( mj.getMj_id() );
                    if( isOk ){
                        if( this.giveTipToUser ){
                            JOptionPane.showMessageDialog(pdiag,
                                SanBootView.res.getString("MenuAndBtnCenter.error.stopMjOk")
                            );
                        }
                    }else{
                        errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_STOP_MJ )+" : "+view.initor.mdb.getErrorMessage();
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_STOP_MJ )+" : "+view.initor.mdb.getErrorMessage();
                }
            }
        }
        
        return isOk;
    }
}