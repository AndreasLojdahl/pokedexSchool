package com.example.pokedex.controllers;

import com.example.pokedex.entities.User;
import com.example.pokedex.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @Operation(summary = "Find users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            //@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content)
    })
    public ResponseEntity<List<User>> findAllUsers(@RequestParam(required = false) String username) {
        var users = userService.findAll(username);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find specific user")
    @Secured("ROLE_USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "Couldn't find user", content = @Content),
    })
    public ResponseEntity<User> findUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created user",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    public ResponseEntity<User> saveUser(@Validated @RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated user",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Couldn't find user", content = @Content)
    })
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable String id, @Validated @RequestBody User user) {
        userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted user",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "Couldn't find user", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }
}

