/*
 * VolumeRenderer.java
 *
 * Created on 2010/6/11, PM�15:04
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.ui;

import guisanboot.res.ResourceCenter;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author zjj
 */
    
class VolumeRenderer extends DefaultListCellRenderer{ 
    @Override public Component getListCellRendererComponent( JList list,
                                             Object value,
                                             int index,
                                             boolean isSelected,
                                             boolean cellHasFocus){
        super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        if( value != null ){
            setIcon( ResourceCenter.ICON_VOL );
        }
        return this;
    }
}


