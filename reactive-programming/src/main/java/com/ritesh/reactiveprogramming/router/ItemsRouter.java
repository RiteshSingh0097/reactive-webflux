package com.ritesh.reactiveprogramming.router;

import com.ritesh.reactiveprogramming.controller.constants.ItemConstant;
import com.ritesh.reactiveprogramming.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(GET(ItemConstant.ITEM_FUN_END_POINT_V1)
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::getAllItems)
                .andRoute(GET(ItemConstant.ITEM_FUN_END_POINT_V1 + "/{id}")
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::getOneItem)
                .andRoute(POST(ItemConstant.ITEM_FUN_END_POINT_V1)
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::create)
                .andRoute(DELETE(ItemConstant.ITEM_FUN_END_POINT_V1 + "/{id}")
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::delete)
                .andRoute(PUT(ItemConstant.ITEM_FUN_END_POINT_V1+"/{id}")
                .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::updateItem);
    }

    @Bean
    public RouterFunction<ServerResponse> errorRoute(ItemsHandler itemsHandler){
        return RouterFunctions
                .route(GET("/v1/fun/runtimeexception")
                .and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::itemsEx);
    }
}
