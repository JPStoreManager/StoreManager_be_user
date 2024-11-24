package manage.store.DTO.converter;

import javax.annotation.processing.Generated;
import manage.store.DTO.request.LoginRequest;
import manage.store.DTO.service.LoginDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-24T13:34:55+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
public class LoginDtoMapperImpl implements LoginDtoMapper {

    @Override
    public LoginDTO reqToDto(LoginRequest loginRequest) {
        if ( loginRequest == null ) {
            return null;
        }

        String id = null;
        String password = null;

        id = loginRequest.getId();
        password = loginRequest.getPassword();

        LoginDTO loginDTO = new LoginDTO( id, password );

        return loginDTO;
    }
}
