/*
 * ProcessEventOnMirrorVol.java
 *
 * Created on July 10, 2008, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.remotemirror.ChiefRollbackHost;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.entity.ChiefCloneDiskList;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncSnapList;
import guisanboot.unlimitedIncMj.entity.ChiefUnLimitedIncSnapList;
import guisanboot.unlimitedIncMj.ui.ChiefCopyJobService;
import guisanboot.unlimitedIncMj.ui.CloneDiskListService;
import guisanboot.unlimitedIncMj.ui.UIMSnapshotListService;
import java.io.IOException;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnMirrorVol extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curMirrorVolNode = null;
    boolean isNormalMDI = true;  // 是否为 mirror disk info
    boolean isUIM_Vol = false;   // 是否为真正的UIM-volume
    boolean isCj_vol = false;    // 是否为复制任务产生的无限增量卷

    /** Creates a new instance of ProcessEventOnMirrorVol */
    public ProcessEventOnMirrorVol(){
        this( null );
    }
    public ProcessEventOnMirrorVol(SanBootView view) {
        super( ResourceCenter.TYPE_MIRROR_VOL,view );
    }
    
    private void showSnapList( BrowserTreeNode mirrorVolNode,int eventType ){
        curMirrorVolNode = mirrorVolNode;
        boolean isMirrorVol = false;
        boolean isChiefLocalUISnap = false;
        
        MirrorDiskInfo mdi = (MirrorDiskInfo)mirrorVolNode.getUserObject();
        isUIM_Vol = mdi.isUIM_Vol();
        isCj_vol = mdi.isRemoteCjVol();
System.out.println("--------->  sa's src_vol_info: " + mdi.getVolumeType() );
        BrowserTreeNode fNode = mdi.getFatherNode();
        Object chiefVol = fNode.getUserObject();
        if( chiefVol instanceof ChiefHostVolume ){
            ChiefHostVolume chiefHvol = (ChiefHostVolume)fNode.getUserObject();
            BrowserTreeNode fNode1 = chiefHvol.getFatherNode();
            SourceAgent sa = (SourceAgent)fNode1.getUserObject();

            BrowserTreeNode ffNode = sa.getFatherNode();
            Object ffObj = ffNode.getUserObject();
            if( ffObj instanceof ChiefRollbackHost ){
                isChiefLocalUISnap = true;
            }
System.out.println("--------->  sa's src_agnt_uws_id: " + sa.getSrc_agnt_uws_id() );
            if( isUIM_Vol || isCj_vol ){
                isMirrorVol = true;
            }else{
                if( sa.isNormalHost() ){
                    isMirrorVol = true;
                }else{
                    isMirrorVol = false;
                }
            }
        }else{ // ChiefRemoteFreeVol
            isMirrorVol = true;
        }
        
        if( isMirrorVol ){
            this.isNormalMDI = true;

            if( !isUIM_Vol && !isCj_vol){
                int rootid = mdi.getSnap_rootid();

                GetSnapDirectlyUnderMirrorVol thread = new GetSnapDirectlyUnderMirrorVol(
                    view,
                    mirrorVolNode,
                    this,
                    eventType,
                    rootid,
                    mdi.isCMDPProtect() && mdi.getSrc_agent_mp().toUpperCase().startsWith("C")
                );
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.getSnap"),
                    SanBootView.res.getString("View.pdiagTip.getSnap"),
                    thread
                );
            }else{
                if( eventType == Browser.TREE_EXPAND_EVENT ){
System.out.println("--------->  bef: remove all data  " );
                    view.removeAllData( curMirrorVolNode );
System.out.println("--------->  aft: remove all data " );
                    // unlimited incremental mirror snapshot list
                    if( !isChiefLocalUISnap ){
                        ChiefUnLimitedIncSnapList chiefUIMSnapList = new ChiefUnLimitedIncSnapList();
                        BrowserTreeNode chiefUIMSnapNode = new BrowserTreeNode( chiefUIMSnapList,false );
                        chiefUIMSnapList.setTreeNode( chiefUIMSnapNode );
                        chiefUIMSnapList.setFatherNode( curMirrorVolNode );
                        curMirrorVolNode.add( chiefUIMSnapNode );
                    }else{
                        ChiefLocalUnLimitedIncSnapList chiefLocalUIMSnapList = new ChiefLocalUnLimitedIncSnapList();
                        BrowserTreeNode chiefLocalUIMSnapNode = new BrowserTreeNode( chiefLocalUIMSnapList,false );
                        chiefLocalUIMSnapList.setTreeNode( chiefLocalUIMSnapNode );
                        chiefLocalUIMSnapList.setFatherNode( curMirrorVolNode );
                        curMirrorVolNode.add( chiefLocalUIMSnapNode );
                    }

                    // clone disk list
                    ChiefCloneDiskList chiefCloneDiskList = new ChiefCloneDiskList();
                    BrowserTreeNode chiefCloneDiskNode = new BrowserTreeNode( chiefCloneDiskList,false );
                    chiefCloneDiskList.setTreeNode( chiefCloneDiskNode );
                    chiefCloneDiskList.setFatherNode( curMirrorVolNode );
                    curMirrorVolNode.add( chiefCloneDiskNode );

                    if( !isCj_vol ){
                        // chief copy job list
                        ChiefCopyJobList chiefCJList = new ChiefCopyJobList();
                        BrowserTreeNode chiefCJNode = new BrowserTreeNode( chiefCJList,false );
                        chiefCJList.setTreeNode( chiefCJNode );
                        chiefCJList.setFatherNode( curMirrorVolNode );
                        curMirrorVolNode.add( chiefCJNode );
                    }
                    view.reloadTreeNode( curMirrorVolNode );
                }else{
                    view.removeRightPane();
                    view.addTable();

                    setupTableList();
                    insertSomethingToTable( null );
                }
            }
        }else{
            this.isNormalMDI = false;
            if( eventType == Browser.TREE_SELECTED_EVENT ){
                showActLinkList( mirrorVolNode,mdi.getTargetID(),eventType );
            }
        }
    }
    
    private void showActLinkList( BrowserTreeNode mdiNode,int tid,int eventType ){       
        try{
            GeneralTreeProcessThread thread = new GeneralTreeProcessThread(
                view, 
                mdiNode, 
                this,
                new GetActiveLink(
                    ResourceCenter.getCmd(
                        ResourceCenter.CMD_GET_ACTLINK
                    ) + tid ,
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
            
        }catch( IOException ex ){
            view.closeProgressDialog();
            ex.printStackTrace();
        }
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
        Object[] one;

        if( this.isNormalMDI ){
            if( !isUIM_Vol && !isCj_vol ){
                MirroredSnap snap = (MirroredSnap)obj;
                snap.setFatherNode( curMirrorVolNode );

                one = new Object[4];
                one[0] = snap;

                one[1] =  new GeneralBrowserTableCell(
                    -1,snap.snap.toString(),JLabel.LEFT
                );

                view.initor.mdb.getSnapSize( String.valueOf(snap.snap.getSnap_root_id( )) ,String.valueOf(snap.snap.getSnap_local_snapid()) );
                String size = view.initor.mdb.getSsize();
                one[2] = new GeneralBrowserTableCell(
                    -1,size,JLabel.LEFT
                );
                one[3] = new GeneralBrowserTableCell(
                    -1,snap.snap.getCreateTimeStr(),JLabel.LEFT
                );
                /*
                one[3] = newGeneralBrowserTableCell(
                    -1,snap.snap.getStatus() ,JLabel.LEFT
                );
                 */
                table.insertRow( one );
            }else{
                one = new Object[2];
                UIMSnapshotListService service = new UIMSnapshotListService();
                one[0] = service;
                one[1] = new GeneralBrowserTableCell(
                    -1, SanBootView.res.getString("common.desc.uimSnapList"),JLabel.LEFT
                );
                table.insertRow( one  );

                CloneDiskListService service1 = new CloneDiskListService();
                one[0] = service1;
                one[1] = new GeneralBrowserTableCell(
                    -1,SanBootView.res.getString("common.desc.clonediskList"),JLabel.LEFT
                );
                table.insertRow( one );

                if( !isCj_vol ){
                    ChiefCopyJobService service2 = new ChiefCopyJobService();
                    one[0] = service2;
                    one[1] = new GeneralBrowserTableCell(
                        -1,SanBootView.res.getString("common.desc.cjList"),JLabel.LEFT
                    );
                    table.insertRow( one );
                }
            }
        }else{
            ActiveLink link = (ActiveLink)obj;

            one = new Object[5];
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
            int[][] widthList;
            if( !isUIM_Vol && !isCj_vol ){
                Object[] title = new Object[]{
                    SanBootView.res.getString("View.table.snap.snapid"),
                    SanBootView.res.getString("View.table.snap.name"),
                    SanBootView.res.getString("View.table.snap.size"),
                    SanBootView.res.getString("View.table.snap.crtTime")
        //            SanBootView.res.getString("View.table.snap.status"),
                };
                table.setupTitle( title );

                widthList = new int[][]{
                    {0,100},  {1,165}, {2,155}, {3,155}
        //            , {3,65}
                };
            }else{
                Object[] title = new Object[]{
                    SanBootView.res.getString("common.table.item"),
                    SanBootView.res.getString("common.table.desc")
                };
                table.setupTitle( title );

                widthList = new int[][]{
                    {0,140},  {1,375}
                };
            }
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
