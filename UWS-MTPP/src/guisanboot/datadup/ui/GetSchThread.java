/*
 * GetSchThread.java
 *
 * Created on July 12, 2008, 12:10 AM
 */

package guisanboot.datadup.ui;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.ui.GeneralGetSomethingThread;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import javax.swing.*;
import mylib.UI.*;

/**
 *
 * @author  Administrator
 */
public class GetSchThread extends GeneralGetSomethingThread{  
    private String profName;
    
    /** Creates a new instance of GetSchThread */
    public GetSchThread( 
        SanBootView view, 
        BrowserTreeNode fNode,
        String profName,
        GeneralProcessEventForSanBoot processEvent,
        int eventType
    ){
        super( view, fNode, processEvent, eventType );
        this.profName = profName;
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
        
        ArrayList schList = null; 
        if( profName == null ){
            schList = view.initor.mdb.getNormalSch(); 
        }else{
            schList = view.initor.mdb.getSchOnProfName( profName );
        }
        int size = schList.size();
        for( int i=0; i<size; i++ ){
            DBSchedule sch =(DBSchedule)schList.get(i);
            if( eventType == Browser.TREE_EXPAND_EVENT  ){
                BrowserTreeNode cNode = new BrowserTreeNode( sch,true );
                sch.setTreeNode( cNode );
                sch.setFatherNode( fNode );
                view.addNode( fNode,cNode );
            }else{
                processEvent.insertSomethingToTable( sch );
            }
        }
        
        if( eventType == Browser.TREE_EXPAND_EVENT ){
            view.reloadTreeNode( fNode );
        }
        
        return true;
    } 
}
