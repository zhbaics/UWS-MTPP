/*
 * BackupUserOld.java
 *
 * Created on April 2, 2005, 5:24 AM
 */

package guisanboot.data;

import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.ui.*;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author  Administrator
 */

//这个类在做完audit后就可以删除了 （2011.3.19）

public class BackupUserOld  extends BasicUIObject {
    public final static int RIGHT_ADMIN = 0x01;
    public final static int RIGHT_OPER  = 0x02;

    public final static String BAKUSER_RECFLAG ="Record:";
    public final static String BAKUSER_ID   ="account_id";
    public final static String BAKUSER_NAME ="account_name";
    public final static String BAKUSER_PASS ="account_pass";
    public final static String BAKUSER_PRIV ="account_priv";
  
    private long id=-1L;
    private String name;   //32
    private String passwd; //32
    private int right;
    
    public BackupUserOld(){
        super( ResourceCenter.TYPE_USER_INDEX );
    }
    
    public BackupUserOld(
        String _name,
        String _passwd,
        int _right
    ) {
        super( ResourceCenter.TYPE_USER_INDEX );
        
        name   = _name;
        passwd = _passwd;
        right  = _right;
    }
    
    /** Creates a new instance of BackupUserOld */
    public BackupUserOld(
        long  _id,
        String _name,
        String _passwd,
        int _right
    ) {
        super( ResourceCenter.TYPE_USER_INDEX );
        
        id = _id;
        name = _name;
        passwd = _passwd;
        right = _right;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 2 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_USER ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_USER ) );
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
        return ( ( right&BackupUserOld.RIGHT_ADMIN )!=0 )?true:false;
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
            right = right | BackupUserOld.RIGHT_ADMIN;
        else
            right = right &(~BackupUserOld.RIGHT_ADMIN );
    }
   
    public boolean isOperRight(){
        return ( ( right&BackupUserOld.RIGHT_OPER )!=0 )?true:false;
    }

    public void setOperRight( boolean val ){
        if( val ){
            right = right | BackupUserOld.RIGHT_OPER;
        }else
            right = right &( ~BackupUserOld.RIGHT_OPER );
    }
    
    @Override public String toString(){
        return name;
    }
    
    @Override public String getComment(){
        return toString();
    }

    //** TableRevealable的实现**/
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
    
    //** TreeRevealable的实现�
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

