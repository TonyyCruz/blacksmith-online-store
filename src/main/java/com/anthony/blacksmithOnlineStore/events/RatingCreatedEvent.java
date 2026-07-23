package com.anthony.blacksmithOnlineStore.events;

public record RatingCreatedEvent(Long itemId, int rate, Long ratingId) {

}
