package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.UserRequestDTO;
import com.example.Tiffin_Management.dto.response.UserResponseDTO;
import com.example.Tiffin_Management.entity.User;
import com.example.Tiffin_Management.exception.BadRequestException;
import com.example.Tiffin_Management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_Success() {
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("John Doe");
        requestDTO.setPhone("1234567890");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setPhone("1234567890");

        when(userRepository.existsByPhone(requestDTO.getPhone())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponseDTO responseDTO = userService.createUser(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("John Doe", responseDTO.getName());
        assertEquals("1234567890", responseDTO.getPhone());

        verify(userRepository, times(1)).existsByPhone(requestDTO.getPhone());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DuplicatePhoneThrowsException() {
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("Jane Doe");
        requestDTO.setPhone("0987654321");

        when(userRepository.existsByPhone(requestDTO.getPhone())).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> userService.createUser(requestDTO));
        assertEquals("This phone number is already registered.", exception.getMessage());

        verify(userRepository, times(1)).existsByPhone(requestDTO.getPhone());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_ReturnsList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User One");
        user1.setPhone("1111111111");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User Two");
        user2.setPhone("2222222222");

        Page<User> userPage = new PageImpl<>(List.of(user1, user2));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(userRepository.findAll(pageRequest)).thenReturn(userPage);

        // Act
        Page<UserResponseDTO> resultPage = userService.getAllUsers(0, 10);

        // Assert
        assertNotNull(resultPage);
        assertEquals(2, resultPage.getContent().size());

        UserResponseDTO response1 = resultPage.getContent().get(0);
        assertEquals(1L, response1.getId());
        assertEquals("User One", response1.getName());

        UserResponseDTO response2 = resultPage.getContent().get(1);
        assertEquals(2L, response2.getId());
        assertEquals("User Two", response2.getName());

        verify(userRepository, times(1)).findAll(pageRequest);
    }
}
