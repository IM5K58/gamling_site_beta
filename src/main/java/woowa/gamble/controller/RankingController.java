package woowa.gamble.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import woowa.gamble.domain.User;
import woowa.gamble.repository.UserRepository;

import java.util.List;

@Controller
public class RankingController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/ranking")
    public String rankingPage(Model model) {
        // 1. DB에서 돈 많은 순서대로 20명을 가져오기
        List<User> topUsers = userRepository.findTop20ByOrderByMoneyDesc();

        // 2. "topUsers"라는 이름으로 명단을 포장해서 화면에 보내기
        model.addAttribute("topUsers", topUsers);

        return "ranking"; // templates/ranking.html 을 보여줌
    }
}