/*
 * UWSSockConnectPool.java
 *
 * Created on 2007/10/29,��PM�7:23
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.tool;
import guisanboot.ui.SanBootView;
import guisanboot.res.ResourceCenter;
import javax.swing.SwingUtilities;


/**
 *
 * @author Administrator
 */
public class UWSSockConnectManager extends Thread {
    SanBootView view;
    private int sleep_time = 60; // 每60秒起来检查uws socket的连通性
    boolean flag = false;
    
    /** Creates a new instance of UWSSockConnectPool */
    public UWSSockConnectManager( SanBootView view) {
        this.view = view;
    }
    
    Runnable setTip = new Runnable(){
        public void run(){
            view.setConnectionTip( ResourceCenter.ICON_SOCK_DISCONNECT,SanBootView.res.getString("common.error.disconnect"));      
        }
    };
    
    Runnable cleanTip = new Runnable(){
        public void run(){
            view.setConnectionTip( ResourceCenter.MENU_ICON_BLANK,"");      
        }
    };
    
    @Override public void run(){
        while( view.initor.isLogined() && view.initor.isInited() ){
SanBootView.log.debug(getClass().getName(), "begin to check socket connections.............");
            
            if(  view.initor.socket != null ){ 
                if( view.initor.socket.isClosed() || !view.initor.socket.isConnected() ||
                    view.initor.socket.isInputShutdown() || view.initor.socket.isOutputShutdown() ){
SanBootView.log.error(getClass().getName(), " ################ client socket is bad. ###################");
                    if( !flag ){
                        try{
                            SwingUtilities.invokeAndWait( setTip );
                        }catch(Exception ex){}
                        flag = true;
                    }
                }
            }
            
            try{
                sleep( sleep_time*1000 );
            }catch(Exception ex){
            }
        }
        
        //try{
        //    SwingUtilities.invokeAndWait( cleanTip );
        //}catch(Exception ex){}
    }
}
