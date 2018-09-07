package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shark
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecResultDO implements Entity {
    private boolean healthy;
    private int code;
    private String msg;
}
