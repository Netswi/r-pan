package com.heisenberg.pan.server.modules.file.service;

import com.heisenberg.pan.server.modules.file.context.CreateFolderContext;
import com.heisenberg.pan.server.modules.file.entity.RPanUserFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 2Bug
* @description 针对表【r_pan_user_file(用户文件信息表)】的数据库操作Service
* @createDate 2023-08-17 15:39:04
*/
public interface IUserFileService extends IService<RPanUserFile> {
    //创建文件夹信息
    Long createFolder(CreateFolderContext createFolderContext);

    /**
     * 查询用户的根文件夹信息
     * @param userId
     * @return
     */
    RPanUserFile getUserRootFile(Long userId);
}
