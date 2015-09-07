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
public class GetMirrorJobInfo extends NetworkRunning {

    private MirrorJobInfo mirrorJobInfo;
    private SanBootView view;

    /** Creates a new instance of GetMirrorJobInfo */
    public GetMirrorJobInfo(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetMirrorJobInfo(String cmd) {
        super(cmd);
    }

    public MirrorJobInfo MirrorJobInfo() {
        return mirrorJobInfo;
    }

    public void parser(String line) {
        if (mirrorJobInfo == null) {
            mirrorJobInfo = new MirrorJobInfo();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(MirrorJobInfo.MIRROR_JOB_SUM)) {
                mirrorJobInfo.addMirrorJobSum(value);
            } else if (s1.startsWith(MirrorJobInfo.MIRROR_JOB_ERR)) {
                mirrorJobInfo.addMirrorJobErr(value);
            } else if (s1.startsWith(MirrorJobInfo.MIRROR_JOB_DETAIL)) {
                int indexMirrorJob = value.indexOf(":");
                String mirrorJobTime = "";
                String mirrorJobResult = "";
                String mirrorJobVersion = "";
                if (index > 0) {
                    mirrorJobTime = value.substring(0, indexMirrorJob).trim();
                    String valueTemp = value.substring(indexMirrorJob + 1).trim();
                    int indexMirrorJob1 = valueTemp.indexOf(":");
                    if (index > 0) {
                        mirrorJobResult = valueTemp.substring(0, indexMirrorJob1).trim();
                        mirrorJobVersion = valueTemp.substring(indexMirrorJob1 + 1).trim();
                    }
                }
                mirrorJobInfo.addMirrorJobDetailTime(mirrorJobTime);
                mirrorJobInfo.addMirrorJobDetailResult(mirrorJobResult);
                mirrorJobInfo.addMirrorJobDetailVersion(mirrorJobVersion);
            } else if (s1.startsWith(MirrorJobInfo.MIRROR_JOB_READ)) {
                int indexMirrorJob = value.indexOf(":");
                String mirrorJobReadTime = "";
                String mirrorJobReadIO = "";
                if (index > 0) {
                    mirrorJobReadTime = value.substring(0, indexMirrorJob).trim();
                    mirrorJobReadIO = value.substring(indexMirrorJob + 1).trim();
                }
                mirrorJobInfo.addMirrorJobReadTime(mirrorJobReadTime);
                mirrorJobInfo.addMirrorJobReadIO(mirrorJobReadIO);
            } else if (s1.startsWith(MirrorJobInfo.MIRROR_JOB_WRITE)) {
                int indexMirrorJob = value.indexOf(":");
                String mirrorJobWriteTime = "";
                String mirrorJobWriteIO = "";
                if (index > 0) {
                    mirrorJobWriteTime = value.substring(0, indexMirrorJob).trim();
                    mirrorJobWriteIO = value.substring(indexMirrorJob + 1).trim();
                }
                mirrorJobInfo.addMirrorJobWriteTime(mirrorJobWriteTime);
                mirrorJobInfo.addMirrorJobWriteIO(mirrorJobWriteIO);
            }
        } else {
        }
    }
}
