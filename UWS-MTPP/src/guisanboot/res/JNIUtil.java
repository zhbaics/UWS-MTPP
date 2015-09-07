/*
 * JNIUtil.java
 *
 * Created on 2007/11/9, PM�1:23
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.res;

// 不能移动到GUILib中，只能每个项目一个。但是要注意保持一致。（2010.9.17）

/**
 *
 * @author Administrator
 */
public class JNIUtil {
    public native int encrypt( int flag,int enc_flag,String svrip,String uname,String passwd );
    public native int isEncrypt( String svrip,String uname );
    public native String dencBackupKey( String verify,String svrip,String uname );
    public native String encUserKey( String passwd );
    public native String dencUserKey( String passwd );
    public native String getAgtPath();
    public native String ListDrivers();
    public native String getAgtUUID();  
    public native String getAgtSysInfo();
    public native String pcgenUUID();
    public native String chkRstPasswd( int flag,String envPasswd,String bkDevPath,String smbusr,String smbpasswd  );
    
    static {
        System.loadLibrary( "pcagent_java_lib" );
    }
    
    /** Creates a new instance of JNIUtil */
    public JNIUtil()  throws ClassNotFoundException {
      //  Class.forName("gui_sanboot.res.JNIUtil");
    }
    
    public String myDencBackupKey( String svrip,String uname ){
        return dencBackupKey( "Odysys Fivstor Denc Verify", svrip,uname );
    }
}
