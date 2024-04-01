package com.hua.dbrouter.dynamic;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.regex.Pattern;

@Intercepts({@Signature(type = StatementHandler.class,method = "prepare", args =  {Connection.class, Integer.class})})
public class DynamicMybatisPlugin implements Interceptor {

    private Pattern pattern = Pattern.compile("(from|into|update)[\\s]{1,}(\\w{1,})", Pattern.CASE_INSENSITIVE);



    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return null;
    }
}
