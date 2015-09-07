package guisanboot.datadup.data;

import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BrowserTreeNode;
import mylib.UI.TableRevealable;
import mylib.UI.TreeRevealable;
import mylib.UI.GeneralInfo;
import mylib.UI.Browser;
import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;

public class UniProfile extends AbstractUniProfile implements GeneralInfo,TreeRevealable,TableRevealable{
    public  boolean isSel = false;
    
    private UniProIdentity identity = new UniProIdentity();
    private UniProHeader   header   = new UniProHeader();
    private UniProDrive    drive1   = new UniProDrive("drive-1");
    private UniProBackup   backup   = new UniProBackup();
    private UniProIBoot    iboot    = new UniProIBoot();
    private UniProDynamic  dynamic  = new UniProDynamic();
    
    // 是否有iboot这一节；对于uws而言，必须有这一节，否则就认为是FIVStorEX的profile，将它过滤掉
    private boolean alreadyIboot = false;  
    
    private String profileName  = null;
    private long lastModTime = 0L;
    
    private int type = ResourceCenter.TYPE_PROF;
    private BrowserTreeNode treeNode;
    private BrowserTreeNode fatherNode = null;
    
    private ArrayList<Integer> fsForTree = null;
    private ArrayList<Integer> fsForTable = null;
    
    public UniProfile(){
    }

    public UniProfile(String _profileName) {
        setProfileName(_profileName);
    }
    
    public void setTreeNode(BrowserTreeNode _treeNode){
        treeNode = _treeNode;
    }
    public BrowserTreeNode getTreeNode(){
        return treeNode;
    }
    
