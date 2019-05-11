package com.sul.mybatisprac;


import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(
        type= StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class})})
public class ExamplePlugin_StatementHandler_prepare implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget().getClass() == RoutingStatementHandler.class) {
            RoutingStatementHandler rsh = (RoutingStatementHandler)invocation.getTarget();
            Field delegateField = RoutingStatementHandler.class.getDeclaredField("delegate");
            delegateField.setAccessible(true);
            BoundSql boundSql = rsh.getBoundSql();
            System.out.println("SQL："+boundSql.getSql());
            Object result = invocation.proceed();
            return result;
        } else {
            return invocation.proceed();
        }
    }
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    public void setProperties(Properties properties) {
    }
}