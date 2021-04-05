package io.github.superqtqt.datag.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Page<T> {
    public static final int DEF_PAGE_SIZE = 10;
    public static final int DEF_CURR_PAGE = 1;
    @ApiModelProperty(name = "数据结构", position = 0)
    @Getter
    @Setter
    private List<T> list;
    @ApiModelProperty(name = "总行数", position = 1)
    @Getter
    @Setter
    private long total;
    @ApiModelProperty(name = "当前页码", position = 2)
    @Getter
    @Setter
    private int pageNo = 1;
    @ApiModelProperty(name = "每页行业", position = 3)
    @Getter
    @Setter
    private int pageSize = 10;

    public static Page of(Integer currPage, Integer pageSize) {
        Page page = new Page();
        page.setPageNo(currPage == null ? 1 : currPage);
        page.setPageSize(pageSize == null ? 10 : pageSize);
        return page;
    }

    public com.baomidou.mybatisplus.extension.plugins.pagination.Page toMybatisPlus() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page(pageNo, pageSize);
    }

    public static <T> Page<T> fromMybatis(com.baomidou.mybatisplus.extension.plugins.pagination.Page data) {
        Page<T> page = new Page<>();
        page.setPageSize(Long.valueOf(data.getSize()).intValue());
        page.setPageNo(Long.valueOf(data.getCurrent()).intValue());
        page.setTotal(data.getTotal());
        page.setList(data.getRecords());
        return page;
    }

}
