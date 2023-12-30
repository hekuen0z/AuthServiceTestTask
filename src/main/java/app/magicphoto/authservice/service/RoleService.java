package app.magicphoto.authservice.service;

import app.magicphoto.authservice.model.dao.Role;
import app.magicphoto.authservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepo;

    @Autowired
    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    /**
     * Finds a role by its name.
     *
     * This method searches for a role in the role repository based on the specified name.
     * If a role with the given name is found, it is returned. Otherwise, null is returned.
     *
     * @param name The name of the role to find.
     * @return The Role object with the specified name, or null if not found.
     */
    public Role findByName(String name) {
        return roleRepo.findByName(name).orElse(null);
    }

}
