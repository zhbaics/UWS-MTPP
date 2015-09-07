/*
 * GetProfileThread.java
 *
 * Created on Aug 6, 2008, 12:10 AM
 */

package guisanboot.datadup.ui;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.datadup.data.UniProfile;
import guisanboot.res.ResourceCenter;
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
public class GetProfileThread extends GeneralGetSomethingThread{
    private long cid;
    private Cluster cluster = null;
    private int mode;

    /** Creates a new instance of GetProfileThread */
    public GetProfileThread( 
        SanBootView view, 
        BrowserTreeNode fNode,
        long _cid,
        Cluster _cluster,
        int mode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType
    ){
        super( view,fNode, processEvent, eventType );
        cid = _cid;
        this.cluster = _cluster;
        this.mode = mode;
    }
    
    public boolean realRun(){
        ArrayList profList = null;

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
        
        if( cid != -1 ){
            if( mode == ResourceCenter.CMD_TYPE_MTPP ){
                if( this.cluster != null ){
                    profList = view.initor.mdb.getAllProfileForCluster( cluster.getCluster_id() );
                }else{
                    profList = view.initor.mdb.getAllProfileOnClntID( cid );
                }
                int size = profList.size();
//System.out.println(" size: "+ size );
                for( int i=0; i<size; i++ ){
                    UniProfile prof =(UniProfile)profList.get(i);
//System.out.println(" prof name: "+prof.getProfileName() );
//System.out.println(" prof content:\n"+prof.prtMe() );
                    if( eventType == Browser.TREE_EXPAND_EVENT  ){
                        BrowserTreeNode cNode = new BrowserTreeNode( prof,true );
                        prof.setTreeNode( cNode );
                        prof.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }else{
                        processEvent.insertSomethingToTable( prof );
                    }
                }
            }else{
                if( cluster != null ){
                    profList = view.initor.mdb.getAllPPProfileForCluster( cluster.getCluster_id() );
                }else{
                    profList = view.initor.mdb.getPPProfile( cid );
                }
                int size = profList.size();
                for( int i=0; i<size; i++ ){
                    PPProfile prof =(PPProfile)profList.get(i);
                    if( eventType == Browser.TREE_EXPAND_EVENT  ){
                        BrowserTreeNode cNode = new BrowserTreeNode( prof,true );
                        prof.setTreeNode( cNode );
                        prof.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }else{
                        processEvent.insertSomethingToTable( prof );
                    }
                }
            }
        }
        
        if( eventType == Browser.TREE_EXPAND_EVENT ){
            view.reloadTreeNode( fNode );
        }
        
        return true;
    }
}
