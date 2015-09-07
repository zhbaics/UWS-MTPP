package guisanboot.vmmanage.service;


import guisanboot.data.BootHost;
import guisanboot.data.SourceAgent;
import guisanboot.data.VMHostInfo;
import guisanboot.ui.ChiefVMHost;
import guisanboot.ui.GeneralGetSomethingThread;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

public class GetVMHostDirectlyUnderChiefNode extends GeneralGetSomethingThread {

    
    /** Creates a new instance of GetViewDirectlyUnderSnap */
    public GetVMHostDirectlyUnderChiefNode( 
        SanBootView view,  
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType
    ){
        super( view,fNode,processEvent,eventType );
 
    }
    
    public boolean realRun(){
        Object obj = fNode.getUserObject();
        boolean ok = true;
        if( obj instanceof ChiefVMHost ){
            ChiefVMHost cvmh = (ChiefVMHost)obj;
            if( cvmh != null){
                obj = cvmh.getFatherNode().getUserObject();
            }
            if( obj instanceof BootHost ){
                BootHost bhost = (BootHost)obj;
                 ok = view.initor.mdb.updateVMHostInfo(bhost.getID() + "");
                if( ok ){
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
                    ArrayList<VMHostInfo> list = view.initor.mdb.getAllVMHostInfo();
                    int size = list.size();
                    for( int i=0; i<size; i++ ){
                        VMHostInfo tmp = list.get(i);
                        if( tmp != null ){
                            if( eventType == Browser.TREE_EXPAND_EVENT  ){
                                BrowserTreeNode cNode = new BrowserTreeNode( tmp,false );
                                tmp.setTreeNode( cNode );
                                tmp.setFatherNode( fNode );
                                view.addNode( fNode,cNode );
                            }else{
                                processEvent.insertSomethingToTable( tmp );
                            }
                        }
                    }
                    if( eventType == Browser.TREE_EXPAND_EVENT ){
                        view.reloadTreeNode( fNode );
                    }
                }
            } else if( obj instanceof SourceAgent ){
                SourceAgent srchost = (SourceAgent)obj;
                 ok = view.initor.mdb.updateVMHostInfo(srchost.getSrc_agnt_id() + "");
                if( ok ){
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
                    ArrayList<VMHostInfo> list = view.initor.mdb.getAllVMHostInfo();
                    int size = list.size();
                    for( int i=0; i<size; i++ ){
                        VMHostInfo tmp = list.get(i);
                        if( tmp != null ){
                            if( eventType == Browser.TREE_EXPAND_EVENT  ){
                                BrowserTreeNode cNode = new BrowserTreeNode( tmp,false );
                                tmp.setTreeNode( cNode );
                                tmp.setFatherNode( fNode );
                                view.addNode( fNode,cNode );
                            }else{
                                processEvent.insertSomethingToTable( tmp );
                            }
                        }
                    }
                    if( eventType == Browser.TREE_EXPAND_EVENT ){
                        view.reloadTreeNode( fNode );
                    }
                }
            }
        } else if( obj instanceof VMHostInfo ){
            VMHostInfo vmh = (VMHostInfo)obj;
            ok = view.initor.mdb.updateVMHostInfo( vmh.getVm_clntid() );
            if( ok ){
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
                VMHostInfo tmp = view.initor.mdb.getVMHostInfoByName(vmh.getVm_name());
                if( tmp != null ){
                            if( eventType == Browser.TREE_EXPAND_EVENT  ){
                                BrowserTreeNode cNode = new BrowserTreeNode( tmp,false );
                                tmp.setTreeNode( cNode );
                                tmp.setFatherNode( fNode );
                                view.addNode( fNode,cNode );
                            }else{
                                processEvent.insertSomethingToTable( tmp );
                            }
                        }
                
            }
        }
        return ok;
    }
}