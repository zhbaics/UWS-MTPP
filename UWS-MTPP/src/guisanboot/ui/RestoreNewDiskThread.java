/*
 * RestoreNewDiskThread.java
 *
 * Created on 2007/12/13,�PM 3:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author Administrator
 */
public class RestoreNewDiskThread implements SlowerLaunch{
    String status="";
    String errormsg="";
    boolean isOK;
    int cnt=0;
    int total;
    JDialog dialog;
    SanBootView view;
    ArrayList diskList; // 新旧盘的恢复关系
    DestAgent host;
    int mode;
    HashMap map = new HashMap();
    
     public RestoreNewDiskThread(
            JDialog dialog,
            SanBootView view,
            ArrayList diskList,
            DestAgent host
    ) {
        this( dialog,view,diskList,host,ResourceCenter.CMD_TYPE_MTPP );
    }

    /** Creates a new instance of RestoreNewDiskThread */
    public RestoreNewDiskThread( 
            JDialog _dialog,
            SanBootView _view,
            ArrayList _diskList,
            DestAgent _host,
            int mode
    ) {
        dialog = _dialog;
        view = _view;
        diskList = _diskList;
        host = _host;
        this.mode = mode;
        
        total = diskList.size();
    }
    
    public boolean init(){
        int old_diskno,new_diskno;
        String contents,conf;
        DiskForWin newDisk;
        boolean isOk,ret = true;
        
        StringBuffer errBuf = new StringBuffer();
        
        String ip = host.getIP();
        int port = host.getPort();
SanBootView.log.info(getClass().getName(), " there are : "+ this.total + " disks to restore" );
        
        Iterator iterator = diskList.iterator();
        while ( iterator.hasNext() ){
            DiskBinder binder = (DiskBinder)iterator.next();
            old_diskno = binder.old_disk.getDiskno();
            new_diskno = binder.new_disk.getDiskno();
SanBootView.log.info(getClass().getName(), " old disk: "+ binder.old_disk.prtMe());
SanBootView.log.info(getClass().getName(), " new disk: "+ binder.new_disk.prtMe() );
            
            cnt++;
            status = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.loadstatus.rstPart")+
                    "( " + SanBootView.res.getString("common.disk") + new_diskno + " )";
            conf = host.getOldDiskConfRelativePath(); 

            if( this.mode == ResourceCenter.CMD_TYPE_MTPP ){
                isOk = view.initor.mdb.sendNetConf( ip, port, conf );
            }else{
                isOk = this.sendFileToAgent( conf );
            }
            if( !isOk ){                
                errBuf.append(
                  "\n" + SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstPart1")+" : "+
                  view.initor.mdb.getErrorMessage()
                ); 
                ret = false;
                break;
            }
            if( this.mode == ResourceCenter.CMD_TYPE_CMDP ){
                this.delTempFile( conf ); // delete file on uws server
            }
            
            // 由于partition的时间不好确定，故而一律将超时时间设置为10 hours
            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
            if( this.mode == ResourceCenter.CMD_TYPE_MTPP ){
                isOk = view.initor.mdb.rstPartition( ip, port, conf, old_diskno, new_diskno );
            }else{
                isOk = view.initor.mdb.rstPartition( ip, port, conf, old_diskno, new_diskno,ResourceCenter.CMD_TYPE_CMDP );
            }
            view.initor.mdb.restoreOldTimeOut();
            
            if( !isOk ){
                errBuf.append(
                  "\n"+SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstPart")+" : "+
                  view.initor.mdb.getErrorMessage()
                );
                ret = false;
                break;
            }
            
            cnt++;
            status = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.loadstatus.formatPart")+
                     "( " + SanBootView.res.getString("common.disk") + new_diskno + " )";
            if( this.mode == ResourceCenter.CMD_TYPE_MTPP ){
                isOk = view.initor.mdb.listPartition( ip,port, conf, old_diskno,new_diskno );
            }else{
                isOk = view.initor.mdb.listPartition( ip,port, conf, old_diskno,new_diskno,ResourceCenter.CMD_TYPE_CMDP );
            }
            if( !isOk ){
                errBuf.append(
                  "\n"+SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.formatPart1")+" : "+
                  view.initor.mdb.getErrorMessage()
                );
                ret = false;
                break;
            }
            contents = view.initor.mdb.getListPartContents();
            
            conf = host.getTmpPartInfoAbsPath( new_diskno );
            
            isOk = saveInfo( conf, contents );
            if( !isOk ){
                errBuf.append(
                  "\n" + errormsg
                );
                ret = false;
                break;
            }
            
            conf = host.getTmpPartInfoRelativePath( new_diskno );
            if( this.mode == ResourceCenter.CMD_TYPE_MTPP ){
                isOk = view.initor.mdb.sendNetConf( ip, port, conf );
            }else{
                isOk = this.sendFileToAgent( conf );
            }
            if( !isOk ){
                errBuf.append(
                  "\n" + SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.formatPart2")+" : "+
                  view.initor.mdb.getErrorMessage()
                );
                ret = false;
                break;
            }
            if( this.mode == ResourceCenter.CMD_TYPE_CMDP ){
                this.delTempFile( conf ); // delete file on uws server
            }
            
            // 由于format的时间不好确定，故而一律将超时时间设置为10 hours
            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
            if( this.mode == ResourceCenter.CMD_TYPE_MTPP  ){
                isOk = view.initor.mdb.formatPartition( ip, port, conf, old_diskno, new_diskno );
            }else{
                isOk = view.initor.mdb.formatPartition( ip, port, conf, old_diskno, new_diskno,"-nofs",ResourceCenter.CMD_TYPE_CMDP );
            }
            view.initor.mdb.restoreOldTimeOut();
            
            if( !isOk ){
                errBuf.append(
                  "\n" + SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.formatPart")+" : "+
                  view.initor.mdb.getErrorMessage()
                );
                ret = false;
                break;
            }
            
            newDisk = view.initor.mdb.getMpFromFormatPart();
            if( newDisk != null ){
                genRstMaper( newDisk );
                replaceNewdisk( newDisk );
            }
        }
        
        errormsg = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstDisk1");
        
        isOK = ret;
        return ret;
    }
    
