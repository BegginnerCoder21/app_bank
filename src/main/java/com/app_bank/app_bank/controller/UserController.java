package com.app_bank.app_bank.controller;

import com.app_bank.app_bank.dto.*;
import com.app_bank.app_bank.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Api de gestion des comptes utilisateur")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Creation d'un compte utilisateur",
            description = "Cette fonctionnalité permet de creer un user et lui assigne un numero de compte"
    )
    @ApiResponse(
            description = "Rétourne une reponse 201 en cas de succès",
            responseCode = "201"
    )
    @PostMapping("/register")
    public BankResponse createAccount(@RequestBody UserRequest userRequest)
    {

       return this.userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Connexion d'un compte utilisateur",
            description = "Cette fonctionnalité de se connecter à son compte"
    )
    @ApiResponse(
            description = "Rétourne une reponse 200 en cas de succès",
            responseCode = "200"
    )
    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto)
    {
        return this.userService.login(loginDto);
    }

    @Operation(
            summary = "Récuperation du solde du compte user",
            description = "Cette fonctionnalité permet recuperer le solde du user"
    )
    @ApiResponse(
            description = "Retourne une reponse 200 en cas de succès",
            responseCode = "200"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return this.userService.balanceEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Récuperation du nom du compte user",
            description = "Cette fonctionnalité permet recuperer le nom du user"
    )
    @ApiResponse(
            description = "Rétourne une reponse 200 en cas de succès",
            responseCode = "200"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return this.userService.nameEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Crédite le compte utilisateur",
            description = "Cette fonctionnalité permet de crédité le compte du user dont le numero de compte est envoyé"
    )
    @ApiResponse(
            description = "Retourne une reponse 200 en cas de succès",
            responseCode = "200"
    )
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request)
    {
        return this.userService.creditAccount(request);
    }

    @Operation(
            summary = "Débité le compte utilisateur",
            description = "Cette fonctionnalité permet de débité le compte du user dont le numero de compte est envoyé"
    )
    @ApiResponse(
            description = "Rétourne une reponse 200 en cas de succès",
            responseCode = "200"
    )
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request)
    {
        return this.userService.debitAccount(request);
    }

    @Operation(
            summary = "Transfert d'argent d'un compte à un autre",
            description = "Cette fonctionnalité permet récuperer un montant dans un compte et l'envoyer à un autre"
    )
    @ApiResponse(
            description = "Rétourne une reponse 200 en cas de succès",
            responseCode = "200"
    )
    @PostMapping("/transfertBalance")
    public BankResponse transfertAccount(@RequestBody TransfertRequest request)
    {
        return this.userService.TransfertBalance(request);
    }

}
