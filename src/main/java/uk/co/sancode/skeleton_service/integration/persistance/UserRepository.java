package uk.co.sancode.skeleton_service.integration.persistance;

import org.springframework.data.repository.PagingAndSortingRepository;
import uk.co.sancode.skeleton_service.model.User;

import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
}
