/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.audit.ctrl;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.audit.cmd.GetAuditLog;
import guisanboot.audit.data.BackupUser;
import guisanboot.audit.data.Audit;
import mylib.tool.Check;

/**
 *
 * @author Administrator
 */
public class AuditCenter {
    private SanBootView view;
    private MenuAndBtnCenterForMainUi mbCenter;
    private String strUser = "";    // current logon user
    private String passwd  = "";    // current logon user's password
    private BackupUser user = null; 
    
    public AuditCenter(){ 
    }
    
    public AuditCenter( SanBootView view,String strUser,String passwd ){
        this.view    = view;
        this.strUser = strUser;
        this.passwd  = passwd;
    }
    
    public void setMenuAndBtnCenter( MenuAndBtnCenterForMainUi mbCenter ){
        this.mbCenter = mbCenter;
    }
    
    public void setView( SanBootView view ){
        this.view = view;
    }
    
    public String getLogonUser(){
        return strUser;
    }
    public void setLogonUser( String strUser ){
        this.strUser = strUser;
        user = view.initor.mdb.getBakUserOnName( strUser );
    }
    public void clearUserObj(){
        user = null;
    }
    
    public String getUserPassword(){
        return passwd;
    }
    public void setUserPassword( String passwd ){
        this.passwd = passwd;
    }
    
    public void ctrlMenuAndBtn(){
        if( user != null ){
System.out.println(" #############  user right: "+ user.getRight() );            
            this.mbCenter.ctrlMenuAndBtnOnRight( user.getRight() );
        }else{ // impossible to happen
            this.mbCenter.ctrlMenuAndBtnOnRight( 0 );
        }
    }
    
