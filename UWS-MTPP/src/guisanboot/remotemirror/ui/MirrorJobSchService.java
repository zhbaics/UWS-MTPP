/*
 * MirrorJobSchService.java
 *
 * Created on 2010/9/8, AM�10:58
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.remotemirror.ui;

import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class MirrorJobSchService extends ServiceItem{
    
    /** Creates a new instance of MirrorJobSchService */
    public MirrorJobSchService() {
    }
    
    @Override public String toTableString(){
        return SanBootView.res.getString("common.mjSchList");
    }
}
