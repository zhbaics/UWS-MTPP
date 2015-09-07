/*
 * SeledHostForNetBoot.java
 *
 * Created on 2008/6/27,�PM 3:19
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
public class SeledHostForNetBoot {
    public BootHost host = null;
    public String mac = "";
    
    /** Creates a new instance of SeledHostForNetBoot */
    public SeledHostForNetBoot( BootHost host,String mac ) {
        this.host = host;
        this.mac = mac;
    }
}
