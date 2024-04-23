package cn.tycoding.langchat.aigc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tycoding
 * @since 2024/4/15
 */
@Data
@TableName(autoResultMap = true)
public class AigcKb implements Serializable {
    private static final long serialVersionUID = 548724967827903685L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 知识库名称
     */
    private String name;

    /**
     * 描述
     */
    private String des;

    /**
     * 创建时间
     */
    private String createTime;

}

