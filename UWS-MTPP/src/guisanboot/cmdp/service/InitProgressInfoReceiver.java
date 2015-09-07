package guisanboot.cmdp.service;

/**
 * InitProgressInfoReceiver.java
 *
 * Created on June 23, 2010, 9:06 AM
 */
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.SubCluster;
import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.data.BootHost;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import java.beans.*;
import java.util.Vector;
import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.util.ArrayList;

public class InitProgressInfoReceiver extends Thread{
    private boolean Continue = true;
    private boolean isFirst = true;
    private int     sleepTime  = 4; //4 秒��
    private Vector<PropertyChangeListener>  pListenerList = new Vector<PropertyChangeListener>();
    private PropertyChangeSupport  agent = new PropertyChangeSupport(this);
    private boolean suspendRequested = false; 
    SanBootView view;
    private BootHost host;
    private Cluster cluster;
    private BootHost masterHost = null;
    private GetInitProgress geter;
    private ArrayList<InitProgressRecord> recList = new ArrayList<InitProgressRecord>();

    public InitProgressInfoReceiver( SanBootView _view ){
        view = _view;

        geter = new GetInitProgress("");
    }

    public void setHost( BootHost host ){
        this.host = host;
    }

    public void setCluster( Cluster cluster ){
        this.cluster = cluster;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener I){
        agent.addPropertyChangeListener(I);
        pListenerList.addElement( I );
    }
    
    public void removePropertyChangeListener(PropertyChangeListener I){
        agent.removePropertyChangeListener(I);
        pListenerList.removeElement( I );
    }
    
    public synchronized void requestSuspend(){
        suspendRequested = true;
    }
    
    private synchronized void checkSuspended() throws InterruptedException{
        while( suspendRequested ){
            wait();
        }
    }
    
    public synchronized void requestResume() {
        suspendRequested = false;
        notify();
    }
    
    public synchronized boolean isContinue(){
        return Continue;
    }

    public synchronized void setContinue(boolean val){
        Continue = val;
    }

    @Override public void  run(){
        while( isContinue() ){
            try{
                // 检查 "是否要求暂停获取 task info"
                checkSuspended();
                sleep( sleepTime*1000 );
            }catch(InterruptedException e){}

            try{
                getInitProgressInfo();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<InitProgressRecord> getInitProgress(){
        return this.recList;
    }

    private BootHost getMasterHostFromCluster(){
        BootHost aHost;
        SubCluster subc;
        boolean isOk;

        ArrayList<SubCluster> subcList = cluster.getRealSubCluster();
        if( subcList.size() == 2 ){
            subc = subcList.get(0);
            isOk = view.initor.mdb.getMirrorClusterInfo( subc.getHost().getIP(),subc.getHost().getPort() );
            if( isOk ){
                if( view.initor.mdb.isMasterNode() ){
                    return subc.getHost();
                }else{
                    return subcList.get(1).getHost();
                }
            }else{
                subc = subcList.get( 1 );
                isOk = view.initor.mdb.getMirrorClusterInfo( subc.getHost().getIP(),subc.getHost().getPort() );
                if( isOk ){
                    if( view.initor.mdb.isMasterNode() ){
                        return subc.getHost();
                    }else{
                        return subcList.get(0).getHost();
                    }
                }else{
                    return null;
                }
            }
        }else{ // 多于3个
            int size = subcList.size();
            for( int i=0; i<size; i++ ){
                aHost = subcList.get(i).getHost();
                isOk = view.initor.mdb.getMirrorClusterInfo( aHost.getIP(),aHost.getPort() );
                if( isOk ){
                    if( view.initor.mdb.isMasterNode() ){
                        return aHost;
                    }
                }
            }
            return null;
        }
    }

    public void setIsFirstFlag( boolean val ){
        this.isFirst = val;
    }

    private void getInitProgressInfo() throws IOException{
        InitProgressRecord curRec;
        BootHost aHost;
        
        this.geter.setSocket( view.getSocket() );
        this.recList.clear();

        ArrayList<VolumeMap> volList;
        if( cluster != null ){
            volList = view.initor.mdb.getVolFromCluster( cluster.getCluster_id() );
            if( this.isFirst ){
                masterHost = this.getMasterHostFromCluster();
                isFirst = false;
            }
        }else{
            volList = view.initor.mdb.getVolMapOnClntID1( host.getID() );
        }
        
        int size = volList.size();
        if( size > 0 ){
            for( int i=0; i<size; i++ ){
                VolumeMap vol = volList.get( i );
                if( vol.isMtppProtect() ) continue;
                
                view.initor.setResetNetwork( false );
                view.initor.mdb.setNewTimeOut( ResourceCenter.LIMITE_OF_RESPOND );
                if( cluster != null ){
                    aHost = masterHost;
                }else{
                    aHost = host;
                }
                
                if( aHost == null ){
                    curRec = new InitProgressRecord( vol.getVolDiskLabel() );
                    curRec.setPercent( SanBootView.res.getString("common.error.noMasterNode"));
                    recList.add( curRec );
                }else{
                    boolean isOk = geter.updateInitProgress( aHost.getIP(),aHost.getPort(),vol.getVolName(),vol.getVolDiskLabel() );
                    if( isOk ){
                        curRec = geter.getInitRecord();
                        curRec.setIsNotConnectError( geter.isNotConnectToHost() );
                    }else{
                        if( geter.getRetCode() == ResourceCenter.ERRCODE_TIMEOUT ){
                            curRec = new InitProgressRecord( vol.getVolDiskLabel() );
                            curRec.setPercent( SanBootView.res.getString("common.timeout") );
                        }else{
                            curRec = new InitProgressRecord( vol.getVolDiskLabel() );
                        }
                    }
                    recList.add( curRec );

                    if( view.initor.isResetNetwork() ){
                        geter.setSocket( view.getSocket() ); // 有可能出现超时,所以要重新设置socket
                    }
                }
            }
            view.initor.mdb.setNewTimeOut( ResourceCenter.DEFAULT_TIMEOUT );
        }
        
        if( pListenerList.size() > 0 ){ // 通知Listener们去获取新的Init-progress info.
            String ip = ( this.cluster!=null )?this.cluster.getClusterIP():host.getIP();
            agent.firePropertyChange(
                "INITPROGRESSINFO"+ip,
                "old",
                "new"
            );
        }
    }
}
