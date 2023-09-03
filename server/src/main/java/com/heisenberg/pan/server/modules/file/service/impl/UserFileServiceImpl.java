package com.heisenberg.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heisenberg.pan.core.constants.RPanConstants;
import com.heisenberg.pan.core.exception.RPanException;
import com.heisenberg.pan.core.utils.IdUtil;
import com.heisenberg.pan.server.modules.file.constants.FileConstants;
import com.heisenberg.pan.server.modules.file.context.CreateFolderContext;
import com.heisenberg.pan.server.modules.file.entity.RPanUserFile;
import com.heisenberg.pan.server.modules.file.enums.DelFlagEnum;
import com.heisenberg.pan.server.modules.file.enums.FolderFlagEnum;
import com.heisenberg.pan.server.modules.file.service.IUserFileService;
import com.heisenberg.pan.server.modules.file.mapper.RPanUserFileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 2Bug
* @description 针对表【r_pan_user_file(用户文件信息表)】的数据库操作Service实现
* @createDate 2023-08-17 15:39:04
*/
@Service(value = "userFileService")
public class UserFileServiceImpl extends ServiceImpl<RPanUserFileMapper, RPanUserFile>
    implements IUserFileService {

    @Override
    public Long createFolder(CreateFolderContext createFolderContext) {

        return saveUserFile(createFolderContext.getPartenId(),
                createFolderContext.getFolderName(),
                FolderFlagEnum.YES,
                null,
                null,
                createFolderContext.getUserId(),
                null);
    }

    /**
     * 查询用户的根文件夹信息
     * @param userId
     * @return
     */
    @Override
    public RPanUserFile getUserRootFile(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("parent_id",FileConstants.TOP_PARENT_ID);
        queryWrapper.eq("del_flag",DelFlagEnum.NO.getCode());
        queryWrapper.eq("folder_flag",DelFlagEnum.YES.getCode());
        return getOne(queryWrapper);
    }


    /**************************************private*******************************/
    /**
     * 保存用户文件的映射记录
     * @param parentId
     * @param fileName
     * @param folderFlagEnum
     * @param fileType
     * @param realFileId
     * @param userId
     * @param fileSizeDesc
     * @return
     */
    private Long saveUserFile(Long parentId, String fileName, FolderFlagEnum folderFlagEnum,Integer fileType,Long realFileId,Long userId,String fileSizeDesc){
        RPanUserFile entity = assembleRPanUserFile(parentId,userId,fileName,folderFlagEnum,fileType,realFileId,fileSizeDesc);
        if (!save(entity)){
            throw new RPanException("保存文件信息时报");
        }
        return entity.getFileId();
    }

    /**
     * 用户文件映射关系实体转化
     * 1.构建并填充实体信息
     * 2.处理文件命名一致问题
     *
     * @param parentId
     * @param userId
     * @param fileName
     * @param folderFlagEnum
     * @param fileType
     * @param realFileId
     * @param fileSizeDesc
     * @return
     */
    private RPanUserFile assembleRPanUserFile(Long parentId, Long userId, String fileName, FolderFlagEnum folderFlagEnum, Integer fileType, Long realFileId, String fileSizeDesc) {

        RPanUserFile entity = new RPanUserFile();
        entity.setFileId(IdUtil.get());
        entity.setUserId(userId);
        entity.setParentId(parentId);
        entity.setRealFileId(realFileId);
        entity.setFilename(fileName);
        entity.setFolderFlag(folderFlagEnum.getCode());
        entity.setFileSizeDesc(fileSizeDesc);
        entity.setFileType(fileType);
        entity.setDelFlag(DelFlagEnum.NO.getCode());
        entity.setCreateUser(userId);
        entity.setCreateTime(new Date());
        entity.setUpdateUser(userId);
        entity.setUpdateTime(new Date());

        handleDuplicateFilName(entity);
        return entity;



    }

    /**
     * 处理用户重复名称
     * 如果同一文件夹下有重复名称
     * 按照系统级规则重命名文件
     * @param entity
     */
    private void handleDuplicateFilName(RPanUserFile entity) {
        String filename = entity.getFilename(),
                newFileNameWithoutSuffix,
                newFileNameSuffix;
        int newFileNamePointPosition = filename.lastIndexOf(RPanConstants.POINT_STR);//找点位置 如 xxx.java文件
        if (newFileNamePointPosition == RPanConstants.MINUS_ONE_INT){
             newFileNameWithoutSuffix = filename;
             newFileNameSuffix = StringUtils.EMPTY;
        }else {
            newFileNameWithoutSuffix = filename.substring(RPanConstants.ZERO_INT,newFileNamePointPosition);
            newFileNameSuffix = filename.replace(newFileNameWithoutSuffix,StringUtils.EMPTY);
        }

       int count = getDuplicateFileName(entity,newFileNameWithoutSuffix);

        if (count == 0){
            //无同名文件
            return;
        }
        String newFileName = assembleNewFileName(newFileNameWithoutSuffix,count,newFileNameSuffix);
        entity.setFilename(newFileName);

    }

    /**
     * 拼装新文件名称
     *
     * @param newFileNameWithoutSuffix
     * @param count
     * @param newFileNameSuffix
     * @return
     */
    private String assembleNewFileName(String newFileNameWithoutSuffix, int count, String newFileNameSuffix) {

        String newFileName = new StringBuilder(newFileNameWithoutSuffix)
                .append(FileConstants.CN_LEFT_PARENTHESES_STR)
                .append(count)
                .append(FileConstants.CN_RIGHT_PARENTHESES_STR)
                .append(newFileNameSuffix)
                .toString();



        return newFileName;

    }

    /**
     * 查找同一文件夹下同名数量
     * @param entity
     * @param newFileNameWithoutSuffix
     * @return
     */
    private int getDuplicateFileName(RPanUserFile entity, String newFileNameWithoutSuffix) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",entity.getParentId());
        queryWrapper.eq("folder_flag",entity.getFolderFlag());
        queryWrapper.eq("user_id",entity.getUserId());
        queryWrapper.eq("del_flag",DelFlagEnum.NO.getCode());
        queryWrapper.likeLeft("filename",newFileNameWithoutSuffix);

        return count(queryWrapper);


    }
}




