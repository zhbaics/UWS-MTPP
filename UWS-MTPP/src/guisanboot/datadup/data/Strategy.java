package guisanboot.datadup.data;

/**
 * Title:        Odysys UWS
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author zjj
 * @version 1.0
 */

import guisanboot.ui.SanBootView;

public class Strategy {
    public final static int ONLY_ONE = 0;
    public final static int PERIOD   = 1;
    public final static int UNKNOWN  = 9;
    
    public final static int PERIOD_MONTH    = 0;
    public final static int PERIOD_WEEK     = 1;
    public final static int PERIOD_DAY      = 2;
    public final static int PERIOD_ONLYONE  = 3;
    public final static int PERIOD_UNKNOWN  = 9;
    
    public final static String PERIOD_MONTH_STR   = "Strategy.period.month";
    public final static String PERIOD_WEEK_STR    = "Strategy.period.week";
    public final static String PERIOD_DAY_STR     = "Strategy.period.day";
    public final static String PERIOD_ONLYONE_STR = "Strategy.period.onlyOne";
    public final static String PERIOD_UNKNOWN_STR = "Strategy.period.unknown";
    
    public final static int LEVEL_BAK_FULL   = 0;
    public final static int LEVEL_BAK_OneInc = 1;
    public final static int LEVEL_BAK_TwoInc = 2;
    public final static int LEVEL_BAK_None   = 9;
    
    public final static String BAKTYPE_SELECTIVE = "Strategy.bakType.selective";
    public final static String BAKTYPE_MASTER = "Strategy.bakType.master";
    public final static String BAKTYPE_INCREMENTAL = "Strategy.bakType.incremental";
    
    public final static String BAKTYPE_NONE = "Strategy.bakType.none";
    public final static String BAKTYPE_FULL = "Strategy.bakType.full";
    public final static String BAKTYPE_INCREMENTAL_ONE = "Strategy.bakType.incremental1";
    public final static String BAKTYPE_INCREMENTAL_TWO = "Strategy.bakType.incremental2";
    
    public final static String STRATEGY_ONLYONE = "Strategy.strategyType.onlyone";
    public final static String STRATEGY_FREQUENCY = "Strategy.strategyType.frequency";
    public final static String STRATEGY_UNKNOWN = "Strategy.strategyType.unknown";
    
    public Strategy(){    
    }
    
    public static int getStrategyTypeInt(String type){
        if( type.equals(SanBootView.res.getString( STRATEGY_ONLYONE))){
            return ONLY_ONE;
        }else if( type.equals( SanBootView.res.getString( STRATEGY_FREQUENCY))){
            return PERIOD;
        }else {
            return UNKNOWN;
        }
    }
    
    public static String getStrategyTypeStr(int type){
        String str;
        switch( type ){
            case ONLY_ONE:
                str = SanBootView.res.getString( STRATEGY_ONLYONE );
                break;
          case PERIOD:
                str = SanBootView.res.getString(STRATEGY_FREQUENCY);
                break;
          default:
                str = SanBootView.res.getString(STRATEGY_UNKNOWN);
                break;
        }
        
        return str;
    }
    
    public static String getPeriodString(int _period){
        String str;
        switch( _period ){
            case PERIOD_DAY:
                str = SanBootView.res.getString(PERIOD_DAY_STR);
                break;
          case PERIOD_MONTH:
                str = SanBootView.res.getString(PERIOD_MONTH_STR);
                break;
          case PERIOD_WEEK:
                str = SanBootView.res.getString(PERIOD_WEEK_STR);
                break;
          case PERIOD_ONLYONE:
                str = SanBootView.res.getString(PERIOD_ONLYONE_STR);
                break;
          default:
                str = SanBootView.res.getString(PERIOD_UNKNOWN_STR);
                break;
        }
        
        return str;
    }
    
    public static int getBakLevelInt( String level ){
        if( level.equals( SanBootView.res.getString(BAKTYPE_FULL))){
            return LEVEL_BAK_FULL;
        }else if( level.equals( SanBootView.res.getString(BAKTYPE_INCREMENTAL_ONE))){
            return LEVEL_BAK_OneInc;
        }else if(level.equals( SanBootView.res.getString(BAKTYPE_INCREMENTAL_TWO))){
            return LEVEL_BAK_TwoInc;
        }else {
            return LEVEL_BAK_None;
        }
    }
    
    public static String getBakTypeStr(int type){
        String str;
        switch( type ){
            case LEVEL_BAK_FULL:
                str = SanBootView.res.getString(BAKTYPE_FULL);
                break;
          case LEVEL_BAK_OneInc:
                str = SanBootView.res.getString(BAKTYPE_INCREMENTAL_ONE);
                break;
          case LEVEL_BAK_TwoInc:
                str = SanBootView.res.getString(BAKTYPE_INCREMENTAL_TWO);
                break;
          default:
                str = SanBootView.res.getString(BAKTYPE_NONE);
                break;
        }
        return str;
    }
}
