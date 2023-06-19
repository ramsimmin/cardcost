package com.example.cardcost.openapi;

import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.dto.ResponseDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Card Cost Controller", description = "Retrieve the clearing cost for a given card number")
@OpenAPIDefinition(
        info = @Info(
                title = "CARD COST API",
                version = "0.0"
        )
)
public interface CardCostControllerOpenApi {




    @Operation(summary = "Get the card cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ClearingCostDto"))}
            ),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class),
                            examples = {
                                    @ExampleObject(name = "Issuer Not Found",
                                            value = """
                                                    {
                                                        "messages": [
                                                            "Requested card number does not belong to a known issuer"
                                                        ],
                                                        "error": true
                                                    }
                                                    """),

                            }
                    )}),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class),
                            examples = {
                                    @ExampleObject(name = "Api not available", value = """
                                                    {
                                                        "messages": [
                                                            "Unable to calculate cost"
                                                        ],
                                                        "error": true
                                                    }
                                                    """ ),
                            }
                    )})
    })

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getCardCost(@RequestBody CardCostRequestDto cardCostRequestDto);
}
