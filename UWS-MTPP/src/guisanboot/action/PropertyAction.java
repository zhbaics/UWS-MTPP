/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cluster.entity.Cluster;
import guisanboot.data.BootHost;
import guisanboot.data.Service;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.HostPropertyDialog;
import guisanboot.ui.SanBootView;
import guisanboot.ui.UnixHostPropertyDialog;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.Icon;

/**
 *
 * @author zourishun
 */
public class PropertyAction extends GeneralActionForMainUi{
    public PropertyAction(Icon menuIcon,Icon btnIcon,String text,int fID){
        super( menuIcon,btnIcon,text,fID);
    }

    public PropertyAction(){
        super(
            ResourceCenter.SMALL_PROPERTY,
            ResourceCenter.MENU_ICON_BLANK,
            "SanBootView.MenuItem.property",
            MenuAndBtnCenterForMainUi.FUNC_PROPERTY
        );
    }

    private void addService( HashMap map,Vector serviceList ){
        int size = serviceList.size();
        for( int i=0; i<size; i++ ){
            Service service = (Service)serviceList.get(i);
            if( map.get( service.getServName() ) == null ){
                map.put( service.getServName(), service );
            }
        }
    }

    @Override public void doAction(ActionEvent evt){
        int width,height;

SanBootView.log.info(getClass().getName(),"########### Entering property action. " );

        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
SanBootView.log.info(getClass().getName(),"########### selObj is null.\nEnd of property action. " );
            return;
        }

        boolean isHost = ( selObj instanceof BootHost );
        boolean isCluster = ( selObj instanceof Cluster );
        if( !isHost && !isCluster ) {
SanBootView.log.info(getClass().getName(),"########### selObj is not host or cluster. \nEnd of property action. " );
            return;
        }

        HashMap map = new HashMap();
        if( isHost ){
            BootHost host =(BootHost)selObj;
            if( host.isWinHost() ){
                if( host.isCMDPProtect() ){
                    // 获取服务器上保存的
                    boolean isOk = view.initor.mdb.getOSService( ResourceCenter.CLT_IP_CONF+"/"+host.getID() + ResourceCenter.CONF_SERVICE,ResourceCenter.CMD_TYPE_CMDP );
                    if( isOk ){
                        this.addService( map, view.initor.mdb.getOSService() );
                    }
                }

                HostPropertyDialog dialog = new HostPropertyDialog( view,host,null,map );
                width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );
            }else{
                UnixHostPropertyDialog dialog = new UnixHostPropertyDialog( view,host );
                width  = 530+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );
            }
        }else if( isCluster ){
            Cluster cluster = (Cluster)selObj;
            if( cluster.isWinHost() ){
                if( cluster.isCMDPProtect() ){
                    // 获取服务器上保存的
                    boolean isOk = view.initor.mdb.getOSService( ResourceCenter.CLT_IP_CONF+"/cluster-"+cluster.getCluster_id() + ResourceCenter.CONF_SERVICE,ResourceCenter.CMD_TYPE_CMDP );
                    if( isOk ){
                        this.addService( map, view.initor.mdb.getOSService() );
                    }
                }

                HostPropertyDialog dialog = new HostPropertyDialog( view,null,cluster,map );
                width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );
            }else{
            }
        }else{
SanBootView.log.warning( getClass().getName(),"unknown host type. \n########### End of property action. " );
        }
SanBootView.log.info(getClass().getName(),"########### End of property action. " );
    }
}
