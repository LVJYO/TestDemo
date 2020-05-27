package com.easy.xmltest;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * @Desc
 * @Author lvyang
 * @Date 2020/5/26
 */
public class MyXMLHandler extends DefaultHandler {


    private Book currBook;
    private String tagName;

    public ArrayList<Book> getBooks() {
        return books;
    }

    private ArrayList<Book> books;

    /**
     * @description 接收文档开始的通知。
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        books = new ArrayList<Book>();
    }

    /**
     * @param uri        元素的命名空间
     * @param localName  元素的本地名称（不带前缀）
     * @param qName      元素的限定名（带前缀）
     * @param attributes 元素的属性集合
     * @description 接收文档的开始的通知。
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("book")) {
            currBook = new Book();
            currBook.setId(attributes.getValue("id"));
        }
        this.tagName = localName;
    }

    /**
     * @param ch
     * @param start
     * @param length
     * @description 接收字符数据的通知。
     * 在DOM中 ch[start:end] 相当于Text节点的节点值（nodeValue）
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (tagName != null) {
            String data = new String(ch, start, length);
            if ("name".equals(tagName)) {
                this.currBook.setName(data);
            } else if ("author".equals(tagName)) {
                this.currBook.setAuthor(data);
            }
        }
    }

    /**
     * @param uri       元素的命名空间
     * @param localName ：元素的本地名称（不带前缀）
     * @param qName     元素的限定名（带前缀）
     * @description 接收文档的结尾的通知。
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("book".equals(localName)) {
            books.add(currBook);
            currBook = null;
        }
        this.tagName = null;
    }

    /**
     * @description 接收文档的结尾的通知。
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

}
