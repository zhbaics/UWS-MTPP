/*
 * File.java
 *
 * Created on March 31, 2005, 3:46 PM
 */

package guisanboot.data;

/**
 *
 * @author  Administrator
 */
public class UnixFileObj {
    private String type;
    private String absPath;
    private String name;
    private long lastModTime = 0L;
    
    /** Creates a new instance of File */
    public UnixFileObj(  String _type, 
                           String _absPath, 
                           String _name,
                           long _lastModTime
    ) {
        type = _type;
        absPath = _absPath;
        name = _name;
        lastModTime = _lastModTime;
    }
    
    public UnixFileObj(
        String type,
        String absPath,
        String name
    ){
        this( type,absPath,name,0L );
    }
    
    public boolean isDir(){
        return type.startsWith("d");
    }
    
    public String getName(){
        return name;
    }
    
    public String getAbsPath(){
        return absPath;
    }
    
    public long getLastModTime(){
        return lastModTime;
    }
}
