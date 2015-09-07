/*
 * StartupProgress.java
 *
 * Created on 2008/9/8,��AM 10:09
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.JDialog;

/**
 *
 * @author zjj
 */
public interface StartupProgress {
    public void setProcessDialg( JDialog diag );
    public void startProcessing();
}
