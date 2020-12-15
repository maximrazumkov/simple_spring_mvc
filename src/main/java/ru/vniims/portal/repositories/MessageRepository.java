package ru.vniims.portal.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.vniims.portal.domains.Message;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByTag(String tag);
}
