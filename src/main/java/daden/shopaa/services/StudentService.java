package daden.shopaa.services;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.req.StudentReq;
import daden.shopaa.entity.Student;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.StudentRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
  private final StudentRepo stdRepo;
  private final MongoTemplate mongoTemplate;

  public Student add(StudentReq stdReq) {
    Long maxMaSV = stdRepo.findTopByOrderByMaSVDesc()
        .map(Student::getMaSV)
        .orElse(0L);

    return stdRepo.save(Student.builder()
        .name(stdReq.getName())
        .maSV(maxMaSV + 1)
        .mark(stdReq.getMark())
        .major(stdReq.getMajor())
        .build());
  }

  public Student update(String id, Student std) {
    Student foundStd = stdRepo.findById(id).orElseThrow(() -> new NotFoundError("id", id));
    foundStd.setName(std.getName());
    foundStd.setMark(std.getMark());
    foundStd.setMajor(std.getMajor());
    return stdRepo.save(foundStd);
  }

  public boolean delete(String id) {
    if (!stdRepo.existsById(id))
      throw new NotFoundError("id", id);

    stdRepo.deleteById(id);
    return true;
  }

  public PageCustom<Student> getAll(Pageable pageable) {
    Query query = new Query();
    Long total = mongoTemplate.count(query, Student.class);
    query.with(pageable.getSort());
    List<Student> list = mongoTemplate.find(query, Student.class);
    return new PageCustom<>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

}
