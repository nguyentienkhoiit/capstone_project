package com.capstone.backend.controller;

import com.capstone.backend.model.dto.role.RoleDTOFilter;
import com.capstone.backend.model.dto.role.RoleDTORequest;
import com.capstone.backend.model.dto.role.RoleDTOUpdate;
import com.capstone.backend.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(API_VERSION + "/role")
@Tag(name = "Role", description = "API for role")
@CrossOrigin
public class RoleController {
    RoleService roleService;

    @GetMapping("/display")
    public ResponseEntity<?> viewSearchRole(@ModelAttribute RoleDTOFilter request) {
        return ResponseEntity.ok(roleService.viewSearchRole(request));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListRoleUser() {
        return ResponseEntity.ok(roleService.getListRoleUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDTORequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> UpdateRole(@PathVariable Long id, @RequestBody RoleDTOUpdate request) {
        request.setRoleId(id);
        return ResponseEntity.ok(roleService.updateRole(request));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.changeStatus(id));
    }
}
