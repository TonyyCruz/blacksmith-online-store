package com.anthony.blacksmithOnlineStore.payment;

@Component
public class PaymentProcessorFactory {

    private final Map<PaymentMethod, PaymentProcessor> processors;

    public PaymentProcessorFactory(
            List<PaymentProcessor> processors) {

        this.processors = processors.stream()
                .collect(Collectors.toMap(
                        PaymentProcessor::supports,
                        Function.identity()
                ));
    }

    public PaymentProcessor getProcessor(
            PaymentMethod method) {

        PaymentProcessor processor =
                processors.get(method);

        if (processor == null) {
            throw new IllegalArgumentException(
                    "Método de pagamento não suportado: " + method
            );
        }

        return processor;
    }
}
