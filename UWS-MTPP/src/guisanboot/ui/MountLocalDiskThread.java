/*
 * MountLocalDiskThread.java
 *
 * Created on 2008/7/1,�PM 4:29
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import java.util.Vector;

/**
 *
 * @author zjj
 */
public class MountLocalDiskThread extends BasicGetSomethingThread{
    Vector mntList;
    String ip;
    int port;
    int mode;
    
    /** Creates a new instance of MountLocalDiskThread */
    public MountLocalDiskThread(
        SanBootView view,
        Vector mntList,
        String ip,
        int port,
        int mode
    ) {
        super( view );
        
        this.mntList = mntList;
        this.ip = ip;
        this.port = port;
        this.mode = mode;
    }
    
    public boolean realRun(){
        /*
        int size = mntList.size();
        for( int i=0; i<size; i++ ){
            RestoreMapper rst = (RestoreMapper)mntList.elementAt(i);
            if( rst.getSrc().toUpperCase().equals("C") ) continue;
            
            String mp = rst.getDestMp();
            String volInfo = rst.getVolName();
            isOk = view.initor.mdb.assignDriver( ip, port, "NULL", "NULL", volInfo, mp );  
        }
         */
         
        // 试图将"源盘恢复时的本地盘再mount起来"
        view.initor.mdb.adjustDriver( ip,port, "",mode ); // 不管结果
        
        return true;
    }
}
