package team.weyoung.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @author <a href="https://github.com/Tunan81">图南</a>
 */
@Data
@Table("user")
public class User implements Serializable {

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @ExcelIgnore
    private Long id;

    /**
     * 用户账号
     */
    @ExcelProperty("姓名")
    @ColumnWidth(35)
    private String userAccount;

    /**
     * 用户密码
     */
    @ExcelIgnore
    private String userPassword;

    /**
     * 用户昵称
     */
    @ExcelIgnore
    private String userName;

    /**
     * 用户头像
     */
    @ExcelIgnore
    private String userAvatar;

    /**
     * 用户简介
     */
    @ExcelIgnore
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @ExcelIgnore
    private String userRole;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 地址
     */
    @ColumnMask(Masks.ADDRESS)
    private String address;

    /**
     * 标签
     */
    private String tags;

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
    @ColumnMask(Masks.MOBILE)
    private String phone;

    /**
     * 邮箱
     */
    @ColumnMask(Masks.EMAIL)
    private String email;

    /**
     * 个人网站(Json)
     */
    private String website;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelIgnore
    private Date updateTime;

    /**
     * 是否删除
     */
    @Column(isLogicDelete = true)
    @ExcelIgnore
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}