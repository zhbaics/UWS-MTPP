/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

import guisanboot.data.BasicVDisk;
import guisanboot.data.BindofMdiWrapAndSnap;
import guisanboot.data.BriefVDisk;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorDiskInfoWrapper;
import guisanboot.data.SnapWrapper;
import guisanboot.data.Snapshot;
import guisanboot.data.SourceAgent;
import guisanboot.data.View;
import guisanboot.data.ViewWrapper;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.entity.CloneDiskWrapper;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
//GetUnixRstVerForSrcAgent is suitable for windowns and linxu platform
public class GetUnixRstVerForSrcAgent extends Thread implements StartupProgress {
    JDialog pdiag;
    Vector<BindofMdiWrapAndSnap> bindList = new Vector<BindofMdiWrapAndSnap>();
    ArrayList<View> viewList = new ArrayList<View>();
    SanBootView view;
    SourceAgent sa;
    boolean mustCheckDiskExist;
    int sa_id;
    boolean noVersion = false;
    boolean isCMDP;
            
    public GetUnixRstVerForSrcAgent( SanBootView view,SourceAgent sa,boolean mustCheckDiskExist,boolean isCMDP ){
        this.view = view;
        this.sa = sa;
        this.mustCheckDiskExist = mustCheckDiskExist;
        this.isCMDP = isCMDP;
        this.sa_id = sa.getSrc_agnt_id();
    }
    
    public GetUnixRstVerForSrcAgent( JDialog diag,SanBootView view,SourceAgent sa,boolean mustCheckDiskExist,boolean isCMDP ){
        this.view = view;
        pdiag = diag;
        this.sa = sa;
        this.mustCheckDiskExist = mustCheckDiskExist;
        this.isCMDP = isCMDP;
        this.sa_id = sa.getSrc_agnt_id();
    }
    
    public void setProcessDialg( JDialog diag ){
        pdiag = diag;
    }

    public void startProcessing(){
        this.start();
    }

    Runnable close = new Runnable(){
        public void run(){
            pdiag.dispose();
        }
    };

