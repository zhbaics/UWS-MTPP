/*
 * GeneralPathDocument.java
 *
 * Created on July 29, 2008, 12:56 PM
 */

package guisanboot.datadup.ui;

import javax.swing.text.*;

/**
 *
 * @author  Administrator
 */
public class GeneralPathDocument extends PlainDocument{              
    protected JTextComponent textComponent;
    
    /** Creates a new instance of GeneralPathDocument */
    public GeneralPathDocument( JTextComponent tc,String initStr ) {
        textComponent = tc;
        try{
            insertString( 0,initStr,null );
        }catch(Exception ex){
        }
    }
    
    @Override public void insertString( int offset,String s,AttributeSet set )
        throws BadLocationException
    {   
        if( s.equals("/") ){
            if( offset >0 ){
                String oldStr = getText( offset-1,1 );
                if( !oldStr.equals("/") ){
                        super.insertString( offset,s,set );
                }
            }else{
                super.insertString( offset,s,set );
            }
        }else{
            super.insertString( offset,s,set );
        }
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
}
