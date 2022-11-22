package com.jkcq.hrwtv.http.bean;

import java.io.Serializable;

public class CourseDetail implements Serializable {
    /**
     * "begin":0,"end":60,"targetRange":1
     */
    int begin;
    int end;
    int targetRange;

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(int targetRange) {
        this.targetRange = targetRange;
    }

    public CourseDetail() {
    }

    public CourseDetail(int begin, int end, int targetRange) {
        this.begin = begin;
        this.end = end;
        this.targetRange = targetRange;
    }

    @Override
    public String toString() {
        return "CourseDetail{" +
                "begin=" + begin +
                ", end=" + end +
                ", targetRange=" + targetRange +
                '}';
    }
}
