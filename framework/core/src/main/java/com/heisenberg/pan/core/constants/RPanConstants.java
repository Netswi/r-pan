package com.heisenberg.pan.core.constants;

import org.apache.commons.lang3.StringUtils;

/*
* RPan公用基础常量类
* */
public interface RPanConstants {

    /*
    * 公用的字符串常量分割符号
    * */
    String COMMON_SEPARATOR = "_,_";
    /*
    * 空字符串
    * */
    String EMPTY_STR = StringUtils.EMPTY;

    /**
     * 公用点字符
     */
    String POINT_STR = ".";
    /**
     * 公用斜杠字符
     */
    String SLASH_STR = "/";
    /**
     * 公用0L
     */
    Long ZERO_LNG = 0L;
    /**
     * 公用0数字
     */
    Integer ZERO_INT = 0;
    /**
     * 公用1数字
     */
    Integer ONE_INT = 1;
    /**
     * 公用2数字
     */
    Integer TWO_INT = 2;
    /**
     * 公用-1数字
     */
    Integer MINUS_ONE_INT = -1;
    /**
     * 公用true字符串
     */
    String TRUE_STR = "true";
    /**
     * 公用false字符串
     */
    String FALSE_STR = "false";
    /**
     * 组件扫描的基础路径
     */
    String BASE_COMPONENT_SCAN_PATH = "com.heisenberg.pan";
}
