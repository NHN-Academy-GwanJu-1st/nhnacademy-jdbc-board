package com.nhnacademy.domain;

import lombok.Data;

@Data
public class PageDTO {

    private int pageStart;
    private int pageEnd;
    private boolean next;
    private boolean prev;
    private int amount;
    private int pageNum;
    private int skip;
    private int total;

    public PageDTO(int pageNum, int amount,int total) {
        this.pageNum = pageNum;
        this.amount = amount;
        this.skip = (pageNum - 1) * amount;
        this.total = total;

        this.pageEnd = (int) (Math.ceil(this.pageNum / 10.0)) * 10;
        this.pageStart = this.pageEnd - 9;

        int realEnd = (int) (Math.ceil(total * 1.0 / this.amount));

        if (this.pageEnd > realEnd) {
            this.pageEnd = realEnd;
        }

        this.prev = this.pageStart > 1;
        this.next = this.pageEnd < realEnd;
    }

    public PageDTO(int total) {
        this(1, 20, total);
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        this.skip = (this.pageNum - 1) * amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.skip = (this.pageNum - 1) * amount;
    }




}
