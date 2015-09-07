/*
 * PaneEditorable.java
 *
 * Created on 2007/8/10, afternoon�3:39
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui.multiRenderTable;

/**
 *
 * @author Administrator
 */
public interface PaneEditorable {
    public void cleanSomething( int row);
    public void setNameCol( Object obj,int row );
    public void setSizeCol( Object obj,int row );
    public void setLVMTypeCol( Object obj,int row);
    public void setSnapSpaceCol( Object obj,int row );
    public void setBlkSizeCol( Object obj,int row );
    public void setPoolCol( Object obj,int row );
    public void setFormatFlagCol( boolean val,int row );
    public boolean isRightLVMType( String type );
    public Object getNameCol( int row );
    public Object getSizeCol( int row );
    public Object getPartitionCol( int row );
}