    public String getLoadingStatus(){
        return status;
    }
    
    public int getLoadingProcessVal(){
        int val = (cnt*100)/(total*2);
        if( val >=100 ){
            val = 99;
        }
        return val;
    }
     
    public String getInitErrMsg(){
        return errormsg;
    }
    public boolean isSuccessfully(){
        return isOK;
    }
    
    public String getSrvIP(){
        return "";
    }
    public boolean isCrtVG(){
        return true;
    }
    
    private void genRstMaper( DiskForWin newDisk ){
        String oldLetter,newLetter,volName;
        Object val;
        RestoreMapper mapper;
        DestDevice dest;
        
        List list = newDisk.getPartitionList();
        Iterator iterator = list.iterator();
        while( iterator.hasNext() ){
            SystemPartitionForWin part = (SystemPartitionForWin)iterator.next();
            oldLetter = part.part_oldLetter;
            newLetter = part.part_mntLetter;
            volName = part.part_volname;
            
            if( !oldLetter.equals("") ){
                if( map.get( oldLetter ) == null ){
                    dest = new DestDevice( newLetter.substring(0, 1), volName );
                    mapper = new RestoreMapper( oldLetter,dest );
                    map.put( oldLetter, mapper );
                }
            }
        }
    }
    
    private void replaceNewdisk( DiskForWin newDisk ){
        int size = diskList.size();
        for( int i=0; i<size; i++ ){
            DiskBinder binder = (DiskBinder)diskList.get(i);
            if( binder.new_disk.compareTo( newDisk ) == 0 ){
                binder.new_disk = newDisk;
                break;
            }
        }
    }
    
    public ArrayList getNewRstRelationship(){
        return diskList;
    }
    
    public HashMap getRstMapper(){
        return map;
    }
    
    private boolean saveInfo( String confile,String contents ){        
        File tmpFile = view.initor.mdb.createTmpFile( ResourceCenter.PREFIX,ResourceCenter.SUFFIX_DISKINFO );
        if( tmpFile == null ){
SanBootView.log.info(getClass().getName(), " crt tmp file failed ");            
            errormsg = SanBootView.res.getString("common.errmsg.crtTmpFileLocallyFailed");
            return false; 
        }
        
        // 发送profile的内容��
        if( !view.initor.mdb.sendFileToServer( tmpFile.getName(),contents ) ){         
            errormsg =  SanBootView.res.getString("common.errmsg.sendFileFailed");
            tmpFile.delete();
            return false;
        }    
           
        tmpFile.delete();
        
        // 将tmpFile move to profile dir
        boolean isOk = view.initor.mdb.moveFile(
            ResourceCenter.TMP_DIR + tmpFile.getName(), confile      
        );
        
        if( !isOk ){
            errormsg = view.initor.mdb.getErrorMessage();
        }
        
        return isOk;   
    }

    String tftp_path="";
    private boolean sendFileToAgent( String conf ){
        if( tftp_path.equals("") ){
            tftp_path = view.initor.mdb.getTftpRootPath();
            if( tftp_path.equals("") ){
SanBootView.log.error(getClass().getName(), "tftp root path is null.");
                return false;
            }
        }

        boolean isOk = view.initor.mdb.copyFiles( ResourceCenter.CLT_IP_CONF + conf,tftp_path + conf );
        if( !isOk ){
SanBootView.log.error(getClass().getName(), "failed to copy config file.");
            return false;
        }

        isOk = view.initor.mdb.getFileFromS2A( host.getDst_agent_ip(),host.getDst_agent_port(),view.initor.txIp,conf,ResourceCenter.CMD_TYPE_CMDP );
        if( !isOk ){
SanBootView.log.error(getClass().getName(), "failed to transfer config file to agent.");
            return false;
        }

        return true;
    }

    private void delTempFile( String conf ){
        view.initor.mdb.delFile( tftp_path + conf );
    }
}