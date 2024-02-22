package server.gdproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping("/search")
    private String search() {
        return "search";
    }

    @GetMapping("/about")
    private String about() {
        return "about";
    }
}
