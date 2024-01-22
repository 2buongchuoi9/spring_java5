package daden.shopaa.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.req.CategoryReq;
import daden.shopaa.entity.Category;
import daden.shopaa.exceptions.DuplicateRecordError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.CategoryRepo;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepo cateRepo;

  public Category addCategory(CategoryReq categoryReq) {
    // check title
    if (cateRepo.existsByTitle(categoryReq.getTitle()))
      throw new DuplicateRecordError("title", categoryReq.getTitle());

    // check parentId
    String parentId = categoryReq.getParentId();
    if (parentId != null && !cateRepo.existsById(parentId))
      throw new NotFoundError("parentId", parentId);

    return cateRepo.save(Category.builder()
        .parentId(parentId)
        .title(categoryReq.getTitle())
        .description(categoryReq.getDescription())
        .thumb(categoryReq.getThumb())
        .build());
  }

  public Category updateCategory(String id, CategoryReq categoryReq) {
    // check id
    Category foundCate = cateRepo.findById(id).orElseThrow(() -> new NotFoundError("id", id));

    // check title
    if (cateRepo.existsByTitleAndIdNot(categoryReq.getTitle(), id))
      throw new DuplicateRecordError("title", categoryReq.getTitle());

    // check parentId
    String parentId = categoryReq.getParentId();
    if (parentId != null && !cateRepo.existsById(parentId))
      throw new NotFoundError("parentId", parentId);

    return cateRepo.save(Category.builder()
        .parentId(parentId)
        .title(categoryReq.getTitle())
        .description(categoryReq.getDescription())
        .thumb(categoryReq.getThumb())
        .build());
  }

  public List<Category> findAll() {

    return cateRepo.findAll();

  }

}
