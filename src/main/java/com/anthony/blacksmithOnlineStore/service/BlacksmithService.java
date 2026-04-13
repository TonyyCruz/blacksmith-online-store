package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.blacksmith.BlacksmithRequestDto;
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

  public Blacksmith findById(Long id) {
    return blacksmithRepository.findById(id)
        .orElseThrow(() -> new BlacksmithNotFoundException(id));
  }

  public Page<Blacksmith> findByName(String name, Pageable pageable) {
    return blacksmithRepository.findByNameContaining(name, pageable);
  }

  @Transactional
  public Blacksmith create(BlacksmithRequestDto dto) {
    return blacksmithRepository.save(BlacksmithRequestDto.toEntity(dto));
  }

  public Blacksmith update(Long id, BlacksmithRequestDto dto) {
    Blacksmith blacksmith = getReferenceById(id);
    blacksmith.setName(dto.name());
    blacksmith.setDescription(dto.description());
    return blacksmithRepository.save(blacksmith);
  }

  public Page<Blacksmith> findAll(Pageable pageable) {
    return blacksmithRepository.findAll(pageable);
  }

  public Blacksmith getReferenceById(Long id) {
    if (!blacksmithRepository.existsById(id)) throw new BlacksmithNotFoundException(id);
    return blacksmithRepository.getReferenceById(id);
  }

  public void addRating(Long blacksmithId, int rating) {
    Blacksmith blacksmith = findById(blacksmithId);
    blacksmith.addRating(rating);
    blacksmithRepository.save(blacksmith);
  }
}
