/*
 * SimpleGetDestUWSrvThread.java
 *
 * Created on 2008/6/18,�Am�10:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.res.ResourceCenter;

/**
 *
 * @author zjj
 */
public class SimpleGetDestUWSrvThread extends BasicGetSomethingThread{
    /** Creates a new instance of SimpleGetDestUWSrvThread */
    public SimpleGetDestUWSrvThread(
        SanBootView view
    ) {
        super( view );
    }
    
    public boolean realRun(){  
        boolean isok = view.initor.mdb.updateSrcAgnt();
        if( !isok ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SRCAGNT ) + " : " +
                    view.initor.mdb.getErrorMessage();
        }else{
            isok = view.initor.mdb.updateUWSrv();
            if( !isok ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_UWS_SRV ) + " : " +
                    view.initor.mdb.getErrorMessage();
            }
        }
        
        return isok;
    }
}
