/*
 * BackupUser.java
 *
 */

package guisanboot.audit.data;

import javax.swing.*;
import java.util.ArrayList;
import mylib.UI.*;
import guisanboot.ui.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class BackupUser  extends BasicUIObject {
    public final static int RIGHT_ADMIN      = 0x01;   // admin bit
    public final static int RIGHT_INIT_HOST  = 0x02;   // init host bit
    public final static int RIGHT_NETBOOT    = 0x04;   // netboot host
    public final static int RIGHT_RST_ORG_DISK = 0x08; // restore original disk
    
    public final static int RIGHT_LOCAL_DISK_BOOT = 0x10; // local disk boot
    public final static int RIGHT_MOD_HOST = 0x20; // modify host
    public final static int RIGHT_DEL_HOST = 0x40; // delete host
    public final static int RIGHT_CRT_VOL  = 0x80; // create volume
    
    public final static int RIGHT_DEL_VOL = 0x100;  // delete volume
    public final static int RIGHT_CRT_LM = 0x200;   // create lunmap
    public final static int RIGHT_DEL_LM = 0x400;   // delete lunmap
    public final static int RIGHT_CRT_SNAP = 0x800; // create snapshot
    
    public final static int RIGHT_DEL_SNAP = 0x1000;  // delete snapshot
    public final static int RIGHT_CRT_VIEW = 0x2000;  // create view
    public final static int RIGHT_DEL_VIEW = 0x4000;  // delete view
    public final static int RIGHT_CRT_PROF = 0x8000;  // create profile
    
    public final static int RIGHT_MOD_PROF = 0x10000; // modify profile
    public final static int RIGHT_DEL_PROF = 0x20000; // delete profile
    public final static int RIGHT_RUN_PROF = 0x40000; // run profile
    public final static int RIGHT_VER_PROF = 0x80000; // verrify profile
    
    public final static int RIGHT_REN_PROF = 0x100000;  // rename profile
    public final static int RIGHT_CRT_SCH  = 0x200000;  // create scheduler
    public final static int RIGHT_MOD_SCH  = 0x400000;  // modify scheduler
    public final static int RIGHT_DEL_SCH  = 0x800000;  // delete scheduler
   
    public final static int RIGHT_DEL_DUPLOG = 0x1000000;  // del dup log
    public final static int RIGHT_DHCP       = 0x2000000;  // dhcp
    public final static int RIGHT_CRT_POOL   = 0x4000000 ; // create pool
    public final static int RIGHT_DEL_POOL   = 0x8000000;  // delete pool
    
    public final static int RIGHT_MOD_PWD = 0x10000000;     // modify password
    public final static int RIGHT_CANCEL_JOB = 0x20000000;  // cancel job
    public final static int RIGHT_DEL_NETBOOT_HOST = 0x40000000; // delete netboot host
    public final static int RIGHT_SHUTDOWN = 0x80000000; // shuwdown uws server
    
    public final static String BAKUSER_RECFLAG = "Record:";
    public final static String BAKUSER_ID   = "account_id";
    public final static String BAKUSER_NAME = "account_name";
    public final static String BAKUSER_PASS = "account_pass";
    public final static String BAKUSER_PRIV = "account_priv";
    public final static String BAKUSER_DEVLIST ="account_dev_list";
    
 
    private long id = -1L;
    private String name;   //32
    private String passwd; //32
    private int  right = 0;
    private long devid = 0;
    
    public BackupUser(){    
        super( ResourceCenter.TYPE_USER_INDEX );
    }
    
    public BackupUser(
        String _name,
        String _passwd,
        int _right
    ) {
        super( ResourceCenter.TYPE_USER_INDEX );
        
        name   = _name;
        passwd = _passwd;
        right  = _right;
    }
    
    /** Creates a new instance of BackupUser */
    public BackupUser(
        long  _id,
        String _name,
        String _passwd,
        int _right,
        int _devid
    ) {
        super( ResourceCenter.TYPE_USER_INDEX );
        
        id = _id;
        name = _name;
        passwd = _passwd;
        right = _right;
        devid = _devid;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }
    
    public long getID(){
        return id;
    }
    public void setID( long _id ){
        id = _id;
    }
    
    public String getUserName(){
        return name;
    }
    public void setUserName(String _name){
        name = _name;
    }
    
    public String getPasswd(){
        return passwd;
    }
    public void setPasswd( String _passwd){
        passwd = _passwd;
    }
    
    public int getRight(){
        return right;
    }
    public void setRight(int _right){
        right = _right;
    }
    
    public boolean isAdminRight(){
        return ( ( right&BackupUser.RIGHT_ADMIN )!=0 );
    }
    
    public boolean isOperRight(){
        return !isAdminRight();
    }
    
    public String getRightString(){
        if( isAdminRight() ){
            return SanBootView.res.getString("common.adminperm");
        }else{
            return SanBootView.res.getString("common.normalperm");
        }
    }
  
    public void setAdminRight( boolean val ){
        if( val )
            right = right | BackupUser.RIGHT_ADMIN;
        else
            right = right &(~BackupUser.RIGHT_ADMIN );
    }
    
    public boolean hasThisRight( int right_mask ){
        return ( ( right & right_mask ) != 0 ); 
    }
    
    public static boolean hasThisRight( int aRight, int right_mask ){
        return ( ( aRight & right_mask ) != 0 );
    }
    
    public void setThisRight( int right_mask,boolean val ){
        if( val )
            right = right | right_mask;
        else
            right = right & ( ~right_mask );
    }
    
    public static int setThisRight( int right, int right_mask, boolean val ){
        if( val )
            right = right | right_mask;
        else
            right = right &(~right_mask );
        
        return right;
    }
    
    public long getDevList(){
        return devid;
    }
    public void setDevList( long val ){
        devid = val;
    }
    
    @Override public String toString(){
        return name;
    }
    
    @Override public String getComment(){
        return toString();
    }

    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.ICON_USER_16;
    }
    public String toTableString(){
        return name;
    }
    
    public Icon getExpandIcon(){
        return null;
    }
    public Icon getCollapseIcon(){
        return null;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return toString();
    }
    public String toTipString(){
        return toTreeString();
    }

    public String printMe(){
        return toString();
    }
}
