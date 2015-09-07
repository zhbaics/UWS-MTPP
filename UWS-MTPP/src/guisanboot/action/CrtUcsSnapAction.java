/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import guisanboot.ui.BasicGetSomethingThread;

import guisanboot.data.BasicVDisk;
import guisanboot.data.View;
import guisanboot.ui.CreateUCSSnapDialog;
/**
 * UCS回放快照
 */
public class CrtUcsSnapAction extends GeneralActionForMainUi{
     public CrtUcsSnapAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtUcsSnap",
            MenuAndBtnCenterForMainUi.FUNC_CRT_UCS_SNAP
        );
    }

    @Override public void doAction(ActionEvent evt){
        // get vg size
SanBootView.log.info(getClass().getName(),"########### Entering create ucs_snap action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        View objView ;
        if(selObj instanceof View){
            objView = (View)selObj;
        } else {
            objView = new View();
        }
        
        if( view.initor.mdb.getUcsDiskCount(objView.getSnap_root_id()) == 0 ){
            JOptionPane.showMessageDialog(view, SanBootView.res.getString("MenuAndBtnCenter.Ucs.isnotucsdiskview"));
            return;
        }
        
        ArrayList snapList = view.initor.mdb.getSnapListFromQuerySql(objView.getSnap_root_id());
        if( snapList == null) snapList = new ArrayList();
        
        CreateUCSSnapDialog ucsDialog = new CreateUCSSnapDialog( view  , snapList , objView);
        int width  = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 300+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        ucsDialog.setSize(width, height);
        ucsDialog.setLocation( view.getCenterPoint( width,height ) );
        ucsDialog.setVisible( true );
        
        Object[] ret = ucsDialog.getValues();
        if( ret == null || ret.length <= 0 ) return;
        System.out.println(ret[0]);
        System.out.println(ret[1]);
        System.out.println(ret[2]);
        
        int forwordType = Integer.parseInt( ret[0].toString() );
        int forwordTimeOrIO = Integer.parseInt( ret[1].toString() );
        String forwordPoint = ret[2].toString() ; 
        
        ForwordUcsSnapThread thread = new ForwordUcsSnapThread(view , objView.getSnap_root_id(),objView.getSnap_local_snapid(),
                forwordPoint , forwordTimeOrIO , forwordType);
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.forworducs"),
            SanBootView.res.getString("View.pdiagTip.forworducs"),
            thread
        );
        
SanBootView.log.info(getClass().getName(),"########### End of create ucs_snap action. " );
    }
    
    public boolean isUcsView( View objView , ArrayList list){
        boolean ret = false;
        int size = list.size();
        BasicVDisk bvdisk;
        if(size >0 ){
            for( int i = 0 ; i < size ; i++){
                bvdisk = (BasicVDisk)list.get(i);
                if( bvdisk != null){
                    if(bvdisk.isUcsDisk()){
                        ret = true;
                    }
                }
            }
        } else {
            ret = false;
        }
        return ret;
    }
}

class ForwordUcsSnapThread extends BasicGetSomethingThread {
    private int rootid ;
    private int localid ;
    private String timeStamp;
    private int ioNum ;
//    private int maxTime ;
//    private int logpos ;
    private int forwordType;
    
    public ForwordUcsSnapThread(SanBootView _view , int _rootid ,int _localid , String _timestamp , int _ioNum , int _forwordType){
        super(_view);
        this.rootid = _rootid ;
        this.localid = _localid ;
        this.timeStamp = _timestamp ;
        this.ioNum = _ioNum ;
        this.forwordType = _forwordType ;
    }
    
    public boolean realRun(){ 
       boolean isOk;
        try{
            System.out.println("进入了realRun");
            if(forwordType == CreateUCSSnapDialog.FOR_TIME){
                isOk = view.initor.mdb.forwordUcsSnapByTime(rootid, localid, timeStamp);
                System.out.println("执行了bytime");
            } else {
                isOk = view.initor.mdb.forwordUcsSnapByIO(rootid, localid, ioNum);
                System.out.println("执行了byio");
            }
            if(isOk){
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("MenuAndBtnCenter.success.forworducs"));
                return isOk;
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("MenuAndBtnCenter.error.forworducsFail"));
                return isOk;
            }
        } catch (Exception ex){
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.forworducsFail");
            return false;
        }
    }
}