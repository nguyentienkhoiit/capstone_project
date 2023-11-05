package com.capstone.backend.entity.type;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PermissionResourceType {
    C("create"),
    V("view"),
    U("update"),
    R("remove"),
    D("download");
    String content;

    public static String sharePermission() {
        return PermissionResourceType.V.toString()
                .concat(PermissionResourceType.D.toString());
    }
}
