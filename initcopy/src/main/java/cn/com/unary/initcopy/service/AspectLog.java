package cn.com.unary.initcopy.service;

import cn.com.unary.initcopy.common.AbstractLoggable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 用于做切面日志
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("AspectLog")
@Aspect
public class AspectLog extends AbstractLoggable {

    /**
     * 为 GRPC 的调用做日志
     *
     * @param pjp 切面
     * @return 该方法返回值
     * @throws Throwable 方法异常
     */
    @Around("execution(* cn.com.unary.initcopy.grpc.service.*.*Linker(...))")
    public Object logGrpcInvoke(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            logger.info(String.format("%s is %s.", arg.getClass().getSimpleName(),
                    arg.toString().replaceAll("\\n", " ")));
        }
        try {
            Object result = pjp.proceed(args);
            logger.info(String.format("%s is %s.", result.getClass().getSimpleName(),
                    result.toString().replaceAll("\\n", " ")));
            return result;
        } catch (Throwable throwable) {
            logger.error(throwable);
            throw throwable;
        }
    }

}
