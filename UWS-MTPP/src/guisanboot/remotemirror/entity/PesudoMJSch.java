/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.entity;

import guisanboot.ui.SanBootView;

/**
 * PesudoMJSch.java
 *
 * Created on 2010-9-9, 11:41:58
 */
public class PesudoMJSch extends MirrorJobSch{
    public PesudoMJSch(){
        super();
    }

    @Override public String toString(){
        return SanBootView.res.getString("common.noMjSch");
    }
}
