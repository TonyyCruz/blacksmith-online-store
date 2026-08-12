package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.docs.BlacksmithControllerDocs;
import com.anthony.blacksmithOnlineStore.controller.dto.blacksmith.BlacksmithRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.blacksmith.BlacksmithResponseDto;
import com.anthony.blacksmithOnlineStore.service.BlacksmithService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blacksmiths")
public class BlacksmithController implements BlacksmithControllerDocs {
  private final BlacksmithService blacksmithService;

  @Autowired
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BlacksmithResponseDto> createBlacksmith(
      @Valid @RequestBody BlacksmithRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(blacksmithService.create(dto));
  }

  @Autowired
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BlacksmithResponseDto> updateBlacksmith(
      @Valid @RequestBody BlacksmithRequestDto dto, @PathVariable Long id) {
    return ResponseEntity.ok(blacksmithService.update(id, dto));
  }

  @Autowired
  @GetMapping
  public ResponseEntity<Page<BlacksmithResponseDto>> findAll(
      @PageableDefault(page = 0, size = 20, sort = "name", direction = Direction.ASC)
      @ParameterObject
      Pageable pageable
  ) {
    return ResponseEntity.ok(blacksmithService.findAll(pageable));
  }

  @Autowired
  @GetMapping("/{id}")
  public ResponseEntity<BlacksmithResponseDto> findById(@PathVariable Long id) {
    return ResponseEntity.ok(blacksmithService.findById(id));
  }

  @Autowired
  @GetMapping("/search")
  public ResponseEntity<Page<BlacksmithResponseDto>> findByName(
      @PageableDefault(page = 0, size = 20, sort = "name", direction = Direction.ASC)
      @ParameterObject
      Pageable pageable,
      @RequestParam(value = "name") String name) {
    return ResponseEntity.ok(blacksmithService.findByName(name, pageable));
  }
}
