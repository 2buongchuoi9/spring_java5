package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.req.StudentReq;
import daden.shopaa.entity.Student;
import daden.shopaa.repository.StudentRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import daden.shopaa.services.StudentService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentController {
  final StudentService stdService;

  // get all students
  @GetMapping("")
  public ResponseEntity<MainResponse<PageCustom<Student>>> getAll(
      @PageableDefault(page = 0, size = 10, sort = "maSV", direction = Direction.ASC) Pageable pageable) {
    return ResponseEntity.ok(MainResponse.oke(stdService.getAll(pageable)));
  }

  // add a student
  @PostMapping("")
  public ResponseEntity<MainResponse<Student>> add(@RequestBody StudentReq stdReq) {
    return ResponseEntity.ok().body(MainResponse.oke(stdService.add(stdReq)));
  }

  // update a student
  @PostMapping("/{id}")
  public ResponseEntity<MainResponse<Student>> update(@PathVariable String id, @RequestBody Student std) {
    return ResponseEntity.ok().body(MainResponse.oke(stdService.update(id, std)));
  }

  // delete a student
  @DeleteMapping("/{id}")
  public ResponseEntity<MainResponse<Boolean>> delete(@PathVariable String id) {
    return ResponseEntity.ok().body(MainResponse.oke(stdService.delete(id)));
  }

}
