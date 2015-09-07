/*
 * BindOfSrcAndDest.java
 *
 * Created on 2007/2/3,�PM�4:18
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
public class BindOfSrcAndDest {
    public String src      = "";
    public String dest     = "";
    public String os_delegate_letter = "";  // 当emboot网启时，源盘恢复时os盘的的代理盘（使用microsoft initiator连接）
    public long totoal     = -1; // 记录文剑的syscopy -c total的输出，即copy源的文件总数
    public String process  = "0";
    public String lvType   = "";
    public String srcUUID  = "";
    public String destUUID = "";
    
    public BindOfSrcAndDest( String _src,String _dest ){
        src = _src;
        dest = _dest;
    }

    public String getRealSrc(){
        if( this.os_delegate_letter.equals("") ){
            return this.src;
        }else{
            return os_delegate_letter;
        }
    }
}
