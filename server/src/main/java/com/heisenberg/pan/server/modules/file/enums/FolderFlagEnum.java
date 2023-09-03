package com.heisenberg.pan.server.modules.file.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 文件夹标识枚举类
 */
@Getter
@AllArgsConstructor
public enum FolderFlagEnum {
    NO(0),
    YES(1);
    private Integer code;
}
