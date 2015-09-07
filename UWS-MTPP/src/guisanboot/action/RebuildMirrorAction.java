package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.data.VolumeMap;
import guisanboot.data.BootHost;
import guisanboot.ui.SanBootView;
import guisanboot.ui.BasicGetSomethingThread;
import javax.swing.JOptionPane;


public class RebuildMirrorAction extends GeneralActionForMainUi {
    public RebuildMirrorAction(){
        super(
          ResourceCenter.MENU_ICON_BLANK,
          ResourceCenter.MENU_ICON_BLANK,
          "View.MenuItem.rebuildMirror",
          MenuAndBtnCenterForMainUi.FUNC_AMS_REBUILD_MIRROR
        );
    }

    @Override public void doAction( ActionEvent evt ){
        String ip ="";
        int port = 5000;
        int targetId = 0;
        String param = "";
        String temp ="";
        int clntId ;

        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj !=null ){
            if( selObj instanceof VolumeMap ){
                VolumeMap volMp = ( VolumeMap )selObj;
                clntId = volMp.getVolClntID();
                BootHost ahost = view.initor.mdb.getBootHostFromVector(clntId);
                ip = ahost.getIP();
                port = ahost.getPort();
                temp = volMp.getVolDiskLabel();
                targetId = volMp.getVolTargetID();
            }
        } else {  
            return;
        } 
        param = " -m "+ temp + " -I "+ ResourceCenter.ISCSI_PREFIX + view.initor.mdb.getHostName() + " -S " + view.initor.getTxIP(ip) + " -P "+ ResourceCenter.ISCSI_LOGIN_PORT + " -t "+ targetId ;  
        SanBootView.log.info(getClass().getName(),"########### Entering start rebuildMirror action." );
        ReBuildMirrorThread thread = new ReBuildMirrorThread( view, ip , port, param );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.rebuildMirror"),
            SanBootView.res.getString("View.pdiagTip.rebuildMirror"),
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of start rebuildMirror action." );
    }
}


class ReBuildMirrorThread extends BasicGetSomethingThread{
    private String ip;
    private int port;
    private String param;

    public ReBuildMirrorThread( SanBootView _view, String _ip , int _port ,String _param){
        super(_view);
        this.ip = _ip;
        this.port = _port;
        this.param = _param;
    }

    public boolean realRun(){ 
       boolean isOk;
        try{
            isOk = view.initor.mdb.amsRebuildMirror(ip, port, param);
            if(isOk){
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("MenuAndBtnCenter.success.amsrebuild"));
                return isOk;
            } else {
                return isOk;
            }
        } catch (Exception e){
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.rebuildMirrorFail");
            return false;
        }
    }
}