/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class GetIdleDisk extends NetworkRunning {

    private ArrayList<IdleDisk> diskList = new ArrayList<IdleDisk>() ;
    private IdleDisk curIdleDisk = null ;
    private boolean hasIdleDisk = false ;
    
    
    
    
    @Override
    public void parser(String line) {
        String s1 = line.trim();

        int index = s1.indexOf("=");
        SanBootView.log.debug(getClass().getName(),"========> "+ s1 );             
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            
            if( s1.startsWith( IdleDisk.IDLE_DISK_NAME )){
                curIdleDisk.setIdleDiskName(value);
            }else if( s1.startsWith( IdleDisk.IDLE_DISK_SIZE )){
                curIdleDisk.setIdleDiskSize(value);
                diskList.add(curIdleDisk);
            } else if( s1.startsWith(IdleDisk.IDLE_DISK_COUNT)){
                try{
                    int count = Integer.parseInt(value);
                    if( count > 0 ){
                        this.hasIdleDisk = true;
                    }
                }catch( Exception e){
                    this.hasIdleDisk = false ;
                }
            }
        }else{
            if( s1.startsWith( IdleDisk.IDLE_DISK_RECFLAG ) ){
                curIdleDisk = new IdleDisk();
            }
        }
    }
    
    public GetIdleDisk( String cmd ,Socket _socket) throws IOException{
        super(cmd , _socket);
    }
    
    public GetIdleDisk( String cmd ){
        super(cmd);
    }
  
    public void removeAll(){
        this.diskList.clear();
        this.curIdleDisk = null ;
        this.hasIdleDisk = false ;
    }
    
    public ArrayList<IdleDisk> getAllIdleDisk(){
        return this.diskList;
    }

    public IdleDisk getCurIdleDisk() {
        return curIdleDisk;
    }

    public boolean isHasIdleDisk() {
        return hasIdleDisk;
    }
    
    
}
