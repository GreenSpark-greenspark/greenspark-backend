package GreenSpark.greenspark.service;

import GreenSpark.greenspark.domain.Category;
import GreenSpark.greenspark.domain.EnergyTips;
import GreenSpark.greenspark.dto.DictionaryResponseDto;
import GreenSpark.greenspark.dto.PointResponseDto;
import GreenSpark.greenspark.repository.CategoryRepository;
import GreenSpark.greenspark.repository.EnergyTipsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DictionaryService {

    private final CategoryRepository categoryRepository;
    private final EnergyTipsRepository energyTipsRepository;

    public List<DictionaryResponseDto.EnergyTipsGetAllResponseDto> getAllEnergyTips(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        List<EnergyTips> energyTipList = energyTipsRepository.findByCategory(category);

        return energyTipList.stream()
                .map(energyTips -> new DictionaryResponseDto.EnergyTipsGetAllResponseDto(
                        energyTips.getEnergyTipsId(), energyTips.getTipContent()))
                .collect(Collectors.toList());
    }
}
