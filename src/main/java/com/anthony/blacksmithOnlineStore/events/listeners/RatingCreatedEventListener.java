package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.events.RatingCreatedEvent;
import com.anthony.blacksmithOnlineStore.service.BlacksmithService;
import com.anthony.blacksmithOnlineStore.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingCreatedEventListener {
  private final ItemService itemService;
  private final BlacksmithService blacksmithService;

  @EventListener
  public void eventHandle(RatingCreatedEvent createdEvent) {
    Item item = itemService.findEntityById(createdEvent.itemId());
    item.addRating(createdEvent.rate());
    blacksmithService.findEntityById(item.getBlacksmithIdSnapshot()).addRating(createdEvent.rate());
  }

}
