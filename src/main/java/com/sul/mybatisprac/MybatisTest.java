package com.sul.mybatisprac;

import com.sul.mybatisprac.domain.Blog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    public static void main(String[] args) throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int offset = 0;
            int limit = 2;
            RowBounds rowBounds = new RowBounds(offset, limit);
            List<Blog> list = session.selectList(
                    "com.sul.mybatisprac.BlogMapper.selectBlogList",null,rowBounds);
            for (Blog blog : list) {
                System.out.println(blog.toString());
            }

        } finally {
            session.close();
        }
    }
}
