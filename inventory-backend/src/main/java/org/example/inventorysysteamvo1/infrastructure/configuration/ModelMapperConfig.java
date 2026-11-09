package org.example.inventorysysteamvo1.infrastructure.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * General infrastructure configuration.
 *
 * Also activates JPA auditing here so that @CreatedBy / @LastModifiedBy /
 * @CreatedDate / @LastModifiedDate annotations on entity classes are
 * automatically populated by AuditorAwareImpl.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class ModelMapperConfig {

    /**
     * ModelMapper bean with STRICT matching strategy.
     *
     * STRICT prevents accidental field name collisions between unrelated
     * DTOs — the mapper only maps fields whose names and types match exactly.
     * setSkipNullEnabled(true) means a null source field will never overwrite
     * an existing non-null destination field (safe for partial updates).
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
        return mapper;
    }
}