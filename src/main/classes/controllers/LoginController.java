package main.classes.controllers;

import main.classes.dao.UserDAO;
import main.classes.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clinic")
public class LoginController {

    private UserDAO userDAO;
    @Autowired
    public LoginController( UserDAO userDAO){

        this.userDAO=userDAO;
    }
    @GetMapping()
    public String welcome(){
        return"welcome";
    }

    @GetMapping("/register")
    public String register(Model model){

        model.addAttribute("user",new User());
        return"LoginController/register";

    }

    @PostMapping()
    public String create(@ModelAttribute("user")User user,Model model){
        System.out.println("метод криейт");
        String regSuccess="redirect:/clinic/authorization";
        String regFailed="LoginController/register";
        String returning=null;
        int createResult= userDAO.save(user);
        switch (createResult) {
            case (0):
                //Успешная регистрация
             returning=regSuccess;
             break;
            case(1):
                //Больше 16 символов
                model.addAttribute("registerError","long not more 16 chars");
                returning=regFailed;
                break;
            case(2):
                model.addAttribute("registerError", "This login are used");
                returning=regFailed;
                //Такой логин уже сущесвтует
                break;
            case(3):
                returning=regFailed;
                //SQLException
                model.addAttribute("registerError","SQL Exception");
                break;
            case(4):
                returning=regFailed;
                model.addAttribute("registerError","Java Error");
                //Exception
        }

       return returning;
    }
    //Реализовать авторизацию,подумать,как сделать это через 1 метод
    @GetMapping("/authorization")
    public String authorization(Model model){
        model.addAttribute("userLogin",new User());
        return "LoginController/authorization";
    }
    @GetMapping("/authorization/login")
    public String login(@ModelAttribute("userLogin")User user,Model model){
        String returning=null;
        String loginFailed="LoginController/authorization";
        String loginSuccess="redirect:/clinic/cabinet";
        int loginResult=userDAO.login(user);
        switch (loginResult){
            case(0):
                returning=loginSuccess;
                break;
            case(1):
                returning=loginFailed;
                model.addAttribute("loginError","Login failed!Check you input");
                break;
            case(2):
                returning=loginFailed;
                model.addAttribute("loginError","Error!no more 16 chars");
                break;
            case(3):
                returning=loginFailed;
                model.addAttribute("loginError","SQLException");
                break;
            case(4):
                returning=loginFailed;
                model.addAttribute("loginError","Exception!");
                break;
        }
        return returning;
    }
}
