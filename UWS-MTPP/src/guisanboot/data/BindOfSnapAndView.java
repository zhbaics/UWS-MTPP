/*
 * BindOfSnapandView.java
 *
 * Created on 2007/1/13,�AM 11:12
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
public class BindOfSnapAndView {
    // snap域可能为snapshot / volmap / mirrordiskinfo / view / CloneDisk,
    // 只有是snapshot/mirrordiskinfo时,下面的view才有意义（新建的）,否则为null
    public Object snap; 
    public View view; 
    
    /** Creates a new instance of BindOfVolMapandSnap */
    public BindOfSnapAndView() {
    }
}
