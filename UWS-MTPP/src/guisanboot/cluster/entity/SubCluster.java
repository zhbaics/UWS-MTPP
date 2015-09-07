/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cluster.entity;

import guisanboot.data.BootHost;
import guisanboot.data.VolumeMap;
import guisanboot.datadup.data.BackupClient;
import java.util.ArrayList;

/**
 * SubCluster.java
 *
 * Created on 2011-7-13, 17:28:49
 */
public class SubCluster {
    private BootHost host;
    private BackupClient d2d_host;
    private ArrayList<ClusterVolume> disks = new ArrayList<ClusterVolume>();
    private boolean isConnected; // 删除cluster时，该subcluster对应的主机是否可以连接上

    public SubCluster(){
    }

    /**
     * @return the host
     */
    public BootHost getHost() {
        return host;
    }

    public boolean isRealHost(){
        return !this.host.getUUID().equals("");
    }

    /**
     * @param host the host to set
     */
    public void setHost(BootHost host) {
        this.host = host;
    }

    /**
     * @return the d2d_host
     */
    public BackupClient getD2d_host() {
        return d2d_host;
    }

    /**
     * @param d2d_host the d2d_host to set
     */
    public void setD2d_host(BackupClient d2d_host) {
        this.d2d_host = d2d_host;
    }

    public void addDisk( ClusterVolume disk ){
        this.disks.add( disk );
    }
    
    public void removeDisk( ClusterVolume disk ){
        this.disks.remove( disk );
    }

    public VolumeMap getVolMapFromSubCluster( String label ){
        int size = this.disks.size();
        for( int i=0; i<size; i++ ){
            ClusterVolume cv = disks.get(i);
            if( cv.getDiskLabel().equals( label ) || cv.isSameLetter( label ) ){
                return cv.getVolMapObj();
            }
        }
        return null;
    }

    public boolean isLocalVolume(){
        int size = this.disks.size();
        if( size > 0 ){
            ClusterVolume cv = this.disks.get(0);
            if( cv.getVolMapObj() != null ){
                return !cv.getVolMapObj().isClusterSharedDisk();
            }else{
                return cv.getBinder().ap.isLocal;
            }
        }else{
            return false;
        }
    }
    
    public ArrayList<ClusterVolume> getDisks(){
        return this.disks;
    }

    public String getDiskString(){
        StringBuffer ret = new StringBuffer();
        boolean isFirst = true;

        int size = this.disks.size();
        for( int i=0; i<size; i++ ){
            ClusterVolume cv = disks.get(i);
            if( isFirst ){
                ret.append( cv.getDiskLabel().substring(0,1).toUpperCase() );
                isFirst = false;
            }else{
                ret.append( cv.getDiskLabel().substring(0,1).toUpperCase() );
            }
        }
        return ret.toString();
    }

    public boolean hasMTPPProtectedDisk(){
        int size = this.disks.size();
        for( int i=0; i<size; i++ ){
            ClusterVolume cv = disks.get(i);
            if( cv.isMtppProtect() ){
                return true;
            }
        }
        return false;
    }

    public boolean isConnected(){
        return this.isConnected;
    }

    public void setIsConnected( boolean val ){
        this.isConnected = val;
    }

    @Override public String toString(){
        return this.host.getIP();
    }

    public SubCluster cloneSubCluster(){
        SubCluster newSubClus = new SubCluster();
        newSubClus.host = this.host.cloneBootHost();
        newSubClus.d2d_host = this.d2d_host.cloneBackupClient();
        int size = disks.size();
        for( int i=0; i<size; i++ ){
            ClusterVolume disk = disks.get(i);
            newSubClus.addDisk( disk.cloneMe() );
        }
        newSubClus.isConnected = this.isConnected;
        return newSubClus;
    }
}
