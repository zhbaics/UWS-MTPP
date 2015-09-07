/*
 * Pool.java
 *
 * Created on 2008/2/19,�AM 11:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author Administrator
 */
public class Pool extends BasicUIObject{
    public final static String POOL_RECFLAG ="Record:";
    public final static String POOL_ID = "pool_id";
    public final static String POOL_NAME ="pool_name";
    public final static String POOL_PATH = "pool_path";
    public final static String POOL_VOLNAME = "pool_vol_name";
    public final static String POOL_PASSWD = "pool_passwd";
    public final static String POOL_FLAG ="pool_flag";
    public final static String POOL_MELT_MOVE_DATA = "pool_melt_move_data";
    public final static String POOL_TYPE = "pool_type";
    
    private int pool_id;
    private String pool_name; // 255
    private String pool_path; // 1024
    private String pool_vol_name; // 255
    private String pool_passwd; // 32
    private int pool_flag = 1; // 1: normal  2:not normal(mount failed)  0: already deleted,not mount when boot server
    private int pool_melt_move_data = 0; // 0: not use shared-fs     1: use shared-fs
    private int pool_type =0;
    
    private long totalSize=0;
    private long vused=0;
    private long free=0;
      
    /** Creates a new instance of Pool */
    public Pool() {
        super( ResourceCenter.TYPE_POOL );
    }

    public Pool( 
        int pool_id,
        String pool_name,
        String pool_path,
        String pool_vol_name,
        String pool_passwd,
        int pool_type
    ){
        super( ResourceCenter.TYPE_POOL );
        
        this.pool_id = pool_id;
        this.pool_name = pool_name;
        this.pool_path = pool_path;
        this.pool_vol_name = pool_vol_name;
        this.pool_passwd = pool_passwd;
        this.pool_type = pool_type;
    }
    
    public Pool( 
        int pool_id,
        String pool_name,
        String pool_path,
        String pool_vol_name,
        String pool_passwd,
        int pool_flag,
        int pool_melt_move_data,
        int pool_type
    ){
        super( ResourceCenter.TYPE_POOL );
        
        this.pool_id = pool_id;
        this.pool_name = pool_name;
        this.pool_path = pool_path;
        this.pool_vol_name = pool_vol_name;
        this.pool_passwd = pool_passwd;
        this.pool_flag = pool_flag;
        this.pool_melt_move_data = pool_melt_move_data;
        this.pool_type = pool_type;
    }
     
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 1 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_POOL ) );
        }
    }
    
    @Override public String toString(){
        return pool_id+"";
    }
    
    public int getPool_id() {
        return pool_id;
    }

    public void setPool_id(int pool_id) {
        this.pool_id = pool_id;
    }

    public String getPool_name() {
        return pool_name;
    }

    public void setPool_name(String pool_name) {
        this.pool_name = pool_name;
    }

    public String getPool_path() {
        return pool_path;
    }

    public void setPool_path(String pool_path) {
        this.pool_path = pool_path;
    }

    public String getPool_vol_name() {
        return pool_vol_name;
    }

    public void setPool_vol_name(String pool_vol_name) {
        this.pool_vol_name = pool_vol_name;
    }

    public String getPool_passwd() {
        return pool_passwd;
    }

    public void setPool_passwd(String pool_passwd) {
        this.pool_passwd = pool_passwd;
    }
    
    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getUsedSize() {
        return vused;
    }

    public void setUsed(long vused) {
        this.vused = vused;
    }

    public long getFreeSize() {
        return free;
    }

    public void setFreeSize(long free) {
        this.free = free;
    }

    public long getRealFreeSize(){
        long aFree = this.totalSize-this.vused;
        return aFree;
    }
    
    public String getRealFreeSizeStr(){
        return BasicVDisk.getCapStr( getRealFreeSize() );
    }
    
    public String getTotalSizeStr(){
        return BasicVDisk.getCapStr( getTotalSize() );
    }
    
    public int getPoolFlag(){
        return pool_flag;
    }
    public void setPoolFlag( int val ){
        this.pool_flag = val;
    }
    public boolean isPoolNormal(){
        return ( pool_flag == 1 );
    }
    public boolean isPoolNotNormal(){
        return ( pool_flag == 2 );
    }
    public boolean isPoolDeleted(){
        return ( pool_flag == 3 );
    }
    
    public int getPoolMeltMoveData(){
        return this.pool_melt_move_data;
    }
    public void setPoolMeltMoveData( int val ){
        this.pool_melt_move_data = val;
    }
    public boolean isUsedMeltMoveData(){
        return ( pool_melt_move_data == 1 );
    }

    @Override public String getComment(){
        return toString();
    }
    
    public int getPool_Type(){
        return this.pool_type;
    }

    public void setPool_Type(int type){
        this.pool_type = type;
    }

    public boolean isNormalPool(){
        return this.pool_type == 0;
    }

    public boolean isUCSPool(){
        return this.pool_type == 1;
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
        return ResourceCenter.ICON_POOL;
    }
    public String toTableString(){
        return pool_id+"";
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_POOL;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_POOL;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.pool_name;
    }
    public String toTipString(){
        return toTreeString();
    }
}
