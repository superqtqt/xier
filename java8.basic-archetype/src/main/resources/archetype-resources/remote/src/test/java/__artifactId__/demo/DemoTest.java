package ${package}.${artifactId}.demo;

import ${package}.${artifactId}.App;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = App.class)
class DemoTest
{
    @Autowired
    private Demo demo;

    @Test
    void testDemo()
    {
        System.out.println(demo.searchBaidu("123"));
    }
}