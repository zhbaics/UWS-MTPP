/*
 * GetFreeMirrorVolNodeUnderChiefRMirrorVol.java
 *
 * Created on 2008/6/18, AM 10:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.SourceAgent;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class GetFreeMirrorVolNodeUnderChiefRMirrorVol extends GeneralGetSomethingThread{
    int src_uws_id;
    
    /** Creates a new instance of GetSrcAgntNodeUnderChiefRHost */
    public GetFreeMirrorVolNodeUnderChiefRMirrorVol(
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int src_uws_id
    ) {
        super( view,fNode, processEvent, eventType );
        this.src_uws_id = src_uws_id;
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
System.out.println("----------> " + src_uws_id );
            SourceAgent srcAgntForSrcUWS = view.initor.mdb.getSrcAgntForSrcUWSOnUWSrvID( src_uws_id );
            if( srcAgntForSrcUWS != null ){
                isok = view.initor.mdb.updateMDI();
                if( isok ){
                    ArrayList list = view.initor.mdb.getMDIFromCacheOnSrcAgntID( srcAgntForSrcUWS.getSrc_agnt_id() );
                    int size = list.size();
                    for( int i=0; i<size; i++ ){
                        MirrorDiskInfo mdi = (MirrorDiskInfo)list.get(i);
                        if( !mdi.isLocalMirrorVol() ){
                            if( eventType == Browser.TREE_SELECTED_EVENT ){
                                processEvent.insertSomethingToTable( mdi );
                            }else{
                                insertSomethingIntoTree( mdi );
                            }
                        }
                    }
                    
                    if( eventType == Browser.TREE_EXPAND_EVENT ){
                        view.reloadTreeNode( fNode );
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_MIRROR_DISK ) + " : " +
                        view.initor.mdb.getErrorMessage();
                }
            }else{
SanBootView.log.warning( getClass().getName()," None free mirror vol for src_uws server:[ "+ src_uws_id +" ]");   
            }
        }
        
        return isok;
    }
}
