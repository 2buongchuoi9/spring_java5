package daden.shopaa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.req.CategoryReq;
import daden.shopaa.entity.Category;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.repository.CategoryRepo;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepo categoryRepo;

  public Category addCategory(CategoryReq categoryReq) {
    // check parentId
    String parentId = categoryReq.getParentId();
    if (parentId != null && !categoryRepo.existsById(parentId))
      throw new BabRequestError("not found parentId=" + parentId);

    return categoryRepo.save(Category.builder()
        .parentId(parentId)
        .title(categoryReq.getTitle())
        .description(categoryReq.getDescription())
        .thumb(categoryReq.getThumb())
        .build());
  }

}
