package manage.store.service;

public interface SecretService {

    /**
     * 사용자의 비밀번호를 암호화한다.
     * @param password 사용자의 비밀번호
     * @return String 암호화된 비밀번호
     */
    String encrypt(String password);

    /**
     * 사용자의 비밀번호를 복호화한다.
     * @param encryptedPassword 암호화된 비밀번호
     * @return String 복호화된 비밀번호
     */
    String verify(String encryptedPassword);

}
