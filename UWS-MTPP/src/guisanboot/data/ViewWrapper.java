/*
 * ViewWrapper.java
 *
 * Created on 2007/1/13, AM 11:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;

/**
 *
 * @author Administrator
 */
public class ViewWrapper {
    public View view;
    
    @Override public String toString(){
        if( view.getSnap_name().equals("") ){
            return "         "+SanBootView.res.getString("common.view") + " : " + view.getCreateTimeStr();
        }else{
            return "         "+SanBootView.res.getString("common.view") + " : " + 
                    view.getCreateTimeStr()+
                    " ["+ view.getSnap_target_id() +":"+ view.getSnap_name()+"]";
        }
    }
    
    /** Creates a new instance of ViewWrapper */
    public ViewWrapper( View view ) {
        this.view = view;
    }
}
