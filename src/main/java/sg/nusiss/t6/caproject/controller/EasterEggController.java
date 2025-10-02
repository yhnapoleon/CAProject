package sg.nusiss.t6.caproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EasterEggController {

    @GetMapping("/easter-egg")
    public String easterEgg() {
        return "easter-egg";
    }
}
