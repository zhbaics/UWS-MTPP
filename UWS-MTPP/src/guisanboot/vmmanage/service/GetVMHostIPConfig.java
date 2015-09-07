/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zourishun
 */
public class GetVMHostIPConfig extends NetworkRunning{

    ArrayList<String> retList = new ArrayList<String>();
    public static String RES_result="Result";
    public static String RES_other = "*is";
    @Override
    public void parser(String line) {
       String s1 = line.trim();
       if( s1.startsWith( RES_result ) || s1.startsWith( RES_other ) ){   
       }else{
           retList.add(s1);
       } 
    }
    
    public GetVMHostIPConfig(String _cmd ,String encode ){
        super( _cmd ,encode );
    }
    
//    public GetVMHostIPConfig(String _cmd){
//        super(_cmd);
//    }
    
//    public GetVMHostIPConfig(String _cmd ,String encode){
//        super( _cmd ,encode);
//    }

    public boolean updateVMHostIPConfig(){
         SanBootView.log.info( getClass().getName(), " ipconfig cmd: " + getCmdLine()  );
        try{
            retList.clear();
            run();
        } catch( Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        SanBootView.log.info( getClass().getName(), " get ipconfig retcode: "+getRetCode() );
         boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " get ipconfig errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public ArrayList<String> getIPConfigInfo1(){
        return retList;
    }
    
    public ArrayList<String> getIPConfigInfo(){
        ArrayList<String> resultList = new ArrayList<String>();
        try{
            File file=new File("C:\\test.txt");
            if(!file.exists()||file.isDirectory()) throw new FileNotFoundException();
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GB2312");
            BufferedReader reader=new BufferedReader(read);
            String lline;     
            while ((lline = reader.readLine()) != null) {      
                resultList.add( lline );    
            }      
            read.close();
        } catch (Exception ex){}
        return resultList;
    }
}
