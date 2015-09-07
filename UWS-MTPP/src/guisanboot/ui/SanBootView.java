/*
 * SanBootView.java
 *
 * Created on 2006/3/10,afternoon 5:01
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.util.*;
import java.net.*;

import mylib.UI.*;
import mylib.tool.*;
import guisanboot.*;
import guisanboot.audit.ctrl.AuditCenter;
import guisanboot.cmdp.service.ProcessEventOnChiefPPProfile;
import guisanboot.cmdp.service.ProcessEventOnPPProfile;
import guisanboot.res.*;
import guisanboot.data.*;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.ui.ProcessEvent.*;
import guisanboot.logging.*;
import guisanboot.remotemirror.ProcessEventOnChiefRollbackHost;
import guisanboot.remotemirror.service.ProcessEventOnChiefMjScheduler;
import guisanboot.remotemirror.service.ProcessEventOnMjScheduler;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import guisanboot.unlimitedIncMj.service.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import guisanboot.audit.data.*;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.service.ProcessEventOnChiefMemberNode;
import guisanboot.cluster.ui.ProcessEventOnCluster;
import guisanboot.vmmanage.service.ProcessEventOnVMHostInfo;

/**
 * 界面操作子系统
 * @author Administrator
 */
public class SanBootView extends Browser{ 
    public MenuAndBtnCenterForMainUi mbCenter;
    public InitApp initor;
    public static InitApp staticInitor;
    public static JNIUtil util = null;
    private boolean addTable = true;
    public static MyLogger log = null;
    public AuditCenter audit = null;
    
    // 初始化文字资源
    public static Internationalization res = new Internationalization("guisanboot.res.SBBundle");
    
    public final static int aWIDTH  = 750;
    public final static int aHEIGHT = 400;
    
    public int mode; // release or demo
    public static int netBootMode;
    public boolean isSupportCMDP = false;
    public boolean hasDP = true; //has date duplication function?
    
    private Hashtable<Integer,Volume> unSelectedVolInfo = new Hashtable<Integer,Volume>();
    private Hashtable<Integer,Volume> bindedVolInfo = new Hashtable<Integer,Volume>();
    
    /** Creates a new instance of SanBootView */
    public SanBootView( int mode ) {
        super();
        initComponents();
        
        try{
            myInit( mode );  
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void initComponents() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
    }                        
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {                          
        System.exit(0);
    }                         
    
    public void myInit( int _mode ) throws Exception {
        mode = _mode;
        
        table = this.getTable();
        table.setRowHeight(18);
         
        util = new JNIUtil();
        
        // 布置框架中的 menu 和 speedbar
        mbCenter = new MenuAndBtnCenterForMainUi( this );
        mbCenter.init();
        
        initor = new InitApp( this ); // 获取admin option选项
        staticInitor = initor;

        audit = new AuditCenter();
        audit.setMenuAndBtnCenter( mbCenter );
        audit.setView( this );

        // 设置本地语言
        GUIAdminOptGlobal global = initor.adminOpt.getGlobal();
        String lang = global.getLang();
        String region = "CN";
        if( lang.equals("en") ){
            region="US";
        }else if( lang.equals("zh") ){
            region="CN";
        }else if ( lang.equals("ja") ){
            region="JP";
        }else{
            // 对于不支持的文字设置，全部强行转换为“en”
            global.setLang( "en" );
            lang = "en";
            region = "US";
        }
        
        int index = res.getCurrentSystemLang(region,lang);
        res.setCurLocale( index );
        mbCenter.languageSet();

        // get buildtime
        StringBuffer buf = new StringBuffer();
        String s1,buildtime="";
        try {
            InputStream is = ResourceCenter.class.getResourceAsStream("buildtime");
            BufferedReader br = new BufferedReader( new InputStreamReader(is) );
            while( ( s1=br.readLine() ) != null ){
                if( s1.equals("") ) continue;
                s1 = s1.trim();
                buf.append( s1 );
            }
            buildtime = buf.toString();
        }catch( Exception ex ){
        }

        try{
            //log = new MyLogger( global.getIntLogLevel(), SanBootView.res.getString("common.product") );
            log = new MyLogger( global.getIntLogLevel() );
            String logBeginer = SanBootView.res.getString("View.frameTitle")+ " "+
                        SanBootView.res.getString("AboutDialog.version") +" buildtime: " + buildtime;
SanBootView.log.info( getClass().getName(), logBeginer + "  Log Level: " + initor.loglevel  );
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("common.error.initLog")
            );
            exitForm( null );
        }

        // 设置恢复镜像的方式（for cmdp）
        ResourceCenter.CMDP_RST_MIRROR_TYPE = global.getRstMirrorType();
SanBootView.log.info( getClass().getName(), "Restore mirror type: " + ResourceCenter.CMDP_RST_MIRROR_TYPE );
        
        // 设置hith_delta和width_delta值
        try{
            ResourceCenter.GLOBAL_DELTA_HIGH_SIZE = Integer.parseInt( global.getGlobal_hight_delta() );
            ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE = Integer.parseInt( global.getGlobal_width_delta() );
        }catch(Exception ex){
        }
SanBootView.log.info( getClass().getName(), "global high delta: " + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE );
SanBootView.log.info( getClass().getName(), "global width delta: " + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE );

        //JDK14LoggingTest test = new JDK14LoggingTest();
        //test.testLog();

        String iconName = findAvailableIcon( 0 ); // left pane icon
        ImageIcon image;
        if( iconName == null ){
SanBootView.log.warning( getClass().getName(),"not found left pane image resource." );             
            image = new ImageIcon( ResourceCenter.class.getResource("def_left_pane.png") );
        }else{
            image = new ImageIcon( ResourceCenter.class.getResource( iconName ) );
        }
        setImageOnLeftPane( image.getImage() );
        
        iconName = this.findAvailableIcon( 1 ); // right pane icon
        if( iconName == null ){
SanBootView.log.warning( getClass().getName(),"not found right pane image resource." );              
            image = new ImageIcon( ResourceCenter.class.getResource("def_right_pane.png") );
        }else{
            image = new ImageIcon( ResourceCenter.class.getResource( iconName ) );
        }
        setImageOnRightPane( image.getImage() );
        
        iconName = this.findAvailableIcon( 2 ); // big logo icon
        if( iconName == null ){
SanBootView.log.warning( getClass().getName(),"not found big_logo image resource." );              
            notDisplayBigLog();
        }else{
            image = new ImageIcon( ResourceCenter.class.getResource( iconName ) );
            setImageOnToolBar( (Icon)image );
        }
        
        // registy processevent
        registryEventProcessor();
        
        initor.setLoginedFlag( false );
        initor.setInitedFlag( false );
        mbCenter.setupConnectButtonStatus( initor.isLogined() );
        
        ImageIcon imageIcon = (ImageIcon)ResourceCenter.SMALL_ICON_SANBOOT;
        setIconImage( imageIcon.getImage() );
        setSize( aWIDTH,aHEIGHT );
    }
    
