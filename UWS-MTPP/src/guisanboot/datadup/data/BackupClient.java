package guisanboot.datadup.data;

/**
 * Title:        Odysys UWS
 * Description:
 * Copyright:    Copyright (c) 2008
 * Company:      odysys
 * @author zjj
 * @version 1.0
 */

public class BackupClient {
    public final static String W_2000 = "2000";
    public final static String W_XP   = "XP";
    public final static String W_2003 = "2003";
    public final static String W_HIGH = "HIGH"; // > win2003
    public final static String W_LOW  = "LOW";  // < win2000
        
    public final static String FLAG_FOR_RST_ORI_DISK = "RST_ORI_DISK";
    
    public final static int  MACHINE_TYPE_ALPHA   = 0;
    public final static int  MACHINE_TYPE_X86     = 1;
    public final static int  MACHINE_TYPE_I686    = 2;
    public final static int  MACHINE_TYPE_POWERPC = 257;
    public final static int  MACHINE_TYPE_MIPS    = 3;
    public final static int  MACHINE_TYPE_PENTIUM = 4;
    public final static int  MACHINE_TYPE_RS      = 5;
    public final static int  MACHINE_TYPE_HP      = 6;
    public final static int  MACHINE_TYPE_88000   = 7;
    public final static int  MACHINE_TYPE_SPARC   = 8;
    public final static int  MACHINE_TYPE_NSC     = 9;
    public final static int  MACHINE_TYPE_UNKNOWN = 10;

    public final static String MACHINE_ALPHA   = "Alpha";
    public final static String MACHINE_X86     = "X86";
    public final static String MACHINE_I686    = "i686";
    public final static String MACHINE_SPARC   = "sparc";
    public final static String MACHINE_MIPS    = "Mips";
    public final static String MACHINE_PENTIUM = "Pentium";
    public final static String MACHINE_RS      = "RS";
    public final static String MACHINE_HP      = "HP";
    public final static String MACHINE_88000   = "88000";
    public final static String MACHINE_POWERPC = "PowerPC";
    public final static String MACHINE_NSC     = "NSC";
    public final static String MACHINE_UNKNOWN = "Unknown";

    public final static int OS_TYPE_UNKNOWS     = 0;
    public final static int OS_TYPE_WIN16       = 1;
    public final static int OS_TYPE_W95         = 2;
    public final static int OS_TYPE_NT          = 3;
    public final static int OS_TYPE_OS2         = 4;
    public final static int OS_TYPE_NOV3        = 5;
    public final static int OS_TYPE_UNIX3       = 6;
    public final static int OS_TYPE_ODT         = 7;
    public final static int OS_TYPE_OS5         = 8;
    public final static int OS_TYPE_UNIX4       = 9;
    public final static int OS_TYPE_SUNOS       = 10;
    public final static int OS_TYPE_SOLARIS     = 11;
    public final static int OS_TYPE_NOV4        = 12;
    public final static int OS_TYPE_OSF1        = 13;
    public final static int OS_TYPE_NT_SERVER   = 14;
    public final static int OS_TYPE_SGI         = 15;
    public final static int OS_TYPE_AIX         = 16;
    public final static int OS_TYPE_DGUX        = 17;
    public final static int OS_TYPE_HPUX        = 18;
    public final static int OS_TYPE_BSDI        = 19;
    public final static int OS_TYPE_LINUX       = 20;
    public final static int OS_TYPE_USL         = 21;
    public final static int OS_TYPE_UNIXWARE    = 22;
    public final static int OS_TYPE_DOS         = 23;

    public final static String OS_DOS       = "DOS";
    public final static String OS_WIN16     = "WIN16";
    public final static String OS_NT        = "NT";
    public final static String OS_W95       = "W95";
    public final static String OS_OS2       = "OS2";
    public final static String OS_NOV3      = "NOV3";
    public final static String OS_UNIX3     = "UNIX3";
    public final static String OS_ODT       = "ODT";
    public final static String OS_OS5       = "OS5";
    public final static String OS_UNIX4     = "UNIX4";
    public final static String OS_SOLARIS   = "SOLARIS";
    public final static String OS_SUNOS     = "SUNOS";
    public final static String OS_NOV4      = "NOV4";
    public final static String OS_OSF1      = "OSF1";
    public final static String OS_NT_SERVER = "NT_SERVER";
    public final static String OS_AIX       = "AIX";
    public final static String OS_SGI       = "SGI";
    public final static String OS_DGUX      = "DGUX";
    public final static String OS_HPUX     = "HP/UX";
    public final static String OS_LINUX     = "Linux";
    public final static String OS_BSDI      = "BSDI";
    public final static String OS_USL       = "USL";
    public final static String OS_UNIXWARE  = "UNIXWARE";
    public final static String OS_UNKNOWN   = "Unknown";

