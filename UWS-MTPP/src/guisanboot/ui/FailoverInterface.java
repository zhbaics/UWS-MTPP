/*
 * FailoverInterface.java
 *
 * Created on 2008/7/3, PM 4:51
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

/**
 *
 * @author zjj
 */
public interface FailoverInterface {
    public void enableNextButton( boolean val );
    public void realDRRecover( );
}
