package bt6;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProfileServiceTest {

    private ProfileService profileService;
    private User existingUser;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService();
        existingUser = createUser("1", "original@example.com", LocalDate.of(1990, 1, 1), "Original Name");
    }

    @AfterEach
    void tearDown() {
        profileService = null;
        existingUser = null;
    }

    @Test
    void updateProfile_withValidEmailAndBirth_updatesUser() {
        UserProfile newProfile = new UserProfile("new@example.com", LocalDate.of(1991, 5, 5), "Updated Name");
        List<User> allUsers = List.of(existingUser, createUser("2", "friend@example.com", LocalDate.of(1989, 3, 3), "Friend"));

        User updated = profileService.updateProfile(existingUser, newProfile, allUsers);

        assertNotNull(updated, "Valid update should return the updated user");
        assertEquals("new@example.com", updated.getEmail(), "Email should change to the new value");
        assertEquals("Updated Name", updated.getDisplayName(), "Display name should be updated");
    }

    @Test
    void updateProfile_withFutureBirthDate_returnsNull() {
        UserProfile newProfile = new UserProfile("new@example.com", LocalDate.now().plusDays(1), "Future Birth");
        User updated = profileService.updateProfile(existingUser, newProfile, List.of(existingUser));

        assertNull(updated, "Future birth date must be rejected");
    }

    @Test
    void updateProfile_withDuplicateEmail_returnsNull() {
        User duplicate = createUser("2", "duplicate@example.com", LocalDate.of(1985, 4, 4), "Dup");
        List<User> allUsers = List.of(existingUser, duplicate);
        UserProfile newProfile = new UserProfile("duplicate@example.com", LocalDate.of(1991, 5, 5), "Collision");

        assertNull(profileService.updateProfile(existingUser, newProfile, allUsers), "Duplicate email among other users must be rejected");
    }

    @Test
    void updateProfile_withSameEmail_stillUpdatesOtherFields() {
        UserProfile newProfile = new UserProfile(existingUser.getEmail(), LocalDate.of(1992, 6, 6), "Renamed");
        List<User> allUsers = List.of(existingUser, createUser("2", "friend@example.com", LocalDate.of(1990, 2, 2), "Friend"));

        User updated = profileService.updateProfile(existingUser, newProfile, allUsers);

        assertNotNull(updated, "Keeping the same email should allow other updates");
        assertEquals("Renamed", updated.getDisplayName());
    }

    @Test
    void updateProfile_withEmptyUserList_allowsEmailChange() {
        UserProfile newProfile = new UserProfile("lonely@example.com", LocalDate.of(1993, 7, 7), "Lonely");
        User updated = profileService.updateProfile(existingUser, newProfile, List.of());

        assertNotNull(updated, "Empty user list should allow changing the email");
        assertEquals("lonely@example.com", updated.getEmail());
    }

    @Test
    void updateProfile_withNullUserList_allowsEmailChange() {
        UserProfile newProfile = new UserProfile("ghost@example.com", LocalDate.of(1993, 7, 7), "Ghost");

        User updated = profileService.updateProfile(existingUser, newProfile, null);

        assertNotNull(updated, "Missing user list should still permit valid updates");
    }

    @Test
    void updateProfile_withDuplicateEmailAndFutureBirth_returnsNull() {
        User duplicate = createUser("2", "duplicate@example.com", LocalDate.of(1985, 4, 4), "Dup");
        List<User> allUsers = List.of(existingUser, duplicate);
        UserProfile newProfile = new UserProfile("duplicate@example.com", LocalDate.now().plusDays(5), "Bad Both");

        assertNull(profileService.updateProfile(existingUser, newProfile, allUsers), "Multiple violations should still reject the update");
    }

    private User createUser(String id, String email, LocalDate birthDate, String displayName) {
        return new User(id, email, birthDate, displayName);
    }
}
