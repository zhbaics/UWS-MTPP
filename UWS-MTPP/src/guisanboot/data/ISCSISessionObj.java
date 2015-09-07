/*
 * ISCSISessionObj.java
 *
 * Created on 2008/7/7, AM�11:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author zjj
 */
public class ISCSISessionObj {
    private String initor;
    private String initor_ip;
    private String tgt_ip;
    private int tid;
    private String right;
    
    /** Creates a new instance of ISCSISessionObj */
    public ISCSISessionObj() {
    }

    public ISCSISessionObj(
        String initor,
        String initor_ip,
        String tgt_ip,
        int tid,
        String right
    ){
        this.initor = initor;
        this.initor_ip = initor_ip;
        this.tgt_ip = tgt_ip;
        this.tid = tid;
        this.right = right;
    }
    
    public String getInitor() {
        return initor;
    }

    public void setInitor(String initor) {
        this.initor = initor;
    }

    public String getInitor_ip() {
        return initor_ip;
    }

    public void setInitor_ip(String initor_ip) {
        this.initor_ip = initor_ip;
    }

    public String getTgt_ip() {
        return tgt_ip;
    }

    public void setTgt_ip(String tgt_ip) {
        this.tgt_ip = tgt_ip;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append(this.initor);
        buf.append(" "+this.initor_ip);
        buf.append(" "+this.tgt_ip);
        buf.append(" "+this.tid );
        buf.append(" "+this.right );
        return buf.toString();
    }
    
}
