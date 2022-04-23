package com.yuchao.managementsystem.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.entity.Files;
import com.yuchao.managementsystem.entity.User;
import com.yuchao.managementsystem.mapper.FileMapper;
import com.yuchao.managementsystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author 蒙宇潮
 * @create 2022-04-22  20:26
 * <p>
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
//    @Autowired 时spring的注解，因为mapper没有显示的注入bean
    private FileMapper fileMapper;

    /**
     * MultipartFile 自动封装上传过来的文件
     * maximum permitted size of 1048576 bytes
     *
     * @return
     * @date 2022/4/22 20:39
     */
    @PostMapping("upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String type = FileUtil.extName(originalFilename);//后缀
            long size = file.getSize();
            // 定义一个文件唯一的标识码
            String uuid = IdUtil.fastSimpleUUID();
            String fileUUID = uuid + StrUtil.DOT + type;

            File uploadFile = new File(fileUploadPath + fileUUID);
            // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
            File parentFile = uploadFile.getParentFile();
            if(!parentFile.exists()) {
                parentFile.mkdirs();
            }

            String url;
            // 从上传过来的，获取文件的md5，之与内容有关
            String md5 = SecureUtil.md5(file.getInputStream());
            // 从数据库查询是否存在相同的记录
            Files dbFiles = getFileByMd5(md5);
            if (dbFiles != null) {// 文件已存在
                url = dbFiles.getUrl();
            } else {
                //磁盘只存一份
                file.transferTo(uploadFile);//把上传的file存到磁盘，file1
                url = "http://localhost:9090/file/" + fileUUID;
            }

            //存到数据库，数据库的记录文件相同，名字不一样
            Files saveFile = new Files();
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size / 1024);//kb
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.insert(saveFile);
            return url;
        }
        return "";
    }


    /**
     * 文件下载接口 http://localhost:9090/file/{fileUuid}
     *
     * @param fileUuid
     * @param response
     * @author yuchao
     * @date 2022/4/22 22:29
     */
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUuid);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUuid, "UTF-8"));
        response.setContentType("application/octet-stream");
        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     *
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    /**
     * 分页查询
     * @param
     * @return
     * @date 2022/4/23 20:38
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name
                           ) {
        QueryWrapper<Files> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("is_delete", false);//就是0
        if (!"".equals(name)) {
            fileQueryWrapper.like("name", name);
        }

        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), fileQueryWrapper));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    /**
     * 是否启用
     * @param
     * @return
     * @date 2022/4/23 21:19
     */
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }

}