    public final static String CLNTRECFLAG ="Record:";
    public final static String CLNTID ="clnt_id";
    public final static String CLNTNAME ="clnt_name";
    public final static String CLNTIP ="clnt_ip";
    public final static String CLNTMACHINE ="clnt_machine";
    public final static String CLNTPORT ="clnt_port";
    public final static String CLNTOS ="clnt_os";
    public final static String CLNTSTATUS = "clnt_status";
    public final static String CLNTUUID="clnt_uuid";
    public final static String CLNTACCT ="clnt_account_id";

    private long    id = -1;       // < 1000000,非空,唯一
    private String   hostName="";  // <32，不含' " 空格，同操作系统机器名限制.
    private String   ip="";        // IP4，不为空，唯一
    
    // 如果machineType为"RST_ORI_DISK",则表明该客户端是专门用来进行源盘恢复的
    private String   machineType="";// <32,不含' " 空格,(list)
    
    private int      port=-1;     // 整数，0< port <10000
    private String   osType="";   // 字符，<32,不含' " 空格,(list)
    private String   status="";   // 字符，<32,不含' " 空格,,(list)
    private String   uuid="";    
    private int      uid;   // 如果该客户端是专门用来进行源盘恢复的，则uid表示对应的dstAgent id

    private byte hostNameStrLen;

    public BackupClient(){
    }

    public BackupClient( String hostName,String ip,String machineType,int port, String osType,String uuid ){
        this( -1L,hostName,ip,machineType,port,osType,"active",uuid, -1 );
    }
    
    public BackupClient( String hostName,String ip,String machineType,int port, String osType,int uid ){
        this( -1L,hostName,ip,machineType,port,osType,"active","", uid );
    }

    public BackupClient(long _id,
      String _hostName,
      String _ip,
      String _machineType,
      int   _port,
      String _osType,
      String _status,
      String _uuid,
      int _uid
    ) {
        id    = _id;
        hostName    = _hostName;
        ip  = _ip;
        machineType = _machineType;
        port = _port;
        osType   = _osType;
        status = _status;
        uuid = _uuid;
        uid = _uid;
    }

    public long getID(){
        return id;
    }
    public void  setID(long _id){
        id = _id;
    }
    public String getHostName(){
        return hostName;
    }
    public void setHostName(String _hostName){
        hostName = _hostName;
    }
    public String getIP(){
        return ip;
    }
    public void setIP(String _ip){
        ip = _ip;
    }
    public String getMachineType(){
        return machineType;
    }
    public void setMachineType(String _machineType){
        machineType = _machineType;
    }
    public boolean isForRstOriDisk(){
        return machineType.equals( BackupClient.FLAG_FOR_RST_ORI_DISK );
    }
    public int getPort(){
        return port;
    }
    public void setPort(int _port){
        port = _port;
    }
    public String getOsType(){
        return osType;
    }
    public void setOsType(String _osType){
        osType = _osType;
    }
    public String getWinPlatForm(){
        try{
            String release = osType.substring(3);
            return release.toUpperCase();
        }catch(Exception ex){
            return "2003";
        }
    }
    public boolean isWin(){
        return osType.toUpperCase().startsWith("WIN");
    }
    public boolean isLinux(){
        return osType.toUpperCase().startsWith("LINUX");
    }
    public boolean isWin2000(){
        String type = getWinPlatForm();
        return type.equals( W_2000 );
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String _status){
        status = _status;
    }
    public String getUUID(){
        return uuid;
    }
    public void setUUID( String val ){
        uuid = val;
    }
    public int getAcctID(){
        return uid;
    }
    public void setAcctID( int val ){
        uid = val;
    }

    public int getHostNameStrLen(){
        return hostNameStrLen;
    }

