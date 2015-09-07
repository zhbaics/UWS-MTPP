/*
 * LicObj.java
 *
 * Created on 2008/3/25,�PM 6:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class LicObj {
    public final static String LIC_POINT = "point";
    public final static String LIC_DATE  = "date";
    public final static String LIC_CAP   = "capacity";
    public final static String LIC_SNAP  = "snapshots";

    public final static String LIC_MTPP = "MTPP";
    public final static String LIC_CMDP = "CMDP";
    public final static String LIC_DEFAULT = "DEFAULT";

    private String product = "";
    private int point=0; // 50;
    private String date=""; // 99991231
    private long cap=0L; // 3000000 MB
    private int snapNum=0; // 2 
    
    /** Creates a new instance of LicObj */
    public LicObj() {
    }

    public LicObj(
            String product,
            int point,
            String date,
            long cap,
            int snapNum
    ){
        this.product = product;
        this.point = point;
        this.date = date;
        this.cap = cap;
        this.snapNum = snapNum;
    }

    public String getProduct(){
        return product;
    }

    public void setProduct( String val ){
        this.product = val;
    }
    
    public boolean isMTPPLic(){
        return product.toUpperCase().equals( LicObj.LIC_MTPP );
    }
    public boolean isCMDPLic(){
        return product.toUpperCase().equals( LicObj.LIC_CMDP );
    }
    public boolean isDefaultLic(){
        return product.toUpperCase().equals( LicObj.LIC_DEFAULT );
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getCap() {
        return cap;
    }

    public void setCap(long cap) {
        this.cap = cap;
    }

    public int getSnapNum() {
        return snapNum;
    }

    public void setSnapNum(int snapNum) {
        this.snapNum = snapNum;
    }

    public void prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append(" product : "+this.product );
        buf.append(" point : "+this.point );
        buf.append(" maxsnap : "+this.snapNum );
        buf.append(" cap : "+this.cap);
        System.out.println("=========\n"+buf.toString() );
    }
}
