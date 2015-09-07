/*
 * GetMJScheduler.java
 *
 * Created on Seq 7, 2010, 5:01  PM
 */

package guisanboot.remotemirror.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.MirrorJob;
import guisanboot.data.NetworkRunning;
import guisanboot.remotemirror.entity.MirrorJobSch;
import guisanboot.ui.*;
import java.util.ArrayList;


/**
 *
 * @author  Administrator
 */
public class GetMJScheduler extends NetworkRunning{
    private ArrayList cache = new ArrayList();
    private MirrorJobSch curMJSch  = null;
    
    /** Creates a new instance of GetMJScheduler */
    public GetMJScheduler( String cmd  ) {
        super( cmd );
    }

    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value );

            if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_id ) ){
                try{
                    int id = Integer.parseInt( value );
                    curMJSch.setScheduler_id( id );
                }catch(Exception ex){
                    curMJSch.setScheduler_id( -1 );
                }
                this.cache.add( curMJSch );
            }else if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_type )){
                try{
                    int aType = Integer.parseInt( value );
                    curMJSch.setScheduler_type( aType );
                }catch(Exception ex){
                    curMJSch.setScheduler_type( -1 );
                }
            }else if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_mon)){
                curMJSch.setScheduler_mon( value );
            }else if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_tue)){
                curMJSch.setScheduler_tue( value );
            }else if(s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_wed )){
                curMJSch.setScheduler_wed( value );
            }else if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_thu) ){
                curMJSch.setScheduler_thu( value );
            }else if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_fri ) ){
                curMJSch.setScheduler_fri( value );
            }else if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_sat ) ){
                curMJSch.setScheduler_sat( value );
            }else if( s1.startsWith( MirrorJobSch.MJ_SCHE_scheduler_sun ) ){
                curMJSch.setScheduler_sun( value );
            }
        }else{
            if( s1.startsWith( MirrorJob.MJ_RECFLAG )){
                curMJSch = new MirrorJobSch();
            }
        }
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName()," get mj-sch list cmd:"+ this.getCmdLine() );

        try{
            this.cache.clear();
            this.curMJSch = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," get mj-sch list cmd retcode:"+ this.getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," get mj-sch list cmd errmsg:"+ this.getErrMsg()  );
        }
        return isOk;
    }

    public ArrayList getMJSchList(){
        int size = this.cache.size();
        ArrayList ret = new ArrayList( size );
        for( int i=0; i<size; i++ ){
            ret.add( cache.get(i) );
        }
        return ret;
    }

    public void addMjSch( MirrorJobSch mjSch ){
        this.cache.add( mjSch );
    }

    public void removeMjSch( MirrorJobSch mjSch ){
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            MirrorJobSch one = (MirrorJobSch)cache.get(i);
            if( one.getScheduler_id() == mjSch.getScheduler_id() ){
                cache.remove( i );
                break;
            }
        }
    }
}
