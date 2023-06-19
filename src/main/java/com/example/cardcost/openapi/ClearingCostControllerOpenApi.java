package com.example.cardcost.openapi;

import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Clearing Cost Matrix Controller", description = "Api to create/ read/ update /delete clearing card costs")
public interface ClearingCostControllerOpenApi {


    @Operation(summary = "Get the clearing cost for a given country_code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ClearingCostDto")
                    )}
            ),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class),
                            examples = {
                                    @ExampleObject(name = "Issuer Not Found",
                                            value = """
                                                    {
                                                        "messages": [
                                                            "Cost entry for country code [countryCodeParam] does not exist"
                                                        ],
                                                        "error": true
                                                    }
                                                    """),

                            }
                    )})
    })

    @GetMapping(value = "/{country_code}")
    ResponseEntity<?> getCost(@Parameter(name = "country_code", example = "gr", description = "The iso2 country code") @PathVariable(name = "country_code") String country_code);

    @Operation(summary = "Get all clearing costs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(name = "List of costs",
                                            value = """
                                                    [
                                                        {
                                                            "country_code": "gr",
                                                            "cost": 15
                                                        },
                                                        {
                                                            "country_code": "uk",
                                                            "cost": 12.5
                                                        },
                                                        {
                                                            "country_code": "other",
                                                            "cost": 20
                                                        }
                                                    ]
                                                    """),

                            }
                    )}
            )}
    )
    @GetMapping
    ResponseEntity<?> getCosts();


    @Operation(summary = "Create a clearing cost matrix entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ResponseDto"),
                            examples = @ExampleObject(value = """
                                    {
                                        "messages": [
                                            "Successfully saved"
                                        ]
                                    }
                                    """)
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ResponseDto"),
                            examples = {@ExampleObject(name = "Clearing cost entry already exists", value = """
                                                                        
                                    {
                                        "messages": [
                                            "Cost entry already exists for country code [country_code]"
                                        ],
                                        "error": true
                                    }
                                    """),
                                    @ExampleObject(name = "Missing values", value = """
                                                                                
                                            {
                                                "messages": [
                                                    "Country code must be provided",
                                                    "Cost must be greater than or equal 0"
                                                ],
                                                "error": true
                                            }
                                            """)
                            }

                    )})
    })
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createCost(@RequestBody ClearingCostDto costRegisterDto);


    @Operation(summary = "Update a clearing cost matrix entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ResponseDto"),
                            examples = @ExampleObject(value = """
                                    {
                                        "messages": [
                                            "Successfully updated"
                                        ]
                                    }
                                    """)
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ResponseDto"),
                            examples =
                                    @ExampleObject(name = "Missing values", value = """
                                                                                
                                            {
                                                "messages": [
                                                    "Country code must be provided",
                                                    "Cost must be greater than or equal 0"
                                                ],
                                                "error": true
                                            }
                                            """)

                    )}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ResponseDto"),
                            examples = @ExampleObject(name = "Clearing cost entry does not exist", value = """
                                    
                                    {
                                        "messages": [
                                            "Cost entry for country code [country_code] does not exist"
                                        ],
                                        "error": true
                                    }
                                    """)

                    )})
    })
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateCost(@RequestBody ClearingCostDto costRegisterDto);

    @Operation(summary = "Delete a clearing cost matrix entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ResponseDto"),
                            examples = @ExampleObject(value = """
                                    {
                                        "messages": [
                                            "Successfully deleted"
                                        ]
                                    }
                                    """)
                    )}
            ),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/ResponseDto"),
                            examples = @ExampleObject(name = "Clearing cost entry does not exist", value = """
                                    
                                    {
                                        "messages": [
                                            "Cost entry for country code [country_code] does not exist"
                                        ],
                                        "error": true
                                    }
                                    """)

                    )})
    })
    @DeleteMapping(value = "/delete/{country_code}")
    ResponseEntity<?> deleteCost(@Parameter(name = "country_code", example = "gr", description = "The iso2 country code") @PathVariable(name = "country_code") String country_code);
}
