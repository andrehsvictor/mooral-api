package io.github.andrehsvictor.mooral.api.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "passwordHash", "emailVerificationToken", "emailChangeToken", "passwordResetToken" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    private static final long serialVersionUID = 9193729052607221924L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    @Size(max = 255, message = "Username must not exceed 255 characters")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "email_verification_token")
    @Size(max = 255, message = "Email verification token must not exceed 255 characters")
    private String emailVerificationToken;

    @Column(name = "email_verification_token_expires_at")
    private Instant emailVerificationTokenExpiresAt;

    @Column(name = "email_change_token")
    @Size(max = 255, message = "Email change token must not exceed 255 characters")
    private String emailChangeToken;

    @Column(name = "email_change_token_expires_at")
    private Instant emailChangeTokenExpiresAt;

    @Column(name = "email_change_new_email")
    @Email(message = "New email must be valid")
    @Size(max = 255, message = "New email must not exceed 255 characters")
    private String emailChangeNewEmail;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lastName;

    @Column(name = "avatar_url")
    @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "password_hash", columnDefinition = "TEXT")
    private String passwordHash;

    @Column(name = "password_reset_token")
    @Size(max = 255, message = "Password reset token must not exceed 255 characters")
    private String passwordResetToken;

    @Column(name = "password_reset_token_expires_at")
    private Instant passwordResetTokenExpiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isEmailVerificationTokenExpired() {
        return emailVerificationTokenExpiresAt != null &&
                emailVerificationTokenExpiresAt.isBefore(Instant.now());
    }

    public boolean isPasswordResetTokenExpired() {
        return passwordResetTokenExpiresAt != null &&
                passwordResetTokenExpiresAt.isBefore(Instant.now());
    }

    public boolean isEmailChangeTokenExpired() {
        return emailChangeTokenExpiresAt != null &&
                emailChangeTokenExpiresAt.isBefore(Instant.now());
    }

    public void clearEmailVerificationToken() {
        this.emailVerificationToken = null;
        this.emailVerificationTokenExpiresAt = null;
    }

    public void clearPasswordResetToken() {
        this.passwordResetToken = null;
        this.passwordResetTokenExpiresAt = null;
    }

    public void clearEmailChangeToken() {
        this.emailChangeToken = null;
        this.emailChangeTokenExpiresAt = null;
        this.emailChangeNewEmail = null;
    }

    public void verifyEmail() {
        this.emailVerified = true;
        clearEmailVerificationToken();
    }
}
