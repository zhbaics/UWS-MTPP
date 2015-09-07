/*
 * ProcessEventOnUnlimitedIncMirrorVol.java
 *
 * Created on Dec 14, 2009, 2:26 PM
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.ui.*;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.model.GetSnapDirectlyUnderUnlimitedIncMirrorVol;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnUnlimitedIncMirrorVol extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curMirrorVolNode = null;
    boolean isNormalMDI = true;

    /** Creates a new instance of ProcessEventOnUnlimitedIncMirrorVol */
    public ProcessEventOnUnlimitedIncMirrorVol(){
        this( null );
    }
    public ProcessEventOnUnlimitedIncMirrorVol( SanBootView view ) {
        super( ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_VOL,view );
    }

    public ProcessEventOnUnlimitedIncMirrorVol( int type ,SanBootView view){
        super( type,view );
    }

    public void realShowSnapList( int rootid,BrowserTreeNode mirrorVolNode,int eventType,int type,boolean isCmdp ){
        GetSnapDirectlyUnderUnlimitedIncMirrorVol thread = new GetSnapDirectlyUnderUnlimitedIncMirrorVol(
            view,
            mirrorVolNode,
            this,
            eventType,
            rootid,
            type,
            isCmdp
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getSnap"),
            SanBootView.res.getString("View.pdiagTip.getSnap"),
            thread
        );
    }

    public void showSnapList( BrowserTreeNode mirrorVolNode,int eventType ){
        curMirrorVolNode = mirrorVolNode;        
        MirrorDiskInfo mdi = (MirrorDiskInfo)mirrorVolNode.getUserObject();
        BrowserTreeNode chiefUimIncMVolNode = mdi.getFatherNode();
        ChiefLocalUnLimitedIncMirrorVolList chiefUimIncVol = (ChiefLocalUnLimitedIncMirrorVolList) chiefUimIncMVolNode.getUserObject();
        BrowserTreeNode volObjNode = chiefUimIncVol.getFatherNode();
        Object volObj = volObjNode.getUserObject();
        boolean isCmdp = false;
        if( volObj instanceof VolumeMap ){
            VolumeMap volMap = (VolumeMap)volObj;
            BrowserTreeNode chiefHostVolNode = volMap.getFatherNode();
            ChiefHostVolume chiefHostVol = (ChiefHostVolume)chiefHostVolNode.getUserObject();
            BrowserTreeNode hostNode = chiefHostVol.getFatherNode();
            BootHost host = (BootHost)hostNode.getUserObject();
            isCmdp = host.isCMDPProtect() && mdi.getSrc_agent_mp().substring(0,1).toUpperCase().equals( "C" );
        }
        realShowSnapList( mdi.getSnap_rootid(),mirrorVolNode,eventType,ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1,isCmdp );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable();

        BrowserTreeNode mirrorVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( mirrorVolNode != null ){
            showSnapList( mirrorVolNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    @Override public void processTreeExpand(TreePath path){
        BrowserTreeNode mirrorVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( mirrorVolNode != null ){
            showSnapList( mirrorVolNode, Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        if( this.isNormalMDI ){
            UnlimitedIncMirroredSnap snap = (UnlimitedIncMirroredSnap)obj;
            snap.setFatherNode( curMirrorVolNode );

            Object[] one = new Object[3];
            one[0] = snap;

            one[1] =  new GeneralBrowserTableCell(
                -1,snap.snap.toString(),JLabel.LEFT
            );
            one[2] = new GeneralBrowserTableCell(
                -1,snap.snap.getCreateTimeStr(),JLabel.LEFT
            );
            /*
            one[3] = new GeneralBrowserTableCell(
                -1,snap.snap.getStatus() ,JLabel.LEFT
            );
             */   
            table.insertRow( one );
        }else{
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
                -1,link.getSend()+"",JLabel.LEFT
            );
            one[4] = new GeneralBrowserTableCell(
                -1,link.getRec()+"" ,JLabel.LEFT
            );
            table.insertRow( one );
        }    
    }
    
    public void setupTableList(){    
        if( this.isNormalMDI ){
            Object[] title = new Object[]{
                SanBootView.res.getString("View.table.snap.snapid"),
                SanBootView.res.getString("View.table.snap.name"),
                SanBootView.res.getString("View.table.snap.crtTime")
    //            SanBootView.res.getString("View.table.snap.status"),
            };
            table.setupTitle( title );

            int[][] widthList = new int[][]{
                {0,100},  {1,165}, {2,155}
    //            , {3,65}
            };
            table.setupTableColumnWidth(widthList);
            table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
            table.getTableHeader().setReorderingAllowed( false );
        }else{
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
}
