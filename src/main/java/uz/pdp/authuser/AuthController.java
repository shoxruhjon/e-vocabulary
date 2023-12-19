package uz.pdp.authuser;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserDao authUserDao;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthUserDao authUserDao, PasswordEncoder passwordEncoder) {
        this.authUserDao = authUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam(required = false) String error){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/auth/login");
        modelAndView.addObject("errorMessage", error);
        return modelAndView;
    }
    @GetMapping("/logout")
    public String logoutPage(){
        return "auth/logout";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "auth/register";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute AuthUserCreateDTO dto){
        AuthUser authUser = AuthUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role(AuthUser.AuthRole.USER.name())
                .build();
        Long id = authUserDao.save(authUser);
        System.out.println(id);
        return "redirect:/auth/login";
    }
}
