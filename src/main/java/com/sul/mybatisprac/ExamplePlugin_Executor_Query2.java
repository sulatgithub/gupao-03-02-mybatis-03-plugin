package com.sul.mybatisprac;


import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

@Intercepts({@Signature(
        type= Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class ExamplePlugin_Executor_Query2 implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        if (invocation.getTarget().getClass() == CachingExecutor.class) {

            RowBounds rowBounds = (RowBounds)invocation.getArgs()[2];
            int limit = rowBounds.getLimit();
            int offset = rowBounds.getOffset();
            invocation.getArgs()[2] = new RowBounds();
            MappedStatement ms = (MappedStatement)invocation.getArgs()[0];
            RawSqlSource rss = (RawSqlSource)ms.getSqlSource();
            Field sssField = rss.getClass().getDeclaredField("sqlSource");
            sssField.setAccessible(true);
            StaticSqlSource sss = (StaticSqlSource)sssField.get(rss);
            Field sqlField = sss.getClass().getDeclaredField("sql");

            sqlField.setAccessible(true);
            String sql = (String)sqlField.get(sss);
          //System.out.println("SQL："+sql);
            sqlField.set(sss,sql + " limit "+limit+" offset "+offset);

            Object result = invocation.proceed();
            long end = System.currentTimeMillis();
            System.out.println("执行时间："+(end - start));
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