package com.pcmmtl.webflux.paymen;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class PaymentController {

    static final int INTERVAL_SECOND = 5;

    static final Random RAND = new Random();
    static List<Payment> payments = new ArrayList<>();

    static final List<String> CURRENCY_LIST = List.of("USD", "CAD", "EUR", "JPY", "MEX");
    static final List<String> DIRECTION_LIST = List.of("Pay", "Rec");
    static final List<String> BIC_LIST = List.of("ALMVUS31","BDSDUS44","CINLUS42","DINLUS43","ELFSUS31","GLLMUS31","FICOUS41");



    static {
        payments = IntStream.range(0, 100).mapToObj( i -> createPayment()).collect(Collectors.toList()); // create payment list at start up.
    }
    @CrossOrigin(allowedHeaders = "*")
    @GetMapping(value = "/payments", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Payment> getPayments() {

        var atomicInt = new AtomicInteger(0);

        return Flux.interval(Duration.ofSeconds(INTERVAL_SECOND))
                .map(it -> {
                    var p = payments.get(atomicInt.getAndIncrement());
                    p.setTimestamp(LocalDateTime.now()); // update timestamp of payment as of now.
                    return p;
                });
    }

    @CrossOrigin(allowedHeaders = "*")
    @GetMapping(value = "/currencies", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> getCurrencies() {

        var currencyList = List.of("USD", "CAD", "EUR", "JPY", "GBP");
        Random random = new Random();
        return Flux.interval(Duration.ofSeconds(INTERVAL_SECOND))
                .map(it ->
                        Map.of("name", currencyList.get(random.nextInt(5)), "value", random.nextInt(10))
                );

    }

    @CrossOrigin(allowedHeaders = "*")
    @GetMapping(value = "/directions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> getDirections() {

        Random random = new Random();
        return Flux.interval(Duration.ofSeconds(INTERVAL_SECOND))
                .map(it ->
                        Map.of(
                                "labels", List.of("01-01","02-01","03-01","04-01","05-01","06-01"),
                                "pay", List.of(800, 1600, 900, 1300, 1950, 1700),
                                "rec", List.of(4900, 2600, 5350, 4800, 5200, 4800 + random.nextInt(1000))
                        )
                );

    }

    private static Payment createPayment(){
        return Payment.builder()
                .id(UUID.randomUUID().toString())
                .amount(RAND.nextInt(1000, 5000))
                .direction(DIRECTION_LIST.get(RAND.nextInt(2)))
                .bic(BIC_LIST.get(RAND.nextInt(7)))
                .name("name of thie bic")
                .currency(CURRENCY_LIST.get(RAND.nextInt(4)))
                .timestamp(LocalDateTime.now()).build();
    }
}