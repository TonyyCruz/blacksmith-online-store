package com.anthony.blacksmithOnlineStore.events.listeners;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.events.RatingCreatedEvent;
import com.anthony.blacksmithOnlineStore.service.BlacksmithService;
import com.anthony.blacksmithOnlineStore.service.ItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RatingCreatedEventListener {
  private final ItemService itemService;
  private final BlacksmithService blacksmithService;

  @Transactional
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void eventHandle(RatingCreatedEvent createdEvent) {
    Item item = itemService.findEntityById(createdEvent.itemId());
    item.addRating(createdEvent.rate());
    blacksmithService.findEntityById(item.getBlacksmithIdSnapshot()).addRating(createdEvent.rate());
  }

}
