package dbclass.movie.mapper;

import dbclass.movie.domain.user.Admin;
import dbclass.movie.dto.user.AdminInfoDTO;
import dbclass.movie.dto.user.AdminInfoDTOExcludePassword;

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

    public static AdminInfoDTOExcludePassword adminToAdminInfoDTOExcludePassword(Admin admin) {
        return AdminInfoDTOExcludePassword.builder()
                .adminId(admin.getAdminId())
                .loginId(admin.getLoginId())
                .name(admin.getName())
                .build();
    }
}
