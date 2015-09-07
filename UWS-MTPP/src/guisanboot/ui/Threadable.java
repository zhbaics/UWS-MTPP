/*
 * Threadable.java
 *
 * Created on June 28, 2005, 11:26 AM
 */

package guisanboot.ui;

/**
 *
 * @author  Administrator
 */
public interface Threadable {
    public void setDestroyFlag(boolean val);
    public boolean isOver();
    public void setOver(boolean val);
    public void startRun();
}
