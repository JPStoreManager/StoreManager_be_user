package manage.store.user.DTO.converter;

import manage.store.user.DTO.request.LoginRequest;
import manage.store.user.DTO.service.LoginDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoginDtoMapper {

    LoginDtoMapper INSTANCE = Mappers.getMapper(LoginDtoMapper.class);

    @Mapping(target = "id", source = "id")
    LoginDTO reqToDto(LoginRequest loginRequest);

}
