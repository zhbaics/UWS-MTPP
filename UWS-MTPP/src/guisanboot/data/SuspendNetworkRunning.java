/*
 * SuspendNetworkRunning.java
 *
 * Created on March 25, 2005, 10:31 AM
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

/*
 * 这个实现并不好，因为它没有实现真正的suspending(中断网络传输),
 * 以后有时间再实现真正的suspending
 */

public abstract class SuspendNetworkRunning extends AbstractNetworkRunning {
    protected String cmdLine;
    private final Object lock = new Object();
    protected boolean destroy = false;
    protected boolean yield = false;
    protected int nestNum = 0;
    protected boolean debug = true;
    protected boolean debug1 = true;
     
    /** Creates a new instance of SuspendNetworkRunning */
    public SuspendNetworkRunning(
        String _cmdLine,
        Socket socket,
        boolean _yield,
        int _nestNum) throws IOException 
    {
        super( socket );
        
        cmdLine = _cmdLine;
        yield = _yield;
        nestNum = _nestNum;
    }

    public SuspendNetworkRunning(
        String _cmdLine,
        Socket socket,
        boolean _yield,
        int _nestNum,
        String encoding
        ) throws IOException
    {
        super( socket );

        cmdLine = _cmdLine;
        yield = _yield;
        nestNum = _nestNum;
        this.encoding = encoding;
    }
    
    public SuspendNetworkRunning(
        String _cmdLine,
        boolean _yield,
        int _nestNum)
    {   
        cmdLine = _cmdLine;
        yield = _yield;
        nestNum = _nestNum;
    }
    
    public void setCmdLine( String _cmdLine ){
        cmdLine = _cmdLine;
    }
    
    public String getCmdLine(){
        return cmdLine;
    }
    
    public void setDestroy( boolean val ){
        synchronized( lock ){
            destroy = val;
        }
    }
    
    public boolean isDestroied(){
        synchronized ( lock ){
            return destroy;
        }
    }
   
    public abstract void parser( String line ) throws InterruptedException;
    
    // 缺省的错误处理方式
    public void parserErr( String line ){
        errMsg += line + "\n";
    }
    
    public void run() throws IOException,
                                BadMagicException,
                                BadPackageLenException,
                                BadVersionException,
                                InterruptedException
    {
        StringBuffer pakBuf = new StringBuffer();
        String[] lines;
        String lastPakStr = null;
        byte[] buf;
        int i,retbyte;
        
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
                if( retCode !=0 ){
                    isContinue = false;
                    in.readLong();  // 读取终止的标志
                    in.readLong();  // 读取真实数据长度
                    continue;
                }

                // 检查是否继续循环
                int flag = in.readLong(); 
                if( flag == 0 ){
                    isContinue = false;
                    in.readLong(); //读真实数据大小,应该为0
                    continue;
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

        if( retCode !=0 ){ // 出问题了
            this.errMsg = "";
            if( lastPakStr == null || lastPakStr.length() <=0 ) return;

            lines = Pattern.compile("\n").split( lastPakStr,-1 );

            if( lines == null || lines.length<=0 ) return;

            for( i=0;i<lines.length;i++ ){
                if( !lines[i].equals("") )
                    parserErr( lines[i] );
            }

        }else{

            lines = Pattern.compile("\n").split( pakBuf.toString(),-1 );
            
            if( lines == null || lines.length<=0 ) return;

            // 分析每次的结果
            i = 0;
            if( yield ){
                if( nestNum !=0 ){
                    int count =1;
                    while( !isDestroied() && ( i<lines.length ) ){
                        if( !lines[i].equals("") ){
                            parser( lines[i] );
                        }
                        count++;
                        if( ( count%nestNum ) == 0 ){
                            try{
                                // 给GUI让点cpu出来
                                Thread.sleep(10);
                            }catch(InterruptedException ex){ }
                        }
                        i++;
                    }
                }else{
                    while( !isDestroied() && ( i< lines.length ) ){
                        if( !lines[i].equals("") ){
                            parser( lines[i] );
                        }
                        try{
                            Thread.sleep(10);
                        }catch(InterruptedException ex){}

                        i++;
                    }
                }
            }else{
                while( !isDestroied()&& ( i < lines.length ) ){
                    if( !lines[i].equals("") ){
                        parser( lines[i] );
                    }
                    i++;
                }
            }
        }
    }
}