    public void setFatherNode(BrowserTreeNode _father){
        this.fatherNode = _father;
    }
    public BrowserTreeNode getFatherNode(){
        return fatherNode;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 8 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RUN_PROF ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_VERIFY_PROF ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ADD_SCH ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_PROF) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_PROF ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RENAME_PROF ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 8 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RUN_PROF ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_VERIFY_PROF ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ADD_SCH ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_PROF) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_PROF ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RENAME_PROF ) );
        }
    }
    
    public String getProfileName(){
        return profileName;
    }
    
    public long getLastModTime(){
        return lastModTime;
    }
    public void setLastModTime(long _time){
        lastModTime = _time;
    }
    
    @Override public String toString(){
        if( profileName != null ){
            int index = profileName.lastIndexOf("/");
            return profileName.substring(index+1).trim();
        }else{
            return "";
        }
    }

    public String getProfileNameWithoutExtName(){
        String name = toString();
        int len = name.length();
        return name.substring( 0, len-4 );
    }
    
    public void setProfileName(String _profileName){
        profileName = _profileName;
    }

    public void setIdentityProfileName(){
        identity.setFileName( profileName );
    }
    
    public void setHeaderProfileName(){
        header.setProfileName( toString() );
    }
    
    public UniProIdentity getUniProIdentity(){
        return identity;
    }
    public void setUniProIdentity( UniProIdentity i ){
        identity = i;
    }
    
    public UniProHeader getUniProHeader(){
        return header;
    }
    public void setUniProHeader( UniProHeader h ){
        header = h;
    }
    
    public UniProDrive getUniProDrive1(){
        return drive1;
    }
    public void setUniProDrive1( UniProDrive drv ){
        drive1 = drv;
    }
    
    public UniProBackup getUniProBackup(){
        return backup;
    }
    public void setUniProBackup( UniProBackup b ){
        backup = b;
    }
    
    public UniProIBoot getUniProIBoot(){
        return iboot;
    }
    public void setUniProIBoot( UniProIBoot i ){
        iboot = i;
    }
    
    public boolean hasIBootSection(){
        return alreadyIboot;
    }
    
    public void outputProfile() throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(profileName));
        outputProfile(out);
        out.close();
    }

    public String prtMe(){
        if( identity == null )
            identity = new UniProIdentity();
        if( header == null )
            header = new UniProHeader();
        if( drive1 == null )
            drive1 = new UniProDrive("drive-1");
        if( backup == null )
            backup = new UniProBackup();
        if( iboot == null )
            iboot = new UniProIBoot();
        
//System.out.println("@@@@@@@@@ begin to write profile........");

        StringBuffer buf = new StringBuffer();
        buf.append( identity.prtMe() );
        buf.append( header.prtMe() );
        buf.append( drive1.prtMe() );
        buf.append( backup.prtMe() );
        buf.append( iboot.prtMe() );
        
        return buf.toString();
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException,IllegalArgumentException {
        if( identity == null )
            identity = new UniProIdentity();
        if( header == null )
            header = new UniProHeader();
        if( drive1 == null )
            drive1 = new UniProDrive("drive-1");
        if( backup == null )
            backup = new UniProBackup();
        if( iboot == null )
            this.iboot = new UniProIBoot();
            
//System.out.println("@@@@@@@@@ begin to write profile........");
        
        identity.outputProfile(out);
        header.outputProfile(out);
        drive1.outputProfile(out);
        backup.outputProfile(out);
        iboot.outputProfile(out);
    }

    public void parserProfile( StringBuffer buf ) throws IllegalArgumentException{
        int begin;
        
        String[] lines = Pattern.compile("\n").split( buf.toString(),-1 );
 
//for( int i=0;i<lines.length;i++ ){
//    System.out.println("line ====> : "+lines[i]+" len:"+lines[i].getBytes().length );
//}
        
        if( lines!=null && lines.length>0 ){
            begin = 0;
        }else{
            begin = -1;
        }
        
        parserProfile( begin,lines );
    }
    
    public int parserProfile( int begin,String[] lines ) throws IllegalArgumentException {
        String line;
        AbstractUniProfile profile;
       
        if( begin == -1 ){
            throw new IllegalArgumentException("None any lines in config file.");
        }
                    
        boolean alreadyIdentity = false;
        boolean alreadyHeader   = false;
        boolean alreadyDrive1   = false;
        boolean alreadyBackup   = false;
        boolean alreadyDynamic  = false;
        
        line = lines[begin];
        begin++;
        
        do{            
//System.out.println( " line[profile]: "+line+" begin: "+begin );

            if( line.length() == 0 || !isTitle( line ) ){
                begin++;
                continue;
            }
            
            if( line.equals("[identity]") ){
                if( alreadyIdentity )
                    throw new IllegalArgumentException("Duplication [identity] ->"+line );
                identity = new UniProIdentity();
                profile = identity;
                alreadyIdentity = true;
            } else if( line.equals("[header]")){
                if( alreadyHeader )
                    throw new IllegalArgumentException("Duplication [header] ->"+line );
                header = new UniProHeader();
                profile = header;
                alreadyHeader = true;
            } else if( line.equals("[drive-1]") ){
                if( alreadyDrive1 )
                    throw new IllegalArgumentException("Duplication [drive-1] ->"+line );
                drive1 = new UniProDrive("drive-1");
                profile = drive1;
                alreadyDrive1 = true;
            } else if( line.equals("[backup]") ){
                if( alreadyBackup )
                    throw new IllegalArgumentException("Duplication [backup] -> "+line );
                backup = new UniProBackup();
                profile = backup;
                alreadyBackup = true;
            }else if( line.equals("[iboot]") ){
                if( alreadyIboot )
                    throw new IllegalArgumentException("Duplication [iboot] -> "+line );
                iboot = new UniProIBoot();
                profile = iboot;
                alreadyIboot = true;
            }else if( line.equals("[dynamic]") ){
                if( alreadyDynamic )
                    throw new IllegalArgumentException("Duplication [dynamic] -> "+line );
                dynamic = new UniProDynamic();
                profile = dynamic;
                alreadyDynamic = true;
            }else if( line.equals("[drive-2]") ){
                // [drive-2],[database], [oracle] 这三节不属于uws的profile的组成部分
                profile = new UniProDrive("drive-2");
            }else if( line.equals("[database]") ){
                profile = new UniProDatabase();
            }else if( line.equals("oracle") ){
                profile = new UniOracle();
            }else
                throw new IllegalArgumentException("Invalid configure line ->"+line );
            
            begin = profile.parserProfile( begin,lines );

            line = profile.getLastLine();
            
            begin++;
        }while( begin <lines.length );
        
        return begin;
    }
    
    //** GeneralInfo的实现**/
    public int getType(){
        return type;
    }
    public int getIpIndex(){
        return -1;
    }
    public ArrayList<Integer> getFunctionList( int type ){
        addFunctionsForTree();
        addFunctionsForTable();
        
        if( type == Browser.POPMENU_TREE_TYPE )
            return fsForTree;
        else 
            return fsForTable;
    }
    public String getComment(){
        return "";
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_PROFILE;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_PROFILE;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return toString();
    }
    public String toTipString(){
        String src = backup.getSrc();
        if( src.length() > 50 ){
            src = src.substring(0, 50)+"...";
        }
        return toTreeString()+" [ "+src +" ] ";
    }
    
    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.ICON_PROFILE;
    }
    public String toTableString(){
        return toString();
    }
}