    public static int getOSTypeInt(String type){
        if( type.equals( OS_DOS ) ){
          return OS_TYPE_DOS;
        }else if( type.equals( OS_WIN16 ) ){
          return OS_TYPE_WIN16;
        }else if( type.equals( OS_NT) ){
          return OS_TYPE_NT;
        }else if( type.equals( OS_W95) ){
          return OS_TYPE_W95;
        }else if( type.equals( OS_OS2)) {
          return OS_TYPE_OS2;
        }else if( type.equals( OS_NOV3)) {
          return OS_TYPE_NOV3;
        }else if( type.equals( OS_UNIX3)) {
          return OS_TYPE_UNIX3;
        }else if( type.equals( OS_ODT)) {
          return OS_TYPE_ODT;
        }else if( type.equals( OS_OS5)) {
          return OS_TYPE_OS5;
        }else if( type.equals( OS_UNIX4)) {
          return OS_TYPE_UNIX4;
        }else if( type.equals( OS_SOLARIS) ){
          return OS_TYPE_SOLARIS;
        }else if( type.equals( OS_SUNOS) ){
          return OS_TYPE_SUNOS;
        }else if( type.equals( OS_NOV4 ) ){
          return OS_TYPE_NOV4;
        }else if( type.equals( OS_OSF1)) {
          return OS_TYPE_OSF1;
        }else if( type.equals( OS_NT_SERVER)) {
          return OS_TYPE_NT_SERVER;
        }else if( type.equals( OS_AIX)) {
          return OS_TYPE_AIX;
        }else if( type.equals( OS_SGI)) {
          return OS_TYPE_SGI;
        }else if ( type.equals(OS_DGUX)){
          return OS_TYPE_DGUX;
        }else if(type.equals(OS_HPUX)){
          return OS_TYPE_HPUX;
        }else if( type.equals( OS_LINUX) ){
          return OS_TYPE_LINUX;
        }else if( type.equals( OS_BSDI) ){
          return OS_TYPE_BSDI;
        }else if( type.equals( OS_USL) ){
          return OS_TYPE_USL;
        }else {
          return OS_TYPE_UNKNOWS;
        }
    }

    public static String getOSTypeString(int type){
        String str;

        switch( type ){
          case OS_TYPE_DOS:
            str = OS_DOS;
            break;
          case OS_TYPE_WIN16:
            str = OS_WIN16;
            break;
          case OS_TYPE_NT:
            str = OS_NT;
            break;
          case OS_TYPE_W95:
            str = OS_W95;
            break;
          case OS_TYPE_OS2:
            str = OS_OS2;
            break;
          case OS_TYPE_UNIXWARE:
            str = OS_UNIXWARE;
            break;
          case OS_TYPE_USL:
            str = OS_USL;
            break;
          case OS_TYPE_BSDI:
            str = OS_BSDI;
            break;
          case OS_TYPE_LINUX:
            str = OS_LINUX;
            break;
          case OS_TYPE_HPUX:
            str = OS_HPUX;
            break;
          case OS_TYPE_DGUX:
            str = OS_DGUX;
            break;
          case OS_TYPE_SGI:
            str = OS_SGI;
            break;
          case OS_TYPE_AIX:
            str = OS_AIX;
            break;
          case OS_TYPE_NT_SERVER:
            str = OS_NT_SERVER;
            break;
          case OS_TYPE_OSF1:
            str = OS_OSF1;
            break;
          case OS_TYPE_NOV4:
            str = OS_NOV4;
            break;
          case OS_TYPE_SUNOS:
            str = OS_SUNOS;
            break;
          case OS_TYPE_SOLARIS:
            str = OS_SOLARIS;
            break;
          case OS_TYPE_UNIX4:
            str = OS_UNIX4;
            break;
          case OS_TYPE_OS5:
            str = OS_OS5;
            break;
          case OS_TYPE_ODT:
            str = OS_ODT;
            break;
          case OS_TYPE_UNIX3:
            str = OS_UNIX3;
            break;
          case OS_TYPE_NOV3:
            str = OS_NOV3;
            break;
          default:
            str = OS_UNKNOWN;
            break;
        }

        return str;
    }

    public static int getMachineTypeInt(String type){
        if( type.equals( MACHINE_ALPHA ) ){
          return MACHINE_TYPE_ALPHA;
        }else if( type.equals( MACHINE_X86 ) ){
          return MACHINE_TYPE_X86;
        }else if( type.equals( MACHINE_I686) ){
          return MACHINE_TYPE_I686;
        }else if( type.equals( MACHINE_SPARC) ){
          return MACHINE_TYPE_SPARC;
        }else if( type.equals( MACHINE_MIPS) ){
          return MACHINE_TYPE_MIPS;
        }else if( type.equals( MACHINE_PENTIUM ) ){
          return MACHINE_TYPE_PENTIUM;
        }else if( type.equals( MACHINE_RS) ){
          return MACHINE_TYPE_RS;
        }else if( type.equals( MACHINE_HP) ){
          return MACHINE_TYPE_HP;
        }else if( type.equals( MACHINE_88000) ){
          return MACHINE_TYPE_88000;
        }else if( type.equals( MACHINE_POWERPC ) ) {
          return MACHINE_TYPE_POWERPC;
        }else if( type.equals( MACHINE_NSC) ){
          return MACHINE_TYPE_NSC;
        }else {
          return MACHINE_TYPE_UNKNOWN;
        }
    }

