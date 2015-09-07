/*
 * AbstractNetworkRunning.java
 *
 * Created on March 25, 2005, 5:41 PM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;
import guisanboot.res.*;
import guisanboot.ui.*;
import guisanboot.exception.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Administrator
 */
public abstract class AbstractNetworkRunning {
    public final static int OK = 0;
    
    // 当前,如下命令使用了定制了timeout
    // format vol (10 hours)
    // copy os (10 hours)
    // stop or start service (10 hours)
    
    protected int timeout = ResourceCenter.DEFAULT_TIMEOUT; // 5 mins
    
    protected int retCode = -1;
    protected String errMsg="";
      
    protected int   wordCnt;
    protected int   byteCnt;
    protected int   status;
    protected Socket socket;
    protected InputDataStream  in;
    protected OutputDataStream out;

    private int cmd_type = ResourceCenter.CMD_TYPE_MTPP; // 命令类型
    protected String retForCMDP = "1";

    public final static String CMDP_RESVERED0 = "count";
    public final static String CMDP_RESVERED1 = "Result:";
    public final static String CMDP_RESVERED2 = "*isEnd=1 *return=0";
    public final static String CMDP_RESVERED3 = "*isEnd=1 *return=1";

    protected String encoding = "";    // 字符集编码,一般为"GBK"

    public AbstractNetworkRunning(){    
    }
    
    public AbstractNetworkRunning( Socket _socket ) throws IOException{
        socket = _socket;
        socket.setSoTimeout( timeout );
        in =  new InputDataStream( socket.getInputStream() );
        out = new OutputDataStream( socket.getOutputStream() );  
    }
    
    public void setSocket( Socket _socket ) throws IOException {
        socket = _socket;
        socket.setSoTimeout( timeout );
        in =  new InputDataStream( socket.getInputStream() );
        out = new OutputDataStream( socket.getOutputStream() );  
    }
    
    public void closeSocketStream() throws IOException {
        in.close();
        out.close();
    }

    public boolean isSpecifiedEncoding(){
        return !this.encoding.equals("");
    }

    public void setEncoding( String val ){
        this.encoding = val;
    }
    
    public String getEncoding(){
        return this.encoding;
    }

    /*
     *  消息头（共21个byte)
     *
    struct _msghdr {
        long label;  //4
        long length;  //4 
        char version; //1
        char cmd;  //1
        char type; //1
        short parlength; //2
        long datlength;  //4
        long status; //4
    };
    */
    
    protected void assembleMessageHeader( int cmd,int wordCnt,int byteCnt,int status ) throws IOException{
        out.writeLong( ResourceCenter.MAGIC );        // magic
//        int realBytes = getRealBytes( byteCnt );      // real bytes
        int realBytes = byteCnt; 
        
        out.writeLong( 21 +wordCnt*4 +realBytes ); // length 
//SanBootView.log.debug(getClass().getName(), "len: long(send header): "+(21+wordCnt*4+realBytes) );
        
        out.writeByte( ResourceCenter.VER );          // ver
        out.writeByte( cmd );                           // cmd
        out.writeByte( 0 );                              // type
        
        out.writeShort( wordCnt );                    // wordcnt
//BackupView.writeLog( "word cnt: short(send header): "+wordCnt );
        
        out.writeLong( realBytes );                   // byteCnt
//BackupView.writeLog( "byte cnt: long(send header): "+realBytes );
        
        out.writeLong( status );                       // status
    }
    
    protected boolean getMesHeader() throws IOException{
        byte[] four_bytes = new byte[4];
        byte singleByte;

        four_bytes[0] = (byte)in.readByte();
//BackupView.writeLog( "===0: "+Integer.toHexString( four_bytes[0])+" "+four_bytes[0]  );
        four_bytes[1] = (byte)in.readByte();
//BackupView.writeLog( "===1: "+Integer.toHexString(four_bytes[1])+" "+four_bytes[1]);
        four_bytes[2] = (byte)in.readByte();
//BackupView.writeLog( "===2: "+Integer.toHexString(four_bytes[2])+"  "+four_bytes[2]);
        four_bytes[3] = (byte)in.readByte();
//BackupView.writeLog( "===3: "+Integer.toHexString(four_bytes[3])+" "+four_bytes[3]);
        
        if( ( four_bytes[0] == -15 ) && ( four_bytes[1] == -14 ) &&
            (four_bytes[2] == -13 ) && ( four_bytes[3] == -12 )) {
//BackupView.writeLog(" find magic ................");
            return true;
        }else{
            boolean findMagic = false;
            while( !findMagic){
                singleByte = (byte)in.readByte();
//BackupView.writeLog("===temp byte: "+Integer.toHexString(singleByte) );
                
                four_bytes[0] = four_bytes[1];
                four_bytes[1] = four_bytes[2];
                four_bytes[2] = four_bytes[3];
                four_bytes[3] = singleByte;
                
                if( ( four_bytes[0] == -15 ) && ( four_bytes[1] ==  -14 ) &&
                        (four_bytes[2] == -13 ) && ( four_bytes[3] == -12)){
//BackupView.writeLog(" find magic in loop ................");
                    findMagic = true;
                }
            }
            return findMagic;
        }
    }

