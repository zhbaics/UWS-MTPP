/*
 * BackupMDBThread.java
 *
 * Created on June 8, 2005, 1:20 PM
 */

package guisanboot.ui;

/**
 *
 * @author  Administrator
 */
public class BackupMDBThread extends BasicGetSomethingThread {
    private String path;
    private boolean retval;
    private int mode; // mode ===> 0: backup mdb 1: restore mdb
     
    /** Creates a new instance of BackupMDBThread */
    public BackupMDBThread(
        SanBootView view, 
        String _path,
        int _mode
    ) {
        super( view );
        
        path = _path;
        mode = _mode;
    }
    
    public boolean realRun(){ 
        if( mode == 0 ){
            retval = view.initor.mdb.backupMDB( path );
        }else{
            retval = view.initor.mdb.restMDB( path );
        }
        if( !retval ){
            errMsg = view.initor.mdb.getErrorMessage();
        }
        return retval;
    }
    
    public boolean getRetVal(){
        return retval;
    }
}
