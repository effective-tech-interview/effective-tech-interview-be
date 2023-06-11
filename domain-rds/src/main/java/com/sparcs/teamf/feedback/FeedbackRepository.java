package com.sparcs.teamf.feedback;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface FeedbackRepository extends Repository<Feedback, Long> {

    Feedback save(Feedback feedback);

    Optional<Feedback> findById(Long id);
}
