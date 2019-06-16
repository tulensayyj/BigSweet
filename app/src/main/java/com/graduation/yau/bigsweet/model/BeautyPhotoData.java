package com.graduation.yau.bigsweet.model;

import java.util.List;

/**
 * Created by yyj on 2019/6/16.
 */

public class BeautyPhotoData {
    private boolean isError;

    private List<BeautyPhoto> results;

    public List<BeautyPhoto> getResults() {
        return results;
    }

    public void setResults(List<BeautyPhoto> beautyPhotos) {
        this.results = beautyPhotos;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
