package com.asiainfo.workbench.search.service.impl;

import com.asiainfo.workbench.search.dao.SendInfoRepository;
import com.asiainfo.workbench.search.domain.SendInfo;
import com.asiainfo.workbench.search.service.SendInfoService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SendInfoServiceImpl implements SendInfoService {

    @Autowired
    private SendInfoRepository sendInfoRepository;

    @Override
    public boolean insert(SendInfo sendInfo) {
        boolean flag=false;
        try{
            sendInfoRepository.save(sendInfo);
            flag=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean delete(long id) {
        boolean flag=false;
        try{
            sendInfoRepository.delete(id);
            flag=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public SendInfo getOne(long id) {
        return sendInfoRepository.findOne(id);
    }

    @Override
    public List<SendInfo> search(String searchContent) {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchContent);
        System.out.println("查询的语句:"+builder);
        Iterable<SendInfo> searchResult = sendInfoRepository.search(builder);
        Iterator<SendInfo> iterator = searchResult.iterator();
        List<SendInfo> list=new ArrayList<SendInfo>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }


    @Override
    public List<SendInfo> searchSendInfo(Integer pageNumber, Integer pageSize,String searchContent) {
        // 分页参数
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchContent);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(builder).build();
        System.out.println("查询的语句:" + searchQuery.getQuery().toString());
        Page<SendInfo> searchPageResults = sendInfoRepository.search(searchQuery);
        return searchPageResults.getContent();
    }


    @Override
    public List<SendInfo> searchSendInfoByWeight(String searchContent) {
        // 根据权重进行查询
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name", searchContent)),
                        ScoreFunctionBuilders.weightFactorFunction(10))
                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("description", searchContent)),
                        ScoreFunctionBuilders.weightFactorFunction(100)).setMinScore(2);
        System.out.println("查询的语句:" + functionScoreQueryBuilder.toString());
        Iterable<SendInfo> searchResult = sendInfoRepository.search(functionScoreQueryBuilder);
        Iterator<SendInfo> iterator = searchResult.iterator();
        List<SendInfo> list=new ArrayList<SendInfo>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

}
