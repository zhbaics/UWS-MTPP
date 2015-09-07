/*
 * PrepareNetBootThread.java
 *
 * Created on 2008/7/1,��PM 4:29
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import java.util.ArrayList;

/**
 *
 * @author zjj
 */
public class PrepareNetBootThread extends BasicGetSomethingThread{
    ArrayList ibootList = null;
    
    /** Creates a new instance of PrepareNetBootThread */
    public PrepareNetBootThread(
        SanBootView view
    ) {
        super( view );
    }
    
    public boolean realRun(){
        boolean ok = view.initor.mdb.listIboot();
        if( ok ){
            ibootList = view.initor.mdb.getAllIboot();
        }
        return ok;
    }
    
    public ArrayList getIbootList(){
        return ibootList;
    }
}
