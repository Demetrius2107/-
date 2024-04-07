package com.hua.dbrouter.config;


import com.hua.dbrouter.DBRouterConfig;
import com.hua.dbrouter.DBRouterJoinPoint;
import com.hua.dbrouter.dynamic.DynamicDataSource;
import com.hua.dbrouter.dynamic.DynamicMybatisPlugin;
import com.hua.dbrouter.strategy.IDBRouterStrategy;
import com.hua.dbrouter.strategy.impl.DBRouterStrategyHashCode;
import com.hua.dbrouter.util.PropertyUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
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


    @Bean(name = "db-router-point")
    @ConditionalOnMissingBean
    public DBRouterJoinPoint point(DBRouterConfig dbRouterConfig, IDBRouterStrategy dbRouterStrategy){
        return new DBRouterJoinPoint(dbRouterConfig,dbRouterStrategy);
    }


    @Bean
    public DBRouterConfig dbRouterConfig(){
        return new DBRouterConfig(dbCount,tbCount,routerKey);
    }

    /**
     * 配置插件 bean , 用于动态决定表信息
     * @return
     */
    @Bean
    public Interceptor plugin(){
        return new DynamicMybatisPlugin();
    }

    @Bean
    public DataSource dataSource(){
        Map<Object,Object> targetDataSources = new HashMap<>();
        for(String dbInfo : dataSourceMap.keySet()){
            Map<String,Object> objMap = dataSourceMap.get(dbInfo);
          //  targetDataSources.put(dbInfo,new DriverManagerDataSource(objMap.get("username").toString(),defaultDataSourseConfig.get("password").toString()));
            targetDataSources.put(dbInfo,new DriverManagerDataSource(objMap.get("url").toString(),objMap.get("username").toString(),objMap.get("password").toString()));
        }
        //设置数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(new DriverManagerDataSource(defaultDataSourseConfig.get("url").toString(),defaultDataSourseConfig.get("usrname").toString(),defaultDataSourseConfig.get("password").toString()));

        return dynamicDataSource;
    }


    /**
     * 依赖注入
     * @param dbRouterConfig
     * @return
     */
    @Bean
    public IDBRouterStrategy dbRouterStrategy(DBRouterConfig dbRouterConfig){
        return new DBRouterStrategyHashCode(dbRouterConfig);
    }

    /**
     *
     * @param dataSource
     * @return
     */
    @Bean
    public TransactionTemplate transactionTemplate(DataSource dataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);

        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(dataSourceTransactionManager);
        transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return transactionTemplate;
    }


    //TODO

    /**
     * 读取yml中的数据源信息
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "db-router.jdbc.datasource.";

        dbCount = Integer.valueOf(environment.getProperty(prefix + "dbCount"));
        tbCount = Integer.valueOf(environment.getProperty(prefix + "tbCount"));
        routerKey = environment.getProperty(prefix + "routerKey");

        //分库分表数据源
        String dataSources = environment.getProperty(prefix + "list");
        assert dataSources != null;
        for(String dbInfo : dataSources.split(",")){
            Map<String,Object> dataSourceProps = PropertyUtil.handle(environment,prefix + dbInfo,Map.class);
            dataSourceMap.put(dbInfo,dataSourceProps);
        }

        //默认数据源
        String defaultData = environment.getProperty(prefix + "default");
        defaultDataSourseConfig = PropertyUtil.handle(environment,prefix + defaultData ,Map.class);

    }
}