    @Override public void run(){
        int i,j,size,size1,vol_rootid;
        boolean isOk,isForCMDP;
        ArrayList<Object> svList;        
        MirrorDiskInfo mdi;
        MirrorDiskInfoWrapper mdiWrapper,key;
        BindofMdiWrapAndSnap binder;
        
        // 准备查询条件
        ArrayList lvList = view.initor.mdb.getMDIFromCacheOnSrcAgntID( sa_id );
        size = lvList.size();
        String where = "";
        boolean isFirst = true;
        for( i=0; i<size; i++ ){
            mdi = (MirrorDiskInfo)lvList.get( i );
            vol_rootid = mdi.getSnap_rootid();

            if( !sa.isWinHost() ){
                if( mdi.getSrc_agent_mp().equals( ResourceCenter.SWAP_MP ) ) continue;
            }
            
            isOk = view.initor.mdb.getCloneDiskList( sa_id, 1, vol_rootid );
            if( !isOk ){
SanBootView.log.warning(getClass().getName(), "Can't get clone disk info on [ " + sa_id +"/1" +"/"+vol_rootid+" ]");
                continue;
            }else{
                ArrayList cloneDiskList = view.initor.mdb.getCloneDiskList();
                size1 = cloneDiskList.size();
                for( j=0; j<size1; j++ ){
                    CloneDisk cloneDisk = (CloneDisk)cloneDiskList.get(j);
                    if( isFirst ){
                        where = BasicVDisk.BVDisk_Snap_Root_ID + "=" + cloneDisk.getRoot_id();
                        isFirst = false;
                    }else{
                        where +=" or "+BasicVDisk.BVDisk_Snap_Root_ID + "=" + cloneDisk.getRoot_id();
                    }
                }
            }

            if( isFirst ){
                where = BasicVDisk.BVDisk_Snap_Root_ID + "=" + vol_rootid;
                isFirst = false;
            }else{
                where +=" or "+BasicVDisk.BVDisk_Snap_Root_ID + "=" + vol_rootid;
            }
        }
        
        isOk = view.initor.mdb.queryVSnapDB(
            "select * from " + ResourceCenter.VSnap_DB +" where "+ where +";"                                    
        );
        if( isOk ){            
            for( i=0; i<size; i++ ){
                mdi = (MirrorDiskInfo)lvList.get( i );
                vol_rootid = mdi.getSnap_rootid();
                if( !sa.isWinHost() ){
                    if( mdi.getSrc_agent_mp().equals( ResourceCenter.SWAP_MP ) ) continue;
                }
                
                // 检查mdi代表的卷是否真实存在(类型或为2或为-4)
                if( mustCheckDiskExist ){
                    if( view.initor.mdb.getQueryedVolume(  vol_rootid ) == null &&
                        view.initor.mdb.getQueryedUIMirVol( vol_rootid ) == null ){
                        continue;
                    }
                }
                
                //isForCMDP = this.isCMDP && mdi.getSrc_agent_mp().toUpperCase().startsWith("C");
                isForCMDP = mdi.isCMDPProtect() && mdi.getSrc_agent_mp().toUpperCase().startsWith("C");
                isOk = view.initor.mdb.getBriefVDiskList(
                    ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + vol_rootid
                );
                if( !isOk ){
                    continue;
                }

                mdiWrapper = new MirrorDiskInfoWrapper(1);
                key = new MirrorDiskInfoWrapper();
                mdiWrapper.mdi = mdi;
                key.mdi = mdi;
                
                svList = new ArrayList<Object>();       

                this.getSnapAndViewFromVolInfo( svList,vol_rootid,isForCMDP );
                this.getCloneDiskVersion( svList,vol_rootid );

                // cmdp在选择版本时，只显示快照和view,不显示卷本身
                if( !mdi.isCMDPProtect() && sa.isRollbackedHost() ){
                    svList.add( 0,mdiWrapper );
                }
                
                if( svList.size() <= 0 ) {
SanBootView.log.error( getClass().getName(),"no snapshot version for fs: " + key.toString() );                    
                    this.noVersion = true;
                }
                
                binder = new BindofMdiWrapAndSnap( key,svList ); 
                bindList.addElement( binder );
            }
        }else{ 
SanBootView.log.error( getClass().getName(), "not found snapshot info,so have no version to select." );             
            for( i=0; i<size; i++ ){
                mdi = (MirrorDiskInfo)lvList.get( i );
                if( !sa.isWinHost() ){
                    if( mdi.getSrc_agent_mp().equals( ResourceCenter.SWAP_MP ) ) continue;
                }
                
                mdiWrapper = new MirrorDiskInfoWrapper(1);
                key = new MirrorDiskInfoWrapper();
                key.mdi = mdi;
                mdiWrapper.mdi = mdi;                                                
                
                svList = new ArrayList<Object>();
                if( !mdi.isCMDPProtect() && sa.isRollbackedHost() ){
                   svList.add( mdiWrapper );
                }
                
                if( svList.size() <=0 ) {
SanBootView.log.error( getClass().getName(),"no snapshot version for fs: " + key.toString() );                    
                    this.noVersion = true;
                }
                binder = new BindofMdiWrapAndSnap( key,svList ); 
                bindList.addElement( binder );            
            }           
        }
        
        try{
            SwingUtilities.invokeAndWait( close );
        }catch( Exception ex ){
            ex.printStackTrace();
        }
    }

    public Vector getBindList(){
        return bindList;
    }
    
    public boolean isNoVersion(){
        return noVersion;
    }

    private void getCloneDiskVersion( ArrayList svList,int root_id ){
        boolean isOk = view.initor.mdb.getCloneDiskList( sa_id, 1, root_id );
        if( !isOk ){
SanBootView.log.warning(getClass().getName(), "Can't get clone disk info on [ " + sa_id +"/1" +"/"+root_id+" ]");
            return;
        }

        ArrayList cloneDiskList = view.initor.mdb.getCloneDiskList();
        int size = cloneDiskList.size();
        for( int i=0; i<size; i++ ){
            CloneDisk cloneDisk = (CloneDisk)cloneDiskList.get(i);
            CloneDiskWrapper cdWrapper = new CloneDiskWrapper(1);
            cdWrapper.cloneDisk = cloneDisk;
            svList.add( cdWrapper );

            this.getSnapAndViewFromVolInfo( svList, cloneDisk.getRoot_id(), false );
        }
    }

