package com.spring.verification.springbackendverification.controller;
import com.spring.verification.springbackendverification.model.AppUser;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.service.MaladoService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(path = "/api/v1/registration/")
public class MaladoController {

    private final MaladoService registrationService;

    @Autowired
    public MaladoController(MaladoService registrationService) {

        this.registrationService = registrationService;
    }

    @PostMapping(path = "loginad")
    public @ResponseBody String loginad(@RequestBody AppUser loginad) {
        return registrationService.loginad(loginad);
    }

    

    @PostMapping(path = "Token")
    public @ResponseBody String confirmToken(@RequestBody String token) {
        //return  String.format("token:"+token);
        return registrationService.confirmToken(token);

    }

    

    @PostMapping(path = "Password")
    public String confirmPassword(@RequestBody String password,String confirmpassword,String token) {
      return registrationService.confirmPassword(password,confirmpassword,token);
//    	return String.format("password : " +password+ "confirmpassword :"+confirmpassword+ "token :"+token);
    }




    @PostMapping(path = "register")
    public String register(@RequestBody MaladoRequest request) {
        return registrationService.register(request);

    }
    

}
