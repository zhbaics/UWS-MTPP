/*
 * GeneralTreeProcessThread.java
 *
 * Created on July 14, 2005, 10:18 AM
 */

package guisanboot.ui;

import javax.swing.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class GeneralTreeProcessThread extends GeneralGetSomethingThread{
    private TreeProcessable processThread;
    
    /** Creates a new instance of GeneralTreeProcessThread */
    public GeneralTreeProcessThread( 
        SanBootView view, 
        BrowserTreeNode node,
        GeneralProcessEventForSanBoot processEvent,
        TreeProcessable _processThread,
        int eventType,
        int cmdType
    ){
        super( view, node, processEvent, eventType );
        
        processThread = _processThread;
        this.cmd = cmdType;
    }
    
    public boolean realRun(){
        boolean ok = true ;
        
        if( eventType == Browser.TREE_EXPAND_EVENT ){
            try{
                SwingUtilities.invokeAndWait( clearTree );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
            
            processThread.setFatherTreeNode( fNode );
            processThread.setAddTreeFlag( true );
            processThread.setAddTableFlag( false );
            processThread.setProcessEvent( processEvent );
            ok = processThread.realDo();
            view.reloadTreeNode( fNode );
            
        }else if( eventType == Browser.TREE_SELECTED_EVENT ){
            try{
                SwingUtilities.invokeAndWait( clearTable );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
            
            processThread.setFatherTreeNode( fNode );
            processThread.setAddTreeFlag( false );
            processThread.setAddTableFlag( true );
            processThread.setProcessEvent( processEvent );
            ok = processThread.realDo();
        }
        
        if( !ok ){
            errMsg =  ResourceCenter.getCmdString( cmd )+" : "+
                ((AbstractNetworkRunning)processThread).getErrMsg();
        }
        return ok;
    }
}
