/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.unlimitedIncMj.model;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefLunMap;
import guisanboot.ui.ChiefSnapshot;
import guisanboot.ui.GeneralGetSomethingThread;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.util.ArrayList;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class GetCloneDiskListThread extends GeneralGetSomethingThread {
    int src_host_id;
    int src_host_type;
    int src_disk_root_id;

    public GetCloneDiskListThread(
        SanBootView view,
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int src_host_id,
        int src_host_type,
        int src_disk_root_id
    ){
        super( view,fNode, processEvent, eventType );
        this.src_host_id = src_host_id;
        this.src_host_type = src_host_type;
        this.src_disk_root_id = src_disk_root_id;
    }

    public boolean realRun(){
        if( eventType == Browser.TREE_EXPAND_EVENT ){
            this.fireClearTree();
        }else if( eventType == Browser.TREE_SELECTED_EVENT ){
            this.fireClearTable();
        }
        
        boolean ok = view.initor.mdb.getCloneDiskList( src_host_id,src_host_type,src_disk_root_id );
        if( !ok ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_CLONE_DISK ) + " : " +
                    view.initor.mdb.getErrorMessage();
        }else{
            ArrayList list = view.initor.mdb.getCloneDiskList();
            int size = list.size();
System.out.println("clone disk size: "+ size );
            for( int i=0; i<size; i++ ){
                CloneDisk cloneDisk = (CloneDisk)list.get(i);
                if( eventType == Browser.TREE_SELECTED_EVENT ){
                    processEvent.insertSomethingToTable( cloneDisk );
                }else{
                    insertSomethingIntoTree( cloneDisk );
                }
            }

            if( eventType == Browser.TREE_EXPAND_EVENT ){
                view.reloadTreeNode( fNode );
            }
        }

        return ok;
    }

    public void insertSomethingIntoTree( CloneDisk cloneDisk ){
        BrowserTreeNode cNode = new BrowserTreeNode( cloneDisk,false );
        cloneDisk.setTreeNode( cNode );
        cloneDisk.setFatherNode( fNode );
        view.addNode( fNode,cNode );

        // 准备lunmap list node
        ChiefLunMap chiefLm = new ChiefLunMap();
        BrowserTreeNode chiefLmNode = new BrowserTreeNode( chiefLm, true );
        chiefLm.setTreeNode( chiefLmNode );
        chiefLm.setFatherNode( cNode );
        cNode.add( chiefLmNode );

        // 准备snapshot list node
        ChiefSnapshot chiefSnap = new ChiefSnapshot();
        BrowserTreeNode chiefSnapNode = new BrowserTreeNode( chiefSnap,false );
        chiefSnap.setTreeNode( chiefSnapNode );
        chiefSnap.setFatherNode( cNode );
        cNode.add( chiefSnapNode );
    }
}
