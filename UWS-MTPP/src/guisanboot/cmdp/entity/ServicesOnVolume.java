/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.entity;

/**
 * ServicesOnVolume.java
 *
 * Created on 2011-4-21, 14:12:15
 */
public class ServicesOnVolume {
    private String dg_name;
    private String[] volume_list;
    private String[] service_list;
    
    public ServicesOnVolume( String dg_name ){
        this.dg_name = dg_name;
    }

    public boolean isBelongedThisDg( String letter ){
        for( int i=0; i<volume_list.length; i++ ){
            if( volume_list[i].equals("") ) continue;

            if( volume_list[i].substring(0,1).toUpperCase().equals( letter.substring(0,1).toUpperCase() ) ){
                return true;
            }
        }
        return false;
    }

    public void setVolumeList( String[] list ){
        this.volume_list = list;
    }

    public void setServiceList( String[] list ){
        this.service_list = list;
    }
    
    public String[] getServiceList(){
        return  service_list;
    }

    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append( this.dg_name );
        buf.append(":");
        boolean isFirst = true;
        for( int i=0; i<this.volume_list.length; i++ ){
            if( isFirst ){
                buf.append( this.volume_list[i] );
                isFirst = false;
            }else{
                buf.append(";");
                buf.append( this.volume_list[i] );
            }
        }
        buf.append(":");
        isFirst = true;
        for( int i=0; i<this.service_list.length; i++ ){
            if( isFirst ){
                buf.append( this.service_list[i] );
                isFirst = false;
            }else{
                buf.append(";");
                buf.append( this.service_list[i] );
            }
        }
        return buf.toString();
    }
}
