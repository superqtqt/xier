#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId};

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ${package}.BaseTest;
import ${package}.config.SwaggerConfig;
import ${package}.${artifactId}.dao.DomainDao;
import ${package}.${artifactId}.orm.entity.Domain;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


/**

 *
 * @author superqtqt 2021/4/29
 */
@AutoConfigureMockMvc
@ComponentScan(value = "${package}.**", lazyInit = false)
class DoaminControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DomainDao domainDao;

    @BeforeEach
    void before() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @SneakyThrows
    public void list() {
        //准备数据
        Domain domain=mock(Domain.class);
        domain.setWorkbenchId(10L);
        domainDao.insert(domain);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/domain/list?workbenchId=10");
        mockMvc.perform(builder)
                //判断请求状态
                .andExpect(status().isOk())
                //判断json的结构
                .andExpect(jsonPath("${symbol_dollar}.data[0].workbenchId").value(domain.getWorkbenchId()))
                .andDo(MockMvcResultHandlers.print());
    }
}