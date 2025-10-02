package io.github.andrehsvictor.mooral.api.user;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Basic finders
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    // Email verification
    Optional<User> findByEmailVerificationToken(String token);
    
    @Query("SELECT u FROM User u WHERE u.emailVerificationToken = :token AND u.emailVerificationTokenExpiresAt > :now")
    Optional<User> findByValidEmailVerificationToken(@Param("token") String token, @Param("now") Instant now);
    
    List<User> findByEmailVerifiedFalseAndEmailVerificationTokenExpiresAtBefore(Instant expiredBefore);

    // Password reset
    Optional<User> findByPasswordResetToken(String token);
    
    @Query("SELECT u FROM User u WHERE u.passwordResetToken = :token AND u.passwordResetTokenExpiresAt > :now")
    Optional<User> findByValidPasswordResetToken(@Param("token") String token, @Param("now") Instant now);
    
    List<User> findByPasswordResetTokenIsNotNullAndPasswordResetTokenExpiresAtBefore(Instant expiredBefore);

    // Email change
    Optional<User> findByEmailChangeToken(String token);
    
    @Query("SELECT u FROM User u WHERE u.emailChangeToken = :token AND u.emailChangeTokenExpiresAt > :now")
    Optional<User> findByValidEmailChangeToken(@Param("token") String token, @Param("now") Instant now);
    
    List<User> findByEmailChangeTokenIsNotNullAndEmailChangeTokenExpiresAtBefore(Instant expiredBefore);

    // Search and filtering
    @Query("SELECT u FROM User u WHERE " +
           "(:searchTerm IS NULL OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.emailVerified = :emailVerified")
    Page<User> findByEmailVerified(@Param("emailVerified") boolean emailVerified, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByCreatedAtBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    // Statistics and counts
    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = true")
    long countVerifiedUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = false")
    long countUnverifiedUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :since")
    long countUsersCreatedSince(@Param("since") Instant since);

    // Bulk operations
    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true, u.emailVerificationToken = null, u.emailVerificationTokenExpiresAt = null WHERE u.id = :userId")
    int verifyUserEmail(@Param("userId") UUID userId);
    
    @Modifying
    @Query("UPDATE User u SET u.passwordResetToken = null, u.passwordResetTokenExpiresAt = null WHERE u.passwordResetTokenExpiresAt < :expiredBefore")
    int clearExpiredPasswordResetTokens(@Param("expiredBefore") Instant expiredBefore);
    
    @Modifying
    @Query("UPDATE User u SET u.emailVerificationToken = null, u.emailVerificationTokenExpiresAt = null WHERE u.emailVerificationTokenExpiresAt < :expiredBefore")
    int clearExpiredEmailVerificationTokens(@Param("expiredBefore") Instant expiredBefore);
    
    @Modifying
    @Query("UPDATE User u SET u.emailChangeToken = null, u.emailChangeTokenExpiresAt = null, u.emailChangeNewEmail = null WHERE u.emailChangeTokenExpiresAt < :expiredBefore")
    int clearExpiredEmailChangeTokens(@Param("expiredBefore") Instant expiredBefore);

    // Profile related
    @Query("SELECT u FROM User u WHERE u.avatarUrl IS NOT NULL")
    List<User> findUsersWithAvatar();
    
    @Query("SELECT u FROM User u WHERE u.bio IS NOT NULL AND LENGTH(u.bio) > 0")
    List<User> findUsersWithBio();

    // Recent activity
    @Query("SELECT u FROM User u ORDER BY u.updatedAt DESC")
    Page<User> findRecentlyUpdated(Pageable pageable);
    
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    Page<User> findRecentlyCreated(Pageable pageable);
    
}