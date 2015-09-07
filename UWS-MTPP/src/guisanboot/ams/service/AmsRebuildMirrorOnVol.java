/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ams.service;

import guisanboot.data.NetworkRunning;
import guisanboot.exception.BadMagicException;
import guisanboot.exception.BadPackageLenException;
import guisanboot.exception.BadVersionException;
import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class AmsRebuildMirrorOnVol extends NetworkRunning{

    private int mirrorId = -1;

    public AmsRebuildMirrorOnVol(String _cmdLine, Socket socket) throws IOException {
        super(_cmdLine, socket);
    }

    public AmsRebuildMirrorOnVol(Socket socket) throws IOException {
        super(socket);
    }

    public AmsRebuildMirrorOnVol(String _cmdLine) {
        super(_cmdLine);
    }

    public AmsRebuildMirrorOnVol(String _cmdLine, String encoding) {
        super(_cmdLine, encoding);
    }

    public AmsRebuildMirrorOnVol() {
    }

    @Override
    public void parser(String line) {
        String s1 = line.trim();
SanBootView.log.debug( getClass().getName(), "=====> "+ s1 );
        try{
            mirrorId = Integer.parseInt( s1 );
        }catch(Exception ex){}
    }

    @Override
    public String getCmdLine() {
        return super.getCmdLine();
    }

    @Override
    public void run() throws IOException, BadMagicException, BadPackageLenException, BadVersionException {
        super.run();
    }

    @Override
    public void setCmdLine(String _cmdLine, Socket _socket) throws IOException {
        super.setCmdLine(_cmdLine, _socket);
    }

    @Override
    public void setCmdLine(String _cmdLine) {
        super.setCmdLine(_cmdLine);
    }

}
