package com.gustavo.mobiauto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gustavo.mobiauto_backend.controller.requests.UserRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.UserNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.OfferRepository;
import com.gustavo.mobiauto_backend.infra.repositories.UserRepository;
import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.offer.OfferStatus;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleColor;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleModel;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleReleaseYear;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleType;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyActiveException;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyDeactivatedException;
import com.gustavo.mobiauto_backend.service.exceptions.DuplicateException;
import com.gustavo.mobiauto_backend.service.exceptions.EntityInUseException;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private UserService userService;

    private UserRequest validUserRequest;
    private User testUser;
    private User existingUser;
    private Offer activeOffer;
    private Offer inactiveOffer;

    private static final Long USER_ID = 1L;
    private static final Long EXISTING_USER_ID = 2L;
    private static final String USER_EMAIL = "john.doe@example.com";
    private static final String EXISTING_EMAIL = "existing@example.com";
    private static final String NEW_EMAIL = "new.email@example.com";

    @BeforeEach
    void setUp() throws Exception {
        validUserRequest = new UserRequest("John", "Doe", USER_EMAIL, "password123");

        testUser = new User("John", "Doe", USER_EMAIL, "password123");
        setUserId(testUser, USER_ID);

        existingUser = new User("Jane", "Smith", EXISTING_EMAIL, "password456");
        setUserId(existingUser, EXISTING_USER_ID);

        Store testStore = new Store("Test Store", "11.222.333/0001-81");
        Vehicle testVehicle = new Vehicle(
                VehicleType.CAR,
                new VehicleModel("Toyota Corolla"),
                new VehicleReleaseYear(2023),
                new VehicleColor("Blue"));

        activeOffer = new Offer(OfferStatus.NEW, testUser, testVehicle, testStore);
        activeOffer.setActive(true);

        inactiveOffer = new Offer(OfferStatus.COMPLETED, testUser, testVehicle, testStore);
        inactiveOffer.setActive(false);
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.registerUser(validUserRequest);

        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        assertEquals("John", result.getName().getFirstName());
        assertEquals("Doe", result.getName().getLastName());
        assertEquals(USER_EMAIL, result.getEmail().getValue());
        assertTrue(result.isActive());

        verify(userRepository).findByEmail(USER_EMAIL);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateException when registering user with existing email")
    void shouldThrowDuplicateExceptionWhenRegisteringUserWithExistingEmail() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(existingUser));

        DuplicateException exception = assertThrows(
                DuplicateException.class,
                () -> userService.registerUser(validUserRequest));

        assertNotNull(exception);
        verify(userRepository).findByEmail(USER_EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void shouldFindUserByIdSuccessfully() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));

        User result = userService.findUser(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        assertEquals(USER_EMAIL, result.getEmail().getValue());

        verify(userRepository).findById(USER_ID);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findUser(nonExistentId));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("User with id " + nonExistentId + " not found"));
        verify(userRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        User result = userService.findByEmail(USER_EMAIL);

        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        assertEquals(USER_EMAIL, result.getEmail().getValue());

        verify(userRepository).findByEmail(USER_EMAIL);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user email does not exist")
    void shouldThrowUserNotFoundExceptionWhenUserEmailDoesNotExist() {
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findByEmail(nonExistentEmail));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("User with email " + nonExistentEmail + " not found"));
        verify(userRepository).findByEmail(nonExistentEmail);
    }

    @Test
    @DisplayName("Should update user name successfully")
    void shouldUpdateUserNameSuccessfully() {
        UserRequest updateRequest = new UserRequest("Jane", "Smith", null, null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should update user email successfully")
    void shouldUpdateUserEmailSuccessfully() {
        UserRequest updateRequest = new UserRequest(null, null, NEW_EMAIL, null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).findByEmail(NEW_EMAIL);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should update user password successfully")
    void shouldUpdateUserPasswordSuccessfully() {
        UserRequest updateRequest = new UserRequest(null, null, null, "newpassword456");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should update user with all fields")
    void shouldUpdateUserWithAllFields() {
        UserRequest updateRequest = new UserRequest("Jane", "Smith", NEW_EMAIL, "newpassword456");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).findByEmail(NEW_EMAIL);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should throw DuplicateException when updating to existing email")
    void shouldThrowDuplicateExceptionWhenUpdatingToExistingEmail() {
        UserRequest updateRequest = new UserRequest(null, null, EXISTING_EMAIL, null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(EXISTING_EMAIL)).thenReturn(Optional.of(existingUser));

        DuplicateException exception = assertThrows(
                DuplicateException.class,
                () -> userService.updateUser(USER_ID, updateRequest));

        assertNotNull(exception);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).findByEmail(EXISTING_EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should allow updating to same email")
    void shouldAllowUpdatingToSameEmail() {
        UserRequest updateRequest = new UserRequest(null, null, USER_EMAIL, null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).findByEmail(USER_EMAIL);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should update only first name when last name is null")
    void shouldUpdateOnlyFirstNameWhenLastNameIsNull() {
        UserRequest updateRequest = new UserRequest("Jane", null, null, null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should update only last name when first name is null")
    void shouldUpdateOnlyLastNameWhenFirstNameIsNull() {
        UserRequest updateRequest = new UserRequest(null, "Smith", null, null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should not update when all fields are null")
    void shouldNotUpdateWhenAllFieldsAreNull() {
        UserRequest updateRequest = new UserRequest(null, null, null, null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(USER_ID, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when updating non-existent user")
    void shouldThrowUserNotFoundExceptionWhenUpdatingNonExistentUser() {
        Long nonExistentId = 999L;
        UserRequest updateRequest = new UserRequest("Jane", "Smith", null, null);
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(nonExistentId, updateRequest));

        assertNotNull(exception);
        verify(userRepository).findById(nonExistentId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should deactivate user successfully when no active offers exist")
    void shouldDeactivateUserSuccessfullyWhenNoActiveOffersExist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(offerRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.deactivateUser(USER_ID);

        assertNotNull(result);
        assertFalse(result.isActive());

        verify(userRepository).findById(USER_ID);
        verify(offerRepository).findAll();
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should deactivate user when offers exist but are inactive")
    void shouldDeactivateUserWhenOffersExistButAreInactive() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(offerRepository.findAll()).thenReturn(List.of(inactiveOffer));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.deactivateUser(USER_ID);

        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
        verify(offerRepository).findAll();
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should throw AlreadyDeactivatedException when user is already inactive")
    void shouldThrowAlreadyDeactivatedExceptionWhenUserIsAlreadyInactive() {
        testUser.setActive(false);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));

        AlreadyDeactivatedException exception = assertThrows(
                AlreadyDeactivatedException.class,
                () -> userService.deactivateUser(USER_ID));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("User with ID " + USER_ID + " is already deactivated"));

        verify(userRepository).findById(USER_ID);
        verify(offerRepository, never()).findAll();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw EntityInUseException when user has active offers")
    void shouldThrowEntityInUseExceptionWhenUserHasActiveOffers() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(offerRepository.findAll()).thenReturn(List.of(activeOffer));

        EntityInUseException exception = assertThrows(
                EntityInUseException.class,
                () -> userService.deactivateUser(USER_ID));

        assertNotNull(exception);
        assertTrue(exception.getMessage()
                .contains("User with ID " + USER_ID + " cannot be deleted because it's being used"));

        verify(userRepository).findById(USER_ID);
        verify(offerRepository).findAll();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when deactivating non-existent user")
    void shouldThrowUserNotFoundExceptionWhenDeactivatingNonExistentUser() {
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deactivateUser(nonExistentId));

        assertNotNull(exception);
        verify(userRepository).findById(nonExistentId);
        verify(offerRepository, never()).findAll();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should reactivate user successfully")
    void shouldReactivateUserSuccessfully() {
        testUser.setActive(false);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.reactivateUser(USER_ID);

        assertNotNull(result);
        assertTrue(result.isActive());

        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should throw AlreadyActiveException when user is already active")
    void shouldThrowAlreadyActiveExceptionWhenUserIsAlreadyActive() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));

        AlreadyActiveException exception = assertThrows(
                AlreadyActiveException.class,
                () -> userService.reactivateUser(USER_ID));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("User with ID " + USER_ID + " is already active"));

        verify(userRepository).findById(USER_ID);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when reactivating non-existent user")
    void shouldThrowUserNotFoundExceptionWhenReactivatingNonExistentUser() {
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.reactivateUser(nonExistentId));

        assertNotNull(exception);
        verify(userRepository).findById(nonExistentId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should verify repository interactions for successful operations")
    void shouldVerifyRepositoryInteractionsForSuccessfulOperations() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));

        userService.registerUser(validUserRequest);

        userService.findUser(USER_ID);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));
        userService.findByEmail(USER_EMAIL);

        verify(userRepository, times(2)).findByEmail(USER_EMAIL);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(USER_ID);
    }

    @Test
    @DisplayName("Should handle edge cases with user registration")
    void shouldHandleEdgeCasesWithUserRegistration() {
        UserRequest edgeCaseRequest = new UserRequest("Test", "User", "test.edge@example.com", "password");
        User edgeCaseUser = new User("Test", "User", "test.edge@example.com", "password");

        when(userRepository.findByEmail("test.edge@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(edgeCaseUser);

        User result = userService.registerUser(edgeCaseRequest);

        assertNotNull(result);
        verify(userRepository).findByEmail("test.edge@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should maintain user relationships after operations")
    void shouldMaintainUserRelationshipsAfterOperations() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));

        User result = userService.findUser(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        assertEquals(USER_EMAIL, result.getEmail().getValue());
        assertEquals("John", result.getName().getFirstName());
        assertEquals("Doe", result.getName().getLastName());
    }

    private void setUserId(User user, Long id) throws Exception {
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);
    }
}