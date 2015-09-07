/*
 * GetInitProgressIntoTableModel.java
 *
 * Created on June 23, 2010, 09:26 AM
 */

package guisanboot.cmdp.service;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.SubCluster;
import javax.swing.*;
import javax.swing.table.*;

import mylib.UI.*;
import guisanboot.ui.*;
import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.data.BootHost;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetInitProgressIntoTableModel {
    private InitProgressRecord curRec  = null;
    private SanBootView view;
    private BootHost host;
    private Cluster cluster;
    private DefaultTableModel model;
    private GetInitProgress geter;
    
    /** Creates a new instance of GetInitProgressIntoTableModel */
    public GetInitProgressIntoTableModel( SanBootView _view,BootHost host,Cluster cluster )throws IOException{
        view = _view;
        this.host = host;
        this.cluster = cluster;
        
        geter = new GetInitProgress("");
        geter.setSocket( view.getSocket() );
    }

    private BootHost getMasterHostFromCluster(){
        BootHost aHost;
        SubCluster subc;
        boolean isOk;

        ArrayList<SubCluster> subcList = cluster.getRealSubCluster();
        if( subcList.size() == 2 ){
            subc = subcList.get(0);
            isOk = view.initor.mdb.getMirrorClusterInfo( subc.getHost().getIP(),subc.getHost().getPort() );
            if( isOk ){
                if( view.initor.mdb.isMasterNode() ){
                    return subc.getHost();
                }else{
                    return subcList.get(1).getHost();
                }
            }else{
                subc = subcList.get( 1 );
                isOk = view.initor.mdb.getMirrorClusterInfo( subc.getHost().getIP(),subc.getHost().getPort() );
                if( isOk ){
                    if( view.initor.mdb.isMasterNode() ){
                        return subc.getHost();
                    }else{
                        return subcList.get(0).getHost();
                    }
                }else{
                    return null;
                }
            }
        }else{ // 多于3个
            int size = subcList.size();
            for( int i=0; i<size; i++ ){
                aHost = subcList.get(i).getHost();
                isOk = view.initor.mdb.getMirrorClusterInfo( aHost.getIP(),aHost.getPort() );
                if( isOk ){
                    if( view.initor.mdb.isMasterNode() ){
                        return aHost;
                    }
                }
            }
            return null;
        }
    }

    public void updateInitProgress() throws IOException {
        BootHost aHost,masterHost=null;

        ArrayList<VolumeMap> volList;
        if( cluster != null ){
            volList = view.initor.mdb.getVolFromCluster( cluster.getCluster_id() );
            masterHost = this.getMasterHostFromCluster();
        }else{
            volList = view.initor.mdb.getVolMapOnClntID1( host.getID() );
        }
        int size = volList.size();
        if( size > 0 ){
            for( int i=0; i<size; i++ ){
                VolumeMap vol = volList.get(i);
                if( vol.isMtppProtect() ) continue;
                
                view.initor.setResetNetwork( false );
                view.initor.mdb.setNewTimeOut( ResourceCenter.LIMITE_OF_RESPOND );
                
                if( cluster != null ){
                    aHost = masterHost;
                }else{
                    aHost = host;
                }
                
                if( aHost == null ){
                    curRec = new InitProgressRecord( vol.getVolDiskLabel() );
                    curRec.setPercent( SanBootView.res.getString("common.error.noMasterNode"));
                }else{
                    boolean isOk = geter.updateInitProgress( aHost.getIP(),aHost.getPort(),vol.getVolName(),vol.getVolDiskLabel() );
                    if( isOk ){
                        curRec = geter.getInitRecord();
                        curRec.setIsNotConnectError( geter.isNotConnectToHost() );

                        if( curRec.isNotConnectToClient() ){
                            curRec.setPercent( SanBootView.res.getString("common.conError") );
                        }else{
                            if( !curRec.isInitState() ){
                                curRec.setPercent( "100%" );
                                curRec.setRemainTime("0");
                            }
                        }
                    }else{
                        if( geter.getRetCode() == ResourceCenter.ERRCODE_TIMEOUT ){
                            curRec = new InitProgressRecord( vol.getVolDiskLabel() );
                            curRec.setPercent( SanBootView.res.getString("common.timeout") );
                        }else{
                            curRec = new InitProgressRecord( vol.getVolDiskLabel() );
                        }
                    }

                    if( view.initor.isResetNetwork() ){
                        geter.setSocket( view.getSocket() ); // 有可能出现超时,所以要重新设置socket
                    }
                }
                
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){}
            } // for end
            
            view.initor.mdb.setNewTimeOut( ResourceCenter.DEFAULT_TIMEOUT );
        }
    }

    Runnable insertModel = new Runnable(){
        public void run(){
            Object[] one = new Object[6];
            
            one[0] = curRec;
            
            one[1] = new GeneralBrowserTableCell(
                -1,curRec.getPercent(),JLabel.LEFT
            );

            one[2] = new GeneralBrowserTableCell(
                -1,curRec.getSpeed(),JLabel.LEFT
            );

            one[3] = new GeneralBrowserTableCell(
                -1,curRec.getFormattedRemainTime(),JLabel.LEFT
            );
            
            one[4] = new GeneralBrowserTableCell(
                -1,curRec.getFormattedElapsedTime(),JLabel.LEFT
            );

            one[5] = new GeneralBrowserTableCell(
                -1,curRec.getFinished(),JLabel.LEFT
            );
            
            model.addRow( one );
        }
    };
    
    public void setTaskTable( BrowserTable table){
        model = (DefaultTableModel)table.getModel();
    }
}
