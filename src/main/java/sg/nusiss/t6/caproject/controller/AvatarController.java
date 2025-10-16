//By Xu Wenzhe

package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/avatars")
public class AvatarController {

    @Value("${app.avatar-path}")
    private String avatarPath;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> listAvatars() {
        File dir = new File(avatarPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        File[] files = dir.listFiles((d, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                    || lower.endsWith(".gif");
        });

        List<Map<String, Object>> result = new ArrayList<>();
        if (files != null) {
            int id = 1;
            for (File f : files) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", id++);
                // Map app.avatar-path to /avatars/** via ImageConfig
                item.put("url", "/avatars/" + f.getName());
                result.add(item);
            }
        }

        return ResponseEntity.ok(result);
    }
}
