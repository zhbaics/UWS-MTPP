package guisanboot.res;

import java.io.*;
import java.net.*;

/**
 * Title:        Message Sender
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author zjj
 * @version 1.0
 */

/*
 *  这段程序是数组拷贝的例子. 当使用不带缓冲的net io时，
 *  就需要在发送之前，由自己来组建好要发送的东西，然后
 *  一次性发出。如果不这样，就会一次只发送一个字节(writeByte),
 *  或几个字节(writeLong)，这样效率极底。
String str1 = "zjj is a it engineer";
int num = str1.getBytes().length+1;
System.out.println(" old str len: "+num);
byte[] b = new byte[num];
System.arraycopy( (Object)str1.getBytes(), 0, (Object)b, 0,num-1 );
String newStr = new String( b );
System.out.println(" new str : "+ newStr+ " len: "+newStr.getBytes().length );
*/

/*
 *  这段程序是数组拷贝的例子. 当使用不带缓冲的net io时，
 *  就需要在发送之前，由自己来组建好要发送的东西，然后
 *  一次性发出。如果不这样，就会一次只发送一个字节(writeByte),
 *  或几个字节(writeLong)，这样效率极底。
 */
public class OutputDataStream extends BufferedOutputStream {
    public OutputDataStream( OutputStream _out ){
        super( _out );
    }

    public void writeShort(int v) throws IOException {
        byte b[] = new byte[2];
        b[0] = (byte)((v>>8)&0xff); // high byte
        b[1] = (byte)(v&0xff);      // low byte
        write( b,0,2 );
    }

    public void writeByte(int v) throws IOException {
        byte[] b = new byte[1];
        b[0] = (byte)(v&0xff);
        write(b,0,1);
    }

    public void writeLong(int v) throws IOException {
        byte[] b = new byte[4];
        b[0] = (byte)((v>>24)&0xff);
        b[1] = (byte)((v>>16)&0xff);
        b[2] = (byte)((v>>8)&0xff);
        b[3] = (byte)(v&0xff);
        write(b,0,4);
    }

    public void writeString(String str) throws IOException {
        write( str.getBytes() );
        writeByte(0);
    }

    public void writeArray( byte[] array,int off,int len ) throws IOException{
        write( array,off,len );
    }
    
    public void writePad(int len) throws IOException {
//        int restByte = (int)((len+7)&(~7L))-len;
//        for( int i=0;i<restByte;i++ )
//          writeByte(0);
    }

    public void writeCRC() throws IOException {
        writeLong( 0 );
    }

    public void writeIP(String ip) throws IOException {
        byte[] b = InetAddress.getByName(ip).getAddress();
        for( int i=b.length-1;i>=0;i-- )
          writeByte(b[i]);
    }
}
