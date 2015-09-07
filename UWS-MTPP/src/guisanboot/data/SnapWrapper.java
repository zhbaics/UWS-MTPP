/*
 * SnapWrapper.java
 *
 * Created on 2007/1/13, AM 11:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;

/**
 *
 * @author Administrator
 */
public class SnapWrapper {
    public Snapshot snap;
    
    @Override public String toString(){
        if( snap.getSnap_desc().equals("") ){
            return SanBootView.res.getString("common.snap") + " : " + snap.getCreateTimeStr();
        }else{
            return SanBootView.res.getString("common.snap") + " : " + 
                    snap.getCreateTimeStr()+
                    " ["+snap.getSnap_desc()+"]";
        }
    }
    
    /** Creates a new instance of VolumeMapWrapper */
    public SnapWrapper( Snapshot snap ) {
        this.snap = snap;
    }
}
