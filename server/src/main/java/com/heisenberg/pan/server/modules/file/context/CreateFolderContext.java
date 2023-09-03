package com.heisenberg.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建文件夹上下文实体
 */
@Data
public class CreateFolderContext implements Serializable {

    private static final long serialVersionUID = 6462873631292368160L;
    /**
     * 父文件夹id
     */
    private Long partenId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 文件夹名称
     */
    private String folderName;


}
