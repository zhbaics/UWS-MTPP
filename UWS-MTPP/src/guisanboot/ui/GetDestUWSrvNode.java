/*
 * GetDestUWSrvNode.java
 *
 * Created on 2008/6/18, AM 10:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.data.UWSrvNode;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class GetDestUWSrvNode extends GeneralGetSomethingThread{
    
    /** Creates a new instance of GetDestUWSrvNode */
    public GetDestUWSrvNode(
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
        
        // 这段代码会屏蔽掉“镜像到本地”的UWS服务器。其实这些被屏蔽的UWS服务器同样可以作为
        // 本地UWS的镜像服务器。所以应该把它们显示出来。
        /*
        boolean isok = view.initor.mdb.updateSrcAgnt();
        if( !isok ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SRCAGNT ) + " : " +
                    view.initor.mdb.getErrorMessage();
        }else{
            isok = view.initor.mdb.updateUWSrv();
            if( isok ){
                ArrayList list = view.initor.mdb.getDestUWSrvNode();
                int size = list.size();
                for( int i=0; i<size; i++ ){
                    UWSrvNode uws = (UWSrvNode)list.get(i);         
                    if( eventType == Browser.TREE_SELECTED_EVENT ){
                        processEvent.insertSomethingToTable( uws );
                    }else{
                        BrowserTreeNode cNode = new BrowserTreeNode( uws,false );
                        uws.setTreeNode( cNode );
                        uws.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
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
         */
        
        boolean isok = view.initor.mdb.updateUWSrv();
        if( isok ){
            ArrayList list = view.initor.mdb.getAllUWSrv();
            int size = list.size();
            for( int i=0; i<size; i++ ){
                UWSrvNode uws = (UWSrvNode)list.get(i);         
                if( eventType == Browser.TREE_SELECTED_EVENT ){
                    processEvent.insertSomethingToTable( uws );
                }else{
                    BrowserTreeNode cNode = new BrowserTreeNode( uws,false );
                    uws.setTreeNode( cNode );
                    uws.setFatherNode( fNode );
                    view.addNode( fNode,cNode );
                }
            }
            
            if( eventType == Browser.TREE_EXPAND_EVENT ){
                view.reloadTreeNode( fNode );
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_UWS_SRV ) + " : " +
                view.initor.mdb.getErrorMessage();
        }
        
        return isok;
    }
}
