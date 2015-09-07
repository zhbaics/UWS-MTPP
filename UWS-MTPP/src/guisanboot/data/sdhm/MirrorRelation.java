/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class MirrorRelation {

    public static final String LOCAL_UWS = "local_uws";
    public static final String MIRROR_UWS = "mirror_uws";
    private Vector localUWSIp = new Vector();
    private Vector localUWSPort = new Vector();
    private Vector mirrorUWSIp = new Vector();
    private Vector mirrorUWSPort = new Vector();

    /** Creates a new instance of MirrorRelation */
    public MirrorRelation() {
    }

    public Vector getLocalUWSIp() {
        return localUWSIp;
    }

    public void addLocalUWS(String _localuwsip) {
        localUWSIp.add(_localuwsip);
    }

    public Vector getLocalUWSPort() {
        return localUWSPort;
    }

    public void addLocalUWSPort(String _localuwsport) {
        localUWSPort.add(_localuwsport);
    }

    public Vector getMirrorUWSIp() {
        return mirrorUWSIp;
    }

    public void addMirrorUWSIp(String _mirroruwsip) {
        mirrorUWSIp.add(_mirroruwsip);
    }

    public Vector getMirrorUWSPort() {
        return mirrorUWSPort;
    }

    public void addMirrorUWSPort(String _mirroruwsip) {
        mirrorUWSPort.add(_mirroruwsip);
    }
}
