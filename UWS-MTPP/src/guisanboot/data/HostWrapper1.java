/*
 * HostWrapper1.java
 *
 * Created on 2008/6/27,�PM 1:27
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author zjj
 */
public class HostWrapper1 extends HostWrapper{
    
    /** Creates a new instance of HostWrapper1 */
    public HostWrapper1( BootHost host) {
        super( host );
    }
    
    @Override public String toString(){
        return host.getIP()+" [ "+host.getName()+" ]";
    }
}
