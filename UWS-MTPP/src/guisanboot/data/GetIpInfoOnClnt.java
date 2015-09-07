package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;


public class GetIpInfoOnClnt extends NetworkRunning {
    public final static String NetInfo_HOSTName = "HostName";
    public final static String NetInfo_Adapter = "Adapter";
    public final static String NetInfo_MAC = "Mac Address";
    public final static String NetInfo_Connection ="Connection";
    public final static String NetInfo_ConnType="Conn Type";
    public final static String NetInfo_IPAddr = "IP Address";
    public final static String NetInfo_SubMask="Sub Mask";
    public final static String NetInfo_Gateway ="Gateway";
    public final static String NetInfo_DHCP="DHCP Enabled";
    public final static String NetInfo_HaveWins = "Have Wins";
    public final static String NetInfo_DNSAddr = "DNS Address";
    public final static String NetInfo_MediaStatus = "Media Status";
    
    private String ipInfo = null;
    private StringBuffer strBuf = null;
    private boolean isFirst = true;

    public void parser(String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line,true );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP( String line,boolean output )  {
        if( line == null ) return;
        line = line.trim();
        if( line.equals("") ) return;

        if( output ){
SanBootView.log.debug(getClass().getName(),"========> "+ line );
        }
        
        ipInfo = line;
        if( isFirst ){
            strBuf.append( ipInfo );
            isFirst = false;
        }else{
            strBuf.append( "\n"+ipInfo );
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line,false );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public GetIpInfoOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetIpInfoOnClnt( String cmd ){
        super( cmd,ResourceCenter.DEFAULT_CHARACTER_SET );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get IP info of client cmd: "+ getCmdLine() ); 
        try{
            ipInfo = null;
            isFirst = true;
            strBuf = null;
            strBuf = new StringBuffer();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get IP info of client retcode: "+ getRetCode() ); 
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get IP info of client errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    public String getAllContent(){
        return strBuf.toString();
    }
    
/*
Adapter:        SiS NIC SISNIC
Mac Address:    00-13-D4-61-DF-D0
Connection:     本地连接�������
Conn Type:      Ethernet
IP Address:     10.10.1.67
Sub Mask:       255.255.255.0
IP Address:     198.88.88.53
Sub Mask:       255.255.255.0
IP Address:     192.253.253.67
Sub Mask:       255.255.255.0
IP Address:     20.20.1.67
Sub Mask:       255.255.255.0
Gateway:        198.88.88.1
DHCP Enabled:   No
Have Wins:      No
DNS Address:    198.88.88.1
Media Status:
*/
    public ArrayList getAllNetCard(){
        int ipbegin,ipend;
        NetCard card;
        BindIPAndMask binder;
        String[] line;
        
        ArrayList<NetCard> ret = new ArrayList<NetCard>();
        String[] list = Pattern.compile("\n").split( strBuf.toString(), -1 );
        for( int i=0; i<list.length; i++ ){
            if( list[i].equals("") )continue;
SanBootView.log.debug( getClass().getName(), "======>(getAllNetCard)"+list[i]);
            
            line = Pattern.compile(":").split( list[i],-1 );
SanBootView.log.debug( getClass().getName(), "======>(getAllNetCard)"+ line.length );            
            if( line != null && line.length >=2 ){
SanBootView.log.debug( getClass().getName(), "======>(getAllNetCard)"+ line[0] );    
                if( line[0].toUpperCase().startsWith( NetInfo_Adapter.toUpperCase() ) ){
                    card = new NetCard();
                    card.info = line[1].trim();

                    i+=1;
                    i = findPattern( list,i,NetInfo_MAC );
                    line = Pattern.compile(":").split( list[i], -1 );
SanBootView.log.debug( getClass().getName(), " 111111111111111111111: "+list[i] );
                    card.mac = line[1].trim();

                    i+=1;
                    i = findPattern( list,i,NetInfo_IPAddr );
SanBootView.log.debug( getClass().getName(), " 222222222222222222222222: "+list[i] );                      
                    ipbegin = i;
                    
                    i+=2; // maybe gateway
SanBootView.log.debug( getClass().getName(), " 333333333333333333: "+list[i] );                        
                                     
                    i = findPattern( list, i, NetInfo_Gateway );
SanBootView.log.debug( getClass().getName(), " gateway indx: "+i );                     
            
                    if( i != -1 ){
                        ipend = i;
                        
                        i = findPattern( list, i,NetInfo_MediaStatus );
SanBootView.log.debug( getClass().getName(), " Media Status indx: "+i );                       
                        if( i != -1 ){
                            try{
                                for( int j=ipbegin; j<ipend; j++,j++ ){
SanBootView.log.debug( getClass().getName(), " ip=======: "+list[j] );                                 
                                    line = Pattern.compile(":").split( list[j],-1 );
                                    binder = new BindIPAndMask();
                                    binder.ip = line[1].trim();
                                    
                                    line = Pattern.compile(":").split( list[j+1],-1 );
SanBootView.log.debug( getClass().getName(), " mask=======: "+list[j+1]  );      
                                    binder.mask = line[1].trim();
                                    card.addIpBinder( binder );
                                }
                                ret.add( card );
                            }catch(Exception ex1){
                            }
                        }
                    }
                }
            }
        }
        
        return ret;
    }
    
    private int findPattern( String[] list,int begin,String pattern ){
        int indx;
        String line[];
        
        indx = begin;
        while( indx <= list.length ){
            if( list[indx].equals("") ) continue;
SanBootView.log.debug( getClass().getName(), " findPattern: "+list[indx]   );             

            line = Pattern.compile(":").split( list[indx], -1 );
            if( line[0].equals( pattern ) ){
                return indx;
            }else{
                indx +=1;
            }
        }
        
        return -1;
    }
}
