/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.data.BasicVDisk;
import guisanboot.res.ResourceCenter;
import guisanboot.tool.ui.BasicProcessor;
import guisanboot.ui.InitProgramDialog;
import guisanboot.ui.SanBootView;
import guisanboot.ui.SlowerLaunch;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.model.ClonediskCmd;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author zjj
 */
public class GetCloneDiskProcess extends BasicProcessor implements SlowerLaunch{
    SanBootView view;
    UnlimitedIncMirroredSnap snap;
    StringBuffer buf;
    int percent=0;
    String status="";
    String name,desc;
    ClonediskCmd cloneDiskCmd;
    boolean commitCmdFlag = true;
    BasicVDisk vdisk = null;

    /** Creates a new instance of LaunchSomething */
    public GetCloneDiskProcess(
        SanBootView view,
        InitProgramDialog diag,
        UnlimitedIncMirroredSnap snap,
        String name,
        String desc
    ) {
        super( diag );
        this.view = view;
        this.snap = snap;
        this.name = name;
        this.desc = desc;
    }

    // implements interface SlowerLaunch
    public boolean init(){ return true; }

    public String getLoadingStatus(){ return ""; }
    public int getLoadingProcessVal(){ 
        if( percent <0 ) return 0;
        if( percent > 100 ) return 100;
        return percent;
    }

    public String getInitErrMsg(){return "";}
    public boolean isCrtVG(){ return true;}
    public String getSrvIP(){ return ""; }
    
    public void doSomething(){
        this.setSlowerLaunch( this );

        String basePath = ResourceCenter.SNAP_DIR +"log/";
        String fname = "process_"+snap.snap.getSnap_root_id()+"_"+snap.snap.getSnap_local_snapid()+".txt";
        String file = basePath+fname;
        boolean isOk = view.initor.mdb.listDir( basePath );
        if( isOk ){
            if( view.initor.mdb.isExistFile( fname ) ){
                // delete output file again.
                isOk = view.initor.mdb.delFile( file );
                if( !isOk ){
                    JOptionPane.showMessageDialog(diag,
                        "Can't delete the output file to parse."
                    );
                    commitCmdFlag = false;
                    return;
                }
            }
        }

        // commit CloneDisk command in background
        try{
            cloneDiskCmd = new ClonediskCmd(  ResourceCenter.getCmd( ResourceCenter.CMD_CLONE_DISK ) );
            cloneDiskCmd.setSocket( view.initor.socket );
        }catch( Exception ex ){
            JOptionPane.showMessageDialog(diag,
                ex.getMessage()
            );
            commitCmdFlag = false;
            return;
        }

        isOk = cloneDiskCmd.realDo( snap.snap.getSnap_root_id(), snap.snap.getSnap_local_snapid(), name,desc );
        if( !isOk ){
            JOptionPane.showMessageDialog(diag,
                cloneDiskCmd.getErrMsg()
            );
            return;
        }

        // find out output file to parse
        boolean skip = false;
        int cnt = 0;
        while( !skip && ( cnt < 50 ) ){
            isOk = view.initor.mdb.listDir( basePath );
            if( isOk ){
                if( view.initor.mdb.isExistFile( fname ) ){
                    skip = true;
                    break;
                }
            }

            try{
                Thread.sleep( 1000*5 );
            }catch(Exception ex){
            }

            cnt+=1;
        }

        if( cnt >=50 ){
            JOptionPane.showMessageDialog(diag,
                view.initor.mdb.getErrorMessage()
            );
            return;
        }

        // parse process percent in loop
        int tryCnt=0;
        while( true ){
            try{
                Thread.sleep( 1000 );  //sleep 1 second
            }catch(Exception ex){
                ex.printStackTrace();
            }

            isOk = view.initor.mdb.viewFileContents( file );
            if( isOk ){
                buf = view.initor.mdb.getContentBuf();
                parseOutPut();
                
                if( this.status.equals("END") || this.status.equals("FAILED") ) break;

            }else{
                tryCnt+=1;
SanBootView.log.warning( getClass().getName(),"the failure count of getting clone process file : " + tryCnt );
                if( tryCnt >= 100 ){ // re-try at most 100 times
                    JOptionPane.showMessageDialog(diag,
                        view.initor.mdb.getErrorMessage()
                    );
                    break;
                }
            }
        }
        
        if( this.status.equals("END") ){
            // parse output file to create a disk
            vdisk = this.getBasicVDisk();
System.out.println("----> tid: "+ vdisk.getTargetID() +" rootid : "+ vdisk.getSnap_root_id() +" localsnapid: "+
                    vdisk.getSnap_local_snapid()
                 );
            // delete the output file
            view.initor.mdb.delFile( file );
        }else{
            JOptionPane.showMessageDialog(diag,
                SanBootView.res.getString("common.error.clonedisk")
            );
        }
    }
    
