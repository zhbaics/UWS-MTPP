/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.entity;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;
import java.util.regex.*;

/**
 * MirrorJobSch.java
 *
 * Created on 2010-9-7, 16:35:31
 */
public class MirrorJobSch extends BasicUIObject{
    public final static String MJ_SCHE_scheduler_id = "scheduler_id";
    public final static String MJ_SCHE_scheduler_type = "scheduler_type";
    public final static String MJ_SCHE_scheduler_mon = "scheduler_mon";
    public final static String MJ_SCHE_scheduler_tue = "scheduler_tue";
    public final static String MJ_SCHE_scheduler_wed = "scheduler_wed";
    public final static String MJ_SCHE_scheduler_thu = "scheduler_thu";
    public final static String MJ_SCHE_scheduler_fri = "scheduler_fri";
    public final static String MJ_SCHE_scheduler_sat = "scheduler_sat";
    public final static String MJ_SCHE_scheduler_sun = "scheduler_sun";

    private int scheduler_id = -1;
    private int scheduler_type  = 1;
    private String scheduler_mon="";
    private String scheduler_tue="";
    private String scheduler_wed="";
    private String scheduler_thu="";
    private String scheduler_fri="";
    private String scheduler_sat="";
    private String scheduler_sun="";

    public MirrorJobSch(){
        super( ResourceCenter.TYPE_MJ_SCHEDULER );
    }

    public MirrorJobSch( String mon_time,String tue_time,String wed_time,String thu_time,
        String fri_time,String sat_time,String sun_time
    ){
        this();
        this.scheduler_mon = mon_time;
        this.scheduler_tue = tue_time;
        this.scheduler_wed = wed_time;
        this.scheduler_thu = thu_time;
        this.scheduler_fri = fri_time;
        this.scheduler_sat = sat_time;
        this.scheduler_sun = sun_time;
    }

    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }

    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 2 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_MJ_SCH ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_MJ_SCH ) );
        }
    }

    /**
     * @return the scheduler_id
     */
    public int getScheduler_id() {
        return scheduler_id;
    }

    /**
     * @return the scheduler_type
     */
    public int getScheduler_type() {
        return scheduler_type;
    }

    /**
     * @return the scheduler_mon
     */
    public String getScheduler_mon() {
        return scheduler_mon;
    }

    /**
     * @return the scheduler_tue
     */
    public String getScheduler_tue() {
        return scheduler_tue;
    }

    /**
     * @return the scheduler_wed
     */
    public String getScheduler_wed() {
        return scheduler_wed;
    }

    /**
     * @return the scheduler_thu
     */
    public String getScheduler_thu() {
        return scheduler_thu;
    }

    /**
     * @return the scheduler_fri
     */
    public String getScheduler_fri() {
        return scheduler_fri;
    }

    /**
     * @return the scheduler_sat
     */
    public String getScheduler_sat() {
        return scheduler_sat;
    }

    /**
     * @return the scheduler_sun
     */
    public String getScheduler_sun() {
        return scheduler_sun;
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
        return ResourceCenter.SMALL_SCH;
    }
    public String toTableString(){
        return this.scheduler_id+"";
    }

    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.SMALL_SCH;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.SMALL_SCH;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.scheduler_id+"";
    }

    public String toTipString(){
        return toTableString();
    }

    /**
     * @param scheduler_id the scheduler_id to set
     */
    public void setScheduler_id(int scheduler_id) {
        this.scheduler_id = scheduler_id;
    }

    /**
     * @param scheduler_type the scheduler_type to set
     */
    public void setScheduler_type(int scheduler_type) {
        this.scheduler_type = scheduler_type;
    }

    /**
     * @param scheduler_mon the scheduler_mon to set
     */
    public void setScheduler_mon(String scheduler_mon) {
        this.scheduler_mon = scheduler_mon;
    }

    /**
     * @param scheduler_tue the scheduler_tue to set
     */
    public void setScheduler_tue(String scheduler_tue) {
        this.scheduler_tue = scheduler_tue;
    }

    /**
     * @param scheduler_wed the scheduler_wed to set
     */
    public void setScheduler_wed(String scheduler_wed) {
        this.scheduler_wed = scheduler_wed;
    }

    /**
     * @param scheduler_thu the scheduler_thu to set
     */
    public void setScheduler_thu(String scheduler_thu) {
        this.scheduler_thu = scheduler_thu;
    }

    /**
     * @param scheduler_fri the scheduler_fri to set
     */
    public void setScheduler_fri(String scheduler_fri) {
        this.scheduler_fri = scheduler_fri;
    }

    /**
     * @param scheduler_sat the scheduler_sat to set
     */
    public void setScheduler_sat(String scheduler_sat) {
        this.scheduler_sat = scheduler_sat;
    }

    /**
     * @param scheduler_sun the scheduler_sun to set
     */
    public void setScheduler_sun(String scheduler_sun) {
        this.scheduler_sun = scheduler_sun;
    }
    
    public String[] getSelectedClock( String timeStr ){
        String[] lines = Pattern.compile(",").split( timeStr );
        return lines;
    }

    public String getSmpleStr(  String timeStr ){
        int i,size,begin,step,tmp;
        boolean isFirst = true;
        
        String[] clocks = this.getSelectedClock( timeStr );
        ArrayList<Integer> intClocks = new ArrayList<Integer>();
        for( i=0; i<clocks.length; i++ ){
            try{
                int val = Integer.parseInt( clocks[i] );
                intClocks.add( new Integer( val ) );
            }catch(Exception ex){}
        }
        
        size = intClocks.size();
        Integer[] ck = new Integer[ size ];
        for( i=0; i<size; i++ ){
            ck[i] = intClocks.get(i);
        }
        if( ck.length == 0 ) return SanBootView.res.getString("common.notime");
        Arrays.sort( ck );
        
        StringBuffer buf = new StringBuffer();
        begin = ck[0].intValue();
        step  = begin;
        for( i=1; i<ck.length; i++ ){
//System.out.println("==============> : " + ck[i] );
            tmp = ck[i].intValue();
            if( tmp - step > 1 ){
                if( begin - step == 0 ){
                    if( isFirst ){
                        buf.append( begin );
                        isFirst = false;
                    }else{
                        buf.append( ","+begin );
                    }
                }else{
                    if( isFirst ){
                        buf.append( begin+"-"+step );
                        isFirst = false;
                    }else{
                        buf.append( ","+begin+"-"+step );
                    }
                }
                begin = tmp;
                step = begin;
            }else{
                step = tmp;
            }
        }
        
        if( begin-step == 0 ){
            if( isFirst ){
                buf.append( begin );
                isFirst = false;
            }else{
                buf.append( ","+begin );
            }
        }else{
            if( isFirst ){
                buf.append( begin+"-"+step);
                isFirst = false;
            }else{
                buf.append(","+begin+"-"+step);
            }
        }
        
        return buf.toString();
    }

    @Override public String toString(){
        return this.scheduler_id+"";
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("mon: " +this.scheduler_mon );
        buf.append("\ntue: " + this.scheduler_tue );
        buf.append("\nwed: " + this.scheduler_wed );
        buf.append("\nthu: " + this.scheduler_thu );
        buf.append("\nfri: " + this.scheduler_fri );
        buf.append("\nsat: " + this.scheduler_sat );
        buf.append("\nsun: " + this.scheduler_sun );
        return buf.toString();
    }
}
