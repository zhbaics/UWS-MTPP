/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class GetUcsDiskCountByView extends NetworkRunning{

    public static final String COUNT="count";
    
    private int count=0;
    
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );         
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            if(s1.startsWith(GetUcsDiskCountByView.COUNT)){
                try{
                    this.count = Integer.parseInt(value);
                }catch( Exception e){
                    count=0;
                }
            }
        }
    }
    
    public GetUcsDiskCountByView( String cmd ,Socket _socket) throws IOException{
        super(cmd , _socket);
    }
    
    public GetUcsDiskCountByView( String cmd ){
        super(cmd);
    }

    public int getCount() {
        return count;
    }
    
    
    
}