    public BasicVDisk getResultVDisk(){
        return this.vdisk;
    }

    private void parseOutPut(){
        String[] valList;

        String[] lines = Pattern.compile("\n").split( buf.toString(),-1 );

        for( int i=0; i<lines.length; i++ ){
            valList = parseKey( lines[i] );
            if( valList == null ) continue;

            if( valList[0].equals(ResourceCenter.VSNAP_EVENT_STATUS ) ){
                this.status = valList[1].toUpperCase();

                if( this.status.equals("END") || this.status.equals("FAILED") ){
                    this.percent = 100;
                    break;
                }
            }

            if( valList[0].equals( ResourceCenter.VSNAP_EVENT_VALUE ) ){
                try{
                    int val = Integer.parseInt( valList[1] );
                    this.percent = val;
                }catch(Exception ex){
                }
                break;
            }
        }
    }

    private BasicVDisk getBasicVDisk(){
        String[] valList;

        BasicVDisk avdisk = new BasicVDisk();
        String[] lines = Pattern.compile("\n").split( buf.toString(),-1 );
        for( int i=0; i<lines.length; i++ ){
            valList = parseKey( lines[i] );
            if( valList == null ) continue;

            if( valList[0].equals(ResourceCenter.snap_root_id_2 ) ){
                try{
                    int rootid = Integer.parseInt( valList[1] );
                    avdisk.setSnap_root_id( rootid );
                }catch(Exception ex){
                    avdisk.setSnap_root_id( -1 );
                }
            }

            // name not need to parse

            if( valList[0].equals( ResourceCenter.snap_local_snapid_2 ) ){
                try{
                    int localSnapId = Integer.parseInt( valList[1] );
                    avdisk.setSnap_local_snapid( localSnapId );
                }catch(Exception ex){
                    avdisk.setSnap_local_snapid( -1 );
                }
            }

            if( valList[0].equals( ResourceCenter.snap_pool_id_2  ) ){
                try{
                    int poolid = Integer.parseInt( valList[1] );
                    avdisk.setSnap_pool_id( poolid );
                }catch(Exception ex){
                    avdisk.setSnap_pool_id( -1 );
                }
            }

            if( valList[0].equals( ResourceCenter.snap_parent_2 ) ){
                try{
                    int parent = Integer.parseInt( valList[1] );
                    avdisk.setSnap_parent( parent );
                }catch(Exception ex){
                    avdisk.setSnap_parent( -1 );
                }
            }

            // can't find "snap_orig_id" from output file

            if( valList[0].equals( ResourceCenter.snap_block_size_2 ) ){
                try{
                    int blocksize = Integer.parseInt( valList[1] );
                    avdisk.setSnap_block_size( blocksize );
                }catch(Exception ex){
                    avdisk.setSnap_block_size( -1 );
                }
            }

            if( valList[0].equals( ResourceCenter.snap_max_block_no_2 ) ){
                try{
                    int maxblkno = Integer.parseInt( valList[1] );
                    avdisk.setSnap_max_block_no( maxblkno );
                }catch(Exception ex){
                    avdisk.setSnap_max_block_no( -1 );
                }
            }

            // snap_type =2 not need to parse

            if( valList[0].equals( ResourceCenter.snap_child_list_2 ) ){
                avdisk.setSnap_child_list( valList[1] );
            }

            if( valList[0].equals( ResourceCenter.snap_target_id_2 ) ){
                try{
                    int tid = Integer.parseInt( valList[1] );
                    avdisk.setSnap_target_id( tid );
                }catch(Exception ex){
                    avdisk.setSnap_target_id( -1 );
                }
            }

            if( valList[0].equals( ResourceCenter.snap_localid_2 ) ){
                try{
                    int localid = Integer.parseInt( valList[1] );
                    avdisk.setSnap_localid( localid );
                }catch(Exception ex){
                    avdisk.setSnap_localid( -1 );
                }
            }

            if( valList[0].equals( ResourceCenter.snap_create_time_2 ) ){
                avdisk.setSnap_create_time( valList[1] );
            }

            if( valList[0].equals( ResourceCenter.snap_desc_2 ) ){
                avdisk.setSnap_desc( valList[1] );
            }

            if( valList[0].equals( ResourceCenter.snap_owner_2 ) ){
                avdisk.setSnap_owner( valList[1] );
            }
        }

        return avdisk;
    }

    private String[] parseKey( String line ){
        try{
//System.out.println(" line -> "+ line);
            int indx = line.indexOf("=");
            String key = line.substring( 0,indx ).trim();
            String val = line.substring( indx+1 ).trim();
            String[] ret = new String[2];
            ret[0] = key;
            ret[1] = val;
            return ret;
        }catch( Exception ex ){
            return null;
        }
    }
}
