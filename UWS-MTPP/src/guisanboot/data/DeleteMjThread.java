/*
 * DeleteMjThread.java
 *
 * Created on 2008/6/13,�AM 11:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;

/**
 *
 * @author zjj
 */
public class DeleteMjThread extends BasicGetSomethingThread{
    private MirrorJob mj;
    private int mj_type;
    private Object host;
    private int src_rootid;
    private String vol_name;
    private String dest_uws_ip;
    private int dest_uws_port;
    private int dest_rootid;
    private int dest_poolid;
    private String dest_pool_passwd;
    private int ptype;
    private boolean delHostInfo = true;
    private boolean isLocalType = true;
    
    /** Creates a new instance of DeleteMjThread */
    public DeleteMjThread(
        SanBootView view,
        MirrorJob mj,
        int action,
        Object host,
        int src_rootid,
        String vol_name,
        String vol_mp,
        boolean delHostInfo,
        int ptype
    ) {
        super( view );
        
        this.mj = mj;
        this.host = host;
        this.src_rootid = src_rootid;
        this.vol_name = vol_name;
        this.mj_type = mj.getMj_job_type();
        this.dest_uws_ip = mj.getMj_dest_ip();
        this.dest_uws_port = mj.getMj_dest_port();
        this.dest_rootid = mj.getMj_dest_root_id();
        this.dest_poolid = mj.getMj_dest_pool();
        this.dest_pool_passwd = mj.getMj_dest_pool_passwd();
        this.delHostInfo = delHostInfo;
        this.ptype = ptype;
    }
    
    public boolean realRun(){ 
        boolean ok;
        String psn,uuid;
        MirrorDiskInfo mdi = null;
        SourceAgent tmpSrcAgnt;

        isLocalType = mj.isLocalMjType();

        view.initor.mdb.targetSrvName = "";
        String tgtSrvName = view.initor.mdb.getHostName();
        if( tgtSrvName.equals("") ){
SanBootView.log.error( getClass().getName()," Can't get source_end target server host name(PSN)." );
            errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
            return false;
        }
        psn = tgtSrvName.toUpperCase();

        if( vol_name.equals("") ){ // 该卷是个空闲卷
            // 查找该free vol的名字
            ok = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where (" +BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_DISK +" or "+BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_AppMirr+" or "+BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_UCSDISK+") "+ "and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+src_rootid+";"
            );
            if( ok ){
                ArrayList ll = view.initor.mdb.getAllQueryResult();
                if( ll.size() == 0 ){
                    errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                    ok = false;
                }else{
                    BasicVDisk bdisk = (BasicVDisk)ll.get(0);
                    vol_name = bdisk.getSnap_name();
                }
            }else{
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
            }
        }else{
            ok = true;
        }

        if( !ok ) return false;

