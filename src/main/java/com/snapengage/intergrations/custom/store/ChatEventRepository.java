package com.snapengage.intergrations.custom.store;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatEventRepository extends CrudRepository<ChatEvent, String> {
    @Query("#{#n1ql.selectEntity}")
    List<ChatEvent> findAll();
}
