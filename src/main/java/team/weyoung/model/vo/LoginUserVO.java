package team.weyoung.model.vo;

import java.io.Serializable;
import java.util.Date;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.Data;

/**
 * 已登录用户视图（脱敏）
 *
 * @author <a href="https://github.com/Tunan81">图南</a>
 **/
@Data
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    private SaTokenInfo token;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}