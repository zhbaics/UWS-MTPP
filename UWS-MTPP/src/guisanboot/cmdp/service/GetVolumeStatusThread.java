/*
 * GetVolumeStatusThread.java
 *
 * Created on 2010/7/16, PM 5:55
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.service;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.cmdp.entity.WorkModeForMirroring;
import guisanboot.ui.*;
import java.util.ArrayList;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.tool.Tool;

/**
 *
 * @author Administrator
 */
public class GetVolumeStatusThread extends BasicGetSomethingThread{
    private GetOrphanVol action = null;
    private Object[] volObjList;  // 类型为: VolumeMap
    private String ip;
    private int port;
    private Cluster cluster = null;
    private ArrayList<InitProgressRecord> initRecList = new ArrayList<InitProgressRecord>();
    private ArrayList<WorkModeForMirroring> workModeList = new ArrayList<WorkModeForMirroring>();
      
    /** Creates a new instance of GetVolumeStatusThread */
    public GetVolumeStatusThread(
        SanBootView view,
        Object[] volObjList,
        String ip,
        int port
    ) {
        super( view,false );
        this.volObjList = volObjList;
        this.ip = ip;
        this.port = port;
    }

    /** Creates a new instance of GetVolumeStatusThread */
    public GetVolumeStatusThread(
        SanBootView view,
        Object[] volObjList,
        Cluster cluster
    ) {
        super( view,false );
        this.volObjList = volObjList;
        this.cluster = cluster;
    }

    public boolean realRun(){
        boolean ok,isMgStart;
        int end;
        
        initRecList.clear();

        try{
            action = new GetOrphanVol(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_VOL ), 
                view.getSocket(),
                view
            );
            action.setAddCacheFlag( true );
            action.setFilterFlag( false );
            
            ok = action.realDo();
            errMsg = action.getErrMsg();

            if( ( ip != null && !ip.equals("") ) || this.cluster != null ){
                if( volObjList.length > 0 ){
                    // 继续获取"同步状态"( for cmdp )
                    view.initor.setResetNetwork( false );
                    view.initor.mdb.setNewTimeOut( ResourceCenter.LIMITE_OF_RESPOND );
                    for( int i=0; i<volObjList.length; i++ ){
                        VolumeMap volMap = (VolumeMap)volObjList[i];
                        if( volMap.isMtppProtect() ) continue;

                        if( cluster != null ){
                            BootHost member = view.initor.mdb.getBootHostFromVector( volMap.getVolClntID() );
                            if( member != null ){
                                ip = member.getIP();
                                port = member.getPort();
                            }else{
SanBootView.log.error(this.getClass().getName(),"Can't find corresponding host member for : " + volMap.getVolName() +" in cluster:" + cluster.getCluster_id());
                                continue;
                            }
                        }

                        if( view.initor.mdb.getCurrentSyncState( ip,port,volMap.getVolName(),volMap.getVolDiskLabel() ) ){
                            if( view.initor.mdb.currentStateIsConnectError() ){
                                if( this.cluster == null ){
                                    addRec( i,this.volObjList.length,InitProgressRecord.STATE_notConnect );
                                    break;
                                }else{
                                    end = this.getEnd( i, ip, port );
                                    addRec( i,end,InitProgressRecord.STATE_notConnect );
                                    i = end-1;
                                }
                            }else{
                                initRecList.add( view.initor.mdb.getCurRec() );
                            }
                        }else{
                            if( view.initor.mdb.currentStateIsConnectError() ){
                                if( this.cluster == null ){
                                    end = this.volObjList.length;
                                    addRec( i,end,InitProgressRecord.STATE_notConnect );
                                    break;
                                }else{
                                    end = this.getEnd( i, ip, port );
                                    addRec( i,end,InitProgressRecord.STATE_notConnect );
                                    i = end-1;
                                }
                            }else if( view.initor.mdb.getErrorCode() == ResourceCenter.ERRCODE_TIMEOUT ){
                                if( this.cluster == null ){
                                    end = this.volObjList.length;
                                    addRec( i,end,InitProgressRecord.STATE_timeout );
                                    break;
                                }else{
                                    end = this.getEnd( i, ip, port );
                                    addRec( i,end,InitProgressRecord.STATE_timeout );
                                    i = end-1;
                                }
                            }else{
                                if( this.cluster == null ){
                                    end = this.volObjList.length;
                                    addRec( i,end,InitProgressRecord.STATE_network );
                                    break;
                                }else{
                                    end = this.getEnd( i, ip, port );
                                    addRec( i,end,InitProgressRecord.STATE_network );
                                    i = end-1;
                                }
                            }
                        }
                    }

                    if( !view.initor.isResetNetwork() ){
                        view.initor.mdb.restoreOldTimeOut();
                    }

                    // 再获取“mg进程的状态”(用于判断是否可以自动创建快照)
                    for( int i=0; i<volObjList.length; i++ ){
                        VolumeMap volMap = (VolumeMap)volObjList[i];
                        if( volMap.isMtppProtect() ) continue;

                        if( volMap.getVol_mgid() > 0 ){
                            isMgStart = view.initor.mdb.checkMg( volMap.getVol_mgid() );
                        }else{
                            // mg id 无效
                            isMgStart = false;
                        }

                        WorkModeForMirroring wkmode = new WorkModeForMirroring( volMap.getVolDiskLabel(),isMgStart );
                        this.workModeList.add( wkmode );
                    }
                }
            }
            
        }catch(Exception ex){
            ok = false;
            Tool.prtExceptionMsg( ex );
        }
        
        return ok;
    }

    private int getEnd( int begin,String ip,int port ){
        int i;

        for( i=begin; i<volObjList.length; i++ ){
            VolumeMap volMap = (VolumeMap)this.volObjList[i];
            BootHost member = view.initor.mdb.getBootHostFromVector( volMap.getVolClntID() );
            if( member != null ){
                if( !member.getIP().equals( ip ) || member.getPort() != port ){
                    return i;
                }
            }
        }
        return i;
    }

    private void addRec( int begin,int end,String state ){
        for( int i=begin; i<end; i++  ){
            VolumeMap volMap = (VolumeMap)this.volObjList[i];
            initRecList.add( new InitProgressRecord( state,volMap.getVolDiskLabel() ) );
        }
    }

    public ArrayList getRet(){
        return action.getAllVolFromCache();
    }
    public ArrayList getSyncState(){
        return this.initRecList;
    }
    public ArrayList getWorkModeofMirroring(){
        return this.workModeList;
    }
    public boolean getRetVal(){
        return isOk();
    }
    public String getErrorMsg(){
        return getErrMsg();
    }
}