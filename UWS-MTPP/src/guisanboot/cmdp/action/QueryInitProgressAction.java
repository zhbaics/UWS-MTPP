/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.service.InitProgressGeter;
import guisanboot.cmdp.ui.QueryInitProgressDialog;
import guisanboot.data.BootHost;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 * QueryInitProgressAction.java
 *
 * Created on 2010-6-22, 15:37:44
 */
public class QueryInitProgressAction extends GeneralActionForMainUi{
    public QueryInitProgressAction(){
        super(
            ResourceCenter.ICON_MOD_PROFILE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.queryInitState",
            MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_INIT_PROGRESS
        );
    }

    @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering query init progress action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isHost = ( selObj instanceof BootHost );
        boolean isCluster = ( selObj instanceof Cluster );
        if( !isHost && !isCluster) return;

        BootHost host = null;
        Cluster cluster = null;
        if( isHost ){
            host = (BootHost)selObj;
        }else{
            cluster = (Cluster)selObj;
        }

        QueryInitProgressDialog dialog = new QueryInitProgressDialog( view,host,cluster );
        InitProgressGeter geter = new InitProgressGeter( dialog,view, host,cluster );
        geter.start();

        int width  = 780+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 380+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

SanBootView.log.info(getClass().getName(),"########### End of query init progress action. " );
    }
}