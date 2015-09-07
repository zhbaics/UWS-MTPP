/*
 * GetBakObjList.java
 *
 * Created on July 28, 2008, 13:06 PM
 */

package guisanboot.datadup.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.datadup.data.BakObject;
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetBakObjList extends NetworkRunning{
    private ArrayList<BakObject> bakobjList = new ArrayList<BakObject>();
    private BakObject curBakObj = null;
    
    public void parser(String line){
SanBootView.log.debug( getClass().getName()," =====> "+ line);        
        String s1 = line.trim();
        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
            if( s1.startsWith( BakObject.BAKOBJ_ID ) ){
                try{
                    long id = Long.parseLong( value );
                    curBakObj.setBakObjID( id );
                }catch(Exception ex){
                    curBakObj.setBakObjID( -1L );
                }
            }else if( s1.startsWith( BakObject.BAKOBJ_FNAME )){
                curBakObj.setFileName(  value );
            }else if( s1.startsWith( BakObject.BAKOBJ_FTYPE )){
                curBakObj.setFileType( value );
            }else if( s1.startsWith( BakObject.BAKOBJ_INCLUDE )){
                curBakObj.setInclude( value );
            }else if(s1.startsWith( BakObject.BAKOBJ_INCLTYPE )){
                curBakObj.setInclType( value );
            }else if(s1.startsWith( BakObject.BAKOBJ_INCLCASE )){
                curBakObj.setInclCase( value.equals("yes") );
            }else if(s1.startsWith( BakObject.BAKOBJ_EXCLUDE )){
                curBakObj.setExclude( value );
            }else if(s1.startsWith( BakObject.BAKOBJ_EXCLTYPE)){
                curBakObj.setExclType( value );
            }else if(s1.startsWith( BakObject.BAKOBJ_EXCLCASE )){
                curBakObj.setExclCase( value.equals("yes") );
            }else if(s1.startsWith( BakObject.BAKOBJ_SINGLEFS )){
                curBakObj.setSingleFs( value.equals("yes") );
            }else if(s1.startsWith( BakObject.BAKOBJ_BS )){
                try{
                    int bs = Integer.parseInt( value );
                    curBakObj.setBS( bs );
                }catch(Exception ex){
                    curBakObj.setBS( 0 );
                }
            }else if(s1.startsWith( BakObject.BAKOBJ_SEEK )){
                try{
                    int seek = Integer.parseInt( value );
                    curBakObj.setSeek( seek );
                }catch(Exception ex){
                    curBakObj.setSeek( -1 );
                }
            }else if(s1.startsWith( BakObject.BAKOBJ_COUNT )){
                try{
                    int count = Integer.parseInt( value );
                    curBakObj.setCount( count );
                }catch(Exception ex){
                    curBakObj.setCount( 0 );
                }
            }else if(s1.startsWith( BakObject.BAKOBJ_LASTBK )){ 
                curBakObj.setLastBk( value );
            }else if(s1.startsWith( BakObject.BAKOBJ_RAWDEV )){
                curBakObj.setRawDev( value.equals("yes") );
            }else if(s1.startsWith( BakObject.BAKOBJ_SN )){
                try{
                    int sn = Integer.parseInt( value );
                    curBakObj.setBakObjSN( sn );
                }catch(Exception ex){
                    curBakObj.setBakObjSN( -1 );
                }
            }else if( s1.startsWith( BakObject.BAKOBJ_CLIENTID )){
                try{
                    int cliId = Integer.parseInt( value );
                    curBakObj.setClientId( cliId );
                }catch(Exception ex){
                    curBakObj.setClientId( -1 );
                }
            }else if( s1.startsWith(BakObject.BAKOBJ_BAKLEVEL )){
                try{
                    int bklevel = Integer.parseInt( value );
                    curBakObj.setBakLevel( bklevel );
                }catch(Exception ex){
                    curBakObj.setBakLevel( 0 );
                }
            }else if( s1.startsWith(BakObject.BAKOBJ_DESC )){
                curBakObj.setDesc( value );
            }else if( s1.startsWith(BakObject.BAKOBJ_ACCTID ) ){
                try{
                    int uid = Integer.parseInt( value );
                    curBakObj.setUid( uid );
                }catch(Exception ex){
                    curBakObj.setUid( 0 );
                }
            }else if( s1.startsWith(BakObject.BAKOBJ_MAX_BKNUM )){
                try{
                    int maxBkNum = Integer.parseInt( value );
                    curBakObj.setMaxBkNum( maxBkNum );
                }catch(Exception ex){
                    curBakObj.setMaxBkNum( 7 );
                }
                bakobjList.add( curBakObj );    
            }
        }else{
            if( s1.startsWith( BakObject.BAKOBJ_RECFLAG )){
                curBakObj = new BakObject();
            }
        }
    }
    
    public GetBakObjList( String cmd,Socket socket )throws IOException {
        super( cmd,socket );
    }
    
    public GetBakObjList( String cmd ){
        super( cmd );
    }
    
    public boolean updateBakObjList(){
SanBootView.log.info( getClass().getName()," list bakobj cmd: "+ getCmdLine() );        
        try{
            bakobjList.clear();
            curBakObj = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg(ex);
            setExceptionRetCode(ex);
        }
SanBootView.log.info( getClass().getName()," list bakobj cmd retcode: "+ getRetCode() );            
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," list bakobj cmd errmsg: "+ getErrMsg() );                
        }
        return isOk;
    }
    
    public boolean updateBakObjList( String cmd ){
        this.setCmdLine( cmd );
SanBootView.log.info( getClass().getName()," list bakobj cmd: "+ getCmdLine() ); 
        
        try{
            bakobjList.clear();
            curBakObj = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg(ex);
            setExceptionRetCode(ex);
        }
SanBootView.log.info( getClass().getName()," list bakobj cmd retcode: "+ getRetCode() );            
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," list bakobj cmd errmsg: "+ getErrMsg() );                
        }
        return isOk;
    }
    
    public void addBakObjectToVector( BakObject bakObj ){
        bakobjList.add( bakObj );
    }
    
    public void modBakObjInVector( long id,String fileList,String excludeList,String filetype ){
        BakObject bkobj = getBakObjectFromVector( id );
        if( bkobj != null ){
            bkobj.setFileName( fileList );
            bkobj.setExclude( excludeList );
            bkobj.setFileType( filetype );
        }
    }
    
    public void removeBakObjectFromVector( BakObject bakObj ){
        long id = bakObj.getBakObjID();
        int size = bakobjList.size();
        for( int i=0;i<size;i++ ){
            BakObject aBakObj = bakobjList.get(i);
            if( aBakObj.getBakObjID() == id ){
                bakobjList.remove( i );
                break;
            }
        }
    }
    
    public BakObject getBakObjectFromVector( long id ){
        int size = bakobjList.size();
        for( int i=0;i<size;i++ ){
            BakObject bakObj = bakobjList.get(i);
            if( bakObj.getBakObjID() == id )
                return bakObj;
        }
        return null;
    }

    public BakObject getBakObjectFromVector( long cliId,String fileName ){
        int size = bakobjList.size();
        for( int i=0;i<size;i++ ){
            BakObject bakObj = bakobjList.get(i);
            if( ( cliId == bakObj.getClientId() ) &&
                fileName.equals( bakObj.getFileName() )
            ){
                return bakObj;
            }
        }
        return null;
    }
    
    public BakObject getBakObjectFromVector( String id ){
        int size = bakobjList.size();
        for( int i=0;i<size;i++ ){
            BakObject bakObj = bakobjList.get(i);
            if( id.equals( bakObj.getBakObjID()+"" ) )
                return bakObj;
        }
        return null;
    }
    
    public ArrayList<BakObject> getBakObjectListFromVector( long cliId,int uid,int type ){
        ArrayList<BakObject> ret = new ArrayList<BakObject>();
        int size = bakobjList.size();
        if( size<=0 ) return ret;
        
        for( int i=size-1; i>=0; i-- ){
            BakObject bakObj = bakobjList.get(i);
            if( (bakObj.getClientId() == cliId) &&
                (bakObj.getUid() == uid) &&
                (bakObj.getBakType() == type)
            ){
                ret.add( bakObj );
            }
        }
        
        return ret;
    }
    
    public ArrayList<BakObject> getBakObjectListFromVector(long cliId){
        ArrayList<BakObject> ret = new ArrayList<BakObject>();
        int size = bakobjList.size();
        if( size<=0 ) return ret;
        
        for( int i=size-1;i>=0;i-- ){
            BakObject bakObj = bakobjList.get(i);
            if( bakObj.getClientId() == cliId ){
                ret.add( bakObj );
            }
        }
        
        return ret;
    }
    
    public BakObject getBakObjectListFromVector( long bkObjId,int uid ){
        int size = bakobjList.size();
        for( int i=0;i<size;i++ ){
            BakObject bakObj = bakobjList.get(i);
            if( bakObj.getBakObjID() == bkObjId &&
                bakObj.getUid() == uid 
            ){
                return bakObj;
            }
        }
        
        return null;
    }
    
    public BakObject getOneBakObj(){
        int size = bakobjList.size();
        if( size > 0 ){
            return bakobjList.get(0);
        }else{
            return null;
        }
    }
}
