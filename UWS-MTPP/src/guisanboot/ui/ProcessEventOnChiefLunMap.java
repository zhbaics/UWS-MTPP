/*
 * ProcessEventOnChiefLunMap.java
 *
 * Created on December 10, 2004, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefLunMap extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefLMNode;
    
    /** Creates a new instance of ProcessEventOnChiefLunMap */
    public ProcessEventOnChiefLunMap(){
        this( null );
    }
    public ProcessEventOnChiefLunMap(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_LUNMAP_INDEX,view );
    }
    
    private void showLunMapList( BrowserTreeNode chiefLMNode,int eventType ){
        curChiefLMNode = chiefLMNode;
        
        ChiefLunMap chiefLm = (ChiefLunMap)chiefLMNode.getUserObject();
        BrowserTreeNode fNode = chiefLm.getFatherNode();
        Object fObj = fNode.getUserObject();
        
        int tid = -1;
        if( fObj instanceof Volume ){
            tid = ((Volume)fObj).getTargetID();
        }else if( fObj instanceof LogicalVol ){ 
            // logicalvol一定要在volumemap之前，因为前者是从后者继承过来的
            tid = view.initor.mdb.getTargetIDOnLV( (LogicalVol)fObj );
        }else if( fObj instanceof VolumeMap ){
            tid = ((VolumeMap)fObj).getVolTargetID();
        }else if( fObj instanceof View ){
            tid =((View)fObj).getTargetID();
        }else if( fObj instanceof MirrorDiskInfo ){
            tid = ((MirrorDiskInfo)fObj).getTargetID();
        }else if( fObj instanceof CloneDisk ){
            tid = ((CloneDisk)fObj).getTarget_id();
        }else {
            return;
        }
        
        if( tid == -1 ){
            return;
        }
        
        try{
            GeneralTreeProcessThread thread = new GeneralTreeProcessThread(
                view, 
                null, 
                this,
                new GetLunMap(
                    ResourceCenter.getCmd(
                        ResourceCenter.CMD_GET_LUNMAP
                    ) + tid,
                    view.getSocket()
                ),
                eventType,
                ResourceCenter.CMD_GET_LUNMAP
            );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.getLunMap"),
                SanBootView.res.getString("View.pdiagTip.getLunMap"),
                thread
            );   
        }catch(IOException ex){
            view.closeProgressDialog(); 
            ex.printStackTrace();
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefLMNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefLMNode != null ){
            showLunMapList( chiefLMNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        LunMap lm = (LunMap)obj;
        lm.setFatherNode( curChiefLMNode );
        
        Object[] one = new Object[5];
        one[0] = lm;
        
        one[1] =  new GeneralBrowserTableCell(
            -1,lm.getMask(), JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,lm.getAccessMode(),JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,lm.getSrvUser(),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,lm.getClntUser() ,JLabel.LEFT
        );
        table.insertRow( one );
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.lm.ip"),
            SanBootView.res.getString("View.table.lm.mask"),
            SanBootView.res.getString("View.table.lm.access"),
            SanBootView.res.getString("View.table.lm.srvusr"),
            SanBootView.res.getString("View.table.lm.cltusr")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,120},  {1,120}, {2,80},
            {3,95},  {4,95},
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
