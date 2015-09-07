/*
 * GetVolumeMap.java
 *
 * Created on 2006/12/22, AM�11:20
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.Vector;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 *
 * @author Administrator
 */
public class GetVolumeMap extends NetworkRunning{
    private ArrayList<VolumeMap> tmpCache = new ArrayList<VolumeMap>();
    private Vector<VolumeMap> volList = new Vector<VolumeMap>();
    private VolumeMap curVol = null;
    
    public void parser(String line){
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"(GetVolumeMap): "+ s1  ); 
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            if( s1.startsWith( VolumeMap.VOLMAP_NAME ) ){
                curVol.setVolName( value );
                tmpCache.add( curVol );
            }else if( s1.startsWith( VolumeMap.VOLMAP_CLNT_ID )){
                try{
                    int clnt_id = Integer.parseInt( value );
                    curVol.setVolClntID( clnt_id );
                }catch(Exception ex){
                    curVol.setVolClntID( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_DISK_LABEL )){
                curVol.setVolDiskLabel( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_TARGET_ID)){
                try{
                    int target_id = Integer.parseInt( value );
                    curVol.setVolTargetID( target_id );
                }catch(Exception ex){
                    curVol.setVolTargetID( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_MAX_SNAP ) ){
                try{
                    int num = Integer.parseInt( value );
                    curVol.setMaxSnapNum( num );
                }catch(Exception ex){
                    curVol.setMaxSnapNum( 0 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_DESC ) ){
                curVol.setVolDesc( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_MGID ) ){
                try{
                    int mgid = Integer.parseInt( value );
                    curVol.setVol_mgid( mgid );
                }catch(Exception ex){
                    curVol.setVol_mgid( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_ROOTID )){
                try{
                    int rootid = Integer.parseInt( value );
                    curVol.setVol_rootid( rootid );
                }catch(Exception ex){
                    curVol.setVol_rootid( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_UUID ) ){
                curVol.setVol_uuid( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_CUR_LOCALID ) ){
                try{
                    int localid = Integer.parseInt( value );
                    curVol.setVol_current_localid( localid );
                }catch(Exception ex){
                    curVol.setVol_current_localid( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_INFO ) ){
                curVol.setVol_info( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_CAP ) ){
                try{
                    long cap = Long.parseLong( value );
                    curVol.setVol_capacity( cap );
                }catch( Exception ex ){
                    curVol.setVol_capacity( -1L );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_VIEW_TID ) ){
                try{
                    int view_tid = Integer.parseInt( value );
                    curVol.setVol_view_targetid( view_tid );
                }catch( Exception ex ){
                    curVol.setVol_view_targetid( 0 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_LAST_GOOD_BOOT_INFO ) ){
                curVol.setLastGoodBootInfo( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_ROLLBACK_ORGI_VOLNAME ) ){
                curVol.setRollbackOrgiVolname( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_CHANGEVER_SERVICES ) ){
                curVol.setChangeVerService( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_ROLLBACK_ORGI_UWS ) ){
                curVol.setRollbackOrgiUws( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_Database_Instances ) ){
                curVol.setDatabase_instances( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_Others ) ){
                curVol.setOthers( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_GroupInfoDetail) ) {
                curVol.setGroupinfodetail( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_GroupInfo ) ){
                curVol.setGroupinfo( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_GroupName ) ) {
                curVol.setGroupname( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_SetVersion ) ){
                curVol.setSetversion( value );
            }else if( s1.startsWith(VolumeMap.VOLMAP_LAST_SEL_VER )){
                try{
                    int last_sel_ver = Integer.parseInt( value );
                    curVol.setVol_last_sel_boot_version( last_sel_ver );
                }catch(Exception ex){
                    curVol.setVol_last_sel_boot_version( 0 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_LAST_SEL_VER_INFO )){
                curVol.setVol_last_sel_boot_info( value );               
            }else if( s1.startsWith( VolumeMap.VOLMAP_LAST_SEl_BOOT_DISK_TYPE ) ){
                try{
                    int last_sel_boot_disk_type = Integer.parseInt( value );
                    curVol.setLast_sel_boot_disk_type( last_sel_boot_disk_type );
                }catch(Exception ex){
                    curVol.setLast_sel_boot_disk_type( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_LAST_GOOD_BOOT_DISK_TYPE ) ){
                try{
                    int last_good_boot_disk_type = Integer.parseInt( value );
                    curVol.setLast_goog_boot_disk_type( last_good_boot_disk_type );
                }catch(Exception ex){
                    curVol.setLast_goog_boot_disk_type( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_SWITCH_LAST_SEL_VER ) ){
                try{
                    int switch_last_sel_ver = Integer.parseInt( value );
                    curVol.setSwitch_last_sel_version( switch_last_sel_ver );
                }catch(Exception ex){
                    curVol.setSwitch_last_sel_version( 0 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_SWITCH_LAST_SEL_INFO ) ){
                curVol.setSwitch_last_sel_info( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_SWITCH_LAST_SEL_DISK_TYPE ) ) {
                try{
                    int switch_last_sel_disk_type = Integer.parseInt( value );
                    curVol.setSwitch_last_sel_disk_type( switch_last_sel_disk_type );
                }catch(Exception ex){
                    curVol.setSwitch_last_sel_disk_type( -1 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_SWITCH_LAST_GOOD_VER ) ){
                try{
                    int switch_last_good_ver = Integer.parseInt( value );
                    curVol.setSwitch_last_good_verison( switch_last_good_ver );
                }catch(Exception ex){
                    curVol.setSwitch_last_good_verison( 0 );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_SWITCH_LAST_GOOD_INFO ) ) {
                curVol.setSwitch_last_good_info( value );
            }else if( s1.startsWith( VolumeMap.VOLMAP_SWITCH_LAST_GOOD_DISK_TYPE ) ){
                try{
                    int switch_last_good_disk_type = Integer.parseInt( value );
                    curVol.setSwitch_last_good_disk_type( switch_last_good_disk_type );
                }catch(Exception ex){
                    curVol.setSwitch_last_good_disk_type( -1 );
                }
            }else if ( s1.startsWith( VolumeMap.VOLMAP_PROTECT_TYPE ) ){
                try{
                    int ptype = Integer.parseInt( value );
                    curVol.setVol_protect_type( ptype );
                }catch( Exception ex ){
                    // 缺省为cmdp，为了兼容前面的版本（2011.4.19）
                    curVol.setVol_protect_type( BootHost.PROTECT_TYPE_UNKNOWN );
                }
            }else if( s1.startsWith( VolumeMap.VOLMAP_CLUSTER_ID ) ){
                try{
                    int cluster_id = Integer.parseInt( value );
                    curVol.setVol_cluster_id( cluster_id );
                }catch(Exception ex){
                    curVol.setVol_cluster_id( 0 );
                }
            }else if (s1.startsWith( VolumeMap.VOLMAP_OPT ) ){
                try{
                    int vol_opt = Integer.parseInt( value );
                    curVol.setVol_opt( vol_opt );
                }catch(Exception ex){
                    curVol.setVol_opt( 0 );
                }
            }
        }else{
            if( s1.startsWith( VolumeMap.VOLMAP_RECFLAG ) ){
                curVol = new VolumeMap();
            }
        }
    }
    
    public GetVolumeMap( String cmd,Socket socket ) throws IOException{
        super( cmd ,socket );
    }
    
    public GetVolumeMap( String cmd ){
        super( cmd );
    }

    public synchronized boolean updateVolMap( String volName ) {
        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_ONE_VOLMAP ) + volName );
SanBootView.log.info( getClass().getName(), " get one volmap list cmd: "+ getCmdLine() );
        try{
            tmpCache.clear();
            curVol = null;

            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get one volmap list retcode: "+ getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get one volmap list errmsg: "+ getErrMsg() );
            tmpCache.clear();
        }else{
            saveNewValue();
        }

        return isOk;
    }

    public synchronized boolean updateVolMap() {
SanBootView.log.info( getClass().getName(), " get volmap list cmd: "+ getCmdLine() );         
        try{
            tmpCache.clear();
            curVol = null;
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get volmap list retcode: "+ getRetCode() );       
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get volmap list errmsg: "+ getErrMsg() ); 
            tmpCache.clear();
        }else{
            saveNewValue();
        }
        
        return isOk;
    }
    
    private void saveNewValue(){
        volList.removeAllElements();
        int size = tmpCache.size();
        for( int i=0; i<size; i++ ){
            volList.addElement( tmpCache.get(i) );
        }
        tmpCache.clear();
    }
    
    public synchronized void addVolMapToVector( VolumeMap volMap ){
        volList.addElement( volMap );
    }

    public synchronized void removeVolMapFromVector( VolumeMap volMap ){
        volList.removeElement( volMap );
    }
    
    public synchronized VolumeMap getVolMapFromVecOnName( String volname ){
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap map = (VolumeMap)volList.elementAt(i);
            if( map.getVolName().equals( volname ) ){
                return map;
            }
        }
        return null;
    }

    public synchronized void replaceVolumeMap( String volname,String vol_info ){
        VolumeMap volMap = getVolMapFromVecOnName( volname );
        if( volMap != null ){
            volMap.setVol_info( vol_info );
        }
    }

    public synchronized VolumeMap getVolMapFromVecOnTID( int targetID ){
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap map = volList.elementAt(i);
            if( map.getVolTargetID() == targetID ){
                return map;
            }
        }
        return null;
    }

    public synchronized VolumeMap generalGetVolMapFromVecOnClntandLabel( int clntid,String label ){
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap map = volList.elementAt(i);
            if( ( map.getVolClntID() == clntid ) &&
                ( map.getVolDiskLabel().equals( label ) )
            ){
                return map;
            }
        }
        return null;
    }

    // 只适用于windows's volumeMap
    public synchronized VolumeMap getVolMapFromVecOnClntandLabel( int clntid,String label ){
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap map = volList.elementAt(i);
            if( (map.getVolClntID() == clntid) &&
                ( map.getVolDiskLabel().equals( label ) ||
                  map.isSameLetter( label )
                )
            ){
                return map;
            }
        }
        return null;
    }

    // 只适用于windows's volumeMap
    public synchronized VolumeMap getVolMapFromVecOnClusterandLabel( int cluster_id,String label ){
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap map = volList.elementAt(i);
            if( ( map.getVol_cluster_id() == cluster_id ) &&
                ( map.getVolDiskLabel().equals( label ) ||
                  map.isSameLetter( label )
                )
            ){
                return map;
            }
        }
        return null;
    }

    public synchronized VolumeMap getVolMapOnMgid( int mgId ){
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( one.getVol_mgid() == mgId ){
                return one;
            }
        }

        return null;
    }

    public synchronized Vector<VolumeMap> getVolMapOnClntID( int cid ){
        int size = volList.size();
        Vector<VolumeMap> list = new Vector<VolumeMap>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( one.getVolClntID() == cid ){
                list.addElement( one );
            }
        }

        return list;
    }

