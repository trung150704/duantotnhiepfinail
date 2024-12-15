package com.mt.rest.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mt.service.UploadService;

@CrossOrigin("*")
@RestController
public class UploadRestController {
    @Autowired
    UploadService uploadService;
    
    @PostMapping("/rest/upload/{folder}")
    public JsonNode upload(@RequestParam("file") MultipartFile file,
                           @PathVariable("folder") String folder) {
        try {
            // Chỉ định thư mục lưu ảnh trong thư mục static
            Path targetLocation = Paths.get("src/main/resources/static/" + folder + "/" + file.getOriginalFilename());

            // Kiểm tra xem thư mục đã tồn tại chưa, nếu chưa thì tạo nó
            Files.createDirectories(targetLocation.getParent());

            // Lưu file vào thư mục static
            file.transferTo(targetLocation);

            // Tạo phản hồi dưới dạng JSON
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("name", file.getOriginalFilename());
            node.put("size", file.getSize());
            node.put("url", "/images/" + folder + "/" + file.getOriginalFilename()); // Đường dẫn để truy cập ảnh

            return node;
        } catch (Exception e) {
            // Xử lý lỗi
            e.printStackTrace();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "Lỗi upload file");
            return errorNode;
        }
    }
}