    private String findAvailableIcon( int what ){
        String[] iconList;
        
        if( what == 0 ){ // left pane icon
            iconList = new String[]{ "left_pane.gif", "left_pane.jpg", "left_pane.png","left_pane.ico" };
        }else if( what == 1 ){ // right pane icon
            iconList = new String[]{ "right_pane.gif","right_pane.jpg","right_pane.png","right_pane.ico" };  
        }else{
            iconList = new String[]{ "big_logo.gif","big_logo.jpg","big_logo.png","big_logo.ico" };
        }
        
        for( int i=0; i<iconList.length; i++ ){
            if( isIconExist( iconList[i] ) ){
                return iconList[i];
            }
        }
        return null;
    }
    
    private boolean isIconExist( String iconName ){
        URL url = ResourceCenter.class.getResource( iconName );
        try{
            url.getFile();
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    public void registryEventProcessor(){
        GeneralProcessEventForSanBoot gpe;
        
        Object[] list = new Object[]{
            new ProcessEventOnRoot(),
            new ProcessEventOnChiefHost(),
            new ProcessEventOnHost(),
            new ProcessEventOnVolume(),
            new ProcessEventOnLV(),
            new ProcessEventOnActiveLink(),
            new ProcessEventOnSnapshot(),
            new ProcessEventOnDeletingSnapshot(),
            new ProcessEventOnView(),
            new ProcessEventOnChiefLunMap(),
            new ProcessEventOnLunMap(),
            new ProcessEventOnChiefSnap(),
            new ProcessEventOnChiefUser(),
            new ProcessEventOnChiefOrphanVol(),
            new ProcessEventOnOrphanVolume(),
            new ProcessEventOnService(),
            new ProcessEventOnChiefPool(),
            new ProcessEventOnPool(),
            new ProcessEventOnScheduler(),
            new ProcessEventOnChiefADSch(),
            new ProcessEventOnChiefDestUWS(),
            new ProcessEventOnDestUWSrvNode(),
            new ProcessEventOnChiefMj(),
            new ProcessEventOnMJ(),
            new ProcessEventOnChiefRemoteHost(),
            new ProcessEventOnSrcUWSrvNode(),
            new ProcessEventOnRemoteHost(),
            new ProcessEventOnMirrorVol(),
            new ProcessEventOnChiefRemoteFreeVol(),
            new ProcessEventOnLocalUWSManage(),
            new ProcessEventOnRemoteUWSManage(),
            new ProcessEventOnMirroredSnap(),
            new ProcessEventOnMirroredSnapView(),
            new ProcessEventOnChiefMjMg(),
            new ProcessEventOnChiefHostVol(),
            new ProcessEventOnChiefVMHost(),
            new ProcessEventOnChiefNetBootHost(),
            new ProcessEventOnNetBootHost(),
            new ProcessEventOnSnapUsage(),        
            new ProcessEventOnChiefSch(),
            new ProcessEventOnScheduler(),
            new ProcessEventOnChiefProfile(),
            new ProcessEventOnProfile(),
            new ProcessEventOnChiefRollbackHost(),
            new ProcessEventOnChiefUnlimitedIncMirrorVol(),
            new ProcessEventOnUnlimitedIncMirrorVol(),
            new ProcessEventOnUnlimitedIncMirroredSnap(),
            new ProcessEventOnChiefCloneDisk(),
            new ProcessEventOnCloneDisk(),
            new ProcessEventOnChiefUIMSnap(),
            new ProcessEventOnChiefCj(),
            new ProcessEventOnLocalUnlimitedIncMirroredSnap(),
            new ProcessEventOnChiefLocalUIMSnap(),
            new ProcessEventOnChiefPPProfile(),
            new ProcessEventOnPPProfile(),
            new ProcessEventOnChiefMjScheduler(),
            new ProcessEventOnMjScheduler(),
            new ProcessEventOnCluster(),
            new ProcessEventOnChiefMemberNode(),
            new ProcessEventOnChiefLMj(),
            new ProcessEventOnVMHostInfo()
        };
        
        for( int i=0; i<list.length; i++ ){
            gpe = (GeneralProcessEventForSanBoot)list[i];
            gpe.setView( this );
            addOneBehavior( new Integer( gpe.getType() ),gpe );
        }
    }
    
    @Override public void prtTypeStr( int type ){
        if( SanBootView.log != null ){
            SanBootView.log.debug(getClass().getName(),ResourceCenter.getObjectTypeStr( type ) );
        }
    }
    
    public void setTreeRootNode( BrowserTree tree ){
        RootOfSanBoot rootData = new RootOfSanBoot( );
        BrowserTreeNode root = new BrowserTreeNode(rootData,false);
        tree.setModel( new DefaultTreeModel( root ) );
        tree.setCellRenderer( new BrowserTreeCellRenderer());
        tree.setSelectionPath( tree.getPathForRow(0) );
    }
    
    public int getMode(){
        return mode;
    }
    
    public Socket getSocket(){
        return initor.socket;
    }
    
    public boolean isAddTable(){
        return addTable;
    }
    public void setAddTable( boolean val ){
        addTable = val;
    }
     
    public synchronized void removeAllData( BrowserTreeNode node ) {
        if( node != null ) node.removeAllChildren();
    }
      
    public DefaultTreeModel getTreeModel(){
        return (DefaultTreeModel)tree.getModel();
    }
    
    public void showError( int cmd,int errcode,String errStr ){
        JOptionPane.showMessageDialog(this,
            ResourceCenter.getCmdString( cmd )+" ["+errcode+"] : " + 
                SanBootView.res.getString( errStr )
        );
    }
    
    public void showError1( int cmd,int errcode,String errStr ){
        JOptionPane.showMessageDialog(this,
            ResourceCenter.getCmdString( cmd )+" ["+errcode+"] : " +  errStr
        );
    }
    
    public void removeAllFromUnSelTab(){
        unSelectedVolInfo.clear();
    }
    public void addVolumeToUnSelTabForTID(int tid,Volume vol ){
SanBootView.log.debug( getClass().getName(),"add volmap into un-sel hash table: "+tid );         
        unSelectedVolInfo.put( new Integer( tid ), vol );
    }
    public void removeVolumeFromUnSelTabForTID( int tid ){
SanBootView.log.debug( getClass().getName(),"remove volmap from un-sel hash table: "+tid );           
        unSelectedVolInfo.remove( new Integer( tid ) );
    }
    public Volume getVolumeFromUnSelTab( int tid ){
        return unSelectedVolInfo.get( new Integer( tid ) );
    }
    public Vector getAllVolInUnSelHashTable(){
        Vector<Volume> ret = new Vector<Volume>();
        Enumeration<Volume> list  = unSelectedVolInfo.elements();
        while( list.hasMoreElements() ){
            ret.addElement( list.nextElement() );
        }
        return ret;
    }
    public void printUnSelHashTabContents(){
        Enumeration<Volume> list  = unSelectedVolInfo.elements();
        while( list.hasMoreElements() ){
            Volume vol = list.nextElement();
//System.out.println(" tid : "+vol.getTargetID() );
        }
    }
    public boolean hasThisVolInUnSelTab( int tid ){
        return unSelectedVolInfo.containsKey( new Integer( tid ) );
    }
    
    public void removeAllFromBindedTab(){
        bindedVolInfo.clear();
    }
    public void addVolumeToBindedTabForTID(int tid,Volume vol ){
        bindedVolInfo.put( new Integer( tid ), vol );
    }
    public void removeVolumeFromBindedTabForTID( int tid ){
        bindedVolInfo.remove( new Integer( tid ) );
    }
    public Volume getVolumeFromBindedTab( int tid ){
        return (Volume)bindedVolInfo.get( new Integer( tid ) );
    }
    public Vector getAllVolInBindedHashTable(){
        Vector<Volume> ret = new Vector<Volume>();
        Enumeration<Volume> list  = bindedVolInfo.elements();
        while( list.hasMoreElements() ){
            ret.addElement( list.nextElement() );
        }
        return ret;
    }
    public void printBindedTabContents(){
        Enumeration<Volume> list  = bindedVolInfo.elements();
        while( list.hasMoreElements() ){
            Volume vol = list.nextElement();
System.out.println(" binded tid : "+vol.getTargetID() );
        }
    }
    public boolean hasThisVolInBindedTab( int tid ){
        return bindedVolInfo.containsKey( new Integer( tid ) );
    }
    
    public boolean hasSameVolName( String name ){
        Volume vol;
        
        Enumeration<Volume> list  = unSelectedVolInfo.elements();
        while( list.hasMoreElements() ){
             vol = list.nextElement();
             if( vol.getSnap_name().equals( name ) ){
                 return true;
             }
        }
        
        list = bindedVolInfo.elements();
        while( list.hasMoreElements() ){
            vol = list.nextElement();
            if( vol.getSnap_name().equals( name ) ){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean hasSameLVName( String name ){
        int size,size1,i,j;
        BootHost one;
        Vector lvList;
        VolumeMap lv;
        
        Vector clntList = initor.mdb.getAllBootHost();
        size = clntList.size();
        for( i=0; i<size; i++ ){
            one = (BootHost)clntList.elementAt(i);
            lvList = initor.mdb.getLVListOnClntID( one.getID() );
            size1 = lvList.size();
            for( j=0; j<size1; j++ ){
                lv = (VolumeMap)lvList.elementAt(j);
                if( lv.getVolName().equals( name ) ){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override public void reloadTreeNode(BrowserTreeNode node){
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        if( treeModel!=null ) treeModel.reload(node);
    }
      
    public void addNode( BrowserTreeNode fNode,BrowserTreeNode cNode ){
        DefaultTreeModel model = ( DefaultTreeModel )tree.getModel();
        if ( fNode != null && cNode != null ){
            try{
                int ind = fNode.getChildCount();
                model.insertNodeInto( cNode,fNode,ind );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
        }
    }
    
    // 这个方法可以将来放到 Browser 中去
    public Object getSelectedObjFromSanBoot(){
        Object selObj = null;
        
        int type = getCurBrowserEventType();
//System.out.println("current browserEvent type(getSelectedObjFromVssView): "+type );
        
        if( type == Browser.POPMENU_TABLE_ITEM_TYPE ||
            type == Browser.POPMENU_TABLE_NONITEM_TYPE ||
            type == Browser.POPMENU_TREE_TYPE
        ){
            Object _selObj = mbCenter.getSelectedObjForPopupMenu();
            if( _selObj instanceof BrowserTreeNode )
                selObj = ((BrowserTreeNode)_selObj).getUserObject();
            else
                selObj = _selObj;
        }else{
            Object[] selObjs = getSelectedItem();
            if( selObjs == null || selObjs.length <=0 ) return null;
            selObj = selObjs[0];
        }
        
        return selObj;
    }
    
    // 这个方法可以将来放到 Browser 中去
    public Object[] getMulSelectedObjFromSanBoot(){        
        int type = getCurBrowserEventType();
//System.out.println("current browserEvent type(getSelectedObjFromVssView): "+type );
        
        if( type == Browser.POPMENU_TABLE_ITEM_TYPE ||
            type == Browser.POPMENU_TABLE_NONITEM_TYPE ||
            type == Browser.POPMENU_TREE_TYPE
        ){
            Object ret;
            Object _selObj = mbCenter.getSelectedObjForPopupMenu();
            if( _selObj instanceof BrowserTreeNode )
                ret = ((BrowserTreeNode)_selObj).getUserObject();
            else
                ret = _selObj;
             return new Object[]{ ret };
        }else{
            Object[] selObjs = getSelectedItem();
            return selObjs;
        }
    }
    
    public void removeSyncNodeFromTree( BrowserTreeNode child ){
        BrowserTreeNode root = getTreeRoot();
        root.remove( child );
        ((DefaultTreeModel)tree.getModel()).reload( root );
    }
    
    // 不是file的，就是database的
    public BrowserTreeNode getChiefRstObjNodeOnBakAgent( BrowserTreeNode bkAgtNode,int realtype ){
        int i = 0,type;
        BrowserTreeNode childNode = null;
        
        int count = bkAgtNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)bkAgtNode.getChildAt(i);
            type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( type == realtype ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public BrowserTreeNode getProfNodeOnChiefProf( BrowserTreeNode chiefProfNode,String profName ){
        int i = 0;
        String name;
        BrowserTreeNode childNode = null;
        
        int count = chiefProfNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefProfNode.getChildAt(i);
            name =((UniProfile)childNode.getUserObject()).toString();
            if( name.equals( profName ) ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public BrowserTreeNode getSnapNodeOnOrphVol( BrowserTreeNode chiefSnapNode,String snapName ){
        int i = 0;
        String name;
        BrowserTreeNode childNode = null;
        
        int count = chiefSnapNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefSnapNode.getChildAt(i);
            Object snap = childNode.getUserObject();
            if( snap instanceof Snapshot ){
                name = ((Snapshot)snap).toTreeString();
            }else{
                name = ((UnlimitedIncMirroredSnap)snap).toTreeString();
            }
            if( name.equals( snapName ) ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public BrowserTreeNode getMjNodeOnMjName( BrowserTreeNode chiefMjNode,String mjName ){
        int i = 0;
        String name;
        BrowserTreeNode childNode = null;
        
        int count = chiefMjNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefMjNode.getChildAt(i);
            name =((MirrorJob)childNode.getUserObject()).toTreeString();
            if( name.equals( mjName ) ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public BrowserTreeNode getHostNodeOnChiefHostNode( BrowserTreeNode chiefHostNode,int _cltID ){
        int i = 0,cltID;
        Object obj;
        BrowserTreeNode childNode = null;
        
        int count = chiefHostNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefHostNode.getChildAt(i);
            obj = childNode.getUserObject();
            if( obj instanceof BootHost ){
                cltID = ((BootHost)obj).getID();
                if( cltID == _cltID ){
                    return childNode;
                }
            }
            
            i++;
        }
        
        return null;
    }

    public BrowserTreeNode getClusterNodeOnChiefHostNode( BrowserTreeNode chiefHostNode,int _cluster_id ){
        int i = 0,cluster_id;
        Object obj;
        BrowserTreeNode childNode = null;

        int count = chiefHostNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefHostNode.getChildAt(i);
            obj = childNode.getUserObject();
            if( obj instanceof Cluster ){
                cluster_id = ((Cluster)obj).getCluster_id();
                if( cluster_id == _cluster_id ){
                    return childNode;
                }
            }

            i++;
        }

        return null;
    }

    public BrowserTreeNode getChiefLunMapNodeOnViewNode( BrowserTreeNode diskNode ){
        int i = 0;
        BrowserTreeNode childNode = null;

        int count = diskNode.getChildCount();
        while( i < count ){
            childNode = (BrowserTreeNode)diskNode.getChildAt(i);
            int type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( ( type == ResourceCenter.TYPE_CHIEF_LUNMAP_INDEX ) ){
                return childNode;
            }

            i++;
        }

        return null;
    }
    
    public BrowserTreeNode getChiefHostVolNodeOnHostNode( BrowserTreeNode hostNode ){
        int i = 0;
        BrowserTreeNode childNode = null;
        
        int count = hostNode.getChildCount();
        while( i < count ){
            childNode = (BrowserTreeNode)hostNode.getChildAt(i);
            int type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( ( type == ResourceCenter.TYPE_CHIEF_HOST_VOL ) ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public BrowserTreeNode getChiefProfNodeOnHostNode( BrowserTreeNode hostNode ){
        int i = 0;
        BrowserTreeNode childNode = null;
        
        int count = hostNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)hostNode.getChildAt(i);
            int type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( type == ResourceCenter.TYPE_CHIEF_PROF ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public BrowserTreeNode getVolMapNodeOnHostNode( BrowserTreeNode hostNode,int _tid ){
        int i = 0,tid;
        BrowserTreeNode childNode = null;
        
        int count = hostNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)hostNode.getChildAt(i);
            tid = ((VolumeMap)childNode.getUserObject()).getVolTargetID();
            if( tid == _tid ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public BrowserTreeNode getOrphVolNodeOnChiefOrphVol( BrowserTreeNode chiefOrphVolNode,int vtid ){
        int i = 0,tid;
        BrowserTreeNode childNode = null;
        
        int count = chiefOrphVolNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefOrphVolNode.getChildAt(i);
            tid = ((Volume)childNode.getUserObject()).getTargetID();
            if( tid == vtid ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }

    public BrowserTreeNode getViewNodeOnSnapshot( BrowserTreeNode snapNode,int vtid ){
        int i = 0,tid;
        BrowserTreeNode childNode = null;

        int count = snapNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)snapNode.getChildAt(i);
            tid = ((View)childNode.getUserObject()).getTargetID();
            if( tid == vtid ){
                return childNode;
            }

            i++;
        }

        return null;
    }

    public BrowserTreeNode getMDINodeOnChiefUIMirVol( BrowserTreeNode chiefUiMirVol,int rootid ){
        int i = 0,aRootid;
        BrowserTreeNode childNode = null;

        int count = chiefUiMirVol.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefUiMirVol.getChildAt(i);
            aRootid = ((MirrorDiskInfo)childNode.getUserObject()).getSnap_rootid();
            if( aRootid == rootid ){
                return childNode;
            }

            i++;
        }

        return null;
    }

    public BrowserTreeNode getCloneDiskNodeOnChiefCloneDisk( BrowserTreeNode chiefCloneDisk,int rootid ){
        int i = 0,aRootid;
        BrowserTreeNode childNode = null;
        
        int count = chiefCloneDisk.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)chiefCloneDisk.getChildAt(i);
            aRootid = ((CloneDisk)childNode.getUserObject()).getRoot_id();
            if( aRootid == rootid ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }

    public BrowserTreeNode getChiefCloneDiskNodeOnHostVolNode( BrowserTreeNode hostVolNode ){
        int i = 0,type;
        BrowserTreeNode childNode = null;

        int count = hostVolNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)hostVolNode.getChildAt(i);

            type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( type == ResourceCenter.TYPE_CHIEF_CLONE_DISK ){
                return childNode;
            }

            i++;
        }

        return null;
    }

    public BrowserTreeNode getChiefCJNodeOnMDI( BrowserTreeNode mdiNode ){
        int i = 0,type;
        BrowserTreeNode childNode = null;

        int count = mdiNode.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)mdiNode.getChildAt(i);

            type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( type == ResourceCenter.TYPE_CHIEF_COPY_JOB ){
                return childNode;
            }

            i++;
        }

        return null;
    }

    public boolean isSameUser( String _name,int index ){
        BrowserTable userTable = this.getTable();
        
        int uIdCol = userTable.getColumn(
            SanBootView.res.getString("View.table.user.name")
        ).getModelIndex();

        DefaultTableModel tableModel = (DefaultTableModel)userTable.getModel();
        int lineNum = tableModel.getRowCount();
        for( int row = 0; row <lineNum; row++){
            if( row == index )
                continue;

            BackupUser user = (BackupUser)tableModel.getValueAt( row,uIdCol );
            if( user.getUserName().equals( _name ) ){
                return true;
            }
        }

        return false;
    }
    
    public void removeTableRow( int row ){
        BrowserTable aTable = this.getTable();
        DefaultTableModel model = (DefaultTableModel)aTable.getModel();
        if( model!=null )
            model.removeRow( row );
    }
    
    public void insertUserToTable( BackupUser user){
        Object[] one = new Object[2];
        one[0] = user;
        one[1] = new GeneralBrowserTableCell(
          -1,user.getRightString(),JLabel.LEFT
        );
        
        BrowserTable aTable = this.getTable();
        aTable.insertRow( one );
    }
    
    // 实际上获取local uws manage节点下的chief node
    public BrowserTreeNode getChiefNodeOnRoot( int _type ){
        int i = 0;
        BrowserTreeNode childNode = null;
    
        BrowserTreeNode root = getTreeRoot(); 
        root = (BrowserTreeNode)root.getChildAt( 0 ); 
        int count = root.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)root.getChildAt(i);
            int type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( type == _type ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    // 获取真正root上的主节点
    public BrowserTreeNode getChiefNodeOnSuperRoot( int _type ){
        int i = 0;
        BrowserTreeNode childNode = null;
    
        BrowserTreeNode root = getTreeRoot(); 
        int count = root.getChildCount();
        while( i<count ){
            childNode = (BrowserTreeNode)root.getChildAt(i);
            int type = ((GeneralInfo)childNode.getUserObject()).getType();
            if( type == _type ){
                return childNode;
            }
            
            i++;
        }
        
        return null;
    }
    
    public boolean isSameHost( String ip,BootHost selHost ){
        int i,size;
        BootHost host;
        
        Vector hostList = initor.mdb.getAllBootHost();
        if( selHost!=null ){
            size = hostList.size();
            for( i=0; i<size; i++ ){
                host = (BootHost)hostList.elementAt(i);
                if( host.getID() == selHost.getID() ){
                    hostList.remove(i);
                    break;
                }
            }
        }
        
        size = hostList.size();
        for( i=0;i<size;i++ ){
            host = (BootHost)hostList.elementAt(i);
            if( host.getIP().equals( ip ) ){
                return true;
            }
        }
        
        return false;
    }
    
    public void insertNodeToTree( BrowserTreeNode father,BrowserTreeNode newNode ){
        if( father == null ) return;
        
        ((DefaultTreeModel)tree.getModel()).insertNodeInto( newNode,father,father.getChildCount());   
    }
    
    public void removeNodeFromTree( BrowserTreeNode father,BrowserTreeNode delNode ){
        if( father == null ) return;
        
        father.remove( delNode );
        ((DefaultTreeModel)tree.getModel()).reload( father );
    }
    
    public boolean isSameUWSrv( String ip,UWSrvNode selSrv ){
        int i,size,id;
        UWSrvNode srv;
        
        id = selSrv.getUws_id();
        ArrayList srvList = initor.mdb.getAllUWSrv();
        if( selSrv != null ){
            size = srvList.size();
            for( i=0; i<size; i++ ){
                srv = (UWSrvNode)srvList.get(i);
                if( srv.getUws_id() == id ){
                    srvList.remove( i );
                    break;
                }
            }
        }
        
        size = srvList.size();
        for( i=0;i<size;i++ ){
            srv = (UWSrvNode)srvList.get(i);
            if( srv.getUws_ip().equals( ip ) ){
                return true;
            }
        }
        
        return false;
    }
    
    public ArrayList getNetbootedHostOnHost( BrowserTreeNode hostNode ){
        int rootid;
        BrowserTreeNode chiefHVolNode;
        
        chiefHVolNode = getChiefHostVolNodeOnHostNode( hostNode );
        if( chiefHVolNode == null ) return new ArrayList();
        
        int childCnt = chiefHVolNode.getChildCount(); 
        ArrayList<Integer> rootidList = new ArrayList<Integer>( childCnt );
        for( int i=0;i<childCnt;i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHVolNode.getChildAt(i);
            Object volObj = node.getUserObject();
            if( volObj instanceof LogicalVol ){
                rootid = ((LogicalVol)volObj).getVol_rootid();
SanBootView.log.debug(getClass().getName(), "getNetbootedHostOnHost on logicalvo, rootid: "+ rootid );                 
            }else if( volObj instanceof VolumeMap ){
                rootid = ((VolumeMap)volObj).getVol_rootid();
SanBootView.log.debug(getClass().getName(),"getNetbootedHostOnHost on volumemap,rootid: "+rootid );                    
            }else if( volObj instanceof MirrorDiskInfo ){
                rootid = ((MirrorDiskInfo)volObj).getSnap_rootid();
            }else{       
SanBootView.log.debug(getClass().getName(),"getNetbootedHostOnHost on unknown object" );                   
                continue;
            }
            rootidList.add( new Integer( rootid ) );
        }
        
        return this.getNetbootedHostOnHost( rootidList );
    }
    
    public ArrayList getNetbootedHostOnHost( ArrayList rootidList ){
        int rootid,i,size,j,size1;
        Integer key;
        SnapUsage su;
        ArrayList list;
        HashMap<Integer,DestAgent> map = new HashMap<Integer,DestAgent>();
        
        size = rootidList.size();
        for( i=0; i<size; i++ ){
            rootid = ((Integer)rootidList.get(i)).intValue();
            list = initor.mdb.getSnapUsageOnRootID( rootid );
            size1 = list.size();
            for( j=0; j<size1; j++ ){
                su = (SnapUsage)list.get( j );
                DestAgent da = initor.mdb.getNBHFromCacheOnDestAgntID( su.getDst_agent_id() );
                key = new Integer( da.getDst_agent_id() );
                if( da != null ){
                    if( map.get( key ) == null ){
                        map.put( key, da );
                    }
                }
            }
        }

        size = map.size();
        ArrayList<DestAgent> ret = new ArrayList<DestAgent>( size );
        Set set = map.keySet();
        Iterator iterator = set.iterator();
        while( iterator.hasNext() ){
            DestAgent da = (DestAgent)map.get( (Integer)iterator.next() );
            ret.add( da );
        }
        return ret;   
    }
    
    private String getErrorMsgForDelFile( String conf ){
        return ResourceCenter.getCmd( ResourceCenter.CMD_DEL_FILE ) +"[ "+conf+" ]" + " : " +
                  initor.mdb.getErrorMessage(); 
    }
    
    String errMsg;
    public String getErrMsg(){
        return errMsg;
    }
    
    public boolean delConfigFileUsingByNetBootHost( int id ){
        boolean isOk;
        
        String[] commonConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/dstagnt-" + id + ResourceCenter.CONF_NETBOOT_DISK,
            ResourceCenter.CLT_IP_CONF + "/dstagnt-" + id + ResourceCenter.CONF_RSTMAP,
            ResourceCenter.CLT_IP_CONF + "/dstagnt-" + id + ResourceCenter.CONF_3RD_DHCP
        };
        
        // 删除公共的配置
        for( int i=0; i<commonConf.length; i++ ){
            isOk = initor.mdb.delFile( commonConf[i] );
            if( !isOk ){
                errMsg = getErrorMsgForDelFile( commonConf[i] ); 
                return false;
            }
        }
        
        return true;
    }
    
    // 获取某个bootHost的缺省启动版本（全部为disk）
    public BootVerList getBootVerForDefault( int hid ){
        BindOfDiskLabelAndTid oldOSBootVer = null;
        
        Vector tgtList = initor.mdb.getVolMapOnClntID( hid );
        int size = tgtList.size();
        ArrayList<BindOfDiskLabelAndTid> oldBootVerList = new ArrayList<BindOfDiskLabelAndTid>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)tgtList.elementAt( i ); 
            BindOfDiskLabelAndTid binder = new BindOfDiskLabelAndTid( volMap.getVolDiskLabel(), volMap.getVolTargetID(),volMap.getVolTargetID() );
            oldBootVerList.add( binder );
            if( volMap.getVolDiskLabel().toUpperCase().equals("C:\\") ){
                oldOSBootVer = binder;
            }
        }
        
        return new BootVerList( oldBootVerList,oldOSBootVer );
    }

    // 获取某个bootHost的缺省启动版本（全部为disk）
    public BootVerList getBootVerForDefaultForWinCluster( int cluster_id ){
        BindOfDiskLabelAndTid oldOSBootVer = null;

        Vector tgtList = initor.mdb.getVolFromCluster1( cluster_id );
        int size = tgtList.size();
        ArrayList<BindOfDiskLabelAndTid> oldBootVerList = new ArrayList<BindOfDiskLabelAndTid>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)tgtList.elementAt( i );
            BindOfDiskLabelAndTid binder = new BindOfDiskLabelAndTid( volMap.getVolDiskLabel(), volMap.getVolTargetID(),volMap.getVolTargetID() );
            oldBootVerList.add( binder );
            if( volMap.getVolDiskLabel().toUpperCase().equals("C:\\") ){
                oldOSBootVer = binder;
            }
        }

        return new BootVerList( oldBootVerList,oldOSBootVer );
    }

    // 获取某个bootHost的“网络盘切换”的“最后一次所选版本”和“最后一次网启所用版本”
    public BootVerList getSwitchVer( int hid ){
        BindOfDiskLabelAndTid oldOSSwitchVer = null;

        Vector tgtList = initor.mdb.getVolMapOnClntID( hid );
        int size = tgtList.size();
        ArrayList<BindOfDiskLabelAndTid> oldSwitchVerList = new ArrayList<BindOfDiskLabelAndTid>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)tgtList.elementAt( i );
            int good_tid = volMap.getSwitch_last_good_verison();  // last good version
            int sel_tid = volMap.getSwitch_last_sel_version();    // last selected version
            if( (( good_tid != -1 ) && ( good_tid != 0 )) || ( (sel_tid !=-1 ) && (sel_tid !=0 ) ) ){
SanBootView.log.debug( getClass().getName(), " host id: " + hid + " switch-good-ver: "+ good_tid +" switch-sel-ver: "+ sel_tid );
                BindOfDiskLabelAndTid binder = new BindOfDiskLabelAndTid( volMap.getVolDiskLabel(), good_tid,sel_tid );
                oldSwitchVerList.add( binder );
                if( volMap.getVolDiskLabel().toUpperCase().equals("C:\\") ){
                    oldOSSwitchVer = binder;
                }
            }
        }

        return new BootVerList( oldSwitchVerList,oldOSSwitchVer );
    }

    // 获取某个bootHost的“网络启动”的“最后一次所选版本”和“最后一次网启所用版本”
    public BootVerList getBootVer( int hid ){
        BindOfDiskLabelAndTid oldOSBootVer = null;
        
        Vector tgtList = initor.mdb.getVolMapOnClntID( hid );
        int size = tgtList.size();
        ArrayList<BindOfDiskLabelAndTid> oldBootVerList = new ArrayList<BindOfDiskLabelAndTid>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)tgtList.elementAt( i ); 
            int vtid = volMap.getVol_view_targetid();   // last good version
            int sel_vtid = volMap.getVol_last_sel_boot_version();  // last selected version
            if( (( vtid != -1 ) && ( vtid != 0 )) || ( (sel_vtid !=-1 ) && (sel_vtid !=0 ) ) ){
SanBootView.log.debug( getClass().getName(), " host id: " + hid + " boot ver: "+ vtid +" sel ver: "+ sel_vtid );
                BindOfDiskLabelAndTid binder = new BindOfDiskLabelAndTid( volMap.getVolDiskLabel(), vtid,sel_vtid );
                oldBootVerList.add( binder );
                if( volMap.getVolDiskLabel().toUpperCase().equals("C:\\") ){
                    oldOSBootVer = binder;
                }
            }
        }
        
        return new BootVerList( oldBootVerList,oldOSBootVer );
    }

    // 获取某个bootHost的“网络启动”的“最后一次所选版本”和“最后一次网启所用版本”
    public BootVerList getBootVerForWinCluster( int cluster_id ){
        BindOfDiskLabelAndTid oldOSBootVer = null;

        Vector tgtList = initor.mdb.getVolFromCluster1( cluster_id );
        int size = tgtList.size();
        ArrayList<BindOfDiskLabelAndTid> oldBootVerList = new ArrayList<BindOfDiskLabelAndTid>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)tgtList.elementAt( i );
            int vtid = volMap.getVol_view_targetid();   // last good version
            int sel_vtid = volMap.getVol_last_sel_boot_version();  // last selected version
            if( (( vtid != -1 ) && ( vtid != 0 )) || ( (sel_vtid !=-1 ) && (sel_vtid !=0 ) ) ){
SanBootView.log.debug( getClass().getName(), " host id: " + cluster_id + " boot ver: "+ vtid +" sel ver: "+ sel_vtid );
                BindOfDiskLabelAndTid binder = new BindOfDiskLabelAndTid( volMap.getVolDiskLabel(), vtid,sel_vtid );
                oldBootVerList.add( binder );
                if( volMap.getVolDiskLabel().toUpperCase().equals("C:\\") ){
                    oldOSBootVer = binder;
                }
            }
        }

        return new BootVerList( oldBootVerList,oldOSBootVer );
    }

    public ArrayList getUnixBootVer( int hid ){
        BindOfDiskLabelAndTid binder;
        
        ArrayList<BindOfDiskLabelAndTid> oldBootVerList = new ArrayList<BindOfDiskLabelAndTid>();
        Vector<VolumeMap> list = initor.mdb.getRealLVListOnClntID( hid );
        int num = list.size();
        for( int i=0; i<num; i++ ){
            VolumeMap lv = list.elementAt( i );
            VolumeMap tgt = initor.mdb.getTgtOnVGname( lv.getVolDesc(),hid );
            if( tgt != null ){
                int tid = tgt.getVolTargetID();
                int vtid = tgt.getVol_view_targetid();
                if( vtid != -1 ){
                    if( vtid == 0 ){ // is a swap
                        binder = new BindOfDiskLabelAndTid( lv.getVolDiskLabel(), tid,tid );
                    }else{
                        binder = new BindOfDiskLabelAndTid( lv.getVolDiskLabel(), vtid,-1 );
                    }
                    oldBootVerList.add( binder );
                }
            }
        }
        
        return oldBootVerList;
    }
    
    // 获取某个DestAgent的网络启动版本
    public BootVerList getBootVerForDestAgent( int da_id ){
        BindOfDiskLabelAndTid oldOSBootVer = null;
        
        ArrayList suList = initor.mdb.getMSUFromCacheOnDstAgntID( da_id );
        int size = suList.size();
        ArrayList<BindOfDiskLabelAndTid> oldBootVerList = new ArrayList<BindOfDiskLabelAndTid>( size );
        for( int i=0; i<size; i++ ){
            SnapUsage su = (SnapUsage)suList.get( i ); 
            int tid = su.getSnapTid();
            if( tid != -1 ){
                BindOfDiskLabelAndTid binder = new BindOfDiskLabelAndTid( su.getExport_mp(), tid, tid );
SanBootView.log.debug(getClass().getName(), binder.prtMe() );         
                oldBootVerList.add( binder );
                if( su.getExport_mp().toUpperCase().equals("C:\\") ){
                    oldOSBootVer = binder;
                }
            }
        }
        
        return new BootVerList( oldBootVerList,oldOSBootVer );
    }
    
    public Object[] getSomethingOnTreeFromChiefMjListObj( ChiefMirrorJobList chiefMj ){
        String mjName,type;
        int root_id;
        BrowserTreeNode fNode;
        BootHost host;
        Object chiefHVolObj;
        ChiefHostVolume chiefHVol;
        boolean isTransferBranch = false;

        BrowserTreeNode volNode = chiefMj.getFatherNode();
        Object volObj = volNode.getUserObject();
        if( volObj instanceof Volume ){
            Volume vol = (Volume)volObj;
            mjName = "MJ_"+vol.getSnap_name();
            root_id = vol.getSnap_root_id();
            type = "Volume";
        }else if( volObj instanceof LogicalVol ){
            LogicalVol lv =(LogicalVol)volObj;
            fNode = lv.getFatherNode();
            chiefHVolObj = fNode.getUserObject();
            chiefHVol = (ChiefHostVolume)chiefHVolObj;
            fNode = chiefHVol.getFatherNode();
            host =(BootHost)fNode.getUserObject();
            mjName = "MJ_"+lv.getVolDiskLabel()+"_"+host.getIP();
            VolumeMap tgt = initor.mdb.getTargetOnLV( lv );
            root_id = tgt.getVol_rootid();
            type = "LogicalVol";
        }else if( volObj instanceof VolumeMap ){
            VolumeMap volMap = (VolumeMap)volObj;
            fNode = volMap.getFatherNode();
            chiefHVolObj = fNode.getUserObject();
            chiefHVol = (ChiefHostVolume)chiefHVolObj;
            fNode = chiefHVol.getFatherNode();            
            host = (BootHost)fNode.getUserObject();
            isTransferBranch = volMap.isCMDPProtect() && volMap.getVolDiskLabel().substring(0,1).toUpperCase().equals("C");
            mjName = "MJ_"+volMap.getVolDiskLabel().substring(0,1)+"_"+host.getIP();
            root_id = volMap.getVol_rootid();
            type = "VolumeMap";
        }else if( volObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)volObj;
            fNode = mdi.getFatherNode();
            chiefHVolObj = fNode.getUserObject();
            chiefHVol = (ChiefHostVolume)chiefHVolObj;
            fNode = chiefHVol.getFatherNode();          
            SourceAgent sa = (SourceAgent)fNode.getUserObject();
            isTransferBranch = mdi.isCMDPProtect() && mdi.getSrc_agent_mp().toUpperCase().startsWith("C");
            if( sa.isWinHost() ){
                mjName = "MJ_"+mdi.getSrc_agent_mp().substring(0,1)+"_"+sa.getSrc_agnt_ip();
            }else{
                mjName = "MJ_"+mdi.getSrc_agent_mp()+"_"+sa.getSrc_agnt_ip();
            }
            root_id=mdi.getSnap_rootid();
            type = "MirrorDiskInfo";
        }else{
SanBootView.log.warning(getClass().getName(),"create mj on unkonw type.");         
            return null;
        }
        
        Object[] ret = new Object[4];
        ret[0] = mjName;
        ret[1] = new Integer( root_id );
        ret[2] = type;
        ret[3] = new Boolean( isTransferBranch );
        return ret;
    }
    
    public Object[] getSomtthingOnTreeFromMjObj( MirrorJob mj ){   
        int rootid,ptype = BootHost.PROTECT_TYPE_MTPP;
        String vol_name="",vol_mp="";
        Object hostObj = null;
        BrowserTreeNode fNode;
        ChiefHostVolume chiefHVol;
        Object chiefHVolObj;
        
        BrowserTreeNode mjFNode = mj.getFatherNode();
        Object fObj = mjFNode.getUserObject();
        if( fObj instanceof ChiefMirrorJobList ){
            ChiefMirrorJobList chiefMjObj = (ChiefMirrorJobList)fObj;
            BrowserTreeNode volNode = chiefMjObj.getFatherNode();
            Object volObj = volNode.getUserObject();
            if( volObj instanceof Volume ){
                Volume vol = (Volume)volObj;
                rootid = vol.getSnap_root_id();
                vol_name = vol.getSnap_name();
                ptype = vol.getProtectType();
            }else if( volObj instanceof LogicalVol ){
                LogicalVol lv = (LogicalVol)volObj;
                VolumeMap tgt = initor.mdb.getTargetOnLV( lv );
                vol_name = lv.getVolName();
                vol_mp = lv.getVolDiskLabel();
                rootid = tgt.getVol_rootid();
                fNode = lv.getFatherNode();
                chiefHVolObj = fNode.getUserObject();
                chiefHVol = (ChiefHostVolume)chiefHVolObj;
                fNode = chiefHVol.getFatherNode();            
                hostObj = fNode.getUserObject(); // is a bootHost
            }else if( volObj instanceof MirrorDiskInfo ){
                MirrorDiskInfo mdi = (MirrorDiskInfo)volObj;
                rootid = mdi.getSnap_rootid();
                vol_mp = mdi.getSrc_agent_mp();
                vol_name = mdi.getSrc_snap_name();
                fNode = mdi.getFatherNode();
                chiefHVolObj = fNode.getUserObject();
                chiefHVol = (ChiefHostVolume)chiefHVolObj;
                fNode = chiefHVol.getFatherNode();              
                hostObj = fNode.getUserObject(); // is a SourceAgent
            }else{
                VolumeMap volMap = (VolumeMap)volObj;
                vol_name = volMap.getVolName();
                vol_mp = volMap.getVolDiskLabel();
                rootid = volMap.getVol_rootid();
                fNode = volMap.getFatherNode();
                chiefHVolObj = fNode.getUserObject();
                chiefHVol = (ChiefHostVolume)chiefHVolObj;
                fNode = chiefHVol.getFatherNode();
                hostObj = fNode.getUserObject(); // is a BootHost
                ptype = volMap.getVol_protect_type();
            }
        }else if( fObj instanceof ChiefCopyJobList ){
            ChiefCopyJobList chiefCJList = (ChiefCopyJobList)fObj;
            BrowserTreeNode mdiNode = chiefCJList.getFatherNode();
            MirrorDiskInfo mdi = (MirrorDiskInfo)mdiNode.getUserObject();
            rootid = mdi.getSnap_rootid();
            vol_mp = mdi.getSrc_agent_mp();
            vol_name = mdi.getSrc_snap_name();
            fNode = mdi.getFatherNode();
            Object ffObj = fNode.getUserObject();
            if( ffObj instanceof ChiefHostVolume ){
                chiefHVol = (ChiefHostVolume)ffObj;
                fNode = chiefHVol.getFatherNode();
                hostObj = fNode.getUserObject(); // is a SourceAgent
            }else{ // ChiefRemoteFreeVol
                hostObj = null;
            }
            ptype = mdi.getSrc_vol_protect_type();
        }else{
            int mg_id = mj.getMj_mg_id();
            MirrorGrp mg = initor.mdb.getMGFromVectorOnID( mg_id );
            if( mg == null ){
                if( mj.isIncMjType() ){
                    rootid = mj.getMj_track_src_rootid();
                }else if( mj.isCjType() ){
                    rootid = mj.getMj_copy_src_rootid();
                }else{
SanBootView.log.error( getClass().getName(),"Cann't find mg or track_src_rootid." );
                    return null;
                }
            }else{
                rootid = mg.getMg_src_root_id();
            }

            // 寻找rootid对应的卷的情况
            VolumeMap volMap1 = initor.mdb.getVolMapOnRootID( rootid );
            if( volMap1 != null ){
                vol_name = volMap1.getVolName();
                vol_mp = volMap1.getVolDiskLabel();
                hostObj = initor.mdb.getBootHostFromVector( (long)volMap1.getVolClntID() );
                if( hostObj == null ){
SanBootView.log.warning( getClass().getName(),"Cann't find clint(BootHost) on cltID: " + volMap1.getVolClntID() );
                    return null;
                }
                ptype = volMap1.getVol_protect_type();
            }else{
                MirrorDiskInfo mdi = initor.mdb.getMDIFromCacheOnRootID( rootid );
                if( mdi != null ){
                    vol_name = mdi.getSrc_snap_name();
                    vol_mp = mdi.getSrc_agent_mp();
                    hostObj = initor.mdb.getSrcAgntFromVectorOnID( mdi.getSrc_agnt_id() );
                    if( hostObj == null ){
SanBootView.log.warning( getClass().getName(),"Cann't find client(SourceAgent) on cltID: " + mdi.getSrc_agnt_id() );                        
                    }
                    ptype = mdi.getSrc_vol_protect_type();
                }else{
SanBootView.log.warning( getClass().getName(),"Cann't find volmap on ROOTID: " + rootid +". Maybe it's a free vol." );
                    // 在StartorStopMjThread中找出这个freevol的名字
                }
            }
        }
        
        Object[] ret = new Object[5];
        ret[0] = hostObj;
        ret[1] = new Integer( rootid );
        ret[2] = vol_name;
        ret[3] = vol_mp;
        ret[4] = new Integer( ptype );
        return ret;
    }
    
    public Object getHostObjFromChiefHostVol( BrowserTreeNode chiefHostVolNode ){
        ChiefHostVolume chiefHostVol = (ChiefHostVolume)chiefHostVolNode.getUserObject();
        BrowserTreeNode hostNode = chiefHostVol.getFatherNode();
        Object hostObj = hostNode.getUserObject();
        return hostObj;
    }
    
    public Object getHostObjFromChiefVMHost( BrowserTreeNode chiefVMHostNode ){
        ChiefVMHost chiefVMHost = (ChiefVMHost)chiefVMHostNode.getUserObject();
        BrowserTreeNode hostNode = chiefVMHost.getFatherNode();
        Object hostObj = hostNode.getUserObject();
        return hostObj;
    }
    
    public boolean isSameScheduler( DBSchedule newsch ){
        DBSchedule sched;
        
        ArrayList list = initor.mdb.getNormalSch();
        int size = list.size();
        for( int i=0; i<size; i++ ){
            sched = (DBSchedule)list.get(i);
            if( sched.getID() == newsch.getID() ){
                continue;
            }
System.out.println(" sched timestr: "+ sched.getTimeStr() +" newsch timestr: "+ newsch.getTimeStr() );
System.out.println(" sched profname: "+sched.getProfName() +" newsch profname: "+newsch.getProfName() );

            if( sched.getTimeStr().equals( newsch.getTimeStr() ) && 
                sched.getProfName().equals( newsch.getProfName() ) 
            ){
                return true;
            }
        }
        
        return false;
    }


     /**
     * 是否是相同的调度
     * @param newsch
     * @return
     */
    public boolean isSameADScheduler( DBSchedule newsch ){
        DBSchedule sched;

        ArrayList list = initor.mdb.getAutoDelSch();
        int size = list.size();
        for( int i=0; i<size; i++ ){
            sched = (DBSchedule)list.get(i);
            if( sched.getID() == newsch.getID() ){
                continue;
            }

            if( !sched.getSimpleProfName().trim().startsWith(ProcessEventOnChiefADSch.AUTO_DEL_SCHEDULER_PROF) )
            {
                return false;
            }

            if( sched.getProfName().split(" ")[1].equals(newsch.getProfName().split(" ")[1]) )
            {
                return true;
            }
        }

        return false;
    }
    
    ProgressDialog pdiag;
    public void startupProcessDiag( String title,String tip,StartupProgress sp ){
        pdiag = new ProgressDialog( 
            this,
            title, 
            tip
        );
        sp.setProcessDialg( pdiag );
        sp.startProcessing();
        pdiag.mySetSize();
        pdiag.setLocation( this.getCenterPoint( pdiag.getDefWidth(),pdiag.getDefHeight() ) );
        pdiag.setVisible( true );
    }
    
    public void closeProgressDialog(){
        if( pdiag != null ){
            pdiag.dispose();
        }
    }

    public int getNetBootMode() {
        return netBootMode;
    }

    public void setNetBootMode(int netBootMode) {
        this.netBootMode = netBootMode;
    }

}