    protected void checkMessageHeader() throws SocketTimeoutException,IOException,
                                           BadMagicException,
                                           BadPackageLenException,
                                           BadVersionException{
//BackupView.writeLog("receive ...(check message header)");
        boolean findMagic = false;
        try {
            findMagic = this.getMesHeader();
            
            if( findMagic ){
                int len = in.readLong();
//BackupView.writeLog("len: "+Integer.toHexString( len )+"("+len+")" );
                int ver = in.readByte();
//BackupView.writeLog("ver: "+Integer.toHexString( ver )+"("+ver+")" );
                if( ver != ResourceCenter.VER ){
                    throw new BadVersionException();
                }else{
                    int cmd = in.readByte();
//BackupView.writeLog("cmd: "+Integer.toHexString( cmd )+"("+cmd+")" );
                    int type = in.readByte();
//BackupView.writeLog("type: "+Integer.toHexString( type )+"("+type+")" );
                    wordCnt = in.readShort();
//BackupView.writeLog(" wordCnt: "+ Integer.toHexString( wordCnt )+"("+wordCnt+")" );
                    byteCnt = in.readLong();
//BackupView.writeLog(" byteCnt: "+Integer.toHexString( byteCnt )+"("+byteCnt+")" );
                    status = in.readLong();
//BackupView.writeLog(" status: "+Integer.toHexString( status )+"("+status+")"+"\n\n");
//System.out.println("status: "+status);
                    if( len!=( 21+wordCnt*4+byteCnt ) ){
//BackupView.writeLog(" #### BAD LENGTH ####");
                        throw new BadPackageLenException();
                    }
                }
            }else{
//BackupView.writeLog(" #### BAD MAGIC ####");
                throw new BadMagicException();
            }
        }catch(SocketTimeoutException ex){
System.out.println("(checkMessageHeader):SocketTimeoutException occured");
            throw new SocketTimeoutException();
        }catch(IOException ex){
System.out.println("(checkMessageHeader):IOException occured");
            throw new IOException();
        }
    }

    protected int getTimeout(){
        return timeout;
    }
    public int getStatus(){
        return status;
    }
    
    public String getErrMsg(){
        return errMsg;
    }
    public int getRetCode(){
        return retCode;
    }
    
    public void setRetCode(int val){
        retCode = val;
    }
    public void setExceptionRetCode( Exception ex){
        retCode = getExceptionErrCode( ex );
    }
    
    public void setErrMsg( String _msg ){
        errMsg = _msg;
    }
    public void setExceptionErrMsg(Exception ex){
        errMsg = getExceptionErrMsg( ex );
    }
    
    public String getIPString(int intIp){
        String ret1="";
        int mask = 0xFF;

        boolean isFirst = true;
        for( int i=0;i<4;i++ ){
            int temp = intIp>>( i*8 );
            if( isFirst ){
                ret1 += (temp&mask);
                isFirst = false;
            }else{
                ret1 += "."+(temp&mask);
            }
        }
        return ret1;
    }

    protected int getRealBytes(int bytes){
        return (int)((bytes+7)&(~7L));
    }
    
