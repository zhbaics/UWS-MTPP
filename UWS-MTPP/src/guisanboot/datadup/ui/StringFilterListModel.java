/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.datadup.ui;

import java.util.ArrayList;

/**
 * StringFilterListModel.java
 *
 * Created on 2011-5-17, 17:50:00
 */
public class StringFilterListModel extends AbstractFilterListModel{
    private int filter_mode; // 0: include 1: exclude
    private ArrayList<String> filter_list = new ArrayList<String>();

    public StringFilterListModel( ArrayList<String> filter_list,int filter_mode ){
        super();
        
        this.filter_list = filter_list;
        this.filter_mode = filter_mode;
    }
    
    public boolean canAddInto( Object obj ){
        boolean founded = false;
        String strObj = (String)obj;
        int size = filter_list.size();
        for( int i=0; i<size; i++ ){
            String aFilter = filter_list.get(i);
            if( aFilter.substring(0,1).toUpperCase().equals( strObj.substring(1,2).toUpperCase() ) ){
                founded = true;
                break;
            }
        }

        if( founded ){
            if( filter_mode == 0 ){
                return true;
            }else{
                return false;
            }
        }else{
            if( filter_mode == 0 ){
                return false;
            }else{
                return true;
            }
        }
    }
}
