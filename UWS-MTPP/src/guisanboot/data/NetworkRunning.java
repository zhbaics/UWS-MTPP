/*
 * NetworkRunning.java
 *
 * Created on March 23, 2005, 1:36 PM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import guisanboot.res.*;
import guisanboot.exception.*;

/**
 * @author  Administrator
 */
public abstract class NetworkRunning extends AbstractNetworkRunning{
    protected String cmdLine;
    
    public NetworkRunning(){
        super();
    }

    public NetworkRunning( String _cmdLine,String encoding ){
        super();
        this.cmdLine = _cmdLine;
        this.encoding = encoding;
    }

    public NetworkRunning( String _cmdLine ){
        super();
        cmdLine = _cmdLine;
    }
    
    public NetworkRunning( Socket socket ) throws IOException {
        super( socket );
    }
    
    /** Creates a new instance of NetworkRunning */
    public NetworkRunning( String _cmdLine,Socket socket ) throws IOException {
        super( socket );
        cmdLine = _cmdLine;
    }
    
    public void setCmdLine( String _cmdLine ,Socket _socket ) throws IOException{
        cmdLine = _cmdLine;
        
        socket = _socket;
        socket.setSoTimeout( timeout );
        in = new InputDataStream( socket.getInputStream() );
        out = new OutputDataStream( socket.getOutputStream() ); 
    }
    
    public void setCmdLine( String _cmdLine ){
        cmdLine = _cmdLine;
    }
    
    public String getCmdLine(){
        return cmdLine;
    }

    public abstract void parser( String line );
    
    public void run() throws IOException,
                                BadMagicException,
                                BadPackageLenException,
                                BadVersionException
    {
        StringBuffer pakBuf = new StringBuffer();
        String[] lines;
        String lastPakStr = null;
        int i,retbyte;
        byte[] buf;
     
        synchronized( ResourceCenter.comLock ){
            // 组装包头
            assembleMessageHeader( 
                ResourceCenter.C_S_CMD_CODE, 
                0, 
                1+cmdLine.getBytes().length, 
                ResourceCenter.C_S_CMD_STATUS
            ); 

            out.writeString( cmdLine );
            out.writePad( cmdLine.getBytes().length+1 );
            out.flush();

            // 开始接受并分析返回包
            boolean isContinue = true;
            while( isContinue ){
                checkMessageHeader();

                // 检查返回的状态
                retCode = status - ResourceCenter.C_S_CMD_STATUS;
                if( retCode != 0 ){
                    in.readLong();  // 读取终止的标志
                    in.readLong();  // 读取真实数据长度
                    isContinue = false;
                    continue;
                }

                // 检查是否继续循环
                int flag = in.readLong();
                if( flag == 0 ){ // flag为0表示结束
                    isContinue = false;
                    in.readLong();  // 真实数据大小应该为 0
                    continue;  // 最后一个包只是确认包,没有实际数据,故直接退出
                }

                // 获取数据区中真实数据大小
                int realByteCnt = in.readLong();

                // 读数据区中的全部数据
                buf = new byte[byteCnt];
                int count = byteCnt;
                int offset = 0;
                while( count >0 ){
                    retbyte = in.read( buf,offset,count );
                    if( retbyte == -1 ){
                        throw new IOException();
                    }
                    count -= retbyte;
                    offset += retbyte;
                }
                
                // 加入对字符集编码的考虑 ( 2010.12.23 )
                if( this.isSpecifiedEncoding() ){
                    lastPakStr = new String( buf,0,realByteCnt,this.getEncoding() );
                }else{
                    lastPakStr = new String( buf,0,realByteCnt );
                }
                
                pakBuf.append( lastPakStr );
            }
        }
        
        this.errMsg = "";
//System.out.println("retCode: "+ retCode );
        if( retCode != 0 ){ // 出问题了
            // 当前协议处理出错信息时没有把正常信息和错误信息
            // 分开，当程序运行到这里时，可能的错误信息已经放在了
            // lastPakStr中
            
            //if( lastPakStr == null || lastPakStr.length() <=0 ) return;
            //lines = Pattern.compile("\n").split( lastPakStr,-1 );
            lines = Pattern.compile("\n").split( pakBuf.toString(),-1 );
            if( lines == null || lines.length<=0 ) return;

            for( i=0; i<lines.length; i++ ){
                if( !lines[i].equals("") )
                    errMsg += lines[i] + "\n";
            }
        }else{
            lines = Pattern.compile("\n").split( pakBuf.toString(),-1 );
            if( lines == null || lines.length <= 0 ) return;
            
            // 分析每次的结果 
            for( i=0; i<lines.length; i++ ){
                if( !lines[i].equals("") ){
                    parser( lines[i].trim() );
                }
            }              
        }
    }
}
