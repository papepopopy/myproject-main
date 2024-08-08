package com.spring.myproject.controller;


import com.spring.myproject.dto.upload.UploadFileDTO;
import com.spring.myproject.dto.upload.UploadResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${com.spring.myproject.path}") // properties파일 설정한 path값 읽어오기
    private String uploadPath; //  => "c:/javaStudy/upload" 인식


    // upload
    @Operation(summary="Upload POST", description="POST방식으로 파일 등록")
    @PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )// 첨부파일 형식의 데이터 받기로 설정
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){// 클라이언트로부터 받은 첨부파일 매개변수역할

        // 1.
        log.info("=> uploadFileDTO:"+uploadFileDTO);// 클라이언트로부터 받은 첨부파일을 담고 있는 객체

        // 2.
        if (uploadFileDTO.getFiles()!=null){// uploadFileDTO List구조의 객체 첨부파일 있으면

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String fileName = multipartFile.getOriginalFilename();
                log.info("=> "+multipartFile.getOriginalFilename());

                // 3. 첨부파일 저장하기
                String uuid = UUID.randomUUID().toString();// 중복을 허용하지 않는 난수
                log.info(uuid);

                // "c:/javaStudy/upload" + uuid+ "_" +"첨부파일 이름"
                Path savPath = Paths.get(uploadPath, uuid+"_"+fileName);


                // 5. 파일 형식 파악(이미지파일)
                boolean image = false;

                // 4. 지정된 위치에 파일 저장
                try {
                    multipartFile.transferTo(savPath);

                    // 이미지 파일이면 섬네일 생성(용량을 줄인 파일 생성): net.coobird:thumbnailator 라이브러리 설치
                    if (Files.probeContentType(savPath).startsWith("image")){
                        image = true;

                        // "c:/javaStudy/upload" + "s_" +uuid+"_"+첨부파일 이름"
                        File thumbFile = new File(uploadPath, "s_"+uuid+"_"+fileName);
                        Thumbnailator.createThumbnail(savPath.toFile(), thumbFile, 200, 200);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                // 파일 저장(업로드)후 파일에 관련 정보를 List객체 저장
                list.add(
                        UploadResultDTO.builder()
                                .uuid(uuid)           // uuid값
                                .fileName(fileName)   // 파일이름
                                .img(image)           // 이미지 여부판단
                                .build());


            });// end forEach

            log.info("=> upload된 파일 정보");
            log.info("=> "+list);

            return list; // 첨부파일이 있으면 UploadResultDTO 타입의 list객체 반환
        }// end if (첨부파일 있을 경우)

        return null;// 첨부파이 없으면 null 반환
    }

    // image view
    @Operation(summary="view 파일", description="GET방식으로 첨부파일 조회")
    @GetMapping(value="/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    // file remove
    @Operation(summary="remove 파일", description="DELETE 방식으로 첨부파일 삭제")
    @GetMapping(value="/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();

        // 결과를 담을 맵을 생성합니다.
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try {
            // 파일의 콘텐츠 타입을 확인합니다.
            String contentType = Files.probeContentType(resource.getFile().toPath());
            // 파일을 삭제합니다
            removed = resource.getFile().delete();

            // 업로드 경로와 파일 이름을 결합하여 파일 시스템 리소스를 생성합니다.
            log.info("=> contentType : "+contentType);
            if (contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                thumbnailFile.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 삭제 결과를 맵에 담아 반환합니다
        resultMap.put("result", removed);
        return resultMap;
    }

}