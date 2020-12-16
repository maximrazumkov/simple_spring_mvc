package ru.vniims.portal.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.vniims.portal.domains.Message;
import ru.vniims.portal.domains.User;
import ru.vniims.portal.repositories.MessageRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class MainController {

    private final MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public MainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = (!"".equals(filter)) ? messageRepository.findByTag(filter) : messageRepository.findAll();
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Message msg = new Message(text, tag, user);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            String uuidFile = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
            file.transferTo(Paths.get(uploadPath, uuidFile));
            msg.setFilename(uuidFile);
        }
        messageRepository.save(msg);
        model.addAttribute("messages", messageRepository.findAll());
        return "main";
    }
}