    public String getExceptionErrMsg(Exception ex){
        if( ex instanceof BadMagicException ){
            return SanBootView.res.getString("common.errmsg.badmagic");
        }else if( ex instanceof BadPackageLenException ){
            return SanBootView.res.getString("common.errmsg.badLen");
        }else if( ex instanceof BadVersionException ){
            return SanBootView.res.getString("common.errmsg.badVer");
        }else if( ex instanceof SocketTimeoutException ){
            try{
                // 遇到timeout错，先logout,再将socket关闭
                if( SanBootView.staticInitor.isLogined() ){
                    SanBootView.staticInitor.mdb.logout();
                }
                socket.close();
            }catch(IOException ex1 ){}
            
            // 自动进行登陆
            if( SanBootView.staticInitor.isLogined() ){
                SanBootView.staticInitor.realLogin();
            }
            return SanBootView.res.getString("common.errmsg.timeout");
        }else if( ex instanceof IOException ){
            try{
                // 遇到io通信错，将socket关闭
                socket.close();
            }catch(IOException ex1 ){}
            return SanBootView.res.getString("common.errmsg.netio");
        }else{
            return SanBootView.res.getString("common.errmsg.generalErr");
        }
    }

    public boolean isCommunicationError( int err_code ){
        switch( err_code ){
            case ResourceCenter.ERRCODE_BadMagic:
                return true;
            case ResourceCenter.ERRCODE_BadLen:
                return true;
            case ResourceCenter.ERRCODE_BadVer:
                return true;
            case ResourceCenter.ERRCODE_TIMEOUT:
                return true;
            case ResourceCenter.ERRCODE_NETIO:
                return true;
            case ResourceCenter.ERRCODE_GENERAL:
                return true;
            default:
                return false;
        }
    }

    public int getExceptionErrCode( Exception ex ){
        if( ex instanceof BadMagicException ){
            return ResourceCenter.ERRCODE_BadMagic;
        }else if( ex instanceof BadPackageLenException ){
            return ResourceCenter.ERRCODE_BadLen;
        }else if( ex instanceof BadVersionException ){
            return ResourceCenter.ERRCODE_BadVer;
        }else if( ex instanceof SocketTimeoutException ){
            return ResourceCenter.ERRCODE_TIMEOUT;
        }else if( ex instanceof IOException ){
            return ResourceCenter.ERRCODE_NETIO;
        }else{
            return ResourceCenter.ERRCODE_GENERAL;
        }
    }

    public int getCmdType(){
        return this.cmd_type;
    }
    public void setCmdType( int type ){
        this.cmd_type = type;
        if( this.isCMDPCmd() ){
            retForCMDP = "1";
        }
    }
    public boolean isMTPPCmd(){
        return ( this.cmd_type == ResourceCenter.CMD_TYPE_MTPP );
    }
    public boolean isCMDPCmd(){
        return ( this.cmd_type == ResourceCenter.CMD_TYPE_CMDP );
    }

    public boolean isResveredForCmdp( String line ){
        return line.equals( CMDP_RESVERED1 ) || line.equals( CMDP_RESVERED2 ) || line.equals( CMDP_RESVERED3 );
    }

    public String getRet(){
        return this.retForCMDP;
    }

    public boolean isEqZero(){
        return this.retForCMDP.equals("0");
    }

    public boolean isNull(){
        return this.retForCMDP.equals("");
    }

    public boolean isOKForExcuteThisCmd(){
        if( this.isMTPPCmd() ){
            return ( this.getRetCode() == AbstractNetworkRunning.OK );
        }else{
            if( !this.isEqZero() ){
                // 有时phydup_fun.sh会出现3/8这样的输出，出现这样的输出就表示出错了，错误码为前面的那个数字
                Pattern pattern = Pattern.compile("^(\\d+)/(\\d+)$");
                Matcher matcher = pattern.matcher( this.errMsg );
                if( matcher.find() ){
                    try{
                        this.retCode = Integer.parseInt( matcher.group( 1 ) );
                    }catch(Exception ex){}
                }
            }
            return this.isEqZero();
        }
    }

    public boolean isContinueToParserRetValueForCMDP1( String line ){
        if( line == null || line.equals("") ) return false;
        String tmp = line.trim();
        if( tmp.toUpperCase().startsWith( CMDP_RESVERED0.toUpperCase() ) ){
            this.retForCMDP = "0";
            return false;
        }
        return true;
    }

    public boolean isContinueToParserRetValueForCMDP( String line ){
        if( line == null || line.equals("") ) return false;
        String tmp = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ tmp );
        if( this.isResveredForCmdp( tmp ) ){
            if( tmp.equals( NetworkRunning.CMDP_RESVERED2 ) ){
                retForCMDP = "0";
            }
            return false;
        }
        return true;
    }
}
