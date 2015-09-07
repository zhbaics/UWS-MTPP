/*
 * ModifyProfileThread.java
 *
 * Created on 2008/12/11,  PM�4:49
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.datadup.data.UniProDrive;
import guisanboot.datadup.data.UniProHeader;
import guisanboot.datadup.data.UniProIBoot;
import guisanboot.datadup.data.UniProfile;
import guisanboot.res.ResourceCenter;
import guisanboot.tool.Tool;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.*;

/**
 *
 * @author zjj
 */
public class ModifyProfileThread {
    private SanBootView view;
    private String userName;
    private String pass;
    private int port;
    private String txIp;
    private String errMsg;
    
    /** Creates a new instance of ModifyProfileThread */
    public ModifyProfileThread( SanBootView view,String userName,String pass,String txIp,int port ) {
        this.view = view;
        this.userName = userName;
        this.pass = pass;
        this.port = port;
        this.txIp = txIp;
    }
    
    public boolean realRun(){
        UniProfile prof;
        UniProHeader header;
        UniProIBoot iboot;
        UniProDrive drive1;
        String path,oldTxIp,source_dest_reference,linux_lvm_reference;
        int indx1,indx2;
        File tmpFile;
        Pattern pattern;
        Matcher matcher;
        
        ArrayList pList = view.initor.mdb.getAllProfile();
        int size = pList.size();
        for( int i=0; i<size; i++ ){
            prof = (UniProfile)pList.get(i);
            
            header = prof.getUniProHeader();
            iboot  = prof.getUniProIBoot();
            drive1 = prof.getUniProDrive1();
            
            if( !userName.equals("") )
                iboot.setUws_username( userName );
            if( !pass.equals("") )
                iboot.setUws_passwd( pass );
            if( port != 0 )
                iboot.setUws_port( port+"" );
            iboot.setUws_ip( txIp );
            
            // 修改 source_dest_reference / linux_lvm_reference / path
            // source_dest_reference=C:\-?odytarget://20.20.1.144:3260@iqn.2005-05.cn.com.odysys.iscsi.uws404:32779
            // source_dest_reference=/boot-?odytarget://20.20.1.144:3260@iqn.2005-05.cn.com.odysys.iscsi.uws404:32769-?/-?odytarget://20.20.1.144:3260@iqn.2005-05.cn.com.odysys.iscsi.uws404:32768
            // linux_lvm_reference= serverip-?20.20.1.144-?tgtid-?32768-?lvmtype-?NONE-?vgname-?vg_1_32768-?lvname-?vol137.root-?fstype-?ext3;serverip-?20.20.1.144-?tgtid-?32769-?lvmtype-?NONE-?vgname-?vg_1_32769-?lvname-?vol137.boot-?fstype-?ext3;
            // path = odytarget://20.20.1.144:3260@iqn.2005-05.cn.com.odysys.iscsi.uws404:32769-?odytarget://20.20.1.144:3260@iqn.2005-05.cn.com.odysys.iscsi.uws404:32768
            
            try{
                source_dest_reference = header.getSource_dest_reference();
                linux_lvm_reference   = header.getLinux_lvm_reference();
                path  = drive1.getPath();
                indx1 = path.indexOf("/");
                indx2 = path.indexOf(":",indx1 );
                oldTxIp = path.substring(indx1+2, indx2 );
                if( !oldTxIp.equals( txIp ) ){
                    pattern = Pattern.compile( oldTxIp );
                    matcher = pattern.matcher( source_dest_reference );
                    header.setSource_dest_reference( matcher.replaceAll( txIp ) );
//System.out.println(" modifyed source_dest_reference: "+ source_dest_reference );
                    
                    matcher = pattern.matcher( linux_lvm_reference );
                    header.setLinux_lvm_reference( matcher.replaceAll( txIp ) );
                    
                    matcher = pattern.matcher( path );
                    drive1.setPath( matcher.replaceAll( txIp ) );
                }
            }catch(Exception ex){
                Tool.prtExceptionMsg( ex );
            }
            
            tmpFile = view.initor.mdb.createTmpFile( ResourceCenter.PREFIX,ResourceCenter.SUFFIX_PROF ); 
            if( tmpFile == null ){
                errMsg = SanBootView.res.getString("common.errmsg.crtTmpFileLocallyFailed");
                return false;
            }
            
            if( !view.initor.mdb.sendFileToServer( tmpFile.getName(),prof.prtMe() ) ){
                errMsg =  SanBootView.res.getString("common.errmsg.sendFileFailed")+" : "+
                    view.initor.mdb.getErrorMessage();
                return false;
            }
            
            // 将tmpFile move to profile dir
            if( !view.initor.mdb.moveFile( ResourceCenter.TMP_DIR + tmpFile.getName(), prof.getProfileName() ) ) {
                 errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_PROFILE)+
                    ": "+
                    SanBootView.res.getString("common.failed");
                 return false;
            }
        }
        
        return true;
    }
    
    public String getErrMsg(){
        return this.errMsg;
    }
}
