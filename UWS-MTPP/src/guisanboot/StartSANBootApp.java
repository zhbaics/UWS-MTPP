package guisanboot;

import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JFrame;
import guisanboot.ui.SanBootView;
import guisanboot.res.ResourceCenter;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.Properties;
import mylib.tool.*;

/**
 * 整体界面
 * @author Administrator
 */
public class StartSANBootApp {
    SanBootView frame;
    boolean packFrame = true;
    
    Runnable refreshFrame = new Runnable(){
        public void run(){
            frame.validate();
            frame.repaint();
        }
    };
    
    public StartSANBootApp( ) {  // Construct the application
        try{
            // frame = new SanBootView( ResourceCenter.MODE_SIMPLE );
            frame = new SanBootView( ResourceCenter.MODE_RELEASE );
            
            if (packFrame)
                frame.pack();  //Pack frames that have useful preferred size info, e.g. from their layout
            else
                frame.validate();  //Validate frames that have preset sizes

            // Get all jdk system properties,including file.encoding etc.
            // If you want to specify VM option such as file.encoding,you can
            // add -Dfile.encoding=GBK to project's property
            Properties p = System.getProperties();
            for ( Iterator it = p.keySet().iterator(); it.hasNext(); )
            {
                 String key = (String ) it.next();
                 String value = (String )  p.get(key);
                 System.out.println(key +":" +value);
            }

            // Center the window
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();
            if (frameSize.height > screenSize.height) {
                frameSize.height = screenSize.height;
            }
            if (frameSize.width > screenSize.width) {
                frameSize.width = screenSize.width;
            }
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            
            // maximized frame
            frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
            frame.setVisible( true );
            
            // 刷新frame,否则有时候第一次启动时（冷启动）会出现界面紊乱的情况,
            // 但下面这段代码不知有无作用
            try{
                SwingUtilities.invokeAndWait( 
                    refreshFrame
                );
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }catch( Exception e ){
            e.printStackTrace();
            JOptionPane.showMessageDialog( null,
                "该应用程序初始化失败"  // "application initialization failed."
            );
            System.exit( 0 );
        }
    }

    public static void main( String[] args ) { // Main method
        
        // 使用户界面具有所在系统的风格
        try{
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            //Class cls = Class.forName("org.fife.plaf.OfficeXP.OfficeXPLookAndFeel");
            //UIManager.setLookAndFeel( (LookAndFeel)cls.newInstance() );
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        // 设置字体,否则用 default font 效果不好
        Check.initGlobalFontSetting( new Font("Dialog",0,12) );
        
        StartSANBootApp ss = new StartSANBootApp(  );
    }
}
