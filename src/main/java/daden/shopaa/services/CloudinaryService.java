package daden.shopaa.services;

import java.io.IOException;
import java.util.Map;

import com.cloudinary.utils.ObjectUtils;

import daden.shopaa.exceptions.NotFoundError;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String upload(MultipartFile file) {
        try {
            Map data = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "overwrite", true,
                    "folder", "demo_spring"));
            return data.get("url").toString();
        } catch (IOException io) {
            throw new NotFoundError("Image upload fail");
        }
    }

    public boolean delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId,
                    ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map uploadV2(MultipartFile multipartFile) {
        try {
            Map params = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "overwrite", true,
                    "folder", "demo_spring");
            Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), params);
            return uploadResult;
        } catch (IOException io) {
            throw new NotFoundError("Image upload fail");
        }
    }

    /* update 1 file lên cloudinary */
    public Map uploadPublicId(MultipartFile multipartFile, String publicId) {
        try {
            Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "public_id", publicId,
                            "folder", "demo_spring"));

            return uploadResult;
        } catch (Exception e) {
            throw new NotFoundError("Image upload fail");
        }
    }
}
