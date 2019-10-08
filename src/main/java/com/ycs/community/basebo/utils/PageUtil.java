package com.ycs.community.basebo.utils;

import com.ycs.community.basebo.constants.Constants;

public class PageUtil {
    private static Integer currentPage = Constants.DEFAULT_CURRENT_PAGE;
    private static Integer pageSize = Constants.DEFAULT_PAGE_SIZE;
    private static Integer totalCount = Constants.DEFAULT_TOTAL_COUNT;
    private static Integer totalPage = Constants.DEFAULT_TOTAL_PAGE;
    private static Integer startRow = Constants.DEFAULT_START_ROW;

    public PageUtil () {
    }

    /**
     * 计算分页数据
     * @param totalCount 总条数
     * @param pageSize 页大小
     * @param currentPage 当前页
     */
    public static void calculatePageInfo(int totalCount, int currentPage, int pageSize) {
        initialize();
        PageUtil.totalCount = totalCount > 0 ? totalCount : Constants.DEFAULT_TOTAL_COUNT;
        PageUtil.currentPage = currentPage > 0 ? currentPage : Constants.DEFAULT_CURRENT_PAGE;
        PageUtil.pageSize = pageSize > 0 ? pageSize : Constants.DEFAULT_PAGE_SIZE;
        // 计算分页数据
        PageUtil.totalPage = (PageUtil.totalCount + PageUtil.pageSize - 1) / PageUtil.pageSize;
        // 计算起始行
        PageUtil.startRow = PageUtil.pageSize * (currentPage - 1);
    }

    /**
     * 初始化分页数据
     */
    private static void initialize() {
        PageUtil.currentPage = Constants.DEFAULT_CURRENT_PAGE;
        PageUtil.pageSize = Constants.DEFAULT_PAGE_SIZE;
        PageUtil.totalCount = Constants.DEFAULT_TOTAL_COUNT;
        PageUtil.totalPage = Constants.DEFAULT_TOTAL_PAGE;
        PageUtil.startRow = Constants.DEFAULT_START_ROW;
    }

    public static int getCurrentPage() {
        return null != currentPage ? currentPage : Constants.DEFAULT_CURRENT_PAGE;
    }

    public static int getPageSize() {
        return null != pageSize ? pageSize : Constants.DEFAULT_PAGE_SIZE;
    }

    public static int getTotalCount() {
        return null != totalCount ? totalCount : Constants.DEFAULT_TOTAL_COUNT;
    }

    public static int getTotalPage() {
        return null != totalPage ? totalPage : Constants.DEFAULT_TOTAL_PAGE;
    }

    public static int getStartRow() {
        return null != startRow ? startRow : Constants.DEFAULT_START_ROW;
    }
}
