/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror;

import guisanboot.ui.DelHost;
import guisanboot.ui.SanBootView;
import guisanboot.data.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;

/**
 *
 * @author Administrator
 */
public class DelSrcAgntThread extends DelHost {
    public DelSrcAgntThread( SanBootView view,Object host,boolean isConnect ){
        super( view,host,isConnect,null );
    }

    // 删除回滚主机的宗旨是：只保留正常的快照和盘（普通镜像卷和快照），一切不适合再次初始化使用的
    // 东西统统删除（无限增量卷、及其快照、cloneDisk和copy job）。
    @Override public boolean realRun(){
        int i,size,j,size1;
        MirrorDiskInfo mdi;
        ArrayList cjList;
        
        BrowserTreeNode selHostNode = getTreeNode();
        BrowserTreeNode chiefHostNode = (BrowserTreeNode)selHostNode.getParent();
        
        // 0. 删除netboot host( destagent )
        if( !delNetBoot( selHostNode ) ){ return false; }

        // 检查有无还在传输数据的copy job
        if( !view.initor.mdb.updateMj() ){
            errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_GET_MJ ) + " : " + view.initor.mdb.getErrorMessage();
            return false;
        }
        ArrayList mdiList = view.initor.mdb.getMDIFromCacheOnSrcAgntID( this.getHostID() );
        size = mdiList.size();
        for( i=0; i<size; i++ ){
            mdi = (MirrorDiskInfo)mdiList.get( i );
            if( mdi.isUIM_Vol() ){
                // delete copy job on uim-vol
                cjList = checkCopyJob( mdi );
                if( cjList == null ){
                    return false;
                }
            }
        }

        // 1. 删除 mirrordiskinfo
        size = mdiList.size();
        for( i=0; i<size; i++ ){
            mdi = (MirrorDiskInfo)mdiList.get( i );

            if( mdi.isUIM_Vol() ){
                // delete copy job on uim-vol
                cjList = checkCopyJob( mdi );
                size1 = cjList.size();
                for(  j=0; j<size1; j++ ){
                    MirrorJob mj = (MirrorJob)cjList.get(j);
                    if( view.initor.mdb.delMj( mj.getMj_id() ) ){
                        view.initor.mdb.removeMJFromVector( mj );
                    }else{
                        errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_DEL_MJ ) + " : " + view.initor.mdb.getErrorMessage();
                        return false;
                    }
                }
            }

