/*
 * GeneralGetSomethingThread.java
 *
 * Created on 2008/6/18,AM 9:55
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.SwingUtilities;
import mylib.UI.BasicUIObject;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public abstract class GeneralGetSomethingThread extends BasicGetSomethingThread{
    protected GeneralProcessEventForSanBoot processEvent;
    protected BrowserTreeNode fNode;
    protected int eventType;
    
    protected Runnable clearTree = new Runnable(){
        public void run(){
            view.removeAllData( fNode );
        }
    };
    
    protected  void fireClearTree(){
        try{
            SwingUtilities.invokeAndWait( clearTree );
        }catch( Exception ex ){
            ex.printStackTrace();
        }
    }
    
    protected Runnable clearTable = new Runnable(){
        public void run(){
            processEvent.setupTableList();
        }
    };
    
    protected  void fireClearTable(){
        try{
            SwingUtilities.invokeAndWait( clearTable );
        }catch( Exception ex ){
            ex.printStackTrace();
        }
    }
    
    /** Creates a new instance of GeneralGetSomethingThread */
    public GeneralGetSomethingThread(
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType
    ) {
        this( view,true, fNode, processEvent, eventType );
    }
    
    public GeneralGetSomethingThread(
        SanBootView view, 
        boolean showErrDiag,
        BrowserTreeNode _fNode,
        GeneralProcessEventForSanBoot _processEvent,
        int _eventType
    ) {
        super( view, showErrDiag );  
        
        this.fNode = _fNode;
        this.processEvent = _processEvent;
        this.eventType = _eventType;
    }
    
    public void insertSomethingIntoTree( BasicUIObject obj ){
        BrowserTreeNode cNode = new BrowserTreeNode( obj,false );
        obj.setTreeNode( cNode );
        obj.setFatherNode( fNode );
        view.addNode( fNode,cNode );
    }
}
