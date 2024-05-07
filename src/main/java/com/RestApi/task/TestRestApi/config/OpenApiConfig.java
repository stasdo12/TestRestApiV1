package com.RestApi.task.TestRestApi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;



@OpenAPIDefinition(
        info = @Info(
                title = "User API",
                description = "Doing CRUD operation REST API",
                summary = "This API users will add, delete, create and update users"
        )
)
public class OpenApiConfig  {

}