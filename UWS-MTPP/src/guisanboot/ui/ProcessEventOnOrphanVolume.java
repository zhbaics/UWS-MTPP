/*
 * ProcessEventOnOrphanVolume.java
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
import guisanboot.unlimitedIncMj.entity.ChiefCloneDiskList;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnOrphanVolume extends GeneralProcessEventForSanBoot {
    
    /** Creates a new instance of ProcessEventOnOrphanVolume */
    public ProcessEventOnOrphanVolume(){
        this( null );
    }
    public ProcessEventOnOrphanVolume(SanBootView view) {
        super( ResourceCenter.TYPE_ORPHAN_VOL,view );
    }
    
    private void showActLinkList( BrowserTreeNode orphVolNode,int eventType ){
        Volume vol = (Volume)orphVolNode.getUserObject();
        
        try{
            GeneralTreeProcessThread thread = new GeneralTreeProcessThread(
                view, 
                orphVolNode, 
                this,
                new GetActiveLink(
                    ResourceCenter.getCmd(
                        ResourceCenter.CMD_GET_ACTLINK
                    ) + vol.getTargetID(),
                    view.getSocket()
                ),
                eventType,
                ResourceCenter.CMD_GET_ACTLINK
            );
            view.startupProcessDiag( 
                SanBootView.res.getString("View.pdiagTitle.getActlink"),
                SanBootView.res.getString("View.pdiagTip.getActlink"), 
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
        
        BrowserTreeNode orphVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( orphVolNode != null ){
            showActLinkList( orphVolNode,Browser.TREE_SELECTED_EVENT );   
        }
    }

    @Override
    public void processTreeExpand(TreePath path){
        BrowserTreeNode orphVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( orphVolNode != null ){
            
            //如果节点上没有ChiefLM/ChiefSnapshot/chiefuimvollist/chiefclonedisk,就添加它们;
            if( orphVolNode.getChildCount() != 5 ){
                // 准备lunmap list node
                ChiefLunMap chiefLm = new ChiefLunMap();
                BrowserTreeNode chiefLmNode = new BrowserTreeNode( chiefLm, true );
                chiefLm.setTreeNode( chiefLmNode );
                chiefLm.setFatherNode( orphVolNode );
                orphVolNode.add( chiefLmNode );

                // 准备snapshot list node
                ChiefSnapshot chiefSnap = new ChiefSnapshot();
                BrowserTreeNode chiefSnapNode = new BrowserTreeNode( chiefSnap,false );
                chiefSnap.setTreeNode( chiefSnapNode );
                chiefSnap.setFatherNode( orphVolNode );
                orphVolNode.add( chiefSnapNode );

                // 准备mj list node
                ChiefMirrorJobList chiefMj = new ChiefMirrorJobList();
                BrowserTreeNode chiefMjNode = new BrowserTreeNode( chiefMj,false );
                chiefMj.setTreeNode( chiefMjNode );
                chiefMj.setFatherNode( orphVolNode );
                orphVolNode.add( chiefMjNode );

                // unlimited incremental mirror vol list
                ChiefLocalUnLimitedIncMirrorVolList chiefUIMVolList = new ChiefLocalUnLimitedIncMirrorVolList();
                BrowserTreeNode chiefUIMVolNode = new BrowserTreeNode( chiefUIMVolList,false );
                chiefUIMVolList.setTreeNode( chiefUIMVolNode );
                chiefUIMVolList.setFatherNode( orphVolNode );
                orphVolNode.add( chiefUIMVolNode );

                // clone disk list
                ChiefCloneDiskList chiefCloneDiskList = new ChiefCloneDiskList();
                BrowserTreeNode chiefCloneDiskNode = new BrowserTreeNode( chiefCloneDiskList,false );
                chiefCloneDiskList.setTreeNode( chiefCloneDiskNode );
                chiefCloneDiskList.setFatherNode( orphVolNode );
                orphVolNode.add( chiefCloneDiskNode );

                view.reloadTreeNode( orphVolNode);
            }
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        ActiveLink link = (ActiveLink)obj;
        
        Object[] one = new Object[5];
        one[0] = link;
        
        one[1] =  new GeneralBrowserTableCell(
            -1,link.getInitor(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,link.getTargetID()+"",JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,link.getSend(),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,link.getRec() ,JLabel.LEFT
        );
        table.insertRow( one );
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.actlink.ip"),
            SanBootView.res.getString("View.table.actlink.initor"),
            SanBootView.res.getString("View.table.actlink.targetID"),
            SanBootView.res.getString("View.table.actlink.send"),
            SanBootView.res.getString("View.table.actlink.rec")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,100},  {1,245}, {2,80},
            {3,65},  {4,95},
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
}
