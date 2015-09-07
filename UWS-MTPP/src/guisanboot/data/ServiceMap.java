/*
 * ServiceMap.java
 *
 * Created on 2006/12/22,�AM 10:23
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
public class ServiceMap {
    public final static String SERVMAP_RECFLAG ="Record:";
    public final static String SERVMAP_ID ="service_id";
    public final static String SERVMAP_CLNT_ID="clnt_id";
    public final static String SERVMAP_SERV_NAME="service_name";
    public final static String SERVMAP_SERV_DESC="service_desc";
    
    private int serv_id;
    private int clnt_id;
    private String name;
    private String desc;
    
    /** Creates a new instance of ServiceMap */
    public ServiceMap() {
    }
    
    public ServiceMap(
        int _serv_id,
        int _clnt_id,
        String _name,
        String _desc
    ){
        serv_id = _serv_id;
        clnt_id = _clnt_id;
        name = _name;
        desc = _desc;
    }
    
    public int getServID(){
        return serv_id;
    }
    public void setServID( int val ){
        serv_id = val;
    }
    
    public int getClntID(){
        return clnt_id;
    }
    public void setClntID( int val ){
        clnt_id = val;
    }
    
    public String getServName(){
        return name;
    }
    public void setServName( String val ){
        name = val;
    }
    
    public String getServDesc(){
        return desc;
    }
    public void setServDesc( String val ){
        desc = val;
    }
    
    @Override public String toString(){
        return name;
    }
}
