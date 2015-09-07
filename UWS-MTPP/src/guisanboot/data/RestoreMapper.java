/*
 * RestoreMapper.java
 *
 * Created on 2007/2/27, PM��5:50
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;

/**
 *
 * @author Administrator
 */
public class RestoreMapper {
    private String src="";
    private DestDevice dest;
    private String label="";  // for linux host
    
    /** Creates a new instance of RestoreMapper */
    public RestoreMapper() {
    }
    
    public RestoreMapper( String _src,DestDevice _dest ){
        src = _src;
        dest = _dest;
    }
    
    public RestoreMapper( String _src,String _dest ){
        src = _src;
        dest = new DestDevice( _dest ); 
    }
    
    public RestoreMapper( String _src,DestDevice _dest,String _label ){
        src = _src;
        dest = _dest;
        label = _label;
    }
    
    public String getSrc(){
        return src;
    }
    public void setSrc( String val ){
        src = val;
    }
    public boolean isSwap(){
        return src.equals( ResourceCenter.SWAP_MP );
    }
    
    public DestDevice getDest(){
        return dest;
    }
    public void setDest( DestDevice val ){
        dest = val;
    }
    
    public String getDestMp(){
        return dest.getMp();
    }
    public void setDestMp( String val ){
        dest.setMp( val );
    }
    
    public String getLabel(){
        return label;
    }
    public void setLabel( String val ){
        label = val;
    }
    
    public String getVolName(){
        return dest.getVolName();
    }
    public void setVolName( String val ){
        dest.setVolName( val );
    }
}
