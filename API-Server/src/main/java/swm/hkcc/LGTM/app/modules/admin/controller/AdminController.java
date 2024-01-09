package swm.hkcc.LGTM.app.modules.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.admin.service.AdminService;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/member/{id}")
    public ApiDataResponse<Boolean> deleteMember(
            @PathVariable("id") Long id
    ) {
        try {
            return ApiDataResponse.of(adminService.deleteMember(id));
        } catch (DataAccessException e) {
            throw new GeneralException("유저의 활동 기록이 있어 삭제할 수 없는 유저입니다.", e.getCause());
        }
    }

    @GetMapping("/token/{id}")
    public ApiDataResponse<String> getMemberToken(
            @PathVariable("id") Long id
    ) {
        return ApiDataResponse.of(adminService.getMemberToken(id));
    }
}
