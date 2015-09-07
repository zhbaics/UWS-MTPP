/*
 * CreatePoolThread.java
 *
 * Created on 2008/3/27, ��AM 11:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.data.*;
import guisanboot.res.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class CreateUcsPoolThread extends BasicGetSomethingThread{
    private String name;
    private long size;
    private String password;
    private int poolType;
    private ArrayList iList;
    
    /** Creates a new instance of CreatePoolThread */
    public CreateUcsPoolThread(
        SanBootView view,
        String name,
        long size,
        String password,
        int poolType,
        ArrayList iList
    ) {
        super( view );
        
        this.name = name;
        this.size = size;
        this.password = password;
        this.poolType = poolType;
        this.iList = iList ;
    }
    
    public boolean realRun(){ 
        view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );  
        //真正分配空间给存储池的操作
        StringBuffer devs = new StringBuffer();
        for ( int i =0 ; i < iList.size() ; i++){
            IdleDisk tmpdisk = (IdleDisk)iList.get(i);
            devs.append(tmpdisk.getIdleDiskName()).append(" ");
        }
        boolean aIsOk = view.initor.mdb.crtUcsPool(name, devs.toString());
        view.initor.mdb.restoreOldTimeOut();
        if( aIsOk ){
//            String devName = view.initor.mdb.getResDevName();
//            String mp = view.initor.mdb.getResMp();
            
            Pool pool = new Pool( -1,name, "", name, password , poolType);
            aIsOk = view.initor.mdb.addPool( pool );
            if(  aIsOk ){
                pool.setPool_id( view.initor.mdb.getNewId() );
                view.initor.mdb.addPoolIntoCache( pool );
            }else{
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.addPool")+
                        view.initor.mdb.getErrorMessage();
            }
        }else{
            errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.crtResVol")+
                    view.initor.mdb.getErrorMessage();
        }
        
        return aIsOk;
    }
    
    public boolean isOK(){
        return isOk();
    }
}