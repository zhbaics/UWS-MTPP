package guisanboot.ams.entity;

import guisanboot.data.Volume;
import guisanboot.ui.SanBootView;

public class VolumeWrapper3 extends Volume{
    public VolumeWrapper3(){
    }
    public VolumeWrapper3(Volume volume){
    }
    @Override public String toString(){
        return SanBootView.res.getString("common.protected");
    }
}
