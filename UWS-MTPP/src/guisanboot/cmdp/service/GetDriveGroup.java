/*
 * GetDriveGroup.java
 *
 * Created on 2010/6/9,�PM�4:25
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.service;

import guisanboot.cmdp.entity.DriveGroup;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class GetDriveGroup extends NetworkRunning {
    private ArrayList<DriveGroup> list = new ArrayList<DriveGroup>();
    private DriveGroup curDg;

    /** Creates a new instance of GetDriveGroup */
    public GetDriveGroup( String cmd, Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetDriveGroup( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( !this.isContinueToParserRetValueForCMDP1(line) ) return;

        if( !this.isEqZero() ){
            this.errMsg += line +"\n";
        }else{
            String s1 = line.trim();
    SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

            int index = s1.indexOf("=");
            if( index>0 ){
                String value = s1.substring( index+1 ).trim();
    SanBootView.log.debug(getClass().getName(),"@@@@@: "+value );

                if( s1.startsWith( DriveGroup.DG_NAME ) ){
                    this.curDg.dg_name = value;
                    list.add( curDg );
                }else if( s1.startsWith( DriveGroup.DG_ROOTID )){
                    try{
                        this.curDg.master_disk_rootid = Integer.parseInt( value );
                    }catch( Exception ex ){
                        this.curDg.master_disk_rootid = -1;
                    }
                }
            }else{
                if( s1.startsWith( BootHost.CMDPRECFLAG )){
                    this.curDg = new DriveGroup();
                }
            }
        }
    }

    public boolean realDo(){
        this.setCmdType( ResourceCenter.CMD_TYPE_CMDP );
SanBootView.log.info( getClass().getName(), " get drivegroup cmd: "+ getCmdLine() );
        try{
            curDg = null;
            list.clear();

            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get drivegroup retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get drivegroup errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public ArrayList<DriveGroup> getDgList(){
        int size = this.list.size();
        ArrayList<DriveGroup> ret = new ArrayList<DriveGroup>( size );
        for( int i=0; i<size; i++ ){
            ret.add( list.get(i) );
        }
        return ret;
    }

    public DriveGroup findDgOnRootId( int rootid ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            DriveGroup dg = list.get(i);
            if( dg.master_disk_rootid == rootid ){
                return dg;
            }
        }

        return null;
    }

    public DriveGroup findDgOnDgName( String dgname ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            DriveGroup dg = list.get(i);
            if( dg.dg_name.equals( dgname ) ){
                return dg;
            }
        }

        return null;
    }
}
