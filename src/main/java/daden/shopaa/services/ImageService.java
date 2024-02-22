package daden.shopaa.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import daden.shopaa.entity.Image;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.ImageRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings({ "null", "rawtypes" })
public class ImageService {
  private final ImageRepo imageRepo;

  private final CloudinaryService cloudinaryService;

  public Image addImageAndFile(MultipartFile multipartFile) {
    Image image = new Image();
    Map uploadResult = cloudinaryService.uploadV2(multipartFile);
    if (uploadResult != null) {
      image.setPublicId(uploadResult.get("public_id").toString());
      image.setUrl(uploadResult.get("url").toString());
      imageRepo.save(image);
    }
    return image;
  }

  public PageCustom<Image> findAll(Pageable pageable) {
    List<Image> list = imageRepo.findAll();
    long total = imageRepo.count();
    return new PageCustom<>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

  public boolean delete(String id) {
    try {
      Image image = imageRepo.findById(id).orElseThrow(() -> new NotFoundError("not found imageId=" + id));
      if (image.getPublicId() != null)
        cloudinaryService.delete(image.getPublicId());
      imageRepo.delete(image);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
