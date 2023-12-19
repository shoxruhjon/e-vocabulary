package uz.pdp.vocabulary;

import lombok.Getter;

import java.util.List;

public record VocabularyCreateDTO(String word, List<String> translations, List<String> synonyms, List<String> examples) {

}
