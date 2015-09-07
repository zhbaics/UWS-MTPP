/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.SourceAgent;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefHostVolume;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncSnapList;
import mylib.UI.BrowserTreeNode;

/**
 * ProcessEventOnChiefLocalUIMSnap.java
 *
 * Created on 2010-1-12, 17:11:32
 */
public class ProcessEventOnChiefLocalUIMSnap extends ProcessEventOnUnlimitedIncMirrorVol{
    public ProcessEventOnChiefLocalUIMSnap(){
        this( null );
    }
    
    public ProcessEventOnChiefLocalUIMSnap( SanBootView view ){
        super( ResourceCenter.TYPE_CHIEF_UIM_SNAP1,view );
    }

    @Override public void showSnapList( BrowserTreeNode chiefLocalUIMSnap,int eventType ){
        curMirrorVolNode = chiefLocalUIMSnap;

        ChiefLocalUnLimitedIncSnapList chiefLocalUIMSnapObj = (ChiefLocalUnLimitedIncSnapList)chiefLocalUIMSnap.getUserObject();
        BrowserTreeNode mirrorVolNode = chiefLocalUIMSnapObj.getFatherNode();
        MirrorDiskInfo mdi = (MirrorDiskInfo)mirrorVolNode.getUserObject();
        BrowserTreeNode chiefHostVol = mdi.getFatherNode();
        ChiefHostVolume chiefHostVolume = (ChiefHostVolume)chiefHostVol.getUserObject();
        BrowserTreeNode srcAgntNode = chiefHostVolume.getFatherNode();
        SourceAgent srcagnt = (SourceAgent)srcAgntNode.getUserObject();
        boolean isCmdp = srcagnt.isCMDPProtect() && mdi.getSrc_agent_mp().substring(0,1).toUpperCase().equals( "C" );
        if( mdi.isRemoteCjVol() ){
            realShowSnapList( mdi.getSnap_rootid(),curMirrorVolNode,eventType,ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1,isCmdp );
        }else{
            realShowSnapList( mdi.getSnap_rootid(),curMirrorVolNode,eventType,ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP,isCmdp );
        }
    }
}
