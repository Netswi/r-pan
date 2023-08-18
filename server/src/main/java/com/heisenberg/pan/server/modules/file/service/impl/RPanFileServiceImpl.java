package com.heisenberg.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heisenberg.pan.server.modules.file.entity.RPanFile;
import com.heisenberg.pan.server.modules.file.service.RPanFileService;
import com.heisenberg.pan.server.modules.file.mapper.RPanFileMapper;
import org.springframework.stereotype.Service;

/**
* @author 2Bug
* @description 针对表【r_pan_file(物理文件信息表)】的数据库操作Service实现
* @createDate 2023-08-17 15:39:04
*/
@Service
public class RPanFileServiceImpl extends ServiceImpl<RPanFileMapper, RPanFile>
    implements RPanFileService{

}




