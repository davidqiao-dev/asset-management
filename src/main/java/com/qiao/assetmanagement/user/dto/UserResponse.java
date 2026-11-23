package com.qiao.assetmanagement.user.dto;

import com.qiao.assetmanagement.user.Role;

public record UserResponse(
        Long id,
        String username,
        Role role
) {
}
