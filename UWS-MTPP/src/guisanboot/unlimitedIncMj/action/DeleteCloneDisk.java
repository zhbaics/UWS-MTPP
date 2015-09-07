/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.unlimitedIncMj.action;

import guisanboot.data.LunMap;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCloneDisk;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 * DeleteCloneDisk.java
 *
 * Created on 2010-1-28, 14:07:58
 */
public class DeleteCloneDisk extends Thread {
    ProgressDialog pdiag;
    CloneDisk disk;
    SanBootView view;
    boolean simpleDo = false;
    
    public DeleteCloneDisk( ProgressDialog pdiag,CloneDisk disk,SanBootView view,boolean simpleDo ){
        this.pdiag = pdiag;
        this.disk = disk;
        this.view = view;
        this.simpleDo = simpleDo;
    }

    Runnable close = new Runnable(){
        public void run(){
            pdiag.dispose();
        }
    };

    @Override public void run(){
        this.realDo();
    }

    public boolean realDo(){
        boolean isOk = view.initor.mdb.updateOrphanVol();
        if( !isOk ){
            view.showError1(
                ResourceCenter.CMD_GET_VOL,
                view.initor.mdb.getErrorCode(),
                view.initor.mdb.getErrorMessage()
            );

            if( !this.simpleDo ){
                try{
                    SwingUtilities.invokeAndWait( close );
                }catch( Exception ex ){
                    ex.printStackTrace();
                }
            }
            return false;
        }

        Volume vol = view.initor.mdb.getVolume( disk.getRoot_id() );
        if( vol != null ){
            delLunMapOnDisk( disk.getTarget_id() );
            delLunMapOnView( disk.getRoot_id() );

            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
            isOk = view.initor.mdb.destroyDisk( disk.getRoot_id() );
            view.initor.mdb.restoreOldTimeOut();
            if( !isOk && ( view.initor.mdb.getErrorCode() == 100 )  ){
                // disk.getRoot_id()代表的disk本身就不存在
                isOk = true;
            }
            
            if( !isOk ){
                view.showError1(
                    ResourceCenter.CMD_DESTORY_DISK,
                    view.initor.mdb.getErrorCode(),
                    view.initor.mdb.getErrorMessage()
                );
                if( !this.simpleDo ){
                    try{
                        SwingUtilities.invokeAndWait( close );
                    }catch( Exception ex ){
                        ex.printStackTrace();
                    }
                }
                return false;
            }
        }

        // delete cloneinfo object
        isOk = view.initor.mdb.delCloneDisk( "", 5010, 1, "", disk.getId() );
        if( !isOk ){
            view.showError1(
                ResourceCenter.CMD_DEL_CLONE_DISK,
                view.initor.mdb.getErrorCode(),
                view.initor.mdb.getErrorMessage()
            );
            return false;
        }else{
            if( !this.simpleDo ){
                BrowserTreeNode fNode = disk.getFatherNode();
                BrowserTreeNode selCloneDiskNode = view.getCloneDiskNodeOnChiefCloneDisk( fNode,disk.getRoot_id() );
                if( selCloneDiskNode !=null ){
                    view.removeNodeFromTree( fNode,selCloneDiskNode );
                }

                view.setCurNode( fNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                TreePath path = new TreePath( fNode.getPath() );
                ProcessEventOnChiefCloneDisk peOnChiefCloneDisk = new ProcessEventOnChiefCloneDisk( view );
                peOnChiefCloneDisk.processTreeSelection( path );
                peOnChiefCloneDisk.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }

        if( !this.simpleDo ){
            try{
                SwingUtilities.invokeAndWait( close );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
        }

        return true;
    }

    private void delLunMapOnDisk( int tid ){
        // 首先删除老的、没用的lunmap,不管是否成功删除
        boolean isOk = view.initor.mdb.getLunMapForTID( tid );
        if( isOk ){
            Vector<LunMap> lmList = view.initor.mdb.getAllLunMapForTid();
            int size = lmList.size();
            for( int j=0;j<size;j++ ){
                LunMap lm = (LunMap)lmList.elementAt( j );
                view.initor.mdb.delLunMap( tid, lm.getIP(),lm.getMask(), lm.getAccessMode() );
            }
        }
    }

    private void delLunMapOnView( int root_id ){
        // 再删除disk上所有快照的view的lunmap
        boolean isOk = view.initor.mdb.getAllView( root_id );
        if( isOk ){
            ArrayList<View> viewList = view.initor.mdb.getViewList();
            int size2 = viewList.size();
            for( int k=0; k<size2; k++ ){
                View viewObj = (View)viewList.get( k );
                isOk = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() );
                if( isOk ){
                    Vector<LunMap> lmList = view.initor.mdb.getAllLunMapForTid();
                    int size1 = lmList.size();
                    for( int j=0;j<size1;j++ ){
                        LunMap lm = (LunMap)lmList.elementAt(j);
                        isOk = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                    }
                }
            }
        }
    }
}
