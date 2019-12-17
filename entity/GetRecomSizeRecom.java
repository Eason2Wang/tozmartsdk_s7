package com.tozmart.tozisdk.entity;

import java.util.List;

public class GetRecomSizeRecom {

    private String measureId;
    private List<RecommendSize> recommendSizeList;

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public List<RecommendSize> getRecommendSizeList() {
        return recommendSizeList;
    }

    public void setRecommendSizeList(List<RecommendSize> recommendSizeList) {
        this.recommendSizeList = recommendSizeList;
    }
}
