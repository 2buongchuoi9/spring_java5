package daden.shopaa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.parampetterRequest.OrderParamRequest;
import daden.shopaa.dto.res.ReportFromOrderRes;
import daden.shopaa.services.ReportService;
import daden.shopaa.utils.Constans.HASROLE;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@PreAuthorize(HASROLE.ADMIN)
@RequestMapping("/api/v1/report")
public class ReportController {

  private final ReportService reportService;

  @Operation(summary = "get report by orderId")
  @PostMapping("/{orderId}")
  public ResponseEntity<MainResponse<ReportFromOrderRes>> reportByOrderId(@PathVariable String orderId) {
    ReportFromOrderRes reportFromOrderRes = reportService.reportFromOrdersByOneOrder(orderId);
    return ResponseEntity.ok().body(MainResponse.oke(reportFromOrderRes));
  }

  @Operation(summary = "get at day")
  @PostMapping("")
  public ResponseEntity<MainResponse<List<ReportFromOrderRes>>> report(
      @ModelAttribute @Valid OrderParamRequest paramRequest) {
    List<ReportFromOrderRes> reportFromOrderRes = reportService.reportFromOrdersByDay(paramRequest);
    return ResponseEntity.ok().body(MainResponse.oke(reportFromOrderRes));
  }

}