        if( !isLocalType ){
            isOk = view.initor.mdb.queryUWSrvOnDestUWSrv( dest_uws_ip, dest_uws_port, psn );
            if( !isOk ){
                errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
            }else{
                UWSrvNode src_uws = view.initor.mdb.isExistUWSrvOnDestUWSrv ( psn );
                if( src_uws == null ){
                    src_uws = new UWSrvNode( -1, view.initor.getTxIP(dest_uws_ip), UWSrvNode.MEDIA_SERVER_PORT, psn );
                    isOk = view.initor.mdb.addUWSrv( dest_uws_ip, dest_uws_port, dest_poolid,dest_pool_passwd,src_uws );
                    if( !isOk ){
                        errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                    }else{
                        src_uws.setUws_id( view.initor.mdb.getNewId() );
                    }
                }

                if( isOk ){
                    if( host != null ){
                        uuid =( (host instanceof BootHost) ? ((BootHost)host).getUUID() : ((SourceAgent)host).getSrc_agnt_desc() );
                        isOk = view.initor.mdb.querySrcAgntOnDestUWSrv( dest_uws_ip,dest_uws_port, uuid );
                        if( !isOk ){
                            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                        }else{
                            srcAgnt = view.initor.mdb.isThisSrcAgntOnDestUWSrv( uuid,src_uws.getUws_id() );
                            if( srcAgnt != null ){
                                // delete srcAgnt( 不删除代表真正主机的sourceAgent)
                                //isOk = delSrcAgent( srcAgnt.getSrc_agnt_id() );
                            }

                            if( isOk ){
                                isOk = queryMDI( dest_rootid );
                                if( isOk ){
                                    mdi = view.initor.mdb.isExistMdiOnDestUWSrv( dest_rootid );
                                }
                            }
                        }

                        if( isOk && delHostInfo ){
                            // 查询代表源端UWS的srcagnt,该对象维系了所有属于源端UWS上的空闲卷
                            isOk = view.initor.mdb.queryUWSSrcAgntOnDestUWSrv( dest_uws_ip,dest_uws_port, psn );
                            if( !isOk ){
                                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
                            }else{
                                // 删除MJ后,diskinfo所代表的盘在目的端UWS上就是一个空闲卷了
                                tmpSrcAgnt = view.initor.mdb.isThisUWSSrcAgntOnDestUWSrv( psn );
                                if( tmpSrcAgnt == null ){
                                    // 代表源端UWS的srcagnt的uuid为空,src_agent_os字段表示psn
                                    isOk = crtSrcAgent( view.initor.getTxIP(dest_uws_ip), psn, "","", src_uws.getUws_id(), -1 );
                                }else{
                                    srcAgnt = tmpSrcAgnt;
                                }

                                if( isOk ){
                                    if( mdi != null ){
                                        isOk = modMDI( dest_rootid, srcAgnt.getSrc_agnt_id(), "" );
                                    }else{
                                        isOk = crtMDI( dest_rootid, srcAgnt.getSrc_agnt_id(), src_rootid, vol_name,"" );
                                    }
                                }
                            }
                        }
                    }else{
                        // do nothing  for real free volume
                    }
                }
            }
        }
        
        if( isOk ){
            isOk = view.initor.mdb.delMj( mj.getMj_id() ); 
            if( !isOk ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_MJ ) + " : " + view.initor.mdb.getErrorMessage(); 
            }
        }
        
        return isOk;
    }
    
    SourceAgent srcAgnt;
    private boolean crtSrcAgent( String parm1,String parm2,String parm3,String uuid,int src_uws_id,int src_agnt_port ){
        srcAgnt = new SourceAgent( -1, parm1, parm2,parm3, uuid, src_uws_id,src_agnt_port,0,1 );
        boolean aIsOk = view.initor.mdb.addSrcAgnt( dest_uws_ip,dest_uws_port,dest_poolid,dest_pool_passwd, srcAgnt );
        if( !aIsOk ){
            errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }else{
            srcAgnt.setSrc_agnt_id( view.initor.mdb.getNewId() );
        }
        return aIsOk;
    }
    
    private boolean crtMDI( int dest_root_id,int srcAgntID,int src_rootid,String vol_name,String vol_mp ){
        MirrorDiskInfo mdi = new MirrorDiskInfo( dest_root_id, srcAgntID, src_rootid, vol_name, vol_mp, 0,this.mj_type,ptype );
        boolean aIsOk = view.initor.mdb.addMDI( dest_uws_ip,dest_uws_port, dest_poolid,dest_pool_passwd,mdi );
        if( !aIsOk ){
            errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }
        return aIsOk;
    }
    
    private boolean modMDI( int dest_rootid,int srcAgntID,String vol_mp ){
        boolean aIsOk = view.initor.mdb.modMDI( dest_uws_ip,dest_uws_port,dest_poolid,dest_pool_passwd,dest_rootid, srcAgntID,vol_mp );
        if( !aIsOk ){
            errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }
        return aIsOk;
    }
    
    private boolean queryMDI( int dest_root_id ){
        boolean aIsOk = view.initor.mdb.queryMdiOnDestUWSrv( dest_uws_ip,dest_uws_port, dest_root_id );
        if( !aIsOk ){
            errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.prepareJobFail");
        }
        return aIsOk;
    }
    
    public boolean isOK(){
        return isOk;
    }
}
