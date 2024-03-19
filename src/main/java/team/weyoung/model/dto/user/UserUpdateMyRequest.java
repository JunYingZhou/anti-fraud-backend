package team.weyoung.model.dto.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 用户更新个人信息请求
 *
 * @author <a href="https://github.com/Tunan81">图南</a>
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 地址
     */
    private String address;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 公司
     */
    private String company;

    /**
     * 职位
     */
    private String position;

    /**
     * 就读学校
     */
    private String school;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 个人网站(Json)
     */
    private String website;


    private static final long serialVersionUID = 1L;
}