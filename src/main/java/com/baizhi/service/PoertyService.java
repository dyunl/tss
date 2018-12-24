package com.baizhi.service;

import com.baizhi.entity.Poetry;
import com.baizhi.view.MessageCount;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PoertyService {

    public List<Poetry> queryAll();
    MessageCount query(String message, Integer page, Integer pageSize) throws Exception;







}
