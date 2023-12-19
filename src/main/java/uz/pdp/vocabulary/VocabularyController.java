package uz.pdp.vocabulary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.config.security.SessionUser;
import uz.pdp.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vocabulary")
public class VocabularyController {

    private final SessionUser sessionUser;
    private final VocabularyDao vocabularyDao;

    public VocabularyController(SessionUser sessionUser, VocabularyDao vocabularyDao) {
        this.sessionUser = sessionUser;
        this.vocabularyDao = vocabularyDao;
    }

    @GetMapping("/home")
    public String homePage(Model model, @RequestParam(required = false, defaultValue = "4") int limit) {
        List<Vocabulary> vocabularies = vocabularyDao.findByUserID(sessionUser.getId(), limit);
        model.addAttribute("vocabularies", vocabularies);
        model.addAttribute("lim", limit);
        return "home";
    }

    @GetMapping("/add")
    public String createVocabularyPage() {
        return "vocabulary_create";
    }

    @GetMapping("/all")
    public String allVocabularyPage(Model model) {
        List<Vocabulary> vocabularyList = vocabularyDao.findByUserID(sessionUser.getId());
        Map<LocalDate, List<Vocabulary>> vocabularies = vocabularyList.stream()
                .collect(Collectors.groupingBy(Vocabulary::getCreatedDate));
        model.addAttribute("vocabularies", vocabularies);
        return "vocabulary_list";
    }

    @PostMapping("/vocabulary/add")
    public String createVocabulary(@ModelAttribute VocabularyCreateDTO dto) {
        // TODO: 12/5/2023 change userID from static to SecurityContextHolder's user id
        vocabularyDao.save(dto, sessionUser.getId());
        return "redirect:/vocabulary/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteVocabularyPage(Model model, @PathVariable Long id){
        Vocabulary vocabulary = vocabularyDao.findByVocabularyID(id);
        model.addAttribute("vocabulary", vocabulary);
        return "vocabulary_delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteVocabulary(@PathVariable Long id){
        System.out.println(id);
        vocabularyDao.deleteVocabulary(id);
        return "redirect:/vocabulary/home";
    }
}
