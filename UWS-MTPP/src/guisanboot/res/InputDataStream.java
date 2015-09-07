package guisanboot.res;

/**
 * Title:        Message Sender
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      Odysys
 * @author       Odysys
 * @version 1.0
 */
import java.io.*;

/*
 * 这里原来想继承BufferedInputStream,但是在使用中发现:
 * 读数据时,数据包会错位，即server后发的包会提前被处理，
 * 而server先发的包会稍后处理。
 */

public class InputDataStream {
    InputStream in;
    
    public InputDataStream(InputStream _in) {
        in = _in;
    }
    
    public int readShort() throws IOException {
        int n = 0;
        int retbyte;
        byte b[] = new byte[2];
    
        int count = 2;
        int offset = 0;
        while( count >0 ){
            retbyte = in.read( b,offset,count );      
            if( retbyte == -1 ){
                throw new IOException();
            }
                    
            count -= retbyte;
            offset += retbyte;
        }
        
        n = b[0] & 0xff;
        n <<= 8;
        n |= b[1] & 0xff;
        return n;
    }

    public int readByte() throws IOException {
        int n = 0;
        int retbyte;
        byte b[] = new byte[1];
    
        int count = 1;
        int offset = 0;
        while( count >0 ){
            retbyte = in.read( b,offset,count );      
            if( retbyte == -1 ){
                throw new IOException();
            }
                    
            count -= retbyte;
            offset += retbyte;
        }
        
        n = b[0] & 0xff;
        return n;
    }

    public int readLong() throws IOException {
        long n = 0L;
        int retbyte;
        byte b[] = new byte[4];
        
        int count = 4;
        int offset = 0;
        while( count >0 ){
            retbyte = in.read( b,offset,count );      
            if( retbyte == -1 ){
                throw new IOException();
            }
                    
            count -= retbyte;
            offset += retbyte;
        }
        
        for(int i = 0; i <= 3; i++){
            n <<= 8;
            n |= b[i] & 0xff;
        }

        return (int)n;
    }

    public int read( byte[] buf ) throws IOException{
        return in.read( buf );
    }
    
    public int read( byte[] buf,int offset,int len ) throws IOException{
        return in.read( buf,offset,len );
    }
    
    public long getSkipBytes(long skipbytes){
        skipbytes %= 8;
        if(skipbytes >0 )
            skipbytes = 8 - skipbytes;
        //return (skipbytes+4); // 没有CRC
        return skipbytes;
    }
    
    public void close() throws IOException{
        in.close();
    }
}
