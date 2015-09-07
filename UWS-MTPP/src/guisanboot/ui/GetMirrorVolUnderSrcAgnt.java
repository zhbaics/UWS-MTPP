/*
 * GetMirrorVolUnderSrcAgnt.java
 *
 * Created on 2008/6/18, PM 4:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class GetMirrorVolUnderSrcAgnt extends GeneralGetSomethingThread{
    int srcAgntID;
    
    /** Creates a new instance of GetMirrorVolUnderSrcAgnt */
    public GetMirrorVolUnderSrcAgnt(
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int srcAgntID
    ) {
        super( view,fNode, processEvent, eventType );
        this.srcAgntID = srcAgntID;
    }
    
    public boolean realRun(){
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
        
        boolean isok = view.initor.mdb.updateMDI();
        if( !isok ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_MIRROR_DISK ) + " : " +
                    view.initor.mdb.getErrorMessage();
        }else{
            ArrayList list = view.initor.mdb.getMDIFromCacheOnSrcAgntID( srcAgntID );
            int size = list.size();
            for( int i=0; i<size; i++ ){
                MirrorDiskInfo mdi = (MirrorDiskInfo)list.get(i); 
                if( eventType == Browser.TREE_SELECTED_EVENT ){
                    processEvent.insertSomethingToTable( mdi );
                }else{
                    insertSomethingIntoTree( mdi );
                }
            }

            if( eventType == Browser.TREE_EXPAND_EVENT ){
                view.reloadTreeNode( fNode );
            }
        }
        
        return isok;
    }
}