    public synchronized ArrayList<VolumeMap> getVolMapOnClntID1( int cid ){
        int size = volList.size();
        ArrayList<VolumeMap> list = new ArrayList<VolumeMap>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( one.getVolClntID() == cid ){
                list.add( one );
            }
        }

        return list;
    }

    public synchronized Vector<VolumeMap> getVolMapOnClntIDAndPType( int cid,int ptype ){
        int size = volList.size();
        Vector<VolumeMap> list = new Vector<VolumeMap>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( ( one.getVolClntID() == cid ) && ( one.getVol_protect_type() == ptype ) ){
                list.addElement( one );
            }
        }
        
        return list;
    }

    public synchronized Vector<VolumeMap> getVolMapOnClusterIDAndPType( int cluster_id,int ptype ){
        int size = volList.size();
        Vector<VolumeMap> list = new Vector<VolumeMap>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( ( one.getVol_cluster_id() == cluster_id ) && ( one.getVol_protect_type() == ptype ) ){
                list.addElement( one );
            }
        }

        return list;
    }

    public synchronized Vector<VolumeMap> getVolMapWithoutOSDiskOnClntID( int cid ){
        int size = volList.size();
        Vector<VolumeMap> list = new Vector<VolumeMap>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( one.getVolClntID() == cid &&
                !one.getVolDiskLabel().substring(0,1).toUpperCase().equals("C")
            ){
                list.addElement( one );
            }
        }

        return list;
    }

    public boolean isExistThisServiceOnVolMap( int cid,String service_name ){
        Vector servList = this.getServiceOfVolMapOnClntID( cid );
        int size = servList.size();
        for( int i=0; i<size; i++ ){
            String service = (String)servList.get(i);
            if( service.equals( service_name ) ){
                return true;
            }
        }
        return false;
    }

    public synchronized Vector getServiceOfVolMapOnClntID( int cid ){
        Vector volMapList = this.getVolMapOnClntID( cid );
        HashMap<String,String> map = new HashMap<String,String>();
        int size = volMapList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap one = (VolumeMap)volMapList.get(i);
            String[] sList = one.getServices();
            for( int j=0; j<sList.length; j++ ){
                if( sList[j].equals("") )continue;
                map.put( sList[j], sList[j] );
            }
        }

        Vector ret = new Vector();
        Set set = map.keySet();
        Iterator key = set.iterator();
        while( key.hasNext() ){
            ret.add( map.get( (String)key.next() ) );
        }
        return ret;
    }

    public synchronized HashMap<Integer,VolumeMap> getVolMapHashMapOnClntID( int cid ){
        int size = volList.size();
        HashMap<Integer,VolumeMap> map = new HashMap<Integer,VolumeMap>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( one.getVolClntID() == cid ){
                map.put( new Integer( one.getVol_rootid()), one );
            }
        }

        return map;
    }
    
    public synchronized Vector<VolumeMapWrapper> getVolMapWrapOnClntID( int id,int type ){
        int size = volList.size();
        Vector<VolumeMapWrapper> list = new Vector<VolumeMapWrapper>( size );
        for( int i=0; i<size; i++ ){
            VolumeMap one = volList.elementAt(i);
            if( one.getVolClntID() == id ){
                VolumeMapWrapper wrap = new VolumeMapWrapper(type);
                wrap.volMap = one;
                list.addElement( wrap );
            }
        }
        
        return list;
    }
    
    public synchronized VolumeMap getVolMapOnRootID( int rootid ){
        VolumeMap ret,one,vg=null,lv=null,tgt=null;
        int i,size;
        
        size = volList.size();
        ArrayList<VolumeMap> list = new ArrayList<VolumeMap>( size );
        for( i=0; i<size; i++ ){
            one = volList.elementAt(i);
            if( one.getVol_rootid() == rootid ){
                list.add( one );
            }
        }
        
        if( list.size() == 1 ){ // for windows
            one = list.get(0);
            ret = new VolumeMap(
                one.getVolName(),
                one.getVolClntID(),
                one.getVolDiskLabel(),
                one.getVolTargetID(),
                one.getMaxSnapNum(),
                one.getVolDesc(),
                rootid,
                one.getVol_protect_type()
            );
            ret.setVol_opt( one.getVol_opt() );
            ret.setVol_cluster_id( one.getVol_cluster_id() );
            return ret;
        }else if( list.size() == 3 ){ // for linux
            for( i=0; i<3; i++ ){
                one = list.get(i);
                if( one.getVolDesc().equals( VolumeMap.TYPE_VG ) ){
                    vg = one;
                }else if( one.getVolDesc().equals( VolumeMap.TYPE_TGT ) ){
                    tgt = one;
                }else{
                    lv = one;
                }
            }
            if( lv !=null && tgt != null ){
                ret = new VolumeMap(
                    lv.getVolName(), lv.getVolClntID(),lv.getVolDiskLabel(),
                        tgt.getVolTargetID(),tgt.getMaxSnapNum(),"",rootid,BootHost.PROTECT_TYPE_MTPP
                );
                ret.setVol_opt( lv.getVol_opt() );
                ret.setVol_cluster_id( lv.getVol_cluster_id() );
                return ret;
            }else {
                return null;
            }
        }else{ // 
            return null;
        }
    }
    
    public synchronized Vector<VolumeMap> getAllVolumeMap(){
        Vector<VolumeMap> list  = new Vector<VolumeMap>();
        int size = volList.size();
        for( int i=0;i<size;i++ ){
            list.addElement( volList.elementAt(i) );
        }
        return list;
    }
    
    public synchronized VolumeMap getRollbackedVolMap( String volname,String uws,int cid ){
        VolumeMap volMap;
        
        int size = volList.size();
        for( int i=0;i<size;i++ ){
            volMap = (VolumeMap)volList.elementAt(i);
            if( volMap.getRollbackOrgiVolname().equals( volname ) && 
                volMap.getRollbackOrgiUws().equals( uws ) &&
                ( volMap.getVolClntID() == cid ) 
            ){
                return volMap;
            }
        }
        return null;
    }

    public VolumeMap isExistVolMap( int clntid,String mp ){
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)volList.get(i);
            if( ( volMap.getVolClntID() == clntid ) && ( volMap.getVolDiskLabel().equals( mp ) ) ){
                return volMap;
            }
        }

        return null;
    }

    public ArrayList<VolumeMap> getVolFromCluster( int cluster_id ){
        ArrayList<VolumeMap> ret = new ArrayList<VolumeMap>();
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)volList.get(i);
            if( volMap.getVol_cluster_id() == cluster_id ){
                ret.add( volMap );
            }
        }
        return ret;
    }

    public Vector<VolumeMapWrapper> getVolFromCluster( int cluster_id,int type ){
        Vector<VolumeMapWrapper> ret = new Vector<VolumeMapWrapper>();
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)volList.get(i);
            if( volMap.getVol_cluster_id() == cluster_id ){
                VolumeMapWrapper wrap = new VolumeMapWrapper( type );
                wrap.volMap = volMap;
                ret.add( wrap );
            }
        }
        return ret; 
    }

    public Vector<VolumeMap> getVolFromCluster1( int cluster_id ){
        Vector<VolumeMap> ret = new Vector<VolumeMap>();
        int size = volList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap volMap = (VolumeMap)volList.get(i);
            if( volMap.getVol_cluster_id() == cluster_id ){
                ret.add( volMap );
            }
        }
        return ret;
    }

    public VolumeMap getVolumeFromClusterOnLabel( int cluster_id,String label ){
        ArrayList<VolumeMap> aVolList= getVolFromCluster( cluster_id );
        int size = aVolList.size();
        for( int i=0; i<size; i++ ){
            VolumeMap vol = aVolList.get(i);
            if( vol.getVolDiskLabel().equals( label ) ||
                vol.isSameLetter( label )
            ){
                return vol;
            }
        }
        return null;
    }
}
