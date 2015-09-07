/*
 * GetRstVersion.java
 *
 * Created on 2008/11/13, PM 12:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.data.BasicVDisk;
import guisanboot.data.BindofVolAndSnap;
import guisanboot.data.BootHost;
import guisanboot.data.BriefVDisk;
import guisanboot.data.SnapWrapper;
import guisanboot.data.Snapshot;
import guisanboot.data.View;
import guisanboot.data.ViewWrapper;
import guisanboot.data.VolumeMap;
import guisanboot.data.VolumeMapWrapper;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.entity.CloneDiskWrapper;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.SwingUtilities;

/**
 *
 * @author zjj
 */
public class GetRstVersion extends Thread{
    /** Creates a new instance of GetRstVersion */   
    ProgressDialog pdiag;
    ArrayList viewList = new ArrayList();
    Vector<BindofVolAndSnap> bindList = new Vector<BindofVolAndSnap>();
    Vector<BindofVolAndSnap> filteredBindList = new Vector<BindofVolAndSnap>(); // for cmdp
    SanBootView view;
    int hid = 0;  // boot host id
    int cluster_id = 0;  // cluster id
    boolean isCMDP = false;
    boolean isWin = true;
    boolean hasOS = true;   //是否包含对C盘的选取
    boolean isContainVolMap = false; // 是否版本中包含volMap本身
    
    public GetRstVersion( ProgressDialog pdiag,SanBootView view,int hid,boolean hasOS  ){
        this( pdiag,view,hid,false,hasOS );
    }
    
    public GetRstVersion( ProgressDialog pdiag,SanBootView view,int hid,boolean isCMDP,boolean hasOS  ){
        this( pdiag,view,hid,0,isCMDP,hasOS,false );
    }

    public GetRstVersion( ProgressDialog pdiag,SanBootView view,int hid,int cid,
            boolean isCMDP,boolean hasOS,boolean isContainVolMap
    ){
        this.pdiag = pdiag;
        this.view = view;
        this.hid = hid;
        this.cluster_id = cid;
        this.isCMDP = isCMDP;
        this.hasOS = hasOS;
        this.isContainVolMap = isContainVolMap;
    }

    Runnable close = new Runnable(){
        public void run(){
            pdiag.dispose();
        }
    };

    @Override public void run(){
        int i,j,size,size1,vol_rootid,host_id;
        VolumeMapWrapper volMap;
        boolean isOk,isForCMDP;
        ArrayList svList;
        BindofVolAndSnap binder;
        Vector volMapList;

        if( this.cluster_id > 0 ){
            volMapList = view.initor.mdb.getVolFromCluster( this.cluster_id,this.isCMDP?2:1 );
        }else{
            // 准备查询条件
            BootHost bootHost = view.initor.mdb.getBootHostFromVector( hid );
            // boothost肯定有
            if( bootHost.isWinHost() ){
                volMapList = view.initor.mdb.getVolMapWrapOnClntID( hid,this.isCMDP?2:1 );
            }else{ // Linux
                volMapList = view.initor.mdb.getVolMapWrapperForRealLVOnClntID( hid,1 );
            }
        }
        
        size = volMapList.size();
        String where = "";
        boolean isFirst = true;
        for( i=0; i<size; i++ ){
            volMap = (VolumeMapWrapper)volMapList.elementAt(i);
            vol_rootid = volMap.volMap.getVol_rootid();
            host_id = volMap.volMap.getVolClntID();
            
            //isOk = view.initor.mdb.getCloneDiskList( hid, 0, vol_rootid );
            isOk = view.initor.mdb.getCloneDiskList( host_id, 0, vol_rootid );
            if( !isOk ){
SanBootView.log.warning(getClass().getName(), "Can't get clone disk info on [ " + host_id +"/0" +"/"+vol_rootid+" ]");
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
                volMap = (VolumeMapWrapper)volMapList.elementAt(i);
                if( !this.hasOS ){
                    if( volMap.volMap.getVolDiskLabel().substring(0,1).toUpperCase().equals("C") ){
                        continue;
                    }
                }
                vol_rootid = volMap.volMap.getVol_rootid();

                isForCMDP = volMap.volMap.isCMDPProtect() && volMap.volMap.getVolDiskLabel().toUpperCase().startsWith("C");
                isOk = view.initor.mdb.getBriefVDiskList(
                    ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + vol_rootid
                );
                if( !isOk ){
                    continue;
                }
                
                svList = new ArrayList();
                this.getSnapAndViewFromVolInfo( svList, vol_rootid,volMap.volMap.getVolTargetID(),isForCMDP );
                this.getCloneDiskVersion( svList,vol_rootid );

                if( !volMap.volMap.isCMDPProtect() || this.isContainVolMap ){
                    volMap.setType( 1 );
                    svList.add( 0, volMap );
                }
                binder = new BindofVolAndSnap( volMap.volMap, svList );
                bindList.addElement( binder );
            }
        }else{     
            for( i=0; i<size; i++ ){
                volMap = (VolumeMapWrapper)volMapList.elementAt(i);
                if( !this.hasOS ){
                    if( volMap.volMap.getVolDiskLabel().substring(0,1).toUpperCase().equals("C") ){
                        continue;
                    }
                }
                svList = new ArrayList();
                if( !volMap.volMap.isCMDPProtect() || this.isContainVolMap ){
                    volMap.setType( 1 );
                    svList.add( volMap ); // volMap本身也可以看成一种快照,而且是最新的
                }
                binder = new BindofVolAndSnap( volMap.volMap, svList ); 
                bindList.addElement( binder );
            }
        }

        try{
            SwingUtilities.invokeAndWait( close );
        }catch( Exception ex ){
            ex.printStackTrace();
        }
    }
    
