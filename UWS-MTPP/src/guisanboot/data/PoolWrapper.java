/*
 * PoolWrapper.java
 *
 * Created on 2008/3/20,�PM�1:11
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class PoolWrapper {
    public Pool pool;
    
    /** Creates a new instance of PoolWrapper */
    public PoolWrapper( Pool pool ) {
        this.pool = pool;
    }
    
    @Override public String toString(){
        return pool.getPool_name()+"("+pool.getRealFreeSizeStr()+"/"+pool.getTotalSizeStr()+")";
    }
}
