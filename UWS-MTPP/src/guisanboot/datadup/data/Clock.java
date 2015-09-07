/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.datadup.data;

/**
 * Clock.java
 *
 * Created on 2011-7-27, 17:36:13
 */
public class Clock{
    private String hour;
    private String min;

    public Clock(){
    }

    public Clock( String hour,String min ){
        this.hour = hour;
        this.min = min;
    }

    /**
     * @return the hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * @param hour the hour to set
     */
    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     * @return the min
     */
    public String getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(String min) {
        this.min = min;
    }

    public boolean isSame( Clock cl ){
        return this.getHour().equals( cl.getHour() ) && this.getMin().equals( cl.getMin() );
    }

    @Override public String toString(){
        return this.hour+":"+this.min;
    }
}
