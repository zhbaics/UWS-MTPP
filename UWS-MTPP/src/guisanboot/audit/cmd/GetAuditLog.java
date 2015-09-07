/*
 * GetAuditLog.java
 *
 * Created on Aug 10, 2009, 12:00 AM
 */

package guisanboot.audit.cmd;

import guisanboot.audit.data.BackupUser;
import guisanboot.MenuAndBtnCenterForMainUi;
import javax.swing.*;
import javax.swing.table.*;
import java.io.IOException;

import mylib.UI.*;
import guisanboot.audit.data.*;
import guisanboot.ui.*;
import guisanboot.res.*;
import guisanboot.data.*;

/**
 *
 * @author  Administrator
 */
public class GetAuditLog extends NetworkRunning{
    private Audit curAuditLog;
    private DefaultTableModel model;
    private SanBootView view;
    private int count;
    
    /** Creates a new instance of GetTaskLog */
    public GetAuditLog( SanBootView _view ,String cmd ) throws IOException {
        super( cmd,_view.getSocket() );
        view = _view;
    }
    
    public GetAuditLog( String cmd ) {
        super( cmd );
    }
    
    public void parser(String line){
        String s1 = line.trim();
//System.out.println("#####(GetTaskLog): "+s1);
        int index = s1.indexOf("=");
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            if( s1.startsWith( Audit.AUDITID ) ){
                try{
                    int id = Integer.parseInt( value );
                    curAuditLog.setID( id );
                }catch(Exception ex){
                    curAuditLog.setID( -1 );
                }
            }else if( s1.startsWith( Audit.AUDITUID )){
                try{
                    int uid = Integer.parseInt( value );
                    curAuditLog.setUid( uid );
                }catch(Exception ex){
                    curAuditLog.setUid( -1 );
                }
            }else if( s1.startsWith( Audit.AUDITTIME )){           
                curAuditLog.setEventtime( value );
            }else if(s1.startsWith( Audit.AUDITCID )){
                try{
                    int cid = Integer.parseInt( value );
                    curAuditLog.setCID( cid );
                }catch(Exception ex){
                    curAuditLog.setCID( -1 );
                }
            }else if( s1.startsWith( Audit.AUDITEID )){
                try{
                    int eid = Integer.parseInt( value );
                    curAuditLog.setEID( eid );
                }catch(Exception ex){
                    curAuditLog.setEID( -1 );
                }
            }else if( s1.startsWith( Audit.AUDITEVENTDESC ) ){
                curAuditLog.setEventDesc( value );
                
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch( Exception ex ){
                }
                count++;
            }
        }else{
            if( s1.startsWith( Audit.AUDITRECFLAG )){
                curAuditLog = new Audit();
            }
        }
//        System.out.println("curAuditLog:------" + curAuditLog.getCID());

    }
    
    public void setTableModel( DefaultTableModel _model ){
        model = _model;
    }
    
    Runnable insertModel = new Runnable(){
        public void run(){
            if( model == null ) return;
            
            Object[] one = new Object[5];
            
            one[0] = curAuditLog;
            
            int uid = curAuditLog.getUid();
            BackupUser user = view.initor.mdb.getBakUserFromVectorOnID( uid );
            if( user != null ){
                one[1] = new GeneralBrowserTableCell(
                    -1, user.getUserName(), JLabel.LEFT 
                );
            }else{
                one[1] = new GeneralBrowserTableCell(
                    -1, SanBootView.res.getString("common.unknown")+" "+
                        SanBootView.res.getString("BackupHistoryDialog.miss"),
                     JLabel.LEFT 
                );
            }
            
            one[2] = new GeneralBrowserTableCell(
                -1,curAuditLog.getEventtimeStr(),JLabel.LEFT
            );
            
            int cid = curAuditLog.getCID();
            BootHost host = view.initor.mdb.getHostFromVectorOnID( cid );
            if( host != null ){
                one[3] = new GeneralBrowserTableCell(
                    -1, host.getName(), JLabel.LEFT
                );
            }else{
                if( MenuAndBtnCenterForMainUi.isRelatedToUser( curAuditLog.getEID() ) ){
                    one[3] = new GeneralBrowserTableCell(
                        -1,SanBootView.res.getString("common.unknown")+" "+
                           SanBootView.res.getString("BackupHistoryDialog.miss"),
                        JLabel.LEFT
                    );
                }else{
                    one[3] = new GeneralBrowserTableCell(
                        -1,"", JLabel.LEFT
                    );
                }
            }
            
            one[4] = new GeneralBrowserTableCell(
                -1,MenuAndBtnCenterForMainUi.getAuditOPType( curAuditLog.getEID() ),JLabel.LEFT
            );
            model.addRow( one );
        }
    };
    
    public boolean updateAuditLog( int begin,int num ){
        try{
            curAuditLog = null;
            count = 0;
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_AUDIT )+
                begin+" "+num
            );
SanBootView.log.info(getClass().getName(), " get audit log cmd: " + getCmdLine() );    
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info(getClass().getName(), " get audit log cmd retcode: " + getRetCode() );        
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(getClass().getName(), " get audit log cmd errmsg: " + getErrMsg() );            
        }
        return isOk;
    }
    
    public int getCount(){
        return count;
    }
}
