/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.entity.CloneDiskWrapper;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class GetUnixRstVer extends Thread implements StartupProgress {
    JDialog pdiag;
    Vector<BindofTgtWrapAndSnap> bindList = new Vector<BindofTgtWrapAndSnap>();
    ArrayList<View> viewList = new ArrayList<View>();
    SanBootView view;
    int hid;

    public GetUnixRstVer( SanBootView view,int hid  ){
        this.view = view;
        this.hid = hid;
    }
    
    public GetUnixRstVer( JDialog diag,SanBootView view,int hid ){
        this.view = view;
        pdiag = diag;
        this.hid = hid;
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
        VolumeMap lv,tgt;
        VolumeMapWrapper volMapWrap;
        boolean isOk;
        ArrayList<Object> svList;
        BindofTgtWrapAndSnap binder;
        TargetWrapper tgtWrap;

        // 准备查询条件
        Vector lvList = view.initor.mdb.getRealLVListOnClntID( hid );
        size = lvList.size();
        String where = "";
        boolean isFirst = true;
        for( i=0; i<size; i++ ){
            lv = (VolumeMap)lvList.elementAt(i);
            if( lv.getVolDiskLabel().equals( ResourceCenter.SWAP_MP ) ) continue;
            
            // lv对应的tgt(VolumeMap)
            tgt = view.initor.mdb.getTgtOnVGname( lv.getVolDesc(), lv.getVolClntID() );
            if( tgt != null ){
                vol_rootid = lv.getVol_rootid();
                isOk = view.initor.mdb.getCloneDiskList( hid, 0, vol_rootid );
                if( !isOk ){
    SanBootView.log.warning(getClass().getName(), "Can't get clone disk info on [ " + hid +"/0" +"/"+vol_rootid+" ]");
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
                    where = BasicVDisk.BVDisk_Snap_Root_ID + "=" + tgt.getVol_rootid();
                    isFirst = false;
                }else{
                    where +=" or "+BasicVDisk.BVDisk_Snap_Root_ID + "=" + tgt.getVol_rootid();
                }
            }else{
SanBootView.log.error( getClass().getName(),"Not found Tgt volmap: "+ lv.getVolName() );                    
                JOptionPane.showMessageDialog(  pdiag,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notFoundTgtVolMap") + " " + lv.getVolName()
                ); 
            }
        }

        isOk = view.initor.mdb.queryVSnapDB(
            "select * from " + ResourceCenter.VSnap_DB +" where "+ where +";"                                    
        );
        if( isOk ){
            for( i=0; i<size; i++ ){
                lv = (VolumeMap)lvList.elementAt(i);
                if( lv.getVolDiskLabel().equals( ResourceCenter.SWAP_MP ) ) continue;

                volMapWrap = new VolumeMapWrapper(1);
                volMapWrap.volMap = lv;

                // lv对应的tgt(VolumeMap)
                tgt = view.initor.mdb.getTgtOnVGname( lv.getVolDesc(), lv.getVolClntID() );
                if( tgt != null ){
                    tgtWrap = new TargetWrapper( lv.getVolDiskLabel(),tgt ); 
SanBootView.log.debug( getClass().getName(), " tgt id: "+ tgt.getVolTargetID() +"======\r\n" );

                    svList = new ArrayList<Object>();
                    this.getSnapAndViewFromVolInfo( svList, tgt.getVol_rootid(), tgt.getVolTargetID() );
                    this.getCloneDiskVersion( svList,tgt.getVol_rootid() );

                    svList.add( 0, volMapWrap ); // volMapWrap本身也可以看成一种快照,而且是最新的
                    binder = new BindofTgtWrapAndSnap( tgtWrap,svList ); 
                    bindList.addElement( binder );
                }
            }
        }else{     
            for( i=0; i<size; i++ ){
                lv = (VolumeMap)lvList.elementAt(i);
                if( lv.getVolDiskLabel().equals( ResourceCenter.SWAP_MP ) ) continue;

                volMapWrap = new VolumeMapWrapper(1);
                volMapWrap.volMap = lv;

                // lv对应的tgt(VolumeMap)
                tgt = view.initor.mdb.getTgtOnVGname( lv.getVolDesc(), lv.getVolClntID() );
                if( tgt != null ){
                    tgtWrap = new TargetWrapper( lv.getVolDiskLabel(),tgt ); 
SanBootView.log.debug( getClass().getName(), " tgt id: "+ tgt.getVolTargetID()  );

                    svList = new ArrayList<Object>();
                    svList.add( volMapWrap ); // volMapWrap本身也可以看成一种快照,而且是最新的
                    binder = new BindofTgtWrapAndSnap( tgtWrap,svList ); 
                    bindList.addElement( binder );
                }
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

    private void getViewChild( int rootid,BasicVDisk beginDisk ){            
        ArrayList childIdList = beginDisk.getChildList();
        ArrayList childDiskList = view.initor.mdb.getDiskFromQuerySql( rootid,childIdList );
        int size = childDiskList.size();
        for( int i=0; i<size; i++ ){
            BasicVDisk disk = (BasicVDisk)childDiskList.get(i);
            if( !disk.isView() && !disk.isOriDisk() ){
                getViewChild( rootid,disk );
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

    private void getSnapAndViewFromVolInfo( ArrayList svList,int rootid,int tid ){
        int j,k,l,size1,size2,size3,local_snap_id;
        SnapWrapper snap;
        Snapshot newSnap;
        View viewObj;
        BasicVDisk directChild;

        ArrayList snapList = view.initor.mdb.getSnapWrapperListFromQuerySql( rootid );
        size1 = snapList.size();
        for( j=0; j<size1; j++ ){
            snap = (SnapWrapper)snapList.get(j);
SanBootView.log.debug(getClass().getName(), " snap:["+ snap.snap.getSnap_root_id() +"."+snap.snap.getSnap_local_snapid()+"] tid: "+ tid );
            // 先更新一下snap's child list
            newSnap = view.initor.mdb.getSnapshotFromQuerySql( snap.snap.getSnap_root_id(),snap.snap.getSnap_local_snapid() );
            if( newSnap != null ){
                snap.snap.setSnap_child_list( newSnap.getSnap_child_list() );
            }else{
SanBootView.log.error( getClass().getName(),"Not found snapshot from vsnap db: ["+snap.snap.getSnap_root_id() +
       "."+snap.snap.getSnap_local_snapid()+"]" );
            }

            // 获取树上从该快照开始所有的 view 节点
            viewList.clear();
            getViewChild( snap.snap.getSnap_root_id(),snap.snap );
            size2 = viewList.size();
SanBootView.log.debug( getClass().getName()," Child of view from this snap node on tree: ["+snap.snap.getSnap_root_id()+
        "."+snap.snap.getSnap_local_snapid() +"]" );
SanBootView.log.debug( getClass().getName(),"===========================");
            for( k=0; k<size2; k++ ){
                viewObj = (View)viewList.get(k);
SanBootView.log.debug( getClass().getName()," view local_snap_id: "+viewObj.getSnap_local_snapid() +
                    " view target_id: "+ viewObj.getSnap_target_id()
                );
            }
SanBootView.log.debug( getClass().getName(),"===========================\r\n");

            svList.add( snap );

            // 根据该快照的子节点来判断直属于它的view
            ArrayList directChildList = snap.snap.getChildList();
            size3 = directChildList.size();
            for( l=0; l<size3; l++ ){
                local_snap_id = ((Integer)directChildList.get(l)).intValue();
                directChild = view.initor.mdb.getVDisk( snap.snap.getSnap_root_id(),local_snap_id );
                if( directChild != null ){
                    viewObj = null;
                    if( directChild.isSnap() ){
                        viewObj = searchViewList( directChild.getSnap_target_id() );
                    }else{ // direct child is view or disk
                        if( directChild.isView() ){
                            if( directChild.getSnap_target_id() != snap.snap.getSnap_target_id() ){
                                viewObj = new View( directChild );
                            }
                        }
                    }

                    if( viewObj != null ){
                        svList.add( new ViewWrapper( viewObj ) );
SanBootView.log.debug( getClass().getName()," Found direct view: ["+ viewObj.getSnap_root_id()+
            "." + viewObj.getSnap_local_snapid() +"]");
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found disk from vsnap db: ["+ snap.snap.getSnap_root_id() +"."+
        local_snap_id +"]");
                }
            }
        }
    }

    private void getCloneDiskVersion( ArrayList svList,int root_id ){
        boolean isOk = view.initor.mdb.getCloneDiskList( hid, 0, root_id );
        if( !isOk ){
SanBootView.log.warning(getClass().getName(), "Can't get clone disk info on [ " + hid +"/0" +"/"+root_id+" ]");
            return;
        }

        ArrayList cloneDiskList = view.initor.mdb.getCloneDiskList();
        int size = cloneDiskList.size();
        for( int i=0; i<size; i++ ){
            CloneDisk cloneDisk = (CloneDisk)cloneDiskList.get(i);
            CloneDiskWrapper cdWrapper = new CloneDiskWrapper(1);
            cdWrapper.cloneDisk = cloneDisk;
            svList.add( cdWrapper );

            this.getSnapAndViewFromVolInfo( svList, cloneDisk.getRoot_id(), cloneDisk.getTarget_id() );
        }
    }
}
