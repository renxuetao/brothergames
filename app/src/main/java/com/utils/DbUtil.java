//package com.utils;
//
//import android.content.ContentValues;
//
//import com.book.BookConfig;
//import com.db.BookRecord;
//import com.db.BookReordBasics;
//
//import org.litepal.crud.DataSupport;
//
//import java.util.List;
//
///**
// * Created by g on 2016/2/29.
// */
//public class DbUtil {
//
//    /*******************************阅读步骤新版**************************************/
//    /**
//     * 获得书籍阅读步骤 列表
//     * @param account
//     * @return
//     */
//    public static List<BookRecord> getBookRecords(String account, int isAuthor){
//        List<BookRecord> list = null;
//        try {
//            list = DataSupport.where("account = ? and isAuthor = ?", account, isAuthor + "").find(BookRecord.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            list = null;
//        }
//        return list;
//    }
//
//    /**
//     * 获得书籍阅读步骤 元素
//     * @param account
//     * @param version
//     * @return
//     */
//    public static synchronized BookRecord getBookRecord(String account ,long bookid, int version, int isAuthor){
//        BookRecord obj = null;
//        try {
//            List<BookRecord> list = DataSupport.where("account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "").find(BookRecord.class);
//            if(list != null&&list.size() > 0){
//                obj = list.get(0);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            obj = null;
//        }
//        return obj;
//    }
//
//    /**
//     * 保存阅读记录
//     * @param account
//     * @param version
//     * @param path
//     * @param current
//     */
//    public static void saveBookRecord(String account,long bookid,int version,String path,int current, int isAuthor){
//        List<BookRecord> list = DataSupport.where("account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "").find(BookRecord.class);
//        if(list != null&&list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("path",path);
//            values.put("current",current);
//            DataSupport.updateAll(BookRecord.class, values, "account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "");
//        }else{
//            BookRecord bookRecord = new BookRecord();
//            bookRecord.setAccount(account);
//            bookRecord.setBookid(bookid);
//            bookRecord.setVersion(version);
//            bookRecord.setPath(path);
//            bookRecord.setCurrent(current);
//            bookRecord.setIsAuthor(isAuthor);
//            bookRecord.save();
//        }
//    }
//
//    /**
//     * 重写saveBookRecord保存阅读记录
//     * @param account
//     * @param bookid
//     * @param version
//     * @param path
//     * @param current
//     * @param lasttime
//     */
//    public static void saveBookRecord(String account,long bookid,int version,String path,int current,long lasttime, int isAuthor, String read_flag){
//        List<BookRecord> list = DataSupport.where("account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "").find(BookRecord.class);
//        if(list != null&&list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("path",path);
//            values.put("current",current);
//            values.put("lasttime",lasttime);
//            values.put("read_flag", read_flag);
//            DataSupport.updateAll(BookRecord.class, values, "account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "");
//        }else{
//            BookRecord bookRecord = new BookRecord();
//            bookRecord.setAccount(account);
//            bookRecord.setBookid(bookid);
//            bookRecord.setVersion(version);
//            bookRecord.setPath(path);
//            bookRecord.setCurrent(current);
//            bookRecord.setLasttime(lasttime);
//            bookRecord.setIsAuthor(isAuthor);
//            bookRecord.setRead_flag(read_flag);
//            LogUtil.d("save com.db-------:" + read_flag);
//            bookRecord.save();
//        }
//    }
//    /**
//     * 保存阅读记录中的路径
//     * @param account
//     * @param version
//     * @param path
//     */
//    public static void saveBookRecordPath(String account,long bookid,int version,String path, int isAuthor){
//        List<BookRecord> list = DataSupport.where("account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "").find(BookRecord.class);
//        if(list != null&&list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("path",path);
//            DataSupport.updateAll(BookRecord.class, values, "account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "");
//        }else{
//            BookRecord bookRecord = new BookRecord();
//            bookRecord.setAccount(account);
//            bookRecord.setBookid(bookid);
//            bookRecord.setVersion(version);
//            bookRecord.setPath(path);
//            bookRecord.setIsAuthor(isAuthor);
//            bookRecord.save();
//        }
//    }
//
//    /**
//     * 保存阅读记录中的节点
//     * @param account
//     * @param version
//     * @param current
//     */
//    public static void saveBookRecordCurrent(String account,long bookid,int version,int current,int chapter, int isAuthor, String read_flag){
//        List<BookRecord> list = DataSupport.where("account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "").find(BookRecord.class);
//        if(list != null&&list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("current",current);
//            values.put("chapter",chapter);
//            values.put("read_flag", read_flag);
//            DataSupport.updateAll(BookRecord.class, values, "account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "");
//            LogUtil.d("read_flag:" + read_flag);
//        }else{
//            BookRecord bookRecord = new BookRecord();
//            bookRecord.setAccount(account);
//            bookRecord.setBookid(bookid);
//            bookRecord.setVersion(version);
//            bookRecord.setCurrent(current);
//            bookRecord.setChapter(chapter);
//            bookRecord.setIsAuthor(isAuthor);
//            bookRecord.setRead_flag(read_flag);
//            bookRecord.save();
//        }
//    }
//
//    public static void saveChapterFirst(String account,long bookid,int version, int isAuthor, String chapterfirst){
//        List<BookRecord> list = DataSupport.where("account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "").find(BookRecord.class);
//        if(list != null&&list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("chapterfirst", chapterfirst);
//            DataSupport.updateAll(BookRecord.class, values, "account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "");
//        }else{
//            BookRecord bookRecord = new BookRecord();
//            bookRecord.setAccount(account);
//            bookRecord.setBookid(bookid);
//            bookRecord.setVersion(version);
//            bookRecord.setIsAuthor(isAuthor);
//            bookRecord.setChapterfirst(chapterfirst);
//            bookRecord.save();
//        }
//    }
//
//    /**
//     * 保存阅读记录中的时间
//     * @param account
//     * @param bookid
//     * @param version
//     * @param lasttime
//     */
//    public static void saveBookRecordLastTime(String account,long bookid,int version,long lasttime, int isAuthor){
//        List<BookRecord> list = DataSupport.where("account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "").find(BookRecord.class);
//        if(list != null&&list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("lasttime",lasttime);
//            DataSupport.updateAll(BookRecord.class, values, "account = ? and bookid = ? and version = ? and isAuthor = ?", account, bookid + "", version + "", isAuthor + "");
//        }else{
//            BookRecord bookRecord = new BookRecord();
//            bookRecord.setAccount(account);
//            bookRecord.setBookid(bookid);
//            bookRecord.setVersion(version);
//            bookRecord.setLasttime(lasttime);
//            bookRecord.setIsAuthor(isAuthor);
//            bookRecord.save();
//        }
//    }
//
//
//    /**
//     * 保存最新阅读版本
//     * @param account
//     * @param bookid
//     * @param latestversion
//     */
//    public static void saveBookRecordLatestVersion(String account ,long bookid,int latestversion, int isAuthor){
//        List<BookReordBasics> list = DataSupport.where("account = ? and bookid = ? and isAuthor = ?", account, bookid + "", isAuthor + "").find(BookReordBasics.class);
//        if(list != null&&list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("latestversion",latestversion);
//            DataSupport.updateAll(BookReordBasics.class, values, "account = ? and bookid = ? and isAuthor = ?", account, bookid + "", isAuthor + "");
//        }else{
//            BookReordBasics bookReordBasics = new BookReordBasics();
//            bookReordBasics.setAccount(account);
//            bookReordBasics.setBookid(bookid);
//            bookReordBasics.setLatestversion(latestversion);
//            bookReordBasics.setIsAuthor(isAuthor);
//            bookReordBasics.save();
//        }
//    }
//
//    /**
//     * 获得最新阅读版本
//     * @param account
//     * @param bookid
//     * @return
//     */
//    public static synchronized int getBookRecordLatestVersion(String account ,long bookid, int isAuthor){
//        int  latestversion = 0;
//        try {
//            List<BookReordBasics> list = DataSupport.where("account = ? and bookid = ? and isAuthor = ?", account, bookid + "", isAuthor + "").find(BookReordBasics.class);
//            if(list != null&&list.size() > 0){
//                latestversion = list.get(0).getLatestversion();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return latestversion;
//    }
//
//
//    public static void mergeBookRecord(String loginAccount){
//        List<BookRecord> list = DataSupport.where("account = ?", BookConfig.NOT_ACCOUNT).find(BookRecord.class);
//        if(list != null && list.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("account", loginAccount);
//            DataSupport.updateAll(BookRecord.class, values, "account = ?", BookConfig.NOT_ACCOUNT);
//        }
//
//        List<BookReordBasics> list1 = DataSupport.where("account = ?", BookConfig.NOT_ACCOUNT).find(BookReordBasics.class);
//        if(list1 != null && list1.size() > 0){
//            ContentValues values = new ContentValues();
//            values.put("account", loginAccount);
//            DataSupport.updateAll(BookReordBasics.class, values, "account = ?", BookConfig.NOT_ACCOUNT);
//        }
//    }
//
//    public static void deleteBookRecordBasic(){
//        DataSupport.deleteAll(BookReordBasics.class);
//    }
//
//    public static void deleteBookRecord(){
//        DataSupport.deleteAll(BookRecord.class);
//    }
//
//}
