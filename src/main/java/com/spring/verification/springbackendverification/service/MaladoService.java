package com.spring.verification.springbackendverification.service;

import com.spring.verification.springbackendverification.model.AppUser;
import com.spring.verification.springbackendverification.model.MaladoUserRole;
import com.spring.verification.springbackendverification.model.MaladoRequest;
import com.spring.verification.springbackendverification.security.EmailValidator;
import com.spring.verification.springbackendverification.security.token.ConfirmationToken;
import com.spring.verification.springbackendverification.security.token.ConfirmationTokenService;
import com.spring.verification.springbackendverification.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MaladoService {
	
    private final MaladoUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmTokenService;


    public MaladoService(MaladoUserService appUserService, EmailValidator emailValidator, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmTokenService) {
        this.appUserService = appUserService;
        this.passwordEncoder= passwordEncoder;
        this.emailValidator = emailValidator;
        this.confirmTokenService = confirmTokenService;

    }
    public String loginad(AppUser request) {
                String tokenForNewUser=appUserService.loginUpUser(request);
                return tokenForNewUser;
    }
    
    
    public String register(MaladoRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (isValidEmail) {
            String CreateNewUser = appUserService.signUpUser(new AppUser(request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getLoginad(),
                    MaladoUserRole.USER));

            return CreateNewUser;
        } else {
            return String.format("Email %s, not valid", request.getEmail());
        }
    }
    
    

    @Transactional
    public String confirmToken(String token) {
        System.out.println("========================================================");
        System.out.println(token);
        Optional<ConfirmationToken> confirmToken = confirmTokenService.getToken(token);
        //return String.format("Token:"+confirmToken);
        if (confirmToken.isEmpty()) {
            return String.format("Token not found!");
        }

        if (confirmToken.get().getConfirmedAt() != null) {
//            throw new IllegalStateException("Email is already confirmed");
        	
            return String.format("Email is already confirmed");
        }

        LocalDateTime expiresAt = confirmToken.get().getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            return String.format("Token is already expired!");
        }


        confirmTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmToken.get().getAppUser().getEmail());
        

        return String.format("Your email is confirmed. confirmed you password please !");
    }
    
    @Transactional
    public String confirmPassword(String password,String confirmPassword,String token) {
        
    	int compareto = password.compareTo(confirmPassword);
    	
        if(compareto==0) {
        	
        	Optional<ConfirmationToken> confirmToken = confirmTokenService.getToken(token);
        	
        if (confirmToken.isEmpty()) {
//            throw new IllegalStateException();
            return String.format("Token not found!");
        }
        
        LocalDateTime expiresAt = confirmToken.get().getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("Token is already expired!");
            return String.format("Token is already expired!");
        }

        if (confirmToken.get().getConfirmedAt() != null){
//            throw new IllegalStateException("Email is already confirmed");
//        	String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(password);
        	
        	String passwordbase = confirmToken.get().getAppUser().getPassword();
        	
//        	System.out.print("Passwordbase "+ passwordbase + "PasswordEncoder :" + password);
        	
        //	int passwordCompare = password.compareTo(passwordbase);
        	
        	if(passwordEncoder.bCryptPasswordEncoder().matches(password,passwordbase)) {
        		
            	return String.format(" Welcome to malado ");
        		
        	}

        	return String.format("password not found");
//            return String.format("Token is already confirmed passwordEncoder : " +encodedPassword);
        }


    }
        return String.format(" password != confirmPassword ");
   } 
    
    

}
