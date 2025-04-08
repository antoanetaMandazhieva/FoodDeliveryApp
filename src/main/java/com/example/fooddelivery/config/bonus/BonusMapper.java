package com.example.fooddelivery.config.bonus;



import com.example.fooddelivery.config.common.Mapper;
import com.example.fooddelivery.dto.bonus.BonusDto;
import com.example.fooddelivery.entity.Bonus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BonusMapper {

    private static final ModelMapper mapper = Mapper.getInstance();


    public static BonusDto toBonusDto(Bonus bonus) {
        return mapper.map(bonus, BonusDto.class);
    }
}