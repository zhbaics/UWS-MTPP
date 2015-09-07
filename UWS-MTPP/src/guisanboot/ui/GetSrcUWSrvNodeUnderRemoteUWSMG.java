/*
 * GetSrcUWSrvNodeUnderRemoteUWSMG.java
 *
 * Created on 2008/6/18, ��AM��10:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.data.UWSrvNode;
import guisanboot.data.SrcUWSrvNodeWrapper;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class GetSrcUWSrvNodeUnderRemoteUWSMG extends GeneralGetSomethingThread{
    
    /** Creates a new instance of GetSrcUWSrvNodeUnderRemoteUWSMG */
    public GetSrcUWSrvNodeUnderRemoteUWSMG(
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType
    ) {
        super( view, fNode, processEvent, eventType );
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
        
        boolean isok = view.initor.mdb.updateSrcAgnt();
        if( !isok ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SRCAGNT ) + " : " +
                    view.initor.mdb.getErrorMessage();
        }else{
            isok = view.initor.mdb.updateUWSrv();
            if( isok ){
                ArrayList list = view.initor.mdb.getSrcUWSrvNode();
                int size = list.size();
                for( int i=0; i<size; i++ ){
                    UWSrvNode uws = (UWSrvNode)list.get(i);
                    SrcUWSrvNodeWrapper one = new SrcUWSrvNodeWrapper( uws );
                    
                    if( eventType == Browser.TREE_SELECTED_EVENT ){
                        processEvent.insertSomethingToTable( one );
                    }else{
                        insertSomethingIntoTree( one );
                    }
                }
                
                if( eventType == Browser.TREE_EXPAND_EVENT ){
                    view.reloadTreeNode( fNode );
                }
            }else{
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_UWS_SRV ) + " : " +
                    view.initor.mdb.getErrorMessage();
            }
        }
        
        return isok;
    }
}
