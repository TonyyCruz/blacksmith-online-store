package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.blacksmith.BlacksmithRequestDto;
import com.anthony.blacksmithOnlineStore.controler.dto.blacksmith.BlacksmithResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.exceptions.BlacksmithNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.BlacksmithRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacksmithService {
  private final BlacksmithRepository blacksmithRepository;

  public BlacksmithResponseDto findById(Long id) {
    return BlacksmithResponseDto.fromEntity(findEntityById(id));
  }

  public Blacksmith findEntityById(Long id) {
    return blacksmithRepository.findById(id)
        .orElseThrow(() -> new BlacksmithNotFoundException(id));
  }

  public Page<BlacksmithResponseDto> findByName(String name, Pageable pageable) {
    Page<Blacksmith> blacksmiths = blacksmithRepository.findByNameContaining(name, pageable);
    return blacksmiths.map(BlacksmithResponseDto::fromEntity);
  }

  @Transactional
  public BlacksmithResponseDto create(BlacksmithRequestDto dto) {
    Blacksmith blacksmith = blacksmithRepository.save(BlacksmithRequestDto.toEntity(dto));
    return BlacksmithResponseDto.fromEntity(blacksmith);
  }

  public BlacksmithResponseDto update(Long id, BlacksmithRequestDto dto) {
    Blacksmith blacksmith = getReferenceById(id);
    blacksmith.setName(dto.name());
    blacksmith.setDescription(dto.description());
    Blacksmith updatedBlacksmith = blacksmithRepository.save(blacksmith);
    return BlacksmithResponseDto.fromEntity(updatedBlacksmith);
  }

  public Page<BlacksmithResponseDto> findAll(Pageable pageable) {
    Page<Blacksmith> blacksmiths = blacksmithRepository.findAll(pageable);
    return blacksmiths.map(BlacksmithResponseDto::fromEntity);
  }

  public Blacksmith getReferenceById(Long id) {
    existsVerify(id);
    return blacksmithRepository.getReferenceById(id);
  }

  public void existsVerify(Long id) {
    if (!blacksmithRepository.existsById(id)) {
      throw new BlacksmithNotFoundException(id);
    }
  }

  public void addRating(Long blacksmithId, int rating) {
    Blacksmith blacksmith = findEntityById(blacksmithId);
    blacksmith.addRating(rating);
    blacksmithRepository.save(blacksmith);
  }
}
