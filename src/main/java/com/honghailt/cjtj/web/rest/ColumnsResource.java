package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.ColumnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
public class ColumnsResource {

    @Autowired
    private ColumnsService columnsService;

    /**
     * 获取业务点的列选择
     *
     * @param business
     * @return
     */
    @GetMapping("/getSelectColumns")
    public List<String> getSelectColumns(@RequestParam String business) {
        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
        List<String> result = columnsService.getSelectColumns(userDetails.getNick(), business);
        return result != null ? result : Lists.newArrayList();
    }

    /**
     * 更新业务点的列选择
     *
     * @param columns
     * @param business
     * @return
     */
    @PostMapping("/updateSelectColumns")
    public String updateSelectColumns(@RequestParam String[] columns, @RequestParam String business) {
        TaobaoUserDetails userDetails = SecurityUtils.getCurrentUser();
        columnsService.updateSelectColumns(userDetails.getNick(), business, Lists.newArrayList(columns));
        return "success";
    }

}
