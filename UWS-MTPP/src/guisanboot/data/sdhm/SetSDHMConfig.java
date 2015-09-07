/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import guisanboot.data.NetworkRunning;
import javax.swing.table.*;
import java.io.IOException;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class SetSDHMConfig extends NetworkRunning {

    private SDHMConfig sdhmConfig;
    private DefaultTableModel model;
    private SanBootView view;
    private int count;

    /** Creates a new instance of SetSDHMConfig */
    public SetSDHMConfig(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public SetSDHMConfig(String cmd) {
        super(cmd);
    }

    public void parser(String line) {
        String s1 = line.trim();
        SanBootView.log.debug(getClass().getName(), "output:" + s1);
    }
}
