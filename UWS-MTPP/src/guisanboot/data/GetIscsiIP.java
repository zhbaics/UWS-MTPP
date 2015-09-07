/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2012.4.19
 * Company:      odysys
 * @author      hwh
 * @version 1.0
 */
public class GetIscsiIP extends NetworkRunning{

    private String ip = "";

    /** Creates a new instance of GetSystemTime */
    public GetIscsiIP( String cmd ){
        super( cmd );
    }

    @Override
    public void parser(String line) {
        line = line.trim();
        if( line == null || line.equals("") )
            return;
        /**
         * line的格式:
         * count=1
         * ----index 0---
         * ip=40.40.1.249
         * port=3260
         * 
         */
        SanBootView.log.debug(getClass().getName(),"========> "+ line );

        if( line.startsWith("ip")){
            int index = line.indexOf("=");
            if( index>0){
                ip = line.substring(index+1).trim();
            }
        }
    }

    public String getIP(){
        return ip;
    }  

}
