package com.tozmart.tozisdk.entity;

import java.util.List;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetDescripRequest {
    private List<GetDescripCode> codes;

    public List<GetDescripCode> getCodes() {
        return codes;
    }

    public void setCodes(List<GetDescripCode> codes) {
        this.codes = codes;
    }
}
