package dbclass.movie.mapper;

import dbclass.movie.domain.user.Admin;
import dbclass.movie.dto.user.AdminInfoDTO;

public class AdminMapper {

    private AdminMapper() {}

    public static Admin adminInfoDTOToAdmin(AdminInfoDTO adminInfoDTO) {
        return Admin.builder()
                .loginId(adminInfoDTO.getLoginId())
                .name(adminInfoDTO.getName())
                .password(adminInfoDTO.getPassword())
                .build();
    }

    public static AdminInfoDTO adminToAdminInfoDTO(Admin admin) {
        return AdminInfoDTO.builder()
                .loginId(admin.getLoginId())
                .name(admin.getName())
                .build();
    }
}
