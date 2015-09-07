/*
 * GetFreePhyVol.java
 *
 * Created on 2008/9/11, PM�2:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author zjj
 */
public class GetFreePhyVol extends GetOrphanVol {
    /** Creates a new instance of GetFreePhyVol */
    public GetFreePhyVol( String cmd ) {
        super( cmd );
    }
    
    public GetFreePhyVol( String cmd,Socket socket,SanBootView view) throws IOException {
        super( cmd,socket,view );
    }
    
    @Override public boolean realDo(){
        // 首先获取最新的volmap/mdi/clonedisk,否则判断是否为空闲卷的证据不足
        // 当然这样做也不一定就能百分百杜绝误删。
        view.initor.mdb.updateVolumeMap(); 
        view.initor.mdb.updateMDI();
        view.initor.mdb.updateCloneDiskList();
SanBootView.log.info( getClass().getName(), "listdisk cmd: "+getCmdLine() );
System.out.println("(GetOrphanVol)addcache: "+ addCache );
System.out.println("(GetOrphanVol)filter: "+this.filter );

        try{
            curVol = null; 
            if( addCache ){ 
                cache.clear(); 
            }
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), "listdisk cmd retcode: "+getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "listdisk cmd errmsg: "+getErrMsg() );             
        }
        return isOk;
    }
}