    public static String getMachineTypeString(int type){
        String str;

        switch( type ){
          case MACHINE_TYPE_ALPHA:
            str = MACHINE_ALPHA;
            break;
          case MACHINE_TYPE_X86:
            str = MACHINE_X86;
            break;
          case MACHINE_TYPE_I686:
            str = MACHINE_I686;
            break;
          case MACHINE_TYPE_SPARC:
            str = MACHINE_SPARC;
            break;
          case MACHINE_TYPE_MIPS:
            str = MACHINE_MIPS;
            break;
          case MACHINE_TYPE_PENTIUM:
            str = MACHINE_PENTIUM;
            break;
          case MACHINE_TYPE_RS:
            str = MACHINE_RS;
            break;
          case MACHINE_TYPE_HP:
            str = MACHINE_HP;
            break;
          case MACHINE_TYPE_88000:
            str = MACHINE_88000;
            break;
          case MACHINE_TYPE_POWERPC:
            str = MACHINE_POWERPC;
            break;
          case MACHINE_TYPE_NSC:
            str = MACHINE_NSC;
            break;
          default:
            str = MACHINE_UNKNOWN;
        }
        return str;
    }

    public static String[] getMachineTypeString(){
        String[] list = new String[11];

        list[0] = MACHINE_ALPHA;
        list[1] = MACHINE_X86;
        list[2] = MACHINE_I686;
        list[3] = MACHINE_SPARC;
        list[4] = MACHINE_MIPS;
        list[5] = MACHINE_PENTIUM;
        list[6] = MACHINE_RS;
        list[7] = MACHINE_HP;
        list[8] = MACHINE_88000 ;
        list[9] = MACHINE_POWERPC ;
        list[10] = MACHINE_NSC ;
        return list;
    }
    
    public static String[] getOsTypeString(){
        String[] list = new String[24];

        list[0] = OS_DOS ;
        list[1] = OS_WIN16  ;
        list[2] = OS_NT ;
        list[3] = OS_W95 ;
        list[4] = OS_OS2 ;
        list[5] = OS_NOV3  ;
        list[6] = OS_UNIX3  ;
        list[7] = OS_ODT  ;
        list[8] = OS_OS5  ;
        list[9] = OS_UNIX4  ;
        list[10] = OS_SOLARIS ;
        list[11] = OS_SUNOS ;
        list[12] = OS_NOV4  ;
        list[13] = OS_OSF1   ;
        list[14] = OS_NT_SERVER ;
        list[15] = OS_AIX   ;
        list[16] = OS_SGI ;
        list[17] = OS_DGUX  ;
        list[18] = OS_HPUX  ;
        list[19] = OS_LINUX;
        list[20] = OS_BSDI ;
        list[21] = OS_USL;
        list[22] = OS_UNIXWARE;
        list[23] = OS_UNKNOWN;

        return list;
    }

    @Override
    public String toString(){
        return this.hostName;
    }

    public String printOwn(){
        StringBuffer buf = new StringBuffer();
        buf.append("\n------- Client info --------\n");
        buf.append("hostname: "+hostName+"\n");
        buf.append("ip: "+ip+"\n");
        buf.append("machine type: "+machineType +"\n");
        buf.append("port: "+port+"\n");
        buf.append("os type: "+osType +"\n");
        buf.append("status: "+status+"\n");
        buf.append("uuid: "+uuid+"\n");
        buf.append("acct id: "+uid+"\n");
        buf.append("------- Client info --------\n");
        return buf.toString();
    }

    public BackupClient cloneBackupClient(){
        BackupClient newBkClnt = new BackupClient();
        newBkClnt.setID( id );
        newBkClnt.setHostName( hostName );
        newBkClnt.setIP( ip );
        newBkClnt.setMachineType( machineType );
        newBkClnt.setPort( port );
        newBkClnt.setOsType( osType );
        newBkClnt.setStatus( status );
        newBkClnt.setUUID( uuid );
        newBkClnt.setAcctID( uid );
        return newBkClnt;
    }
}
