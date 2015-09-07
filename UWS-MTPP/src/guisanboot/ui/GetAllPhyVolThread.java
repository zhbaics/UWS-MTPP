/*
 * GetAllPhyVolThread.java
 *
 * Created on 2007/5/18, PM 4:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import java.util.ArrayList;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.tool.Tool;

/**
 *
 * @author Administrator
 */
public class GetAllPhyVolThread extends BasicGetSomethingThread{
    private GetOrphanVol action = null;
      
    /** Creates a new instance of GetAllPhyVolThread */
    public GetAllPhyVolThread(
        SanBootView view
    ) {
        super( view,false );
    }
    
    public boolean realRun(){
        boolean ok;
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
        }catch(Exception ex){
            ok = false;
            Tool.prtExceptionMsg( ex );
        }
        
        return ok;
    }
    
    public ArrayList getRet(){
        return action.getAllVolFromCache();
    }
    public boolean getRetVal(){
        return isOk();
    }
    public String getErrorMsg(){
        return getErrMsg();
    }
}