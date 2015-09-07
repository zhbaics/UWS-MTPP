/*
 * FetchFileable.java
 *
 * Created on 2008/8/14,�PM�5:57
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.datadup.ui;

/**
 *
 * @author zjj
 */
public interface FetchFileable {
    public void setDestroyFlag(boolean val);
    public boolean isOver();
    public void setOver(boolean val);
    public void startup();
}