    public boolean canDoThis( int func_id ){
        int aRight = 0;
        
        if( user != null ){
            aRight = user.getRight();
        }
        
        if( BackupUser.hasThisRight( aRight, BackupUser.RIGHT_ADMIN ) ){
            return true;
        }
        
        switch( func_id ){
            case MenuAndBtnCenterForMainUi.FUNC_PROPERTY :
                return true;
            case MenuAndBtnCenterForMainUi.FUNC_INIT:
                return BackupUser.hasThisRight( aRight,BackupUser.RIGHT_INIT_HOST );
            case MenuAndBtnCenterForMainUi.FUNC_FAILOVER:
                return BackupUser.hasThisRight( aRight,BackupUser.RIGHT_NETBOOT );
            case MenuAndBtnCenterForMainUi.FUNC_FAILBACK:
                return BackupUser.hasThisRight( aRight,BackupUser.RIGHT_LOCAL_DISK_BOOT );
            case MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK:
                return BackupUser.hasThisRight( aRight,BackupUser.RIGHT_RST_ORG_DISK );
            case MenuAndBtnCenterForMainUi.FUNC_DEL_DSTAGNT:
                return BackupUser.hasThisRight( aRight,BackupUser.RIGHT_DEL_NETBOOT_HOST );
            case MenuAndBtnCenterForMainUi.FUNC_CRT_VOL:
                return BackupUser.hasThisRight( aRight,BackupUser.RIGHT_CRT_VOL );
            case MenuAndBtnCenterForMainUi.FUNC_DEL_VOL:
                return BackupUser.hasThisRight(aRight, BackupUser.RIGHT_DEL_VOL );
            case MenuAndBtnCenterForMainUi.FUNC_ONLINE:
            case MenuAndBtnCenterForMainUi.FUNC_OFFLINE:
            case MenuAndBtnCenterForMainUi.FUNC_MNT_SNAP: 
                return true;
            case MenuAndBtnCenterForMainUi.FUNC_CRT_HOST:
                return BackupUser.hasThisRight( aRight,BackupUser.RIGHT_INIT_HOST );
            case MenuAndBtnCenterForMainUi.FUNC_CRT_UWS_SRV:
            case MenuAndBtnCenterForMainUi.FUNC_DEL_UWS_SRV:
            case MenuAndBtnCenterForMainUi.FUNC_MOD_UWS_SRV:
            case MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN:
            case MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN:
            case MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER:
            case MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD:
            case MenuAndBtnCenterForMainUi.FUNC_MNT_VIEW:
            case MenuAndBtnCenterForMainUi.FUNC_UMNT_VIEW:
            case MenuAndBtnCenterForMainUi.FUNC_CRT_MJ:
            case MenuAndBtnCenterForMainUi.FUNC_DEL_MJ:
            case MenuAndBtnCenterForMainUi.FUNC_MOD_MJ:
            case MenuAndBtnCenterForMainUi.FUNC_START_MJ:
            case MenuAndBtnCenterForMainUi.FUNC_STOP_MJ:
            case MenuAndBtnCenterForMainUi.FUNC_MONITOR_MJ:
            case MenuAndBtnCenterForMainUi.FUNC_MOD_SRC_UWS_SRV:                
                return true;
            case MenuAndBtnCenterForMainUi.FUNC_CRT_SNAP:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_CRT_SNAP );
            case MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_DEL_SNAP );
            case MenuAndBtnCenterForMainUi.FUNC_LUNMAP:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_CRT_LM );
            case MenuAndBtnCenterForMainUi.FUNC_CANCEL_LM:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_DEL_LM );
            case MenuAndBtnCenterForMainUi.FUNC_DEL_HOST:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_DEL_HOST );
            case MenuAndBtnCenterForMainUi.FUNC_MOD_HOST:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_MOD_HOST );
            case MenuAndBtnCenterForMainUi.FUNC_CRT_VIEW:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_CRT_VIEW );
            case MenuAndBtnCenterForMainUi.FUNC_DEL_VIEW:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_DEL_VIEW );
            case MenuAndBtnCenterForMainUi.FUNC_CRT_POOL:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_CRT_POOL );
            case  MenuAndBtnCenterForMainUi.FUNC_DEL_POOL:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_DEL_POOL );
            case MenuAndBtnCenterForMainUi.FUNC_ADD_PROF:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_CRT_PROF );
            case MenuAndBtnCenterForMainUi.FUNC_MOD_PROF:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_MOD_PROF );
            case MenuAndBtnCenterForMainUi.FUNC_DEL_PROF:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_DEL_PROF );
            case MenuAndBtnCenterForMainUi.FUNC_RENAME_PROF:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_REN_PROF );
            case MenuAndBtnCenterForMainUi.FUNC_RUN_PROF:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_RUN_PROF );
            case MenuAndBtnCenterForMainUi.FUNC_VERIFY_PROF:
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_VER_PROF );
            case MenuAndBtnCenterForMainUi.FUNC_ADD_SCH :
            case MenuAndBtnCenterForMainUi.FUNC_ADD_ADSCH :
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_CRT_SCH );
            case MenuAndBtnCenterForMainUi.FUNC_MOD_SCH: 
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_MOD_SCH );
            case MenuAndBtnCenterForMainUi.FUNC_DEL_SCH:    
                return BackupUser.hasThisRight( aRight, BackupUser.RIGHT_DEL_SCH );
            default:
                return true;
        } 
    }
   
    public boolean isLoginUsrIsAdmin(){
        if( user != null ){
            return user.isAdminRight();
        }else{
            return false;
        }
    }
    
    public boolean hasThisRight( int right_mask ){
        if( !isLoginUsrIsAdmin() ){
            if( user != null ){    
                return BackupUser.hasThisRight( user.getRight(), right_mask );
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
    
    public boolean isLoginUserName( String name ){
        if( user != null ){
            return user.getUserName().equals( name );
        }else{
            return false;
        }
    }
    
    public Audit registerAuditRecord( int cid,int eid ){
        return registerAuditRecord( cid,eid,"" );
    }
    
    public Audit registerAuditRecord( int cid,int eid,String event_desc ){
        Audit audit;
        
        if( user != null ){
            audit = new Audit(
               (int)user.getID(),
               Check.getCurrentTime(),
               cid,
               eid,
               event_desc
            );
        }else{
            audit = new Audit(
                0,
                Check.getCurrentTime(),
                cid,
                eid,
                event_desc
            );
        }
        return audit;
    }
    
    public boolean addAuditRecord( Audit audit ){
        return view.initor.mdb.addAudit( audit );
    }   
}
