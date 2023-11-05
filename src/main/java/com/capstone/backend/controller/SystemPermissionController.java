package com.capstone.backend.controller;

import com.capstone.backend.model.dto.systempermission.SystemPermissionDTOFilter;
import com.capstone.backend.model.dto.systempermission.SystemPermissionDTORequest;
import com.capstone.backend.model.dto.systempermission.SystemPermissionDTOUpdate;
import com.capstone.backend.service.SystemPermissionService;
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
@RequestMapping(API_VERSION + "/system-permission")
@Tag(name = "System permission", description = "API for System Permission")
@CrossOrigin
public class SystemPermissionController {
    SystemPermissionService systemPermissionService;

    @GetMapping("/display")
    public ResponseEntity<?> viewSearchPermission(
            @ModelAttribute SystemPermissionDTOFilter request
    ) {
        return ResponseEntity.ok(systemPermissionService.viewSearchPermission(request));
    }

    @GetMapping("/list/{roleId}")
    public ResponseEntity<?> getSystemPermissionsByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(systemPermissionService.getSystemPermissionsByRole(roleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSystemPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(systemPermissionService.getSystemPermissionById(id));
    }

    @PostMapping
    public ResponseEntity<?> createSystemPermission(
            @RequestBody SystemPermissionDTORequest systemPermissionDTORequest
    ) {
        return ResponseEntity.ok(systemPermissionService
                .createSystemPermission(systemPermissionDTORequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSystemPermission(
            @RequestBody SystemPermissionDTOUpdate systemPermissionDTOUpdate,
            @PathVariable Long id
    ) {
        systemPermissionDTOUpdate.setPermissionId(id);
        return ResponseEntity.ok(systemPermissionService
                .updateSystemPermission(systemPermissionDTOUpdate));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestParam(name = "active") Boolean active) {
        return ResponseEntity.ok(systemPermissionService.changeStatus(active, id));
    }

}
