package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.request.UserRequestDTO;
import com.example.Tiffin_Management.dto.response.UserResponseDTO;
import com.example.Tiffin_Management.entity.User;
import com.example.Tiffin_Management.exception.BadRequestException;
import com.example.Tiffin_Management.repository.UserRepository;
import com.example.Tiffin_Management.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ShopService shopService;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getPhone() != null && userRepository.existsByPhone(userRequestDTO.getPhone())) {
            throw new BadRequestException("This phone number is already registered.");
        }

        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setPhone(userRequestDTO.getPhone());
        user.setShop(shopService.getCurrentShop());

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    public Page<UserResponseDTO> getAllUsers(int page, int size) {
        Long shopId = shopService.getCurrentShop().getId();
        return userRepository.findAllByShop_Id(shopId, PageRequest.of(page, size))
                .map(this::mapToResponseDTO);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        return dto;
    }
}
