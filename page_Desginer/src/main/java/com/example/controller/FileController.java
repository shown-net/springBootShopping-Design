package com.example.controller;

import com.example.anno.OperationLogRecord;
import com.example.anno.RoleCheck;
import com.example.pojo.Result;
import com.example.pojo.SalesMan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@CrossOrigin
public class FileController {
    private final String baseUrl = System.getProperty("user.dir") + "/images/";

    // 获取商品图片
    @GetMapping("/images/download/{imageSrc}")
    public byte[] getImage(@PathVariable String imageSrc) throws IOException {
        return Files.readAllBytes(Path.of(baseUrl + imageSrc));
    }

    // 获取走马灯商品图片的URL地址(文件名）
    @GetMapping("/images/getCarouselRes")
    public Result getCarouselImageUrl() throws IOException {
        List<String> UrlList = new ArrayList<>();
        File directory = new File(baseUrl + "carousel");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    UrlList.add(file.getName());
                }
            }
        }
        return Result.success(UrlList);
    }

    // 获取走马灯商品图片
    @GetMapping("/images/download/carousel/{imageSrc}")
    public byte[] getCarouseImage(@PathVariable String imageSrc) throws IOException {
        return Files.readAllBytes(Path.of(baseUrl + "carousel/" + imageSrc));
    }

    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("上传商品图片")
    @PostMapping("/images/upload")
    public Result uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            // 如果文件为空，返回错误信息
            return Result.error("上传的文件格式有误");
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            fileExtension = originalFilename.substring(dotIndex + 1);
        }
        String fileName = UUID.randomUUID().toString();
        if (!fileExtension.isEmpty()) {
            // 如果原文件名有扩展名，则添加到UUID后面
            fileName += "." + fileExtension;
        }
        final Path path = Path.of(baseUrl);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        File saveFile = new File(baseUrl + fileName);
        file.transferTo(saveFile);//存储文件到本地磁盘
        return Result.success(fileName);
    }

    @RoleCheck(SalesMan.roleValue)

    @PostMapping("/images/delete")
    public Result deleteImage(@RequestBody String FilePath) throws IOException {
        final Path path = Path.of(baseUrl + FilePath.replace("\"", ""));
        if (Files.exists(path)) {
            Files.delete(path);
            return Result.success("ok");
        }
        return Result.error("not Exist");
    }


}
