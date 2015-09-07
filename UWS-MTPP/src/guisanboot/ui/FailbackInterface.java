/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

/**
 *
 * @author zjj
 */
public interface FailbackInterface {
    public void enableNextButton( boolean val );
    public void setDefaultCloseOperation( int act );
    public void setProcess();
    public void realFailback();
}
