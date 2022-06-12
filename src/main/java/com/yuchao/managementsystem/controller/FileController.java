package com.yuchao.managementsystem.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.common.Constants;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.controller.dto.uploadDTO;
import com.yuchao.managementsystem.entity.Files;
import com.yuchao.managementsystem.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import static cn.hutool.json.JSONUtil.toBean;

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

    @Value("${server.ip}")
    private String serverIp;

    @Resource
//    @Autowired 时spring的注解，因为mapper没有显示的注入bean
    private FileMapper fileMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


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
            if (!parentFile.exists()) {
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
                url = "http://"+serverIp+":9090/file/" + fileUUID;

            }

            //存到数据库，数据库的记录文件相同，名字不一样
            Files saveFile = new Files();
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size / 1024);//kb
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.insert(saveFile);

            //方式1从redis获取数据
            String json = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
            List<Files> files1 = toBean(json, new TypeReference<List<Files>>() {
            }, true);
            if (files1 != null) {
                files1.add(saveFile);//加入新上传的到redis
                setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files1));//重新设置一下缓存
            }

            //方式2数据库查
//            List<Files> files = fileMapper.selectList(null);
//            //设置最新缓存
//            setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files));
//            方式3清空缓存
//            flushRedis(Constants.FILES_KEY);
            return url;
        }
        return "";
    }

    /**
     * 远程上传
     *
     * @param file
     * @return java.lang.String
     * @author yuchao
     * @date 2022/5/27 17:37
     */
    @PostMapping("uploadFile")
    public String uploadFile(@RequestParam MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String type = FileUtil.extName(originalFilename);//后缀
            long size = file.getSize();
            // 定义一个文件唯一的标识码
            String uuid = IdUtil.fastSimpleUUID();
            String fileUUID = uuid + StrUtil.DOT + type;

            String url = null;
            // 从上传过来的，获取文件的md5，之与内容有关
            String md5 = SecureUtil.md5(file.getInputStream());
            // 从数据库查询是否存在相同的记录
            Files dbFiles = getFileByMd5(md5);
            if (dbFiles != null) {// 文件已存在
                url = dbFiles.getUrl();
            } else {
                //上传到阿里云盘
                String uploadUrl = "http://120.25.172.243:5244/api/public/upload";
                File tempFile = File.createTempFile("aaa",StrUtil.DOT + type);
                file.transferTo(tempFile);
                tempFile = FileUtil.rename(tempFile, uuid, true, true);
                System.out.println(tempFile.getName());
                HashMap<String, Object> parm = new HashMap<>();
                parm.put("files", tempFile);
                parm.put("path", "/files");
                String res = HttpRequest.post(uploadUrl)
                        .header("Content-Type", "multipart/form-data")
                        .header("token", "fc52f5c56800ab6e62a5711581c26e64")
                        .form(parm)
                        .timeout(10*1000)
                        .execute().body();

                System.out.println(res);
                uploadDTO uploadDTO = toBean(res, uploadDTO.class);
                System.out.println(uploadDTO.getCode());
                if (uploadDTO.getCode().equals("200") ) {
                    url = "http://120.25.172.243:5244/d/files/" + fileUUID;
                }
                //删除临时文件
                deleteFile(tempFile);
            }
            //存到数据库，数据库的记录文件相同，名字不一样
            Files saveFile = new Files();
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size / 1024);//kb
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.insert(saveFile);

            //方式1从redis获取数据
            String json = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
            List<Files> files1 = JSONUtil.toBean(json, new TypeReference<List<Files>>() {
            }, true);
            if (files1 != null) {
                files1.add(saveFile);//加入新上传的到redis
                setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files1));//重新设置一下缓存
            }
            return url;
        }
        return "";
    }
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
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
     *
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
        fileQueryWrapper.eq("is_delete", false);//就是0,假删除
        if (!"".equals(name)) {
            fileQueryWrapper.like("name", name);
        }

        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), fileQueryWrapper));
    }

    @DeleteMapping("/{id}")
//    @CacheEvict(value = "files",key = "'frontAll'")//清空缓存,会重新请求数据
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        flushRedis(Constants.FILES_KEY);
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
     *
     * @param
     * @return
     * @date 2022/4/23 21:19
     */
    @PostMapping("/update")
//    @CachePut(value = "files",key = "'frontAll'")//更新缓存
    public Result update(@RequestBody Files files) {
        fileMapper.updateById(files);
//        List<Files> list = fileMapper.selectList(null);
//        return Result.success(list);
        flushRedis(Constants.FILES_KEY);
        return Result.success();
    }

    //删除缓存
    private void flushRedis(String key) {
        stringRedisTemplate.delete(key);
    }

    //设置缓存
    private void setCache(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
}
