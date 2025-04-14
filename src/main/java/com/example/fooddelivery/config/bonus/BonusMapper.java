package com.example.fooddelivery.config.bonus;




import com.example.fooddelivery.dto.bonus.BonusDto;
import com.example.fooddelivery.entity.Bonus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BonusMapper {

    private final ModelMapper mapper;

    public BonusMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }


    public BonusDto toBonusDto(Bonus bonus) {
        return mapper.map(bonus, BonusDto.class);
    }
}