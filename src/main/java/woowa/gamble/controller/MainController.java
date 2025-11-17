package woowa.gamble.controller;

import woowa.gamble.domain.User;
import woowa.gamble.repository.UserRepository;
import woowa.gamble.dto.UserDto;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String mainPage(Model model, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("myUserId");

        if (currentUserId != null) {
            // 1. DB에서 raw data를 가져옴
            User userEntity = userRepository.findById(currentUserId).orElse(null);

            if (userEntity != null) {
                // 2. 화면에 보여줄 용도인 DTO로 변환
                UserDto userDto = new UserDto(userEntity);
                // 3. model에는 DTO를 담음
                model.addAttribute("user", userDto);
            }
        } else {
            model.addAttribute("user", null);
        }
        return "main";
    }

    @PostMapping("/start-game")
    public String startGame(@RequestParam("nickname") String nickname, HttpSession session) {
        User newUser = new User(nickname);
        User savedUser = userRepository.save(newUser);
        session.setAttribute("myUserId", savedUser.getId());
        return "redirect:/";
    }
}