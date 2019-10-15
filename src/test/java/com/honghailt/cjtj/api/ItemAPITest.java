package com.honghailt.cjtj.api;

import com.honghailt.cjtj.CjtjApp;

import com.honghailt.cjtj.api.Item.ItemAPI;
import com.honghailt.cjtj.service.ItemService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CjtjApp.class)
@ActiveProfiles({"dev"})
public class ItemAPITest {

    @Autowired
    private ItemAPI itemAPI;
    @Autowired
    private ItemService itemService;



}
