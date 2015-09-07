/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cluster.entity;

import guisanboot.data.BindOfPartandVol;
import guisanboot.data.VolumeMap;

/**
 * ClusterVolume.java
 *
 * Created on 2011-7-13, 18:11:25
 */
public class ClusterVolume {
    private BindOfPartandVol binder = null;
    private VolumeMap volume = null;

    public ClusterVolume(){
    }

    public ClusterVolume( BindOfPartandVol binder ){
        this.binder = binder;
    }

    public ClusterVolume( VolumeMap vol ){
        this.volume = vol;
    }

    public String getDiskLabel(){
        if( this.volume != null ){
            return this.volume.getVolDiskLabel();
        }else{
            return this.binder.part.getDiskLabel();
        }
    }

    public boolean isMtppProtect(){
        if( this.volume != null ){
            return this.volume.isMtppProtect();
        }else{
            return this.binder.isProtectedByMTPP();
        }
    }
    
    public boolean isCMDPProtect(){
        if( this.volume != null ){
            return this.volume.isCMDPProtect();
        }else{
            return this.binder.isProtectedByCMDP();
        }
    }
    
    public boolean isSameLetter( String label ){
        if( (this.getDiskLabel().indexOf(":\\") >= 0) && (label.indexOf(":\\") >= 0) ){
            String header = this.getDiskLabel().substring( 0,1 );
            return header.toUpperCase().equals( label.substring( 0,1 ).toUpperCase() );
        }else{
            return false;
        }
    }
    
    public VolumeMap getVolMapObj(){
        return this.volume;
    }

    public BindOfPartandVol getBinder(){
        return this.binder;
    }

    public ClusterVolume cloneMe(){
        ClusterVolume newOne = new ClusterVolume();
        if( this.volume != null ){
            newOne.volume = this.volume.cloneMe();
        }
        if( this.binder != null ){
            newOne.binder = this.binder.cloneMe();
        }
        return newOne;
    }
}
