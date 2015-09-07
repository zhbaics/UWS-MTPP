package guisanboot.ams.entity;

import guisanboot.data.MirrorGrp;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.TableRevealable;

public class InitAmsProgressRecord implements TableRevealable {
   public final static String STAT = "STAT";
   public final static String INIT_STATE = "stat";
   public final static String INIT_SPEED = "speed";
   public final static String INIT_LAUGH = "laugh";
   public final static String INIT_TIME = "time";
    
    public final static String STATE_init = "init";
    public final static String STATE_sync = "syn";
    public final static String STATE_async = "asyn";
    public final static String STATE_async2sync = "async2sync";
    public final static String STATE_unknown = "unknow";
    public final static String STATE_false = "false";
    public final static String STATE_timeout = "timeout";
    public final static String STATE_network = "network";
    public final static String STATE_notConnect = "notConnect";

    private String disk="";
    private String state="";
    private String percent="";
    private String speed="";
    private String remainTime="";
    private String elapsedTime="";
    private String finished="";

    private boolean isNotConnectErr = false;

    public InitAmsProgressRecord(){
    }

    public InitAmsProgressRecord( String disk ){
        this.disk = disk;
    }

    public InitAmsProgressRecord( String status,String disk ){
        this.state = status;
        this.disk = disk;
    }

    public InitAmsProgressRecord(
//        String disk,
        String state,
        String percent,
        String speed,
//        String remainTime,
        String elapsedTime
//        String finished
    ){
//        this.disk = disk;
        this.state = state;
        this.percent = percent;
        this.speed = speed;
//        this.remainTime = remainTime;
        this.elapsedTime = elapsedTime;
//        this.finished = finished;
    }

    /**
     * @return the disk
     */
    public String getDisk() {
        return disk;
    }

    /**
     * @param disk the disk to set
     */
    public void setDisk(String disk) {
        this.disk = disk;
    }

    /**
     * @return the percent
     */
    public String getPercent() {
        return percent;
    }

    /**
     * @param percent the percent to set
     */
    public void setPercent(String percent) {
        this.percent = percent;
    }

    /**
     * @return the speed
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * @return the remainTime
     */
    public String getRemainTime() {
        return remainTime;
    }

    public String getFormattedRemainTime(){
        int index = this.remainTime.indexOf("s");
        if( index > 0 ){
            try{
                float rTime = Float.parseFloat( this.remainTime.substring( 0,index ) );
                int intRTime =(int)rTime;
                //float rest = rTime-intRTime;
                Integer[] items = MirrorGrp.getDayOfIntervalTime1( intRTime );
                return this.getFormattedTime( items );
            }catch(Exception ex){
                ex.printStackTrace();
                return this.remainTime;
            }
        }else{
            return this.remainTime;
        }
    }

    /**
     * @param remainTime the remainTime to set
     */
    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    /**
     * @return the elapsedTime
     */
    public String getElapsedTime() {
        return elapsedTime;
    }

    public String getFormattedElapsedTime(){
        int index = this.elapsedTime.indexOf("s");
        if( index > 0 ){
            try{
                float eTime = Float.parseFloat( this.elapsedTime.substring( 0,index ) );
                int intETime = (int)eTime;
                //float rest = eTime-intETime;
                Integer[] items = MirrorGrp.getDayOfIntervalTime1( intETime );
                return this.getFormattedTime( items );
            }catch(Exception ex){
                ex.printStackTrace();
                return this.elapsedTime;
            }
        }else{
            return this.elapsedTime;
        }
    }
    
    /**
     * @param elapsedTime the elapsedTime to set
     */
    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * @return the finished
     */
    public String getFinished() {
        return finished;
    }

    /**
     * @param finished the finished to set
     */
    public void setFinished(String finished) {
        this.finished = finished;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    public boolean isInitState(){
        return state.toUpperCase().equals( STATE_async.toUpperCase() );
    }

    public boolean isSyncState(){
        return state.toUpperCase().equals( STATE_sync.toUpperCase() );
    }

    public boolean isUnknownState(){
        return state.toUpperCase().equals( STATE_unknown.toUpperCase() );
    }

    public boolean isFalse(){
        return state.toUpperCase().equals( STATE_false.toUpperCase() );
    }
    
    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    @Override public String toString(){
        return this.disk;
    }

    public void setIsNotConnectError( boolean val ){
        this.isNotConnectErr = val;
    }
    public boolean isNotConnectToClient(){
        return this.isNotConnectErr;
    }

    // TableRevealable的实现
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
        return ResourceCenter.ICON_ORPHAN_VOL;
    }
    public String toTableString(){
        return this.disk;
    }
    
    public static String getStateString( String stateStr ){
        if( stateStr.equals( STATE_init ) ){
            return SanBootView.res.getString("common.init");
        }else if( stateStr.equals( STATE_sync ) ){
            return SanBootView.res.getString("common.sync");
        }else if( stateStr.equals( STATE_async ) ){
            return SanBootView.res.getString("common.nsync");
        }else if( stateStr.equals( STATE_async2sync ) ){
            return SanBootView.res.getString("common.async2sync");
        }else if( stateStr.equals( STATE_notConnect ) ){
            return SanBootView.res.getString("common.conError");
        }else if( stateStr.equals( STATE_unknown ) ){
            return SanBootView.res.getString("common.unknown");
        }else if( stateStr.equals( STATE_timeout ) ){
            return SanBootView.res.getString("common.errmsg.timeout");
        }else if( stateStr.equals( STATE_network ) ){
            return SanBootView.res.getString("common.errmsg.netio");
        }else{
            return SanBootView.res.getString("common.errmsg.generalErr");
        }
    }

    private String getFormattedTime( Integer[] items  ){
        if( items[3].intValue() == 0 ){
            if( items[2].intValue() == 0 ){
                if( items[1].intValue() == 0 ){
                    return items[0] +" Sec.";
                }else{
                    return items[1].intValue()+" Min. " + items[0]  +" Sec.";
                }
            }else{
                return items[2].intValue()+" Hour "+items[1].intValue()+" Min. " + items[0]  +" Sec.";
            }
        }else{
            return items[3].intValue()+" Day " +items[2].intValue()+" Hour "+items[1].intValue()+" Min. " + items[0] +" Sec.";
        }
    }
}
