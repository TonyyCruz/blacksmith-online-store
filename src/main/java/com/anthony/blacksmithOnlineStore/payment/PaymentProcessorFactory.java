package com.anthony.blacksmithOnlineStore.payment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidPaymentException;

@Component
public class PaymentProcessorFactory {
    private final Map<PaymentMethod, PaymentProcessor> processors;

    public PaymentProcessorFactory(List<PaymentProcessor> processors) {
        this.processors = processors.stream()
                .collect(Collectors.toMap(PaymentProcessor::getPaymentMethod, Function.identity()));
    }

    public PaymentProcessor getProcessor(PaymentMethod method) {
        PaymentProcessor processor = processors.get(method);
        if (processor == null) {
            throw new InvalidPaymentException("Invalid payment method: ".concat(method.name()));
        }
        return processor;
    }
}
