package com.hua.dbrouter.config;

import com.hua.dbrouter.DBRouterConfig;
import com.hua.dbrouter.DBRouterJoinPoint;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceAutoConfig implements EnvironmentAware{

    /**
     * 数据源配置组
     * value 数据源详细信息
     */
    private Map<String, Map<String,Object>> dataSourceMap = new HashMap<>();

    /**
     * 默认配置数据源
     */
    private Map<String,Object> defaultDataSourseConfig;

    /**
     * 分库数量
     */
    private int dbCount;


    /**
     * 分表数量
     */
    private int tbCount;

    /**
     * 路由字段
     */
    private String routerKey;


    public DBRouterJoinPoint point(DBRouterConfig dbRouterConfig,)


    @Override
    public void setEnvironment(Environment environment) {

    }
}
