package com.GradeGoal.service;

import com.GradeGoal.model.Image;
import com.GradeGoal.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserService userService;

    public Image saveImage(MultipartFile file,String studno) throws IOException {

        String contentType = file.getContentType();
        byte[] data = file.getBytes();

        Image image = new Image();
        image.setData(data);
        image.setName(studno);
        image.setType(contentType);
        image.setUser(userService.getUser(studno));
        log.info("image saved to database as: {}", image.getName() + ".png");
        return imageRepository.save(image);
    }

    public Image getImage(String studno){
        return imageRepository.findByName(studno);
    }
    public void deleteImage(String studNo){
        try{
            imageRepository.delete(getImage(studNo));
            log.info("image has been deleted.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
