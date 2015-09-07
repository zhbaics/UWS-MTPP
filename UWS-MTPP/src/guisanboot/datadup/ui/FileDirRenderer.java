/*
 * FileDirRenderer.java
 *
 * Created on 2008/7/29, PM�12:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.datadup.ui;

import guisanboot.res.ResourceCenter;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author zjj
 */
    
class FileDirRenderer extends DefaultListCellRenderer{ 
    @Override public Component getListCellRendererComponent( JList list,
                                             Object value,
                                             int index,
                                             boolean isSelected,
                                             boolean cellHasFocus){
        super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        if( value != null ){
            String file =(String)value;
            char tmp = file.charAt( file.length() -1 );
            if( (tmp == '/') || (tmp == '\\') ){
                setIcon( ResourceCenter.SMALL_ICON_FOLDER );
            }else{
                setIcon( ResourceCenter.SMALL_ICON_FILE );
            }

            setText( file );
        }
        return this;
    }
}


