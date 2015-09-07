/*
 * HostWrapper.java
 *
 * Created on 2007/12/10,�AM 11:33
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
public class HostWrapper {
    public BootHost host;
    
    /** Creates a new instance of HostWrapper */
    public HostWrapper( BootHost _host ) {
        host = _host;
    }
    
    @Override public String toString(){
        return host.getIP();
    }
    
    public int getPort(){
        return host.getPort();
    }

    public int getCmdpPort(){
        return host.getCmdpPort();
    }

    public int getMtppPort(){
        return host.getMtppPort();
    }
    
    public String getUUID(){
        return host.getUUID();
    }
}
