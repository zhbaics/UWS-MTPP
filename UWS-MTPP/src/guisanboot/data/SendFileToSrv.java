/*
 * SendFileToSrv.java
 *
 * Created on March 29, 2005, 10:29 AM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import guisanboot.res.*;
import guisanboot.exception.*;

/**
 *
 * @author  Administrator
 */
public class SendFileToSrv extends AbstractNetworkRunning{
    private String filename;
    private String contents;
    private boolean debug = false;
    private boolean debug1 = false;
    
    /** Creates a new instance of SendFileToSrv */
    public SendFileToSrv() {
        super();
    }
   
    public SendFileToSrv( Socket socket ) throws IOException{
        super( socket );
    }
    
    public String getFilename(){
        return filename;
    }
    
    public void parser( String line ){
    }
        
    public void run() throws IOException,
                                BadMagicException,
                                BadPackageLenException,
                                BadVersionException
    {
        boolean isFirst = true;
        byte[] pader,content_bytes,fname_bytes;
        
        synchronized( ResourceCenter.comLock ){
            
            int filenamelen = filename.getBytes().length;// 不包括最后的结束符
            int contentlen  = contents.getBytes().length; // 不包括最后的结束符
            
            fname_bytes    = filename.getBytes();
            content_bytes  = contents.getBytes();
            int off = 0;
            while( contentlen >0 ){
                // 组装包头
                if( contentlen >1024 ){
                    assembleMessageHeader( 
                        ResourceCenter.C_S_SEND_FILE, 
                        1, 
                        128 + 1024 ,
                        ResourceCenter.C_S_SENDF_STATUS
                    ); 
                    
                    if( isFirst ){
                        out.writeLong( 0 );
                        isFirst = false;
                    }else{
                        out.writeLong( 1 );
                    }
                    
                    // file name length 最长为 128
                    if( filenamelen < 128 ){
                        pader = new byte[ 128-filenamelen-1 ];
                        String newfname = filename + new String( pader );
                        out.writeString( newfname );
                    }else{
                        // 只取前128位
                        out.writeArray( fname_bytes,0,128 );
                    }
                    
                    out.writeArray( content_bytes,off,1024 );
                    
                    out.flush();
                    if( !receive() ) break;
                    
                    off += 1024;
                    contentlen -= 1024;
                }else{
                     assembleMessageHeader( 
                        ResourceCenter.C_S_SEND_FILE, 
                        1, 
                        128+contentlen, 
                        ResourceCenter.C_S_SENDF_STATUS
                    ); 
                    
                    if( isFirst ){
                        out.writeLong( 0 );
                        isFirst = false;
                    }else{
                        out.writeLong( 1 );
                    }
                    
                    if( filenamelen < 128 ){
                        pader = new byte[ 128-filenamelen-1 ];
                        String newfname = filename + new String( pader );
                        out.writeString( newfname );
                    }else{
                        // 只取前128位
                        out.writeArray( fname_bytes,0,128 );
                    }
                    
                    out.writeArray( content_bytes,off,contentlen );
                    out.writePad( 128 + contentlen );
                    
                    
                    out.flush();
                    if( !receive() ) break;
                    
                    off += contentlen;
                    contentlen = 0;
                }
            }
        }
    }

    private boolean receive() throws IOException,
                                BadMagicException,
                                BadPackageLenException,
                                BadVersionException{
        StringBuffer errStr = new StringBuffer();
        int i,retbyte;
        byte[] buf;
        String[] lines;
        String lastLine;
        Pattern pattern;
        Matcher matcher;

        // 开始接受并分析返回包
        checkMessageHeader();

        // 检查返回的状态
        retCode = status - ResourceCenter.C_S_SENDF_STATUS;

        // 不管成功和失败，一律接收data area的数据
        buf = new byte[byteCnt];
        int count = byteCnt;
        int offset = 0;
        while( count >0 ){
            retbyte = in.read( buf,offset,count );

            if( debug ){
                //SanBootView.writeLog( " each real read byte: "+Integer.toHexString( retbyte )+"("+retbyte+")" );
            }
            if( retbyte == -1 ){
                throw new IOException();
            }

            if( debug1 ){
                for( i=0;i<buf.length;i++ ){
                   // SanBootView.writeLog( "byte@@@@@@@: "+Integer.toHexString( buf[i]) );
                }
            }

            count -= retbyte;
            offset += retbyte;
        }

        errStr.append( new String( buf,0,byteCnt ) );

        lines = Pattern.compile("\n").split( errStr.toString(),-1 );

        if( lines == null || lines.length<=0 ) return false;
//System.out.println( "line lenght: " + lines.length );

        if( lines.length>1 ){
            for( i=0;i<lines.length-1;i++ ){
                if( !lines[i].equals("") )
                    errMsg += lines[i] + "\n";
            }
            lastLine = lines[i];
        }else{
//System.out.println(" only one line.............");
            lastLine = lines[0];
        }
//System.out.println("last line:" + lastLine );

        // 处理最后一行
        pattern = Pattern.compile("^\\00*$");
        matcher = pattern.matcher( lastLine );
        if ( !matcher.find() ){
            errMsg += lastLine+"\n";
        }

        if( retCode == 0 ){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean sendFileToSrv( String _filename,String _contents ){
        filename = _filename;
        contents = _contents;
        
        try{
            run();
        }catch( Exception ex ){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        
        return ( getRetCode() == AbstractNetworkRunning.OK );
    }
}
