package com.anthony.blacksmithOnlineStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthony.blacksmithOnlineStore.entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
}