    private boolean hasThisVersion( String timeStamp,String diskLabel ){
        BindofVolAndSnap binder = getBinder( diskLabel );
        if( binder != null ){
            ArrayList snapList = binder.getSnapList();
            int size = snapList.size();
            for( int i=0; i<size; i++ ){
                Object snapObj = snapList.get(i);
                if( !(snapObj instanceof SnapWrapper) ) continue;

                SnapWrapper snap = (SnapWrapper)snapObj;
                if( snap.snap.getSnap_create_time().equals( timeStamp ) ){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    private BindofVolAndSnap getBinder( String diskLabel ){
        int size = bindList.size();
        for( int i=0; i<size; i++ ){
            BindofVolAndSnap binder = bindList.get(i);
            if( binder.getVolMap().getVolDiskLabel().equals( diskLabel ) ){
                return binder;
            }
        }
        return null;
    }

    private void findSnapForDg( PPProfile prof ){
        Iterator iterator;
        int i,j,size,size1;
        PPProfileItem ele;
        SnapWrapper snap;
        Object snapObj;
        boolean finded,del=false;
        ArrayList snapList;
        
        ArrayList<PPProfileItem> elements1 = prof.getElements();
        ArrayList<PPProfileItem> elements = new ArrayList<PPProfileItem>();
        for( i=0; i<elements1.size(); i++ ){
            elements.add( elements1.get( i ) );
        }
        
        // 从 master disk 中找出共有的版本信息
        PPProfileItem master_disk = prof.getMainDiskItem();
        if( master_disk.getVolMap().getVolDiskLabel().toUpperCase().substring( 0,1 ).equals("C") ){
            if( !this.hasOS ){
                // 从prof中找一个不是C的其他盘，使之假装是master_disk
                elements.remove( master_disk ); // 先把C从vg中去掉
                master_disk = prof.getItemNotPointed( master_disk.getVolMap().getVolDiskLabel() );
            }
        }
        
        BindofVolAndSnap master_disk_binder = getBinder( master_disk.getVolMap().getVolDiskLabel() );
        if( master_disk_binder != null ){
            snapList = master_disk_binder.getSnapList();
            iterator = snapList.iterator();
            while( iterator.hasNext() ){
                snapObj = iterator.next();
                if( snapObj instanceof SnapWrapper ) {
                    del = false;
                }else if( snapObj instanceof ViewWrapper ){
                    if( del ){
                        iterator.remove();
                    }
                    continue;
                }else{
                    del = false;
                    continue;
                }

                snap = (SnapWrapper)snapObj;

                finded = true;
                size1 = elements.size();
                for( j=0; j<size1; j++ ){
                    ele = elements.get( j );
                    if( ele.getVolMap().getVolDiskLabel().equals( master_disk.getVolMap().getVolDiskLabel() ) ) continue;

                    if( !hasThisVersion( snap.snap.getSnap_create_time(),ele.getVolMap().getVolDiskLabel() ) ){
                        finded = false;
                        break;
                    }
                }

                if( !finded ){
                    del = true;
                    iterator.remove();
                }
            }
        }

        // 从其他成员盘中删除非共有版本
        size = elements.size();
        for( i=0; i<size; i++ ){
            ele = elements.get( i );
            if( ele.getVolMap().getVolDiskLabel().equals( master_disk.getVolMap().getVolDiskLabel() ) ) continue;

            BindofVolAndSnap item_binder = getBinder( ele.getVolMap().getVolDiskLabel() );
            if( item_binder != null ){
                snapList = item_binder.getSnapList();
                iterator = snapList.iterator();
                while( iterator.hasNext() ){
                    snapObj = iterator.next();

                    if( snapObj instanceof SnapWrapper ) {
                        del = false;
                    }else if( snapObj instanceof ViewWrapper ){
                        if( del ){
                            iterator.remove();
                        }
                        continue;
                    }else{
                        del = false;
                        continue;
                    }

                    snap = (SnapWrapper)snapObj;

                    if( !hasThisVersion( snap.snap.getSnap_create_time(),master_disk.getVolMap().getVolDiskLabel() ) ){
                        del = true;
                        iterator.remove();
                    }
                }
            }
        }

        // 将最新结果放到filteredBindList中去
        size = elements.size();
        for( i=0; i<size; i++ ){
            ele = elements.get( i );
            BindofVolAndSnap binder = this.getBinder( ele.getVolMap().getVolDiskLabel() );
            if( binder != null ){
                this.filteredBindList.add( binder );
                this.bindList.remove( binder );
            }
        }
    }
    
    public Vector getBindList(){
        PPProfile prof;

        //if( this.isCMDP ){
            // 按照 dg 进行编排
            while( bindList.size() > 0 ){
                BindofVolAndSnap binder = bindList.get( 0 );
                VolumeMap volMap = binder.getVolMap();
                if( volMap.isCMDPProtect() ){
                    if( this.cluster_id > 0 ){
                        prof = view.initor.mdb.getBelongedDg1( this.cluster_id,volMap.getVolDiskLabel() );
                    }else{
                        prof = view.initor.mdb.getBelongedDg( hid,volMap.getVolDiskLabel() );
                    }
                    if( prof == null ) {
                        this.bindList.remove( binder );
                        continue;
                    }
                    
                    if( !prof.isValidDriveGrp() ){
                        this.filteredBindList.add( binder );
                        this.bindList.remove( binder );
                    }else{
                        findSnapForDg( prof );
                    }
                }else{
                    this.filteredBindList.add( binder );
                    this.bindList.remove( binder );
                }
            }

            return filteredBindList;
        //}else{
        //    return bindList;
        //}
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
            
            this.getSnapAndViewFromVolInfo( svList, cloneDisk.getRoot_id(), cloneDisk.getTarget_id(),false );
        }
    }

    private void getSnapAndViewFromVolInfo( ArrayList svList,int rootid,int tid,boolean isForCMDP ){
        int j,k,l,size1,size2,size3,local_snap_id;
        View viewObj;
        SnapWrapper snap;
        Snapshot newSnap;
        BasicVDisk directChild;

        ArrayList snapList = view.initor.mdb.getSnapWrapperListFromQuerySql( rootid );
        size1 = snapList.size();
        for( j=0; j<size1; j++ ){
            snap = (SnapWrapper)snapList.get(j);
            if( isForCMDP ){
                if( isOnMainBranch( snap.snap.getSnap_local_snapid() ) ) continue;
            }else{
                if( !isOnMainBranch( snap.snap.getSnap_local_snapid() ) ) continue;
            }
SanBootView.log.debug(getClass().getName(), " snap:["+ snap.snap.getSnap_root_id() +"."+snap.snap.getSnap_local_snapid()+"] tid: "+ tid );
            // 先更新一下snap's child list
            newSnap = view.initor.mdb.getSnapshotFromQuerySql( snap.snap.getSnap_root_id(),snap.snap.getSnap_local_snapid() );
            if( newSnap != null ){
                snap.snap.setSnap_child_list( newSnap.getSnap_child_list() );
            }else{
SanBootView.log.error( getClass().getName(),"Not found snapshot from vsnap db: ["+snap.snap.getSnap_root_id() +"."+snap.snap.getSnap_local_snapid()+"]" );
            }
            
            // 获取树上从该快照开始所有的 view 节点
            viewList.clear();
            getViewChild( snap.snap.getSnap_root_id(),snap.snap );
            size2 = viewList.size();
SanBootView.log.debug( getClass().getName()," Child of view on snap node: ["+snap.snap.getSnap_root_id()+"."+snap.snap.getSnap_local_snapid() +"]" );
SanBootView.log.debug( getClass().getName(),"===========================");
            for( k=0; k<size2; k++ ){
                viewObj = (View)viewList.get(k);
SanBootView.log.debug( getClass().getName()," view local_snap_id: "+viewObj.getSnap_local_snapid() +" view target_id: "+ viewObj.getSnap_target_id() );
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
SanBootView.log.debug( getClass().getName()," Found direct view: ["+ viewObj.getSnap_root_id()+"." + viewObj.getSnap_local_snapid() +"]" );
                        svList.add( new ViewWrapper( viewObj ) );
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found disk from vsnap db: ["+ snap.snap.getSnap_root_id() +"."+local_snap_id +"]" );
                }
            }
        }
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
