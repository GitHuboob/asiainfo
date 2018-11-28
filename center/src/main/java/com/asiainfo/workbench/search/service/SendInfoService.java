package com.asiainfo.workbench.search.service;

import com.asiainfo.workbench.search.domain.SendInfo;

import java.util.List;

public interface SendInfoService {

    boolean insert(SendInfo sendInfo);

    boolean delete(long id);

    SendInfo getOne(long id);

    /**
     * 根据关键字进行全文搜索
     * @param searchContent
     * @return
     */
    List<SendInfo> search(String searchContent);

    /**
     * 根据关键字进行搜索并分页
     * @param pageNumber
     * @param pageSize
     * @param searchContent
     * @return
     */
    List<SendInfo> searchSendInfo(Integer pageNumber, Integer pageSize, String searchContent);

    /**
     * 根据关键词权重进行查询
     * @param searchContent
     * @return
     */
    List<SendInfo> searchSendInfoByWeight(String searchContent);
}
