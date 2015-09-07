
package guisanboot.data;

import java.util.*;
import java.io.*;

public abstract class AbstractConfIO {
    String lastLine = null;

    public AbstractConfIO() {
    }
    
    public abstract void prtMe();
    public abstract void outputConf(OutputStreamWriter out) throws IOException;
    public abstract int parserConf(int begin,String[] lines );

    public String getLastLine(){
        return lastLine;
    }

    public void setLastLine(String _lastLine){
        lastLine = _lastLine;
    }

    public String readLine(InputStreamReader in) throws IOException {  
        StringBuffer strBuf = new StringBuffer();

        int c;
        c = in.read();
        while( c >= 0 && c != '\n' ){
          strBuf.append((char)c);
          c = in.read();
        }

        if( c<0 && strBuf.length() == 0 )
          return null;

        return strBuf.toString().trim();
    }

    public Vector splitLine(String line){
        Vector<String> v = new Vector<String>();
        int pos = line.indexOf('=');

        if( pos < 0 ){ // 构造一个空的 vector
            v.add("");
            v.add("");
            return v;
        }

        try{
            v.add( line.substring(0,pos).trim().toUpperCase() );
            v.add( line.substring( pos+1 ).trim() );
        }catch(Exception ex){
            v.add("");
        }

        return v;
    }

    public boolean isTitle(String str){
        return str.charAt(0) == '[' && str.charAt(str.length()-1) == ']';
    }
}
