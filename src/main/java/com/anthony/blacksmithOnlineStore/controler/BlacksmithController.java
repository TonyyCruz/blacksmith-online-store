package com.anthony.blacksmithOnlineStore.controler;

import com.anthony.blacksmithOnlineStore.controler.dto.blacksmith.BlacksmithRequestDto;
import com.anthony.blacksmithOnlineStore.controler.dto.blacksmith.BlacksmithResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.service.BlacksmithService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class BlacksmithController {
  private final BlacksmithService blacksmithService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BlacksmithResponseDto> createBlacksmith(
      @Valid @RequestBody BlacksmithRequestDto dto) {
    Blacksmith createdBlacksmith = blacksmithService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BlacksmithResponseDto.fromEntity(createdBlacksmith));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BlacksmithResponseDto> updateBlacksmith(
      @Valid @RequestBody BlacksmithRequestDto dto, @PathVariable Long id) {
    Blacksmith createdBlacksmith = blacksmithService.update(id, dto);
    return ResponseEntity.ok(BlacksmithResponseDto.fromEntity(createdBlacksmith));
  }

  @GetMapping
  public ResponseEntity<Page<BlacksmithResponseDto>> findAll(
      @PageableDefault(page = 0, size = 20, sort = "name", direction = Direction.ASC)
      Pageable pageable
  ) {
    Page<Blacksmith> blacksmiths = blacksmithService.findAll(pageable);
    return ResponseEntity.ok(blacksmiths.map(BlacksmithResponseDto::fromEntity));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BlacksmithResponseDto> findById(@PathVariable Long id) {
    Blacksmith blacksmith = blacksmithService.findById(id);
    return ResponseEntity.ok(BlacksmithResponseDto.fromEntity(blacksmith));
  }

  @GetMapping("/search")
  public ResponseEntity<Page<BlacksmithResponseDto>> findByName(
      @PageableDefault(page = 0, size = 20, sort = "name", direction = Direction.ASC)
      Pageable pageable,
      @RequestParam(value = "name") String name) {
    Page<Blacksmith> blacksmiths = blacksmithService.findByName(name, pageable);
    return ResponseEntity.ok(blacksmiths.map(BlacksmithResponseDto::fromEntity));
  }
}