            // remote copy job vol是将无限增量快照通过copy job传到第三方
            // UWS上而产生的一种卷类型
            if( mdi.isUIM_Vol() || mdi.isRemoteCjVol() ){
                // del uim-vol and its clonedisk
                if( !this.delCloneDisk( mdi.getSnap_rootid() ) ){
                    errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_DEL_MIRROR_DISK ) + " : " + view.initor.mdb.getErrorMessage();
                    return false;
                }else{
                    if( !delUIMVol( mdi ) ){
                        errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_DEL_MIRROR_DISK ) + " : " + view.initor.mdb.getErrorMessage();
                        return false;
                    }
                }
            }

            isOk = view.initor.mdb.delMDI( mdi.getSnap_rootid() ); 
            if( !isOk ){
                errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_DEL_MIRROR_DISK ) + " : " + view.initor.mdb.getErrorMessage();
                return false;
            }else{
                view.initor.mdb.removeMDIFromCache( mdi );                    
                // 删除disk(MirrorDiskInfo)上的lunmap,don't care result
                delLunMapOnMDI( mdi.getTargetID() );
                // 删除disk(MirrorDiskInfo)上所有快照的view的lunmap,don't care result
                delLunMapOnView( mdi.getSnap_rootid() );
            }
        }
        
        // 2. 删除config,don't care result
        delConfigFile(); 
        
        // 3. 删除host from tree
        return realDelHost( chiefHostNode, selHostNode ); 
    }
    
    private boolean realDelHost( BrowserTreeNode chiefHostNode,BrowserTreeNode selHostNode ){
        boolean isOK = view.initor.mdb.delSrcAgnt("",0,0,"", this.getHostID() ); 
        if( isOK ){
            SourceAgent host = (SourceAgent)selHost;
            view.initor.mdb.removeSrcAgntFromCache( host );
            view.removeNodeFromTree( chiefHostNode, selHostNode );
            
            // 显示点击 chiefHostNode 后的右边tabpane中的内容
            view.setCurNode( chiefHostNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefRollbackHost peOnChiefHost = new ProcessEventOnChiefRollbackHost( view );
            TreePath path = new TreePath( chiefHostNode.getPath() );
            peOnChiefHost.processTreeSelection( path );
            peOnChiefHost.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }else{
            errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_DEL_SRCAGNT )+":"+
                    view.initor.mdb.getErrorMessage();
        }
        
        return isOk;
    }
    
    private boolean delLunMapOnMDI( int tid ){
        LunMap lm;

        boolean isOK = view.initor.mdb.getLunMapForTID( tid );
        if( isOK ){
            Vector lmList = view.initor.mdb.getAllLunMapForTid();
            int size1 = lmList.size();
            for( int j=0;j<size1;j++ ){
                lm = (LunMap)lmList.elementAt(j);
                isOK = view.initor.mdb.delLunMap( tid, lm.getIP(),lm.getMask(), lm.getAccessMode() );
                if( !isOK ){
                    errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_DEL_LUNMAP) + " "+ tid +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+
                              view.initor.mdb.getErrorMessage(); 
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_GET_LUNMAP) + " [ tid:" + tid + " ] "+
                      view.initor.mdb.getErrorMessage(); 
            return false;
        }

        return true;
    }
    
    private boolean delLunMapOnView( int rootid ){
        int k,j,size2,size1;
        Vector lmList;
        LunMap lm;

        boolean isOK = view.initor.mdb.getAllView( rootid );
        if( isOK ){
            ArrayList viewList = view.initor.mdb.getViewList();
            size2 = viewList.size();
            for( k=0; k<size2; k++ ){
                View viewObj = (View)viewList.get( k );
                isOK = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() ); 
                if( isOK ){
                    lmList = view.initor.mdb.getAllLunMapForTid();
                    size1 = lmList.size();
                    for( j=0;j<size1;j++ ){
                        lm = (LunMap)lmList.elementAt(j);
                        isOK = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                        if( !isOK ){
                            errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_DEL_LUNMAP) + " "+ viewObj.getSnap_target_id() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+
                                    view.initor.mdb.getErrorMessage();
                            return false;
                        }
                    }
                }else{
                    errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_GET_LUNMAP) + " [ " +viewObj.getSnap_target_id() + " ] "+
                            view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_GET_VIEW) + " "+
                    view.initor.mdb.getErrorMessage();
            return false;
        }

        return true;
    }
    
    private boolean delConfigFile(){
        int host_id = this.getHostID();
        String[] commonConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_IP,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_NETBOOT_DISK,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_OLDDISK,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_RSTMAP,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_3RD_DHCP
        };
        String[] winConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_NORMAL_DISK,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_SERVICE,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_SERVICE_RELATED_WITH_VOLUME
        };
        String[] unixConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id +ResourceCenter.CONF_MP,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id +ResourceCenter.CONF_LVMINFO,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_OS_LOADER,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_FSTAB,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_NETWORK_CMD,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_KILLALL_CMD,
            ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host_id + ResourceCenter.CONF_HALT_CMD                
        };
        
        // 删除公共的配置
        for( int i=0; i<commonConf.length; i++ ){
            view.initor.mdb.delFile( commonConf[i] );           
        }

        if( this.isWin() ){
            // 删除windows配置
            for( int i=0; i<winConf.length; i++ ){
                view.initor.mdb.delFile( winConf[i] );
            }
        }else{
            for( int i=0; i<unixConf.length; i++ ){
                view.initor.mdb.delFile( unixConf[i] );                
            }
            
            // del remir ( to be continued.....)
        }
        
        return true;
    }

    private boolean delCloneDisk( int rootid ){
        boolean aIsOk = true, destroyOk;
        CloneDisk cd;

        aIsOk = view.initor.mdb.getCloneDiskList( this.getHostID(),CloneDisk.IS_SRCAGNT,rootid );
        if( aIsOk ){
            ArrayList list = view.initor.mdb.getCloneDiskList();
            int size = list.size();
            for( int i=0; i<size; i++ ){
                cd = (CloneDisk)list.get(i);

                this.delLunMapOnMDI( cd.getTarget_id() );
                this.delLunMapOnView( cd.getRoot_id() );

                view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                destroyOk = view.initor.mdb.destroyDisk( cd.getRoot_id() );
                view.initor.mdb.restoreOldTimeOut();
                if( !destroyOk && ( view.initor.mdb.getErrorCode() == 100 )  ){
                    // cd.getRoot_id()代表的disk本身就不存在
                    destroyOk = true;
                }
/*
                if( view.initor.mdb.isExistThisDisk( cd.getRoot_id() ) ){
                    view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                    destroyOk = view.initor.mdb.destroyDisk( cd.getRoot_id() );
                    view.initor.mdb.restoreOldTimeOut();
                }else{
SanBootView.log.error( getClass().getName(),"this clone disk maybe not exist, it's rootid is : " + cd.getRoot_id() );
                    destroyOk = true;
                }
*/
                if( !destroyOk ){
                    errMsg = ResourceCenter.getCmdString(ResourceCenter.CMD_DESTORY_DISK) +" : "+ view.initor.mdb.getErrorMessage();
                    aIsOk = false;
                    break;
                }else{
                    if( !view.initor.mdb.delCloneDisk( "", 5010, 1, "", cd.getId() ) ){
                        errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_CLONE_DISK )+" : " + view.initor.mdb.getErrorMessage();
                        aIsOk = false;
                        break;
                    }
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_GET_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
        }

        return aIsOk;
    }

    private boolean delUIMVol( MirrorDiskInfo mdi ){
        boolean aIsOk=true,destroyOk;

        view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
        destroyOk = view.initor.mdb.destroyDisk( mdi.getSnap_rootid() );
        view.initor.mdb.restoreOldTimeOut();
        if( !destroyOk && ( view.initor.mdb.getErrorCode() == 100 )  ){
            // mdi.getSnap_rootid()代表的disk本身就不存在
            destroyOk = true;
        }
/*
        if( view.initor.mdb.isExistThisDisk( mdi.getSnap_rootid() ) ){
            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
            destroyOk = view.initor.mdb.destroyDisk( mdi.getSnap_rootid() );
            view.initor.mdb.restoreOldTimeOut();
        }else{
            destroyOk = true;
        }
*/
        if(  destroyOk ){
            /*
            if( view.initor.mdb.delMDI( mdi.getSnap_rootid() ) ){
                view.initor.mdb.removeMDIFromCache( mdi );
            }else{
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_MIRROR_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                aIsOk = false;
            }
             */
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DESTORY_DISK ) +" : " + view.initor.mdb.getErrorMessage();
            aIsOk = false;
        }

        return aIsOk;
    }

    private ArrayList checkCopyJob( MirrorDiskInfo disk ){
        int rootid = disk.getSnap_rootid();

        ArrayList mjList = view.initor.mdb.getCjListFromVecOnSrcRootId( rootid );
        int size = mjList.size();
        for( int i=0; i<size; i++ ){
            MirrorJob mj = (MirrorJob)mjList.get(i);
            if( ( mj.getMj_current_snap_id() != mj.getMj_copy_src_snapid() ) || ( mj.getMj_current_process() != 100 ) ){
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.runningMj") + " : " + mj.getMj_id() ;
                return null;
            }
        }

        return mjList;
    }
}
