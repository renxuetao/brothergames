//package com.db;
//
//import com.bean.Book;
//
//import org.litepal.crud.DataSupport;
//
//import java.io.Serializable;
//
///**
// * Created by g on 2016/1/19.
// */
//public class BookChapter extends DataSupport implements Serializable {
//
//    //8                  INT64         BookID             //书籍ID
//    // if Result == EC_REQUEST_GETBOOKPRICEINFO_OK {
//    //    2                  UINT16        Num                //章节数量
//    // for (int i=0; i<Num; i++) {
//    //    8                 INT64         Chapter            //章节
//    //    2                 UINT16        ChapterInfoLen     //章节名长度
//    //MAX_CHAPTERNAME_LEN   TEXT          ChapterName        //章节名
//    //    8                 INT64         Words              //字数
//    //    8                 INT64         Price              //价格
//    //    8                 FLOAT64       Discount           //折扣
//    //    8                 INT64         FromTime           //折扣开始时间
//    //    8                 INT64         ToTime             //折扣结束时间
//    //    8                 INT64         UpdateTime         //更新时间
//    //    1                 UINT8         IsBuy              //是否购买（0：未购买；1：已购买）
//
//
//    /**书籍ID*/
//    private long bookID = -1;
//
//    /**章节*/
//    private long Chapter = 0;
//
//    /**章节名*/
//    private String ChapterName = "";
//
//    /**字数*/
//    private long Words = 0;
//
//    /**价格*/
//    private long Price = 0;
//
//    /**折扣*/
//    private float Discount = 0;
//
//    /**折扣开始时间*/
//    private long FromTime = 0;
//
//    /**折扣结束时间*/
//    private long ToTime = 0;
//
//    /**更新时间*/
//    private long UpdateTime = 0;
//
//    /**是否购买（0：未购买；1：已购买）*/
//    private int IsBuy = 0;
//
//    private boolean isChoose = false;
//
//    /**0免费标题 1免费内容 2收费标题 3收费内容*/
//    private int chapterType = 0;
//
//    /**阅读进度 0是未读 1是已读 2是当前*/
//    private int readState = 0;
//
//    /**
//     * 审核状态 0无审核 1待审核 2审核通过 3审核失败 4取消审核
//     */
//    private byte BookReview = 0;
//
//    private Book bookItem = null;
//
//    private int bookindex = -1;
//
//    public long getBookID() {
//        return bookID;
//    }
//
//    public void setBookID(long bookID) {
//        this.bookID = bookID;
//    }
//
//    public long getChapter() {
//        return Chapter;
//    }
//
//    public void setChapter(long chapter) {
//        Chapter = chapter;
//    }
//
//    public String getChapterName() {
//        return ChapterName;
//    }
//
//    public void setChapterName(String chapterName) {
//        ChapterName = chapterName;
//    }
//
//    public long getWords() {
//        return Words;
//    }
//
//    public void setWords(long words) {
//        Words = words;
//    }
//
//    public long getPrice() {
//        return Price;
//    }
//
//    public void setPrice(long price) {
//        Price = price;
//    }
//
//    public float getDiscount() {
//        return Discount;
//    }
//
//    public void setDiscount(float discount) {
//        Discount = discount;
//    }
//
//    public long getFromTime() {
//        return FromTime;
//    }
//
//    public void setFromTime(long fromTime) {
//        FromTime = fromTime;
//    }
//
//    public long getToTime() {
//        return ToTime;
//    }
//
//    public void setToTime(long toTime) {
//        ToTime = toTime;
//    }
//
//    public long getUpdateTime() {
//        return UpdateTime;
//    }
//
//    public void setUpdateTime(long updateTime) {
//        UpdateTime = updateTime;
//    }
//
//    public int getIsBuy() {
//        return IsBuy;
//    }
//
//    public void setIsBuy(int isBuy) {
//        IsBuy = isBuy;
//    }
//
//    public boolean isChoose() {
//        return isChoose;
//    }
//
//    public void setChoose(boolean choose) {
//        isChoose = choose;
//    }
//
//    public int getChapterType() {
//        return chapterType;
//    }
//
//    public void setChapterType(int chapterType) {
//        this.chapterType = chapterType;
//    }
//
//    public int getReadState() {
//        return readState;
//    }
//
//    public void setReadState(int readState) {
//        this.readState = readState;
//    }
//
//    public byte getBookReview() {
//        return BookReview;
//    }
//
//    public void setBookReview(byte bookReview) {
//        BookReview = bookReview;
//    }
//
//    public Book getBookItem() {
//        return bookItem;
//    }
//
//    public void setBookItem(Book bookItem) {
//        this.bookItem = bookItem;
//    }
//
//    public int getBookindex() {
//        return bookindex;
//    }
//
//    public void setBookindex(int bookindex) {
//        this.bookindex = bookindex;
//    }
//}
