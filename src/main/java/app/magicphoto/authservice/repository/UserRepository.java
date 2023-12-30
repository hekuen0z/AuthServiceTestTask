package app.magicphoto.authservice.repository;

import app.magicphoto.authservice.model.dao.CustomUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<CustomUser, Long> {

    Optional<CustomUser> findByLogin(String login);

    Optional<CustomUser> findByAccessCode(String accessCode);

    Boolean existsByLogin(String login);

    Boolean existsByAccessCode(String accessCode);

    @Query("select u.accessCode from CustomUser u")
    List<String> findAllAccessCodes();

}
