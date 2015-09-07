/*
 * BuildMirrorOnClnt.java
 *
 * Created on June 25, 2010, 11:37 AM
 */

package guisanboot.cmdp.service;

import guisanboot.data.SyncOpCmdWithoutput;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class BuildMirrorOnClnt extends SyncOpCmdWithoutput{
    public final static String  BM_RET = "ret";
    public final static String  BM_NAME = "name";

    public final static String ERR_6  = "6";  // 添加数据库失败
    public final static String ERR_10 = "10"; // 保护存在同名的
    public final static String ERR_11 = "11"; // 客户端的镜像已经存在
    public final static String ERR_12 = "12"; // 创建磁盘失败
    public final static String ERR_13 = "13"; // 创建客户端镜像失败

    private String vol_name = "";
    
    /** Creates a new instance of BuildMirrorOnClnt */
//    public BuildMirrorOnClnt( String cmd ) {
//        super( cmd );
//    }
    
    public void parser(String line){
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
        if( index > 0 ){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.info(this.getClass().getName(), "(BuildMirrorOnClnt)===> "+ s1);

            if( s1.startsWith(  BM_RET ) ){
                this.retForCMDP = value;
            }else if( s1.startsWith( BM_NAME )){
                this.vol_name = value;
            }
        }
    }

    public boolean buildMirror( String clntIP,int port,String letter,
           String interval_time,String min_size, int blk_size,int max_snap,
           String desc, String poolName,int worktype,String pub_ip1,String pub_ip2,String src_type
    ){
        this.retForCMDP = "1";
        this.vol_name = "";
        
        if( pub_ip1.equals("") ){
            if( src_type.equals("") ){
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype
                );
            }
            else
            {
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype+",,," + src_type
                );
            }
        }else{
            if( src_type.equals("") ){
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype+","+pub_ip1+","+pub_ip2
                );
            }else{
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype+","+pub_ip1+","+pub_ip2+","+src_type
                );
            }
        }
SanBootView.log.info( getClass().getName(), " build mirroring cmd: "+ getCmdLine() );

        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        boolean isOk = this.isOKForExcuteThisCmd();
        // retcode在isOKForExcuteThisCmd()里面会重新赋值
SanBootView.log.info( getClass().getName(), " build mirroring cmd retcode: "+ getRetCode() );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " build mirroring cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }
    
    public boolean buildMirror( String clntIP,int port,String letter,
           String interval_time,String min_size, int blk_size,int max_snap,
           String desc, String poolName
    ){
        return this.buildMirror( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,0,"","","" );
    }
    
    public boolean buildMirrorNoLabel( String clntIP,int port,String letter,
           String interval_time,String min_size, int blk_size,int max_snap,
           String desc, String poolName, String src_type
    ){
        return this.buildMirror( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,0,"","",src_type );
    }
    
    /*****************  UCS  ************************/
    public boolean buildMirrorByUcs( String clntIP,int port,String letter,
           String interval_time,String min_size, int blk_size,int max_snap,
           String desc, String poolName,int worktype,String pub_ip1,String pub_ip2,String src_type,
           int diskType, String LogMaxSize ,String logMaxTime , int latestPool , int oldestPool , int LogPool , int logNum 
    ){
        this.retForCMDP = "1";
        this.vol_name = "";
        
        if( pub_ip1.equals("") ){
            if( src_type.equals("") ){
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype
                    +",,,,"+
                    diskType+","+LogMaxSize+","+logMaxTime+","+oldestPool+","+latestPool+","+
                    LogPool+","+logNum
                );
            }
            else
            {
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype+",,," + src_type
                    +","+
                    diskType+","+LogMaxSize+","+logMaxTime+","+oldestPool+","+latestPool+","+
                    LogPool+","+logNum
                );
            }
        }else{
            if( src_type.equals("") ){
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype+","+pub_ip1+","+pub_ip2
                    +",,"+
                    diskType+","+LogMaxSize+","+logMaxTime+","+oldestPool+","+latestPool+","+
                    LogPool+","+logNum
                );
            }else{
                this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_CRT_PROTECT ) +
                    clntIP+","+port+","+letter+","+interval_time+","+min_size+","+blk_size+","+
                    max_snap+","+desc+","+poolName+","+worktype+","+pub_ip1+","+pub_ip2+","+src_type
                    +","+
                    diskType+","+LogMaxSize+","+logMaxTime+","+oldestPool+","+latestPool+","+
                    LogPool+","+logNum
                );
            }
        }
SanBootView.log.info( getClass().getName(), " build mirroring cmd: "+ getCmdLine() );

        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        boolean isOk = this.isOKForExcuteThisCmd();
        // retcode在isOKForExcuteThisCmd()里面会重新赋值
SanBootView.log.info( getClass().getName(), " build mirroring cmd retcode: "+ getRetCode() );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " build mirroring cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }
    
    public boolean buildMirrorbyUcs( String clntIP,int port,String letter,
           String interval_time,String min_size, int blk_size,int max_snap,
           String desc, String poolName,
           int diskType , String LogMaxSize ,String logMaxTime , int oldestPool , int latestPool  , int LogPool , int logNum,int worktype
    ){
        if(LogMaxSize.equals("") && logMaxTime.equals("")){
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",""
                    ,diskType,"" ,"" ,oldestPool ,latestPool ,LogPool ,logNum);
        } else if(LogMaxSize.equals("")){
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",""
                    ,diskType,"" ,logMaxTime ,oldestPool ,latestPool ,LogPool ,logNum);
        } else if(logMaxTime.equals("")){
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",""
                    ,diskType,LogMaxSize ,"" ,oldestPool ,latestPool ,LogPool ,logNum);
        } else {
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",""
                    ,diskType,LogMaxSize ,logMaxTime ,oldestPool ,latestPool ,LogPool ,logNum);
        }
     }
    
    public boolean buildMirrorNoLabelByUcs( String clntIP,int port,String letter,
           String interval_time,String min_size, int blk_size,int max_snap,
           String desc, String poolName, String src_type,
           int diskType , String LogMaxSize ,String logMaxTime , int oldestPool , int latestPool  , int LogPool , int logNum, int worktype
    ){
        if(LogMaxSize.equals("") && logMaxTime.equals("")){
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",src_type
                    ,diskType,"" ,"" ,oldestPool ,latestPool ,LogPool ,logNum);
        } else if(LogMaxSize.equals("")){
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",src_type
                    ,diskType,"" ,logMaxTime ,oldestPool ,latestPool ,LogPool ,logNum);
        } else if(logMaxTime.equals("")){
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",src_type
                    ,diskType,LogMaxSize ,"" ,oldestPool ,latestPool ,LogPool ,logNum);
        } else {
            return this.buildMirrorByUcs( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,worktype,"","",src_type
                    ,diskType,LogMaxSize ,logMaxTime ,oldestPool ,latestPool ,LogPool ,logNum);
        }
    }
    /**********************************************************/
    
    public String getRetVal(){
        return this.retForCMDP;
    }
    
    public boolean isExitMirrorRelationshipOnClnt(){
        return this.retForCMDP.equals( ERR_11 );
    }
    
    public String getVolName(){
        return this.vol_name;
    }
}
