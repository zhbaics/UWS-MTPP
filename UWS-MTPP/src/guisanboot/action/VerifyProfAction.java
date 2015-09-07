/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;
import guisanboot.res.ResourceCenter;
import guisanboot.data.*;
import guisanboot.ui.*;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.cluster.entity.Cluster;
import guisanboot.audit.data.Audit;
/**
 *
 * @author zourishun
 */
public class VerifyProfAction extends GeneralActionForMainUi{
    public VerifyProfAction(){
        super(
            ResourceCenter.ICON_VERIFY_PROFILE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.verifyProf",
            MenuAndBtnCenterForMainUi.FUNC_VERIFY_PROF
        );
    }

    @Override public void doAction(ActionEvent evt){
        BootHost host = null;
        Cluster cluster = null;

SanBootView.log.info(getClass().getName(),"########### Entering verify profile action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isProf = ( selObj instanceof UniProfile );
        if( isProf ){
            UniProfile _prof = (UniProfile)selObj;
            BrowserTreeNode fNode = _prof.getFatherNode();
            ChiefProfile chiefProf = (ChiefProfile)fNode.getUserObject();
            fNode = chiefProf.getFatherNode();
            Object obj = fNode.getUserObject();
            if( obj instanceof BootHost ){
                host = (BootHost)obj;
            }else{
                cluster = (Cluster)obj;
            }

            // get real profile from cache. contents of profile on GUI maybe older.
            UniProfile prof = view.initor.mdb.getOneProfile( _prof.getProfileName() );
SanBootView.log.info( getClass().getName(),"profile to verify: " + _prof.getProfileName() );
            if( prof == null ) return;

            if( cluster != null ){
                host = view.initor.mdb.getHostFromVectOnD2DClntID1( prof.getUniProIdentity().getClntID() );
                if( host == null ){
SanBootView.log.error( getClass().getName()," not find boot host according to d2d_clnt_id:"+ prof.getUniProIdentity().getClntID() );
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("common.errcode.inconsistentProf")
                    );
                    return;
                }
            }
            Audit audit = view.audit.registerAuditRecord( host.getID(), MenuAndBtnCenterForMainUi.FUNC_VERIFY_PROF );

            if( !view.initor.mdb.checkProfile( prof ) ){
                audit.setEventDesc( "Failed to verify profile: " + prof.getProfileNameWithoutExtName() + " successfully." );
                view.audit.addAuditRecord( audit );

                JOptionPane.showMessageDialog( view,
                    view.initor.mdb.getProfErrMsg()
                );
            }else{
                audit.setEventDesc( "Verify profile: " + prof.getProfileNameWithoutExtName() + " successfully." );
                view.audit.addAuditRecord( audit );

                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("common.errcode.consistentProf")
                );
            }
        }
SanBootView.log.info(getClass().getName(),"########### End of verify profile action. " );
    }
}