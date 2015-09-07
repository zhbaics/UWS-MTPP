/*
 * Schedulerable.java
 *
 * Created on 2008/8/21,��PM�12:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.datadup.ui;

import guisanboot.datadup.data.DBSchedule;

/**
 *
 * @author zjj
 */
public interface Schedulerable {
    public boolean isSameScheduler( DBSchedule newsch );
}
