/*
 * RemoveTaskLogThread.java
 *
 * Created on May 27, 2005, 1:13 PM
 */

package guisanboot.datadup.ui;
import javax.swing.*;
import javax.swing.table.*;

import guisanboot.datadup.data.*;
import guisanboot.res.*;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.SanBootView;


/**
 *
 * @author  Administrator
 */
public class RemoveTaskLogThread extends BasicGetSomethingThread{
    private int[] rows = null;
    private BackupHistoryDialog diag = null;
    private DefaultTableModel model = null;
    private int cnt = 0;
    private int selRow = -1;
    private int tskColId = -1;
    
    /** Creates a new instance of RemoveTaskLogThread */
    public RemoveTaskLogThread(
        SanBootView view,
        BackupHistoryDialog _diag,
        int _tskColId,
        int[] _rows
    ) {
        super( view );
        
        diag = _diag;
        tskColId = _tskColId;
        rows = _rows;
        model = diag.getTableModel();
    }
    
    public RemoveTaskLogThread(
        SanBootView view,
        BackupHistoryDialog _diag
    ) {
        super( view );
        diag = _diag;
    }
    
    public boolean realRun(){ 
        if( model!=null ){
            return clearLogArray();
        }else{
            return cleanAllLog();
        }
    }
    
    private boolean clearLogArray(){
        long id;
        boolean ok;
        
        int length = rows.length;
        for( int i=length-1; i>=0; i-- ){
            selRow = rows[i];
            
            id = ( (BakTask)model.getValueAt( rows[i],tskColId )).getID() ;
            
            ok = view.initor.mdb.removeTaskLog( id );
            if( !ok ){
                JOptionPane.showMessageDialog(diag,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_TSKLOG )+
                    ": "+view.initor.mdb.getErrorMessage()
                );
            }else{
                view.initor.mdb.delTaskLogInfo( id );  // 删除相关的日志文件，不管结果
                cnt ++;
                model.removeRow( selRow );
            }
        }
        
        return true;
    }
    
    public boolean cleanAllLog(){
        boolean ok = view.initor.mdb.removeAllTskLog();
        if( !ok ){
            JOptionPane.showMessageDialog(diag,
                ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_ALLLOG )+
                ": "+view.initor.mdb.getErrorMessage()
            );
        }else{
            view.initor.mdb.delFile( ResourceCenter.OUTPUT_DIR + "*" );
            cnt = 1; // 表示清除成功
        }
        
        return true;
    }
    
    public int getOkCount(){
        return cnt;
    }
}
