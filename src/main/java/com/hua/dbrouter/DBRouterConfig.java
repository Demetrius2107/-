package com.hua.dbrouter;

/**
 * 数据路由配置
 */
public class DBRouterConfig {

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

    public DBRouterConfig(){}

    public DBRouterConfig(int dbCount,int tbCount,String routerKey){
        this.dbCount = dbCount;
        this.tbCount = tbCount;
        this.routerKey = routerKey;
    }

    public int getDbCount() {
        return dbCount;
    }

    public int getTbCount() {
        return tbCount;
    }

    public String getRouterKey() {
        return routerKey;
    }

    public void setDbCount(int dbCount) {
        this.dbCount = dbCount;
    }

    public void setTbCount(int tbCount) {
        this.tbCount = tbCount;
    }

    public void setRouterKey(String routerKey) {
        this.routerKey = routerKey;
    }

}
