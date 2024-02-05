package daden.shopaa.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.parampetterRequest.ProductParamRequets;
import daden.shopaa.dto.req.ProductReq;
import daden.shopaa.entity.Category;
import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.exceptions.DuplicateRecordError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.CategoryRepo;
import daden.shopaa.repository.ProductRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ProductService {
  private final MongoTemplate mongoTemplate;
  private final ProductRepo productRepo;
  private final ProductvariationService variationService;
  private final CategoryRepo categoryRepo;

  public Product add(ProductReq productReq) {
    // check name, product name must not duplicate
    if (productRepo.existsByName(productReq.getName()))
      throw new DuplicateRecordError("product name", productReq.getName());

    List<Category> cates = productReq.getCategoryIds().stream()
        .map(v -> categoryRepo.findById(v).orElseThrow(() -> new NotFoundError("category id", v)))
        .toList();

    Product product = productRepo.save(
        Product.builder()
            .name(productReq.getName())
            .description(productReq.getDescription())
            .categories(cates)
            .price(productReq.getPrice())
            .build());

    List<ProductVariation> variations = variationService.addOrUpdateProductVariation(productReq.getVariations(),
        product);

    product.setVariations(variations);
    return productRepo.save(product);
  }

  public PageCustom<Product> findAll(Pageable pageable, ProductParamRequets paramRequets) {
    String keySearch = paramRequets.getKeySearch();
    Double minPrice = paramRequets.getMinPrice();
    Double maxPrice = paramRequets.getMaxPrice();
    String categoryId = paramRequets.getCategoryId();
    String color = paramRequets.getColor();
    String size = paramRequets.getSizee();
    String status = paramRequets.getStatus();

    Query query = new Query();

    // keySearch
    if (keySearch != null && !keySearch.isEmpty()) {
      String regexPattern = "(?i)" + keySearch.trim(); // Thêm ?i để không phân biệt chữ hoa chữ thường
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("name").regex(regexPattern),
          Criteria.where("description").regex(regexPattern),
          Criteria.where("categories").elemMatch(
              Criteria.where("title").regex(regexPattern)),
          Criteria.where("categories").elemMatch(
              new Criteria().orOperator(Criteria.where("title").is(regexPattern),
                  Criteria.where("description").is(regexPattern)))));
    }

    if (minPrice != null)
      query.addCriteria(Criteria.where("price").gte(minPrice));

    if (maxPrice != null)
      query.addCriteria(Criteria.where("price").lte(maxPrice));

    if (categoryId != null)
      query.addCriteria(Criteria.where("categories").elemMatch(
          new Criteria().orOperator(Criteria.where("id").is(categoryId), Criteria.where("parentId").is(categoryId))));

    if (color != null)
      query.addCriteria(Criteria.where("variations.color").is(color));

    if (size != null)
      query.addCriteria(Criteria.where("variations.size").is(size));

    if (status != null)
      query.addCriteria(Criteria.where("status").is(status));

    query.with(pageable);

    List<Product> list = mongoTemplate.find(query, Product.class);
    long total = mongoTemplate.count(query, Product.class);
    return new PageCustom<>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

  public Product updateProduct(ProductReq productReq, String id) {
    Product foundProduct = productRepo.findById(id).orElseThrow(() -> new NotFoundError("product id", id));
    if (productRepo.existsByNameAndIdNot(productReq.getName(), id))
      throw new DuplicateRecordError("product id", id);

    List<Category> cates = productReq.getCategoryIds().stream()
        .map(v -> categoryRepo.findById(v).orElseThrow(() -> new NotFoundError("category id", v)))
        .toList();

    List<ProductVariation> variations = variationService.addOrUpdateProductVariation(productReq.getVariations(),
        foundProduct);

    foundProduct.setCategories(cates);
    foundProduct.setName(productReq.getName());
    foundProduct.setDescription(productReq.getDescription());
    foundProduct.setDiscount(productReq.getDiscount());
    foundProduct.setPrice(productReq.getPrice());
    foundProduct.setStatus(productReq.getStatus());
    foundProduct.setVariations(variations);

    return productRepo.save(foundProduct);
  }

}
