/*
 * Service.java
 *
 * Created on 2006/12/22, AM�10:16
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
public class Service {
    public final static String SERV_RECFLAG ="Record:";
    public final static String SERV_ID ="service_id";
    public final static String SERV_NAME="service_name";
    public final static String SERV_DESC="service_desc";

    public final static String SERV_CMDP_PATH = "path";
    public final static String SERV_CMDP_NAME = "name";
    public final static String SERV_CMDP_DISPNAME = "disp";
    
    private int serv_id=-1;
    private String serv_name; // 256
    private String serv_desc=""; // 1024
    private String serv_path="";
    
    private boolean isSeled = false;
    
    /** Creates a new instance of Service */
    public Service() {
    }
    
    public Service( String name ){
        this.serv_name = name;
    }

    public Service( 
        int _serv_id,
        String _serv_name,
        String _serv_desc
    ){
        serv_id = _serv_id;
        serv_name = _serv_name;
        serv_desc = _serv_desc;
    }
    
    public int getServID(){
        return serv_id;
    }
    public void setServID( int val ){
        serv_id = val;
    }
    
    public String getServName(){
        return serv_name;
    }
    public void setServName( String val ){
        serv_name = val;
    }
    
    public String getServDesc(){
        return serv_desc;
    }
    public void setServDesc( String val ){
        serv_desc = val;
    }

    public String getServPath(){
        return this.serv_path;
    }
    public void setServPath( String val ){
        this.serv_path = val;
    }
    public String getLetter(){
        int index = serv_path.indexOf(":");
        return serv_path.substring( index-1, index );
    }
    
    public boolean iSeled(){
        return isSeled;
    }
    public void setSeled( boolean val ){
        isSeled = val;
    }
    
    @Override public String toString(){
        return serv_name;
    }
    
    public String prtMe(){
        return serv_name+" "+serv_desc;
    }
    
    public String prtMeByCMDP(){
        StringBuffer buf = new StringBuffer();
        buf.append( SERV_CMDP_NAME );
        buf.append("=");
        buf.append(serv_name);
        buf.append(";");
        buf.append( SERV_CMDP_DISPNAME );
        buf.append("=");
        buf.append(serv_desc);
        buf.append(";");
        buf.append( SERV_CMDP_PATH );
        buf.append("=");
        buf.append(this.serv_path);
        buf.append(";");
        return buf.toString();
    }
}
