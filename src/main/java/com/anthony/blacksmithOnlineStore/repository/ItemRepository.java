package com.anthony.blacksmithOnlineStore.repository;

import com.anthony.blacksmithOnlineStore.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

  @Modifying
  @Query("""
        UPDATE Item i
        SET
            i.stock = i.stock - :qty,
            i.sold = i.sold + :qty
        WHERE
            i.id = :itemId AND i.stock >= :qty
    """)
  int decrementStockAndIncrementSoldQuantity(long itemId, int qty);

  @Modifying
  @Query("""
        UPDATE Item i
        SET
            i.stock = i.stock + :qty,
            i.sold = i.sold - :qty
        WHERE
            i.id = :id AND i.sold >= :qty
    """)
  int incrementStockAndDecrementSoldQuantity(long id, int qty);

  @Query("""
        SELECT i.active FROM Item i WHERE i.id = :id
    """)
  boolean isItemActive(long id);
}
