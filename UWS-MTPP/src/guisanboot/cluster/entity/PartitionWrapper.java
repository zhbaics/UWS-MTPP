/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cluster.entity;

import guisanboot.data.SystemPartitionForWin;

/**
 * PartitionWrapper.java
 *
 * Created on 2011-8-9, 10:54:49
 */
public class PartitionWrapper {
    private SystemPartitionForWin part;
    public PartitionWrapper(){
    }
    public PartitionWrapper( SystemPartitionForWin part ){
        this.part = part;
    }
    @Override public String toString(){
        return part.disklabel+"  [ "+part.uuid+" ]";
    }
    public void setPart( SystemPartitionForWin part ){
        this.part = part;
    }
    public SystemPartitionForWin getPart(){
        return part;
    }
}
