package daden.shopaa.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.req.CategoryReq;
import daden.shopaa.entity.Category;
import daden.shopaa.exceptions.DuplicateRecordError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.CategoryRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CategoryService {
  private final MongoTemplate mongoTemplate;
  private final CategoryRepo cateRepo;

  public Category addCategory(CategoryReq categoryReq) {
    // check title
    if (cateRepo.existsByTitleAndParentId(categoryReq.getTitle(), categoryReq.getParentId()))
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

    foundCate.setParentId(parentId);
    foundCate.setTitle(categoryReq.getTitle());
    foundCate.setDescription(categoryReq.getDescription());
    foundCate.setThumb(categoryReq.getThumb());

    return cateRepo.save(foundCate);
  }

  public List<Category> findAll(String id, String parentId) {
    Query query = new Query();

    System.out.println("parentId::::" + parentId);

    if (id != null && !id.isEmpty()) {
      if (cateRepo.existsById(id) == false)
        throw new NotFoundError("id", id);
      query.addCriteria(Criteria.where("id").is(id));
    }

    if (parentId != null && !parentId.isEmpty()) {
      if (!parentId.equals("null") && cateRepo.existsByParentId(parentId) == false)
        throw new NotFoundError("parentId", parentId);
      query.addCriteria(
          parentId.equals("null")
              ? new Criteria().orOperator(Criteria.where("parentId").exists(false), Criteria.where("parentId").is(null))
              : Criteria.where("parentId").is(parentId));
    }

    return mongoTemplate.find(query, Category.class);
  }

}
