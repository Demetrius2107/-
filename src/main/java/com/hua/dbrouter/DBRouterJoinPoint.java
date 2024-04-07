package com.hua.dbrouter;

import com.hua.dbrouter.strategy.IDBRouterStrategy;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据路由切面 通过自定义注解的方式 拦截被切面的方法,进行数据库路由
 */
@Aspect
public class DBRouterJoinPoint {

    private Logger logger = LoggerFactory.getLogger(DBRouterJoinPoint.class);

    private DBRouterConfig dbRouterConfig;

    private IDBRouterStrategy dbRouterStrategy;

    public DBRouterJoinPoint(DBRouterConfig dbRouterConfig, IDBRouterStrategy dbRouterStrategy){
        this.dbRouterConfig = dbRouterConfig;
        this.dbRouterStrategy = (IDBRouterStrategy) dbRouterStrategy;
    }

    @Pointcut("@annotation(com.hua.dbrouter.annotation.DBRouter)")
    public void appPoint(){
    }

    public String getAttrValue(String attr,Object[] args){
        if( 1 == args.length){
            Object arg = args[0];
            if( arg instanceof  String){
                return arg.toString();
            }
        }

        String filedValue =  null;
        for(Object arg : args){
            try{
                if(StringUtils.isNotBlank(filedValue)){
                    break;
                }
                filedValue = BeanUtils.getProperty(arg,attr);
            } catch (Exception e){
                logger.error("获取路由属性值失败 attr:{}",attr,e);
            }
        }
        return filedValue;
    }


}
