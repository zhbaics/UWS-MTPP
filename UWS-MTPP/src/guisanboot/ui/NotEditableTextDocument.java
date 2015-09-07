/*
 * NotEditableTextDocument.java
 *
 * Created on 2005/11/11,��PM 4:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.text.*;

/**
 *
 * @author Administrator
 */
public class NotEditableTextDocument extends PlainDocument{    
    protected JTextComponent textComponent;
    
    /** Creates a new instance of GeneralPathDocument */
    public NotEditableTextDocument( JTextComponent tc ) {
        textComponent = tc;
    }
    
    // can't insert anything.
    @Override public void insertString( int offset,String s,AttributeSet set )
        throws BadLocationException
    {   
    }
    
    // can't remove anything
    @Override public void remove( int offset,int len )
            throws BadLocationException
    {
    }
    
    public void forceInserting( int offset,String s,AttributeSet set )
        throws BadLocationException
    {
        super.insertString( offset,s,set );
    }
    
    public void forceRemove( int offset,int len )
        throws BadLocationException
    {
        super.remove( offset,len );
    }
    
    public void forceRemoveAll()
        throws BadLocationException
    {
        int len = super.getLength();
        forceRemove( 0,len );
    }
}
