/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import guisanboot.data.NetworkRunning;
import java.io.IOException;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class GetMirrorRelation extends NetworkRunning {

    private MirrorRelation mirrorRelation;
    private SanBootView view;

    /** Creates a new instance of GetMirrorRelation */
    public GetMirrorRelation(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetMirrorRelation(String cmd) {
        super(cmd);
    }

    public MirrorRelation MirrorRelation() {
        return mirrorRelation;
    }

    @Override
    public void parser(String line) {
        if (mirrorRelation == null) {
            mirrorRelation = new MirrorRelation();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(MirrorRelation.LOCAL_UWS)) {
                int indexLocal = value.indexOf(":");
                String localUWSip = "";
                String localUWSport = "";
                if (index > 0) {
                    localUWSip = value.substring(0, indexLocal).trim();
                    localUWSport = value.substring(indexLocal + 1).trim();
                }
                mirrorRelation.addLocalUWS(localUWSip);
                mirrorRelation.addLocalUWSPort(localUWSport);
            } else if (s1.startsWith(MirrorRelation.MIRROR_UWS)) {
                int indexMirror = value.indexOf(":");
                String mirrorUWSip = "";
                String mirrorUWSport = "";
                if (index > 0) {
                    mirrorUWSip = value.substring(0, indexMirror).trim();
                    mirrorUWSport = value.substring(indexMirror + 1).trim();
                }
                mirrorRelation.addMirrorUWSIp(mirrorUWSip);
                mirrorRelation.addMirrorUWSPort(mirrorUWSport);
            }
        } else {
        }
    }
}
