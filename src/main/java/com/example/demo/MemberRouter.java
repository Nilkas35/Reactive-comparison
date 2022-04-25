package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.Random;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class MemberRouter {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    MemberRouter(MemberRepository repository, CartRepository cartRepository) {
        this.memberRepository = repository;
        this.cartRepository = cartRepository;
    }

    @Bean
    public RouterFunction<ServerResponse> getMultipleMembersRoute() {
        return route(GET("/member/{idOne}/{idTwo}"),
                req -> {
                    Mono<Cart> test = cartRepository.findCartById(req.pathVariable("idOne"));
                    Mono<Member> test2 = memberRepository.findMemberById(req.pathVariable("idTwo"));
                    Flux<Object> res = Flux.concat(test, test2);
                    return ok().body(res, Member.class);
                }
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getRandom() {
        return route(GET("/member/random/{min}/{max}"),
                req -> {
                    Random r = new Random();
                    int low = Integer.parseInt(req.pathVariable("min"));;
                    int high = Integer.parseInt(req.pathVariable("max"));
                    int result = r.nextInt(high-low) + low;
                    Mono<Member> res = memberRepository.findMemberById(String.valueOf(result));
                    return ok().body(res, Member.class);
                }
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAllMembersRoute() {
        return route(GET("/member"),
                req ->
                    ok().body(
                            memberRepository.findAll(), Member.class)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getBigData() {
        return route(GET("/member/big"),
                req ->
                        ok().body(
                                memberRepository.getBigData(), String.class)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> one() {
        return route(GET("/member/{id}"),
                req -> ok().body(
                        memberRepository.findMemberById(req.pathVariable("id")), Member.class));
    }
    @Bean
    RouterFunction<ServerResponse> updateMemberRoute() {
        return route(POST("/member"),
                req -> req.bodyToMono(Member.class)
                        .flatMap(member -> Mono.just(memberRepository.save(member)))
                        .flatMap(member -> ServerResponse.status(HttpStatus.CREATED)
                                .body(member, Member.class)));
    }
}

