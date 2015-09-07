/*
 * GetSnapDirectlyUnderUnlimitedIncMirrorVol.java
 *
 * Created on Dec 14, 2009, 5:19 PM
 */

package guisanboot.unlimitedIncMj.model;

import guisanboot.ui.*;
import guisanboot.data.BasicVDisk;
import guisanboot.data.BriefVDisk;
import guisanboot.data.Snapshot;
import javax.swing.*;
import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.entity.LocalUnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  Administrator
 */
public class GetSnapDirectlyUnderUnlimitedIncMirrorVol extends GeneralGetSomethingThread {
    private int rootid;
    private int begin;
    private int end;
    private boolean addCache = false;
    private DefaultTableModel model;
    private int count = 0;
    private UnlimitedIncMirroredSnap curMS;
    private int type;
    private boolean isCmdp;

    public GetSnapDirectlyUnderUnlimitedIncMirrorVol(
        SanBootView view,
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int rootid,
        int begin,
        int end,
        boolean addCache,
        int type,
        boolean isCmdp
    ){
        super( view,fNode, processEvent, eventType );
        this.rootid = rootid;
        this.begin = begin;
        this.end = end;
        this.addCache = addCache;
        this.type = type;
        this.isCmdp = isCmdp;
    }

    /** Creates a new instance of GetSnapDirectlyUnderUnlimitedIncMirrorVol */
    public GetSnapDirectlyUnderUnlimitedIncMirrorVol( 
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int rootid,
        int type,
        boolean isCmdp
    ){
        this( view,fNode,processEvent,eventType,rootid,0,32,false,type,isCmdp );
    }

    public void setTableModel( DefaultTableModel _model ){
        model = _model;
    }

    public boolean realRun(){
        if( !this.addCache ){
            if( eventType == Browser.TREE_EXPAND_EVENT ){
                try{
                    SwingUtilities.invokeAndWait( clearTree );
                }catch( Exception ex ){
                    ex.printStackTrace();
                }
            }else if( eventType == Browser.TREE_SELECTED_EVENT ){
                try{
                    SwingUtilities.invokeAndWait( clearTable );
                }catch( Exception ex ){
                    ex.printStackTrace();
                }
            }
        }

        // 步骤 :
        //(1) listsnap -m rootid -b 0 -e 32 找出直属快照的 local_id list
        //(2) select * from vsnap where rootid=<disk.root_id> ===> <snap set>
        //(3) select * from <snap set> where snap.local_id == each local_id

        boolean ok = view.initor.mdb.getBriefVDiskList( 
            ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + rootid +
            " -b "+ begin + " -e "+ end
        );
        if( ok ){ 
            ok = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where " + BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid
            );    
            if( ok ){
                searchUIMirroredSnapHeader();
                searchUIMirroredSnap( );
                if( !this.addCache ){
                    if( eventType == Browser.TREE_EXPAND_EVENT ){
                        view.reloadTreeNode( fNode );
                    }
                }
            }else{
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SNAP ) + " : " +
                        view.initor.mdb.getErrorMessage();
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + " : " +
                    view.initor.mdb.getErrorMessage();
        }
        
        return ok;
    }
    
    private void searchUIMirroredSnap(){
        ArrayList<Snapshot> snapList = view.initor.mdb.getUISnapListFromQuerySql( this.rootid );
        int size = snapList.size();
        for( int i=0; i<size; i++ ){
            Snapshot snap = snapList.get( i );

            if( this.isCmdp ){
                // 主分支上的不要
                if( isOnMainBranch( snap.getSnap_local_snapid() ) ) continue;
                // 从分支上的snap的父亲必须是 main branch 上
                if( !isOnMainBranch( snap.getSnap_parent() ) ) continue;
            }else{
                if( !isOnMainBranch( snap.getSnap_local_snapid() ) ) continue;
            }

            if( !snap.isUIMirroredSnap() ){
                continue;
            }

            if( type == ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1 ){
                LocalUnlimitedIncMirroredSnap lms = new LocalUnlimitedIncMirroredSnap( snap );
                curMS = lms;
            }else{
                UnlimitedIncMirroredSnap ms = new UnlimitedIncMirroredSnap( snap );
                curMS = ms;
            }

            if( !this.addCache ){
                if( eventType == Browser.TREE_EXPAND_EVENT  ){
                    BrowserTreeNode cNode = new BrowserTreeNode( curMS, false );
                    curMS.setTreeNode( cNode );
                    curMS.setFatherNode( fNode );
                    view.addNode( fNode,cNode );
                }else{
                    processEvent.insertSomethingToTable( curMS );
                }
            }else{
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){
                }
                count++;
            }
        }
    }
    
    private void searchUIMirroredSnapHeader(){
        ArrayList<Snapshot> snapList = view.initor.mdb.getUISnapListFromQuerySql( this.rootid );
        int size = snapList.size();
        for( int i=0; i<size; i++ ){
            Snapshot snap = snapList.get( i );

            if( this.isCmdp ){
                // 主分支上的不要
                if( isOnMainBranch( snap.getSnap_local_snapid() ) ) continue;
                // 从分支上的snap的父亲必须是 main branch 上
                if( !isOnMainBranch( snap.getSnap_parent() ) ) continue;
            }else{
                if( !isOnMainBranch( snap.getSnap_local_snapid() ) ) continue;
            }

            if( !snap.isUIMirroredSnapHeader() ){
                continue;
            }

            if( type == ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1 ){
                LocalUnlimitedIncMirroredSnap lms = new LocalUnlimitedIncMirroredSnap( snap );
                curMS = lms;
            }else{
                UnlimitedIncMirroredSnap ms = new UnlimitedIncMirroredSnap( snap );
                curMS = ms;
            }

            if( !this.addCache ){
                if( eventType == Browser.TREE_EXPAND_EVENT  ){
                    BrowserTreeNode cNode = new BrowserTreeNode( curMS,false );
                    curMS.setTreeNode( cNode );
                    curMS.setFatherNode( fNode );
                    view.addNode( fNode,cNode );
                }else{
                    processEvent.insertSomethingToTable( curMS );
                }
            }else{
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){
                }
                count++;
            }
        }    
    }
    
    Runnable insertModel = new Runnable(){
        public void run(){
            if( model == null ) return;
            
            Object[] one = new Object[3];

            one[0] = curMS;

            one[1] = new GeneralBrowserTableCell(
                -1,curMS.snap.toString(),JLabel.LEFT
            );

            one[2] = new GeneralBrowserTableCell(
                -1,curMS.snap.getCreateTimeStr(),JLabel.LEFT
            );
            model.addRow( one );
        }
    };

    public int getCount(){
        return this.count;
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
