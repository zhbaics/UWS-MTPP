package guisanboot.ams.service;

import guisanboot.data.BindOfUnixPartandLV;
import guisanboot.ui.SanBootView;
import guisanboot.data.MirrorGrp;

public class CreatePPProfiel {
    public final static String MG_DEFAULT_BEF_CMD = "/usr/odybk/server/bin/mg_ams_snapbf.sh";
    public final static String MG_DEFAULT_AFT_CMD = "/usr/odybk/server/bin/mg_ams_snapaf.sh";
    
    public boolean createMg( SanBootView view, BindOfUnixPartandLV binder, int hostid, String hostname, String ip, int port){
        MirrorGrp mg ;
        if( binder.isucsprotected ){
            mg = new MirrorGrp(
                -1,//int mg_id,
                hostid + "_" + binder.part.mp + "_cmdp_" + hostname ,//String mg_name,
                4,  //int mg_type,
                ip, //String mg_src_ip,
                port,   //int mg_src_port,
                "", //String mg_src_disk_uuid,
                binder.rootID,  //int mg_src_root_id,
                3600, //int mg_interval_time,
                65536, //int mg_min_snap_size,
                24, //int mg_max_snapshot,
                -1, //int mg_pid,
                MG_DEFAULT_BEF_CMD, //String mg_before_cmd,
                MG_DEFAULT_AFT_CMD, //String mg_after_cmd,
                "",//String mg_desc
                binder.ucsLogMaxSize,//String mg_log_max_size
                binder.ucsLogMaxTime//String mg_log_max_time
            );
        } else {
             mg = new MirrorGrp(
                -1,//int mg_id,
                hostid + "_" + binder.part.mp + "_cmdp_" + hostname ,//String mg_name,
                2,  //int mg_type,
                ip, //String mg_src_ip,
                port,   //int mg_src_port,
                "", //String mg_src_disk_uuid,
                binder.rootID,  //int mg_src_root_id,
                3600, //int mg_interval_time,
                65536, //int mg_min_snap_size,
                24, //int mg_max_snapshot,
                -1, //int mg_pid,
                MG_DEFAULT_BEF_CMD, //String mg_before_cmd,
                MG_DEFAULT_AFT_CMD, //String mg_after_cmd,
                ""//String mg_desc
            );
        }
        boolean isok = view.initor.mdb.addMg(mg);
        SanBootView.log.debug(getClass().getName(), "mg_id:" + view.initor.mdb.getNewId() );
        if( !isok ){
            SanBootView.log.error(getClass().getName(), "create ams mg for [" + binder.part.mp + "] error.");
        }
    
        return true;
    }

}
