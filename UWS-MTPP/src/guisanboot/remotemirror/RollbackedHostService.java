/*
 * RollbackedHostService.java
 *
 * Created on 2008/6/18,�PM 2:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.remotemirror;

import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class RollbackedHostService extends ServiceItem{
    
    /** Creates a new instance of RollbackedHostService */
    public RollbackedHostService() {
    }
    
    @Override public String toTableString(){
        return SanBootView.res.getString("common.rbhostlist");
    }
}
