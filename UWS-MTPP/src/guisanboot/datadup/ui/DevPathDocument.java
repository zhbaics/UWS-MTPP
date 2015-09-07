/*
 * DevPathDocument.java
 *
 * Created on September 8, 2005, 11:19 AM
 */

package guisanboot.datadup.ui;

import javax.swing.text.*;

/**
 *
 * @author  Administrator
 */

// the following customed document class is for raw disk backup
public class DevPathDocument extends GeneralPathDocument{
    public static String initString = "/dev/";
    public static int sep1 = 0, sep2 = 1, sep3 = 2, sep4 = 3, setp5 = 4;
    private int newOffset;
    
    /** Creates a new instance of DevPathDocument */
    public DevPathDocument( JTextComponent tc ){
        super( tc,initString );
    }
    
    @Override public void insertString( int offset,String s,AttributeSet set )
        throws BadLocationException
    {    
        if( s.equals(initString) ){
            super.insertString(offset,s,set);
        }else{
            newOffset = offset;
            
            if( atSeparator( offset ) ){
                newOffset++;
                textComponent.setCaretPosition(newOffset);
            }else{
                if( s.equals("/") ){
                    String oldStr = getText( offset-1,1 );
                    if( !oldStr.equals("/") ){
                        super.insertString( newOffset,s,set );
                    }
                }else if( s.equals(".") ){
                    // 不能输入".",因为"../"和"./"会造成不必要的混乱
                }else{
                    super.insertString( newOffset,s,set );
                }
            }
        }
    }

    @Override public void remove( int offset,int len )
            throws BadLocationException
    {
        if( atSeparator( offset ) ){
            if( offset >=1 )
                textComponent.setCaretPosition(offset-1);
        }else{
            textComponent.setCaretPosition(offset);
            super.remove( offset,len );
        }
    }
        
    public boolean atSeparator( int offset ){
        return offset == sep1 || offset == sep2 ||
                offset == sep3 || offset == sep4 ||
                offset == setp5;
    }
}
