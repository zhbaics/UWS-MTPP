/*
 * SyncOpCmd.java
 *
 * Created on July 15, 2005, 3:27 PM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;
import java.util.regex.*;

import guisanboot.res.*;
import guisanboot.exception.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class SyncOpCmd extends AbstractNetworkRunning{
    private String cmd;
    private int pid = -1;
    
    /** Creates a new instance of SyncOpCmd */
    public SyncOpCmd() {
    }
    
    public SyncOpCmd( Socket socket ) throws IOException{
        super( socket );
    }
    
    private void parser( String line ){
        int tmp;
        
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(this.getClass().getName(), "========>" + line ); 

        String s1 = line.trim();
        if( s1.equals("") ) return;
        
SanBootView.log.debug(this.getClass().getName(), "@@@@@@@@@ s1 length: " + s1.length() ); 
        
        try{
            tmp = Integer.parseInt( s1 );
        }catch(Exception ex){
            tmp = -1;
        }
        
        if( tmp>0 ){
            pid = tmp;
        }
    }
    
    public void run() throws IOException,
                                BadMagicException,
                                BadPackageLenException,
                                BadVersionException
    {
        StringBuffer pakBuf = new StringBuffer();   
        Pattern pattern;
        Matcher matcher;
        String[] lines;
        String lastLine;
        int i,retbyte;
        byte[] buf;
        
        synchronized( ResourceCenter.comLock ){
            // 组装包头
            int cmdlen = cmd.getBytes().length;

            assembleMessageHeader( 
                ResourceCenter.C_S_SYNCOP, 
                0, 
                1+cmdlen,
                ResourceCenter.C_S_SYNCOP_STATUS
            );
            
            out.writeString( cmd );
            out.writePad( cmdlen+1 );
            
            out.flush();

            // 开始接受并分析返回包
            checkMessageHeader();
        
            // 检查返回的状态
            retCode = status - ResourceCenter.C_S_SYNCOP_STATUS;

            // 不管成功和失败，一律接收data area的数据
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
        }
        
        pakBuf.append( new String( buf,0,byteCnt ) );
SanBootView.log.debug( this.getClass().getName()," #########:(SyncOpCmd: output): "+pakBuf.toString()   );  
        
        lines = Pattern.compile("\n").split( pakBuf.toString(),-1 );
            
        if( retCode !=0 ){ // 出问题了
            errMsg = "";
            if( lines == null || lines.length<=0 ) return;
            
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
        }else{
            if( lines == null || lines.length <=0 ) return;
            
            // 分析每次的结果 
            for( i=0;i<lines.length;i++ ){
                if( !lines[i].equals("") ){
                    parser( lines[i] );
                }
            }              
        }
    }
    
    public boolean execCmd( String _cmd ){
        cmd = _cmd;        
SanBootView.log.info( this.getClass().getName(),"sync cmd: " + cmd  );
        try{
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( this.getClass().getName(),"sync cmd retcode: " + getRetCode()  ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( this.getClass().getName(),"sync cmd errmsg: " + getErrMsg()  );             
        }
        return isOk;
    }
    
    public int getPID(){
        return pid;
    }
}
