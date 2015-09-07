/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2012.4.12
 * Company:      odysys
 * @author      hwh
 * @version 1.0
 */
public class GetSnapSize extends NetworkRunning{

    private String size = "";

    /** Creates a new instance of GetSystemTime */
    public GetSnapSize( String cmd ){
        super( cmd );
    }

    @Override
    public void parser(String line) {
        if( line == null || line.equals("") )
            return;
        /**
         * line的格式:
         * 34.22 G bytes
         */
        SanBootView.log.debug(getClass().getName(),"========> "+ line );

        int index = line.indexOf("bytes");
        if(index >0 ){
            size = line.substring(0, index-1);
            //size = line.trim();
        }
    }

    public String getSize(){
        return size;
    }  

}
