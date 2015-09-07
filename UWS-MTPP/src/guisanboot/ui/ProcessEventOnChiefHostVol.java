/*
 * ProcessEventOnChiefHostVol.java
 *
 * Created on July 28, 2008, 3:25 PM
 */

package guisanboot.ui;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.cmdp.entity.WorkModeForMirroring;
import guisanboot.cmdp.service.GetVolumeStatusThread;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;

import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefHostVol extends GeneralProcessEventForSanBoot { 
    ArrayList list;
    ArrayList<InitProgressRecord> initRecList;
    ArrayList<WorkModeForMirroring> workModeOfMirroring;
    Object hostObj;
    boolean isCMDP;
    
    /** Creates a new instance of ProcessEventOnChiefHostVol */
    public ProcessEventOnChiefHostVol(){
        this( null ); 
    }
    public ProcessEventOnChiefHostVol(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_HOST_VOL,view );
    }
    
    @Override public void processTreeSelection(TreePath path){   
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();

        this.isCMDP = false;

        // 布置table中的内容
        BrowserTreeNode chiefHostVolNode = (BrowserTreeNode)path.getLastPathComponent();
        hostObj = view.getHostObjFromChiefHostVol( chiefHostVolNode );
        if( hostObj instanceof BootHost ){
            BootHost host = (BootHost)hostObj;
            if( host.isWinHost() ){
                this.isCMDP = host.isCMDPProtect();
                if( this.isCMDP ){
                    showVolMapList( chiefHostVolNode,host.getIP(),host.getPort() );
                }else{
                    showVolMapList( chiefHostVolNode,"",1 );
                }
            }else{ // non-windows host
                showLVList( chiefHostVolNode );
            }
        }else if( hostObj instanceof Cluster ){
            Cluster cluster = (Cluster)hostObj;
            if( cluster.isWinHost() ){
                this.isCMDP = cluster.isCMDPProtect();
                if( this.isCMDP ){
                    showVolMapListForCluster( chiefHostVolNode,cluster );
                }else{
                    showVolMapList( chiefHostVolNode,"",1 );
                }
            }else{ // non-windows host
                showLVList( chiefHostVolNode );
            }
        }else{
            SourceAgent sa = (SourceAgent)hostObj;
            if( sa.isNormalHost() ){
                showMirrorVolList( chiefHostVolNode,Browser.TREE_SELECTED_EVENT );
            }else{
                if( sa.isWinHost() ){
                    showVolMapList( chiefHostVolNode,"",1 );
                }else{ // non-windows host
                    showLVList( chiefHostVolNode );
                }
            }
        }
    }
    
    @Override public void processTreeExpand( TreePath path ){
        // 布置table中的内容
        BrowserTreeNode chiefHostVolNode = (BrowserTreeNode)path.getLastPathComponent();
        hostObj = view.getHostObjFromChiefHostVol( chiefHostVolNode );
        if( hostObj instanceof SourceAgent ){
            SourceAgent sa = (SourceAgent)hostObj;
            if( sa.isNormalHost() ){
                showMirrorVolList( chiefHostVolNode,Browser.TREE_EXPAND_EVENT );
            }
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    //////////////////////////////////////////////////////////
    //
    //    click "SourceAgent"
    //
    //////////////////////////////////////////////////////////
    private void showMirrorVolList( BrowserTreeNode chiefHostVolNode,int eventType ){             
        removeAllRow();
        setupTableList( );
        
        SourceAgent srcAgnt = (SourceAgent)hostObj;
        int srcAgntID = srcAgnt.getSrc_agnt_id();
        GetMirrorVolUnderSrcAgnt thread = new GetMirrorVolUnderSrcAgnt(
            view, 
            chiefHostVolNode,
            this,
            eventType, 
            srcAgntID
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getMirrorVol"),
            SanBootView.res.getString("View.pdiagTitle.getMirrorVol"),
            thread
        );
    }
    
    ///////////////////////////////////////////////////////////////
    // 
    //      click "BootHost" 
    //
    ///////////////////////////////////////////////////////////////
    private void showVolMapListForCluster( BrowserTreeNode chiefHostVolNode,Cluster cluster ){
        removeAllRow();
        setupTableList();

        int childCnt = chiefHostVolNode.getChildCount();
        Object[] volObjList = new Object[childCnt];
        for( int i=0; i<childCnt; i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHostVolNode.getChildAt(i);
            volObjList[i] = node.getUserObject();
        }

        GetVolumeStatusThread thread = new GetVolumeStatusThread(
            view,volObjList,cluster
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getVolState"),
            SanBootView.res.getString("View.pdiagTip.getVolState"),
            thread
        );

        boolean retval = thread.getRetVal();
        if( !retval ){
            JOptionPane.showMessageDialog( view,
                ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VOL )+" : "+
                thread.getErrorMsg()
            );
            list = new ArrayList();
        }else{
            list = thread.getRet();
        }

        if( this.isCMDP ){
            this.initRecList = thread.getSyncState();
            this.workModeOfMirroring = thread.getWorkModeofMirroring();
        }

        for( int i=0;i<childCnt;i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHostVolNode.getChildAt(i);
            insertSomethingToTable( node.getUserObject() );
        }
    }

    private void showVolMapList( BrowserTreeNode chiefHostVolNode,String ip,int port ){
        removeAllRow();
        setupTableList();
        
        int childCnt = chiefHostVolNode.getChildCount();
        Object[] volObjList = new Object[childCnt];
        for( int i=0; i<childCnt; i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHostVolNode.getChildAt(i);
            volObjList[i] = node.getUserObject();
        }
        
        GetVolumeStatusThread thread = new GetVolumeStatusThread(
            view,volObjList,ip,port
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getVolState"),
            SanBootView.res.getString("View.pdiagTip.getVolState"),
            thread
        );
        
        boolean retval = thread.getRetVal();
        if( !retval ){
            JOptionPane.showMessageDialog( view,
                ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VOL )+" : "+
                thread.getErrorMsg()
            );
            list = new ArrayList();
        }else{
            list = thread.getRet();
        }
        
        if( this.isCMDP ){
            this.initRecList = thread.getSyncState();
            this.workModeOfMirroring = thread.getWorkModeofMirroring();
        }
        
        for( int i=0;i<childCnt;i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHostVolNode.getChildAt(i);  
            insertSomethingToTable( node.getUserObject() );
        }
    }
    
    private void showLVList( BrowserTreeNode chiefHostVolNode ){    
        removeAllRow();
        setupTableList1(); 
        
        GetAllPhyVolThread thread = new GetAllPhyVolThread(
            view
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getVol"),
            SanBootView.res.getString("View.pdiagTip.getVol"),
            thread
        );
        
        boolean retval = thread.getRetVal();
        if( !retval ){
            JOptionPane.showMessageDialog(view,
                ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VOL )+" : "+
                thread.getErrorMsg()
            );
            list = new ArrayList();
        }else{
            list = thread.getRet();
        }  
        
        int childCnt = chiefHostVolNode.getChildCount();
        for( int i=0;i<childCnt;i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHostVolNode.getChildAt(i);        
            insertSomethingToTable1( node.getUserObject() );
        }
    }
    
    public void insertSomethingToTable1( Object obj ){
        Volume vol;
        VolumeMap tgt;
        Object[] one;
        boolean founded = false;
        
        boolean isSrcAgnt = !( hostObj instanceof BootHost );
        if( !isSrcAgnt ){
            LogicalVol lv = (LogicalVol)obj;
            int size = list.size();
            for( int i=0; i<size; i++ ){
                vol = (Volume)list.get(i);
                tgt = view.initor.mdb.getTargetOnLV( lv );
                if( vol.getTargetID() == view.initor.mdb.getTargetIDOnLV( lv ) ){
                    founded = true;                
                    one = new Object[6];
                    one[0] = lv;
                    one[1] = new GeneralBrowserTableCell(  -1,lv.getLVName(),JLabel.LEFT );
                    one[2] = new GeneralBrowserTableCell(  -1,vol.getTargetID()+"",JLabel.LEFT );
                    one[3] = new GeneralBrowserTableCell(  -1,vol.getCapStr(),JLabel.LEFT );
                    one[4] = new GeneralBrowserTableCell(  -1,tgt.getMaxSnapNum()+"",JLabel.LEFT );
                    one[5] = new GeneralBrowserTableCell(  -1,vol.getStatus(),JLabel.LEFT );

                    table.insertRow(one);
                    break;
                }
            }

            if( !founded ){
                one = new Object[6];
                one[0] = lv;
                one[1] = new GeneralBrowserTableCell( -1,lv.getLVName(),JLabel.LEFT );
                one[2] = new GeneralBrowserTableCell( -1,view.initor.mdb.getTargetIDOnLV( lv )+"",JLabel.LEFT );
                one[3] = new GeneralBrowserTableCell( -1,"N/A",JLabel.LEFT );
                tgt = view.initor.mdb.getTargetOnLV( lv );
                if( tgt != null ){
                    one[4] = new GeneralBrowserTableCell( -1,tgt.getMaxSnapNum()+"",JLabel.LEFT );
                }else{
                    one[4] = new GeneralBrowserTableCell( -1,"N/A",JLabel.LEFT );
                }
                one[5] = new GeneralBrowserTableCell( -1,"N/A",JLabel.LEFT );
                table.insertRow(one);
            }
        }else{ // rollbacked host
            MirrorDiskInfo mdi = (MirrorDiskInfo)obj; 
            int size = list.size();
            for( int i=0; i<size; i++ ){
                vol = (Volume)list.get(i);
                if( vol.getTargetID() == mdi.getTargetID() ){
                    founded = true;                
                    one = new Object[6];
                    one[0] = mdi;
                    one[1] = new GeneralBrowserTableCell(  -1,mdi.getSrc_agent_mp(),JLabel.LEFT );
                    one[2] = new GeneralBrowserTableCell(  -1,mdi.getSrc_snap_name(),JLabel.LEFT );
                    one[3] = new GeneralBrowserTableCell(  -1,MirrorJob.getTypeString( mdi.getVolumeType() ),JLabel.LEFT );
                    one[4] = new GeneralBrowserTableCell(  -1,mdi.getTargetID()+"",JLabel.LEFT );
                    one[5] = new GeneralBrowserTableCell(  -1,vol.getStatus(),JLabel.LEFT );
                    table.insertRow(one);
                    break;
                }
            }

            if( !founded ){
                one = new Object[6];
                one[0] = mdi;
                one[1] = new GeneralBrowserTableCell( -1,mdi.getSrc_agent_mp(),JLabel.LEFT );
                one[2] = new GeneralBrowserTableCell( -1,mdi.getSrc_snap_name(),JLabel.LEFT );
                one[3] = new GeneralBrowserTableCell( -1,MirrorJob.getTypeString( mdi.getVolumeType() ),JLabel.LEFT );
                if( mdi.getVolumeType() == MirrorJob.MJ_TYPE_REMOTE_TRACK_JOB ||
                    mdi.getVolumeType() == MirrorJob.MJ_TYPE_REMOTE_COPY_JOB
                ){
                    one[4] = new GeneralBrowserTableCell( -1,"",JLabel.LEFT );
                    one[5] = new GeneralBrowserTableCell( -1,"",JLabel.LEFT );
                }else{
                    one[4] = new GeneralBrowserTableCell( -1,mdi.getTargetID()+"",JLabel.LEFT );
                    one[5] = new GeneralBrowserTableCell( -1,"N/A",JLabel.LEFT );
                }
                table.insertRow(one);
            }
        }
    }

    private InitProgressRecord getInitRecord(String driveLetter ){
        int size = this.initRecList.size();
        for( int i=0; i<size; i++ ){
            InitProgressRecord rec = initRecList.get(i);
            if( rec.getDisk().equals( driveLetter ) ){
                return rec;
            }
        }
        return null;
    }

    private WorkModeForMirroring getWorkModeOfMirroring( String driveLetter ){
        int size = this.workModeOfMirroring.size();
        for( int i=0; i<size; i++ ){
            WorkModeForMirroring mode  = workModeOfMirroring.get(i);
            if( mode.getDisk().equals( driveLetter ) ){
                return mode;
            }
        }
        return null;
    }
    
    public void insertSomethingToTable( Object obj ){
        Volume vol;
        BootHost host;
        Object[] one;
        boolean founded = false;
        boolean isMgStart = false;
        
        if( obj instanceof VolumeMap ){
            VolumeMap volMap = (VolumeMap)obj;
            host = view.initor.mdb.getHostFromVectorOnID( volMap.getVolClntID() );
            if( ( host!=null ) && host.isCluster() )volMap.setVol_Cluster_Identity( host.getLastItemOfIp() );
            
            if( volMap.isCMDPProtect() ){
                WorkModeForMirroring workMode = this.getWorkModeOfMirroring( volMap.getVolDiskLabel() );
                isMgStart = ( workMode == null )? false : workMode.isIsMgStart();
            }
            
            String syncState = "";
            if( volMap.isCMDPProtect() ){
                InitProgressRecord rec = this.getInitRecord( volMap.getVolDiskLabel() );
                syncState = " , "+( (rec != null)? InitProgressRecord.getStateString( rec.getState() ) :"" );
            }
            
            int size = list.size();
            for( int i=0; i<size; i++ ){
                vol = (Volume)list.get(i);
                if( vol.getTargetID() == volMap.getVolTargetID() ){
                    founded = true;
                    one = new Object[10];
                   
                    one[0] = volMap;
                    one[1] = new GeneralBrowserTableCell( -1,volMap.getVolName()+"",JLabel.LEFT );
                    one[2] = new GeneralBrowserTableCell( -1,volMap.getVolTargetID()+"",JLabel.LEFT );
                    one[3] = new GeneralBrowserTableCell( -1,vol.getCapStr(),JLabel.LEFT );

                    PPProfile prof = view.initor.mdb.getBelongedDg( volMap.getVolClntID(),volMap.getVolDiskLabel() );
                    if( prof != null ){
                        PPProfileItem master_disk = prof.getMainDiskItem();
                        one[4] = new GeneralBrowserTableCell( -1,master_disk.getMg().getMg_max_snapshot()+"",JLabel.LEFT );
                    }else{
                        if( volMap.getVol_mgid() > 0 ){
                            MirrorGrp mg = view.initor.mdb.getMGFromVectorOnID( volMap.getVol_mgid() );
                            if( mg != null ){
                                one[4] = new GeneralBrowserTableCell( -1,mg.getMg_max_snapshot()+"",JLabel.LEFT );
                            }else{
                                one[4] = new GeneralBrowserTableCell( -1,"0",JLabel.LEFT );
                            }
                        }else{
                            one[4] = new GeneralBrowserTableCell( -1,volMap.getMaxSnapNum()+"",JLabel.LEFT );
                        }
                    }

                    one[5] = new GeneralBrowserTableCell( -1,
                        volMap.getProtectString(),
                        JLabel.LEFT
                    );
                    if( vol.isUcsDisk() && vol.getSnap_localid() == 0) {
                        one[6] = new GeneralBrowserTableCell( -1,SanBootView.res.getString("common.status.online")+syncState,JLabel.LEFT );
                    } else {
                        one[6] = new GeneralBrowserTableCell( -1,vol.getStatus()+syncState,JLabel.LEFT );
                    }
                    if( volMap.isCMDPProtect() ){
                        // 只有vol_info中设置为“同步” 且 mg对应进程启动，才能认为可以“自动创建快照”  2011.9.6
                        one[7] = new GeneralBrowserTableCell( -1,
                            ( !volMap.isWorkStateASync() && isMgStart ) ? SanBootView.res.getString("common.button.up"):SanBootView.res.getString("common.button.stop"),
                            JLabel.LEFT
                        );
                    }else{
                        one[7] = new GeneralBrowserTableCell( -1,"",JLabel.LEFT );
                    }
                    one[8] = new GeneralBrowserTableCell( -1,volMap.getVol_uuid(),JLabel.LEFT );
                    one[9] = new GeneralBrowserTableCell( -1,volMap.getVolDesc(),JLabel.LEFT );
                    table.insertRow(one);
                    break;
                }
            }

            if( !founded ){
                one = new Object[10];
                one[0] = volMap;
                one[1] = new GeneralBrowserTableCell( -1,volMap.getVolName(),JLabel.LEFT );               
                one[2] = new GeneralBrowserTableCell( -1,volMap.getVolTargetID()+"",JLabel.LEFT );              
                one[3] = new GeneralBrowserTableCell( -1,"N/A",JLabel.LEFT );              
                one[4] = new GeneralBrowserTableCell( -1,volMap.getMaxSnapNum()+"",JLabel.LEFT );
                one[5] = new GeneralBrowserTableCell( -1,
                    volMap.getProtectString(),
                    JLabel.LEFT
                );
                one[6] = new GeneralBrowserTableCell( -1,"N/A" + syncState,JLabel.LEFT );
                if( volMap.isCMDPProtect() ){
                    // 只有vol_info中设置为“同步” 且 mg对应进程启动，才能认为可以“自动创建快照”  2011.9.6
                    one[7] = new GeneralBrowserTableCell( -1,
                        ( !volMap.isWorkStateASync() && isMgStart ) ? SanBootView.res.getString("common.button.up"):SanBootView.res.getString("common.button.stop"),
                        JLabel.LEFT
                    );
                }else{
                    one[7] = new GeneralBrowserTableCell( -1,"",JLabel.LEFT );
                }
                one[8] = new GeneralBrowserTableCell( -1,volMap.getVol_uuid(),JLabel.LEFT );
                one[9] = new GeneralBrowserTableCell( -1,volMap.getVolDesc(),JLabel.LEFT );
                table.insertRow(one);
            }
        }else{            
            SourceAgent sa = ( SourceAgent )hostObj;
            if( sa.isRollbackedHost() ){
                MirrorDiskInfo mdi = (MirrorDiskInfo)obj;
                int size = list.size();
                for( int i=0; i<size; i++ ){
                    vol = (Volume)list.get(i);
                    if( vol.getTargetID() == mdi.getTargetID() ){
                        founded = true;                
                        one = new Object[7];
                        one[0] = mdi;
                        one[1] = new GeneralBrowserTableCell(  -1,mdi.getSrc_agent_mp(),JLabel.LEFT );
                        one[2] = new GeneralBrowserTableCell(  -1,mdi.getSrc_snap_name(),JLabel.LEFT );
                        one[3] = new GeneralBrowserTableCell( -1,
                            mdi.getProtectString(),
                            JLabel.LEFT
                        );
                        one[4] = new GeneralBrowserTableCell(  -1,MirrorJob.getTypeString( mdi.getVolumeType() ),JLabel.LEFT );
                        one[5] = new GeneralBrowserTableCell(  -1,mdi.getTargetID()+"",JLabel.LEFT );
                        one[6] = new GeneralBrowserTableCell(  -1,vol.getStatus(),JLabel.LEFT );
                        table.insertRow(one);
                        break;
                    }
                }

                if( !founded ){
                    one = new Object[7];
                    one[0] = mdi;
                    one[1] = new GeneralBrowserTableCell( -1,mdi.getSrc_agent_mp(),JLabel.LEFT );
                    one[2] = new GeneralBrowserTableCell( -1,mdi.getSrc_snap_name(),JLabel.LEFT );
                    one[3] = new GeneralBrowserTableCell( -1,
                        mdi.getProtectString(),
                        JLabel.LEFT
                    );
                    one[4] = new GeneralBrowserTableCell( -1,MirrorJob.getTypeString( mdi.getVolumeType() ),JLabel.LEFT );
                    if( mdi.getVolumeType() == MirrorJob.MJ_TYPE_REMOTE_TRACK_JOB ||
                        mdi.getVolumeType() == MirrorJob.MJ_TYPE_REMOTE_COPY_JOB
                    ){
                        one[5] = new GeneralBrowserTableCell( -1,"",JLabel.LEFT );
                        one[6] = new GeneralBrowserTableCell( -1,"",JLabel.LEFT );
                    }else{
                        one[5] = new GeneralBrowserTableCell( -1,mdi.getTargetID()+"",JLabel.LEFT );
                        one[6] = new GeneralBrowserTableCell( -1,"N/A",JLabel.LEFT );
                    }
                    table.insertRow(one);
                }
            }else{
                MirrorDiskInfo mdi = (MirrorDiskInfo)obj;
                one = new Object[5];
                one[0] = mdi;
                one[1] = new GeneralBrowserTableCell( -1,mdi.getSrc_agent_mp(),JLabel.LEFT );
                one[2] = new GeneralBrowserTableCell( -1,mdi.getSrc_snap_name(),JLabel.LEFT );
                one[3] = new GeneralBrowserTableCell( -1,
                    mdi.getProtectString(),
                    JLabel.LEFT
                );
                one[4] = new GeneralBrowserTableCell( -1,MirrorJob.getTypeString( mdi.getVolumeType() ),JLabel.LEFT );
                table.insertRow(one);
            }
        }
    }
    
    public void setupTableList( ){
        Object[] title;
        int[] widthList;

        boolean isHost = hostObj instanceof BootHost;
        boolean isCluster =  hostObj instanceof Cluster;
        if( isHost || isCluster ){// boothost or cluster
            /*
            if( host.isMTPPProtect() ){
                title = new Object[]{
                    SanBootView.res.getString("View.table.volmap.fs"),
                    SanBootView.res.getString("View.table.volmap.name"),
                    SanBootView.res.getString("View.table.volmap.targetid"),
                    SanBootView.res.getString("View.table.volmap.size"),
                    SanBootView.res.getString("View.table.volmap.maxSnap"),
                    SanBootView.res.getString("View.table.volmap.status"),
                    SanBootView.res.getString("View.table.volmap.desc")
                };
                table.setupTitle( title );

                widthList = new int[]{
                  85,150,95,85,85,175,150
                };
            }else{
             */
                title = new Object[]{
                    SanBootView.res.getString("View.table.volmap.fs"),
                    SanBootView.res.getString("View.table.volmap.name"),
                    SanBootView.res.getString("View.table.volmap.targetid"),
                    SanBootView.res.getString("View.table.volmap.size"),
                    SanBootView.res.getString("View.table.volmap.maxSnap"),
                    SanBootView.res.getString("View.table.host.protecttype"),
                    SanBootView.res.getString("View.table.volmap.status"),
                    SanBootView.res.getString("View.table.volmap.autoCrtSnap"),
                    SanBootView.res.getString("View.table.volmap.uuid"),
                    SanBootView.res.getString("View.table.volmap.desc")
                };
                table.setupTitle( title );

                widthList = new int[]{
                  85,150,95,85,85, 85, 175,90,300,150
                };
            //}
            table.setupTableColumnWidth( widthList );
            table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
            table.getTableHeader().setReorderingAllowed( false );
        }else{ // sourceagent
            SourceAgent sa = (SourceAgent)hostObj;
            if( sa.isNormalHost() ){
                title = new Object[]{
                    SanBootView.res.getString("View.table.volmap.id"),
                    SanBootView.res.getString("View.table.volmap.fs"),
                    SanBootView.res.getString("View.table.volmap.name"),
                    SanBootView.res.getString("View.table.host.protecttype"),
                    SanBootView.res.getString("View.table.volmap.type")
                };
                table.setupTitle( title );

                widthList = new int[]{
                    70,85,150,85,100
                };
            }else{ // rollbacked host
                title = new Object[]{
                    SanBootView.res.getString("View.table.volmap.id"),
                    SanBootView.res.getString("View.table.volmap.fs"),
                    SanBootView.res.getString("View.table.volmap.name"),
                    SanBootView.res.getString("View.table.host.protecttype"),
                    SanBootView.res.getString("View.table.volmap.type"),
                    SanBootView.res.getString("View.table.volmap.targetid"),
                    SanBootView.res.getString("View.table.volmap.status")                       
                };
                table.setupTitle( title );

                widthList = new int[]{
                    70,85,150,85,95,95,80
                };
            }
            table.setupTableColumnWidth( widthList );
            table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
            table.getTableHeader().setReorderingAllowed( false );
        }
    }    
    
    public void setupTableList1(){
        Object[] title;
        int[][] widthList;
        
        if( hostObj instanceof BootHost ){
            title = new Object[]{
                SanBootView.res.getString("View.table.volmap.fs"),
                SanBootView.res.getString("View.table.volmap.name"),
                SanBootView.res.getString("View.table.volmap.targetid"),
                SanBootView.res.getString("View.table.volmap.size"),
                SanBootView.res.getString("View.table.volmap.maxSnap"),
                SanBootView.res.getString("View.table.volmap.status"),
            };
            table.setupTitle( title );

            widthList = new int[][]{
              {0,85},  {1,95}, {2,95},{3,85},{4,85},{5,80}
            };
            table.setupTableColumnWidth( widthList );
            table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
            table.getTableHeader().setReorderingAllowed( false );
        }else{
            SourceAgent sa = (SourceAgent)hostObj;
            if( sa.isNormalHost() ){
                title = new Object[]{
                    SanBootView.res.getString("View.table.volmap.id"),
                    SanBootView.res.getString("View.table.volmap.fs"),
                    SanBootView.res.getString("View.table.volmap.name"),
                    SanBootView.res.getString("View.table.volmap.type")
                };
                table.setupTitle( title );

                widthList = new int[][]{
                    {0,70},{1,85},{2,150},{3,100}
                };
            }else{ // rollbacked host
                title = new Object[]{
                    SanBootView.res.getString("View.table.volmap.id"),
                    SanBootView.res.getString("View.table.volmap.fs"),
                    SanBootView.res.getString("View.table.volmap.name"),
                    SanBootView.res.getString("View.table.volmap.type"),
                    SanBootView.res.getString("View.table.volmap.targetid"),
                    SanBootView.res.getString("View.table.volmap.status")                       
                };
                table.setupTitle( title );

                widthList = new int[][]{
                    {0,70},{1,85},{2,150},{3,95},{4,95},{5,80}
                };
            }
            table.setupTableColumnWidth( widthList );
            table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
            table.getTableHeader().setReorderingAllowed( false );
        }
    }    
}
