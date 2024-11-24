package manage.store.DTO.converter;

import manage.store.DTO.request.LoginRequest;
import manage.store.DTO.service.LoginDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoginDtoMapper {

    LoginDtoMapper INSTANCE = Mappers.getMapper(LoginDtoMapper.class);

    @Mapping(target = "id", source = "id")
    LoginDTO reqToDto(LoginRequest loginRequest);

}
