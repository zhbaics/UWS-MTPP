/*
 * TreeProcessable.java
 *
 * Created on July 14, 2005, 11:50 AM
 */

package guisanboot.ui;

import mylib.UI.*;

/**
 *
 * @author  Administrator
 */
public interface TreeProcessable {
    public boolean realDo();
    public void setFatherTreeNode( BrowserTreeNode fNode);
    public void setProcessEvent( GeneralProcessEventForSanBoot process);
    public void setAddTableFlag( boolean addTable );
    public void setAddTreeFlag( boolean addTree );
}