    private void getSnapAndViewFromVolInfo( ArrayList svList, int rootid,boolean isForCMDP ){
        int l,k,j,size1,size2,size3,local_snap_id;
        Snapshot snap;
        View viewObj;
        SnapWrapper snapWrap;
        BasicVDisk directChild;
        
        ArrayList<Snapshot> snapList = view.initor.mdb.getSnapListFromQuerySql( rootid );
        size1 = snapList.size();
        for( j=0; j<size1; j++ ){
            snap = snapList.get(j);

            if( isForCMDP ){
                if( isOnMainBranch( snap.getSnap_local_snapid() ) ) continue;
            }else{
                if( !isOnMainBranch( snap.getSnap_local_snapid() ) ) continue;
            }

            if( sa.isNormalHost() ){
                if( !snap.isMirroredSnap() && !snap.isMirroredSnapHeader() && !snap.isSnap() ){
                    continue;
                }
            }else{
                if( !snap.isSnap() ){
                    continue;
                }
            }

            // 获取树上从该快照开始所有的 view 节点
            viewList.clear();
            getViewChild( snap );
            size2 = viewList.size();
SanBootView.log.debug( getClass().getName()," Child of view from this snap node on tree: ["+snap.getSnap_root_id()+
        "."+snap.getSnap_local_snapid() +"]" );
SanBootView.log.debug( getClass().getName(),"===========================");
            for( k=0; k<size2; k++ ){
                viewObj = (View)viewList.get( k );
SanBootView.log.debug( getClass().getName()," view local_snap_id: "+viewObj.getSnap_local_snapid() +
                    " view target_id: "+ viewObj.getSnap_target_id()
                );
            }
SanBootView.log.debug( getClass().getName(),"===========================\r\n");

            snapWrap = new SnapWrapper( snap );
            svList.add( snapWrap );

            // 根据该快照的子节点来判断直属于它的view
            ArrayList directChildList = snap.getChildList();
            size3 = directChildList.size();
            for( l=0; l<size3; l++ ){
                local_snap_id = ((Integer)directChildList.get(l)).intValue();
                directChild = view.initor.mdb.getVDisk( snap.getSnap_root_id(),local_snap_id );
                if( directChild != null ){
                    viewObj = null;
                    if( directChild.isSnap() ){
                        viewObj = searchViewList( directChild.getSnap_target_id() );
                    }else{ // direct child is view or disk
                        if( directChild.isView() ){
                            if( directChild.getSnap_target_id() != snap.getSnap_target_id() ){
                                viewObj = new View( directChild );
                            }
                        }
                    }

                    if( viewObj != null ){
                        svList.add( new ViewWrapper( viewObj ) );
SanBootView.log.debug( getClass().getName()," Found direct view: ["+ viewObj.getSnap_root_id()+"." + viewObj.getSnap_local_snapid() +"]");
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found disk from vsnap db: ["+ snap.getSnap_root_id() +"."+local_snap_id +"]");
                }
            }
        }
    }

    private void getViewChild( BasicVDisk beginDisk ){
        int i,size;
        
        ArrayList childIdList = beginDisk.getChildList();
        ArrayList childDiskList = view.initor.mdb.getDiskFromQuerySql( childIdList );
        
        size = childDiskList.size();
        for( i=0; i<size; i++ ){
            BasicVDisk disk = (BasicVDisk)childDiskList.get(i);
            if( !disk.isView() && !disk.isOriDisk() && !disk.isMirroredSnapHeader() ){
                getViewChild( disk );
            }else{
                if( disk.isView() ){
                    viewList.add( new View( disk ) );
                }
            }
        }
    }
    
    private View searchViewList( int target_id ){
        View viewObj;
        
        int size = viewList.size();
        for( int j=0; j<size; j++ ){
            viewObj = (View)viewList.get(j);
            if( viewObj.getSnap_target_id() == target_id ){
                return viewObj;
            }
        }
        return null;
    }

    private boolean isOnMainBranch( int localId ){
        ArrayList mainBranchIdList = view.initor.mdb.getBriefVDiskList();
        int size = mainBranchIdList.size();
        for( int i=0; i<size; i++ ){
            BriefVDisk disk = (BriefVDisk)mainBranchIdList.get(i);
            if( localId == disk.getLocal_snap_id() ){
                return true;
            }
        }
        return false;
    }
}
